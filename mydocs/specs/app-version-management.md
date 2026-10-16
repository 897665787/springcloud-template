# APP 版本管理方案

> 目标：在现有 `AppVersion` 基础上补齐灰度放量统计与设备白名单，并打通版本/白名单的配置入口。
>
> 已确认口径：
> - **单 App 场景**，设备池复用现有 `device_info` 表，不改表结构、不加 `app_code` 列。
> - 白名单维度 `app_code + deviceid`（应用级、长期有效）。

---

## 一、现状与差距

现有实现链路：

```
app 服务  VersionController.check
  └─ feign → tool 服务 AppVersionController.check(appCode, currentVersion)
        └─ AppVersionService.selectLastByAppCode(appCode)   // 取最新已发布版本
              └─ 版本比较 → noUpdate / forceUpdate / tipsUpdate
```

已具备能力：

- 多应用：`app_info`（`app_code` 唯一标识）+ `app_version`（按 `app_code` 关联）——表结构已支持多应用，本方案按**单 App**使用
- 强制更新：`min_supported_version` 低于则 `forceUpdate`
- 提示更新：`min_supported_version ≤ current < version` 则 `tipsUpdate`
- 灰度字段：`gray_percent` / `total_count` / `update_count` 已建表，但**统计逻辑未实现**
- 白名单：`continueUpdate()` 仅 `TODO`，且误用 `userId` 维度

### 差距清单

| 需求 | 现状 | 差距 |
|------|------|------|
| 1 多应用版本管理 | `app_info`+`app_version` | 结构已支持；缺管理端配置接口（新增/编辑版本、配置灰度） |
| 2 最低版本强制更新 | `min_supported_version` | 已支持 |
| 3 提示更新可跳过 | `tipsUpdate` | 已支持 |
| 4 灰度按百分比放量 | 字段在、框架在 | **缺**：创建时统计总设备量、定时统计已升级设备数 |
| 5 设备白名单 | TODO + 用 `userId` | **缺**：白名单表、`deviceid` 维度判断 |

---

## 二、方案设计

### 2.1 设备池与统计口径（复用 `device_info`）

`device_info` 表已有 `deviceid`（唯一键）和 `version` 字段，且由现有链路持续刷新：

```
DeviceInfoFilter（framework-edge，每次外部请求）
  └─ 异步发广播 → DeviceInfoRecordStrategy（user 服务）
        └─ device_info  upsert（ON DUPLICATE KEY UPDATE version = 最新版本）
```

单 App 场景下直接以 `device_info` 作为设备池：

- **总设备量** = `SELECT COUNT(*) FROM device_info`
- **已升级设备数** = `device_info.version ≥ 目标版本号` 的设备数

> 注意：`device_info.version` 是 `varchar`，SQL 里 `version >= '1.0.2'` 不可靠，需在 Java 侧用 `CharSequenceUtil.compareVersion` 逐条比较后计数（见 2.4）。当前「先不动 device_info」，暂不加数值化版本列；设备量级上来后可再评估加 `version_code` 下沉到 SQL。

### 2.2 数据库设计（新增 1 张白名单表）

`app_version` 表**不加列**，仅明确 `total_count` / `update_count` 注释：

```sql
`total_count`  int NOT NULL DEFAULT 0 COMMENT '总设备量（创建/放量时统计 device_info 行数）',
`update_count` int NOT NULL DEFAULT 0 COMMENT '已升级设备数（device_info.version = 本记录 version 的设备数）',
```

新增白名单表：

```sql
CREATE TABLE `app_version_whitelist` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `app_code` varchar(32) NOT NULL COMMENT 'app_info.app_code',
  `deviceid` varchar(64) NOT NULL COMMENT '设备ID（白名单）',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_appcode_deviceid` (`app_code`,`deviceid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='APP版本-设备白名单';
```

### 2.3 核心检查逻辑（重写 `AppVersionController.check`）

接口签名不变（`check(appCode, currentVersion)`），`deviceid` 从请求头取 `HeaderContextUtil.deviceid()`（`x-deviceid`，feign 已透传）。

```text
1. latest = selectLastByAppCode(appCode)；为空 → noUpdate
2. currentVersion >= latest.version           → noUpdate
3. currentVersion < latest.minSupportedVersion → forceUpdate
4. grayPercent >= 1.0                         → tipsUpdate        // 全量
5. deviceid 在白名单                           → tipsUpdate        // 白名单优先于“放量已满”
6. grayCount = totalCount * grayPercent（向下取整）
   updateCount > grayCount                    → noUpdate          // 放量已满，不提示
   else                                       → tipsUpdate        // 灰度放量中，提示
```

- 版本比较统一用 `CharSequenceUtil.compareVersion`（与现状一致）。
- 白名单命中只判 `deviceid`，删除现有 `continueUpdate()` 的 `userId` 逻辑。
- 检查接口是**只读**的：设备版本上报由 `DeviceInfoFilter` 链路负责，check 内不再 upsert 设备池。

### 2.4 统计已升级设备数

`device_info` 在 user 服务库中，版本比较在 user 服务内完成，tool/job 通过 feign 取数：

**user 服务新增统计接口**（`DeviceApi` 增加两个方法，或新建 `DeviceInfoApi`，由 `DeviceController` 实现）：

| 方法 | 说明 |
|------|------|
| `Integer deviceCount()` | `SELECT COUNT(*) FROM device_info` |
| `Integer deviceCountUpgraded(String version)` | 分页取 `device_info` 的 `version`，Java `compareVersion ≥` 计数 |

**tool 服务新增 `AppVersionStatService.statUpgradedDevices(appCode)`**：

```text
latest = selectLastByAppCode(appCode)
if latest == null or latest.grayPercent >= 1: 跳过
total   = userFeign.deviceCount()
upgraded = userFeign.deviceCountUpgraded(latest.version)
update app_version set total_count = total, update_count = upgraded where id = latest.id
```

**job 服务新增 `AppVersionUpgradeCountJob`（`@XxlJob`）**：

```text
遍历 app_version 中 gray_percent < 1 的 app_code
  → feign 调 tool 服务 statUpgradedDevices(appCode)
```

> 单 App 场景下即对唯一 app_code 执行一次统计。`total_count` 在版本创建时由 `deviceCount()` 初始化，定时任务同步刷新，保证「创建时统计总量 + 定时统计已升级数」两条需求都覆盖。

### 2.5 缓存与一致性

- `app_version` 最新版本：沿用现有 `AppVersionCache` + Canal 失效（不变）。
- 白名单：数据量小、高频读，新增 `AppVersionWhitelistCache`，`key = appCode` 存 `Set<deviceid>`，Canal 监听 `app_version_whitelist` 变更失效。
- `device_info` 上报链路：不动。

### 2.6 管理端配置入口（补齐需求 1）

按现有 `tool` 服务 Controller + feign 模式新增：

| 接口 | 说明 |
|------|------|
| `POST /appVersion/saveOrUpdate` | 新增/编辑版本（`app_code`、`version`、`min_supported_version`、`download_url`、`release_notes`、`gray_percent`）；新增时 `total_count = deviceCount()` |
| `GET /appVersion/detail` | 版本详情（含实时 `total_count` / `update_count`） |
| `POST /appVersionWhitelist/add` / `/remove` / `GET /list` | 白名单增删查 |

> 管理端若需独立 Admin 门面，按 `admin-api-conventions.md` 在 `adminapi` 建 Controller 透传；本方案先落到 tool 服务。

---

## 三、关键决策点（已确认 / 待确认）

| # | 决策 | 结论 |
|---|------|------|
| 1 | 总设备量口径 | 复用 `device_info`（单 App），不改结构、不加 `app_code` 列 |
| 2 | 白名单维度 | `app_code + deviceid`（应用级、长期有效） |
| 3 | 灰度满判定边界 | `update_count > total_count * percent` 不提示（贴合需求原文「大于」） |
| 4 | 统计任务位置 | `job` 模块 xxl-job + feign（沿用项目既有模式） |
| 5 | 已升级数比较方式 | 暂 Java 逐条 `compareVersion`，后续可加 `version_code` 下沉 SQL |

---

## 四、实施步骤

1. 建表：`app_version_whitelist`（更新 `sql/tool/APP版本设计相关表.sql`）
2. user 服务：`DeviceApi` 增加 `deviceCount` / `deviceCountUpgraded`，`DeviceController` 实现（分页 Java 比较）
3. tool 服务：新增 `AppVersionWhitelist` 实体 + Mapper + `AppVersionWhitelistCache` + Canal 失效
4. tool 服务：重写 `AppVersionController.check`（灰度 + 白名单）
5. tool 服务：新增 `AppVersionStatService` + `statUpgradedDevices` feign 接口（内部调 user feign 取数）
6. job 服务：新增 `AppVersionUpgradeCountJob`（xxl-job）定时调用；新增 tool/user feign 客户端
7. 管理端：版本 `saveOrUpdate`/`detail`、白名单增删查
8. 版本创建时初始化 `total_count`

---

## 参考规范

| 规范文件 | 读取时机 |
|----------|----------|
| `sp-rules/naming-conventions.md` | 确定类名/表名/包名时 |
| `sp-rules/db-conventions.md` | 设计建表 DDL 时 |
| `sp-rules/java-conventions.md` | 描述 Java 实体与逻辑时 |
| `sp-rules/engineering-conventions.md` | 确定实施步骤与版本升级时 |
| `sp-rules/admin-api-conventions.md` | 设计管理端接口时 |
| `sp-rules/swagger-rules.md` | 涉及管理端接口注解时 |

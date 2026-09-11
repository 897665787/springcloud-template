# i18n 国际化模块

## 简介

i18n 模块提供**消息编码级（message code）**的国际化能力，基于 Spring `MessageSource` 扩展，支持「数据库动态文案 + classpath  properties 文件」两级解析，并与 graceful-response 全局响应框架打通，实现异常消息按请求语言自动翻译。

核心特性：

- **动态文案**：译文存储在 MySQL `common_i18n` 表，运营可直接修改，无需重新发版
- **两级回退**：数据库未命中时，自动回退到 classpath 下的 `messages*.properties`
- **缓存加速**：译文查询结果（含 null）走 Spring Cache，本地 Caffeine / 分布式 Redis 可切换
- **自动翻译**：业务异常抛出的 msg 即消息编码，响应出参时自动渲染为对应语言
- **全链路透传**：`Accept-Language` 请求头经 Filter 进入 `LocaleContextHolder`，并通过 Feign 拦截器透传到下游服务
- **平滑降级**：下游模块不提供 `MessageSourceResolver` 实现时，自动退回 Spring 原生 properties 解析，不产生任何侵入

## 核心组件

| 组件 | 职责 |
| --- | --- |
| [MessageSourceResolver.java](MessageSourceResolver.java) | 消息文案解析器 SPI。框架层不依赖数据访问层，由下游模块提供具体实现（直连 DB 或 Feign 调用） |
| [MysqlMessageSource.java](MysqlMessageSource.java) | 继承 `AbstractMessageSource`，按 `code + locale` 查缓存，缓存未命中委托 `MessageSourceResolver` 查库；查不到返回 null，回退 parent |
| [I18nAutoConfiguration.java](I18nAutoConfiguration.java) | 自动配置类。容器中存在 `MessageSourceResolver` Bean 时，注册 `@Primary` 的 `MysqlMessageSource`，并将 Spring Boot 默认 MessageSource 设为其 parent |
| [GrI18nResponseBodyAdvice.java](../globalresponse/gracefulresponse/extend/advice/GrI18nResponseBodyAdvice.java) | 重写 graceful-response 自带的 i18n 响应体通知：以响应 `status.msg` 作为消息编码翻译，未命中则保留原文 |
| [ExceptionUtil.java](../globalresponse/ExceptionUtil.java) | 异常抛出工具，抛出的 `BusinessException` / `ArgsBusinessException` 的 msg 即消息编码 |

## 消息解析链路

```
HTTP 请求 (Accept-Language: en-US)
        │
        ▼
LocaleContextHolder.getLocale()   ← Spring MVC 根据 Accept-Language 解析
        │
        ▼
MessageSource.getMessage(code, args, defaultMsg, locale)
        │
        ▼
┌─────────────────────────────────────────────┐
│ MysqlMessageSource（@Primary，第一级）        │
│  1. 查 Spring Cache（cacheName = i18n）       │
│  2. 未命中 → MessageSourceResolver 查库       │
└─────────────────────────────────────────────┘
        │ 未命中（返回 null）
        ▼
┌─────────────────────────────────────────────┐
│ parent MessageSource（第二级）                │
│  读取 classpath: i18n/messages*.properties   │
│               i18n-framework/messages*.properties │
└─────────────────────────────────────────────┘
        │ 仍未命中
        ▼
defaultMsg（即 code 原文）
```

> 应用配置了 `spring.messages.use-code-as-default-message: true`，任何一级都找不到译文时直接返回消息编码本身，不会抛 `NoSuchMessageException`。

## 快速开始

### 1. 配置消息资源文件

在服务的 `application.yml` 中配置 `spring.messages.basename`（各业务服务现状）：

```yaml
spring:
  messages:
    basename: i18n/messages,i18n-framework/messages # 多个basename用逗号分隔
    encoding: UTF-8
    fallback-to-system-locale: true
    always-use-message-format: false
    use-code-as-default-message: true # 找不到code时直接返回code，而不是抛异常
```

- `i18n/messages`：本服务自定义文案，文件位于服务自己的 `src/main/resources/i18n/` 目录
- `i18n-framework/messages`：framework 内置的系统级异常文案，随 jar 分发，无需业务服务维护

### 2. 实现 MessageSourceResolver

框架只定义 SPI，需要下游模块提供实现 Bean，`I18nAutoConfiguration` 检测到该 Bean 后才会启用数据库消息源。

**方式一：直连数据库**（数据所在服务，参考 tool 服务 [CommonI18nMessageSourceResolver.java](../../../../../../../../tool/service/src/main/java/com/company/tool/i18n/CommonI18nMessageSourceResolver.java)）

```java
@Component
public class CommonI18nMessageSourceResolver implements MessageSourceResolver {

    /** 全局/系统消息在 common_i18n 中的 business_id 占位值（不绑定具体业务实体行） */
    private static final int GLOBAL_BUSINESS_ID = 0;

    @Autowired
    private CommonI18nService commonI18nService;

    @Override
    public String resolve(String code, Locale locale) {
        CommonI18n commonI18n =
            commonI18nService.selectByBusinessTypeBusinessIdLocale(code, GLOBAL_BUSINESS_ID, locale.toLanguageTag());
        if (commonI18n == null) {
            return null;
        }
        return commonI18n.getI18nText();
    }
}
```

**方式二：Feign 远程调用**（非数据所在服务，参考 web/user/system/order 服务）

```java
@Component
public class CommonI18nMessageSourceResolver implements MessageSourceResolver {

    private static final int GLOBAL_BUSINESS_ID = 0;

    @Autowired
    private CommonI18nFeign commonI18nFeign;

    @Override
    public String resolve(String code, Locale locale) {
        CommonI18nResp commonI18n =
            commonI18nFeign.selectByBusinessTypeBusinessIdLocale(code, GLOBAL_BUSINESS_ID, locale.toLanguageTag());
        if (commonI18n == null) {
            return null;
        }
        return commonI18n.getI18nText();
    }
}
```

> 不实现该接口时，`MysqlMessageSource` 不会注册，国际化完全走 classpath properties，可按模块渐进式接入。

### 3. 准备译文数据

建表脚本见 [国际化设计相关表.sql](../../../../../../../../sql/tool/国际化设计相关表.sql)。消息编码级翻译复用统一的 `common_i18n` 表，约定如下：

| 字段 | 消息翻译的取值约定 |
| --- | --- |
| `business_type` | 消息编码，如 `test.hello` |
| `business_id` | 固定填 `0`（全局消息占位，不绑定业务实体行） |
| `locale` | 地区编码，与 `Locale.toLanguageTag()` 一致，如 `zh-CN`、`en-US`、`zh-HK` |
| `i18n_text` | 译文文案，支持 `{0}`、`{1}` 占位符（`java.text.MessageFormat` 格式） |

唯一键：`uniq_businessid_businesstype_locale (business_id, business_type, locale)`。

```sql
-- 例：code = test.hello.name，文案中 {0} 会被运行时参数替换
INSERT INTO common_i18n (business_id, business_type, locale, i18n_text)
VALUES (0, 'test.hello.name', 'en-US', 'Hello {0}');
```

## 使用方式

### 1. 手动获取译文

业务代码中直接注入 Spring `MessageSource` 即可（实际注入的是 `@Primary` 的 `MysqlMessageSource`）：

```java
@RestController
public class I18nController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/accept-language")
    public Map<String, String> acceptLanguage() {
        String hello1 = messageSource.getMessage("test.hello", null, "Default message", LocaleContextHolder.getLocale());
        String hello2 = messageSource.getMessage("test.hello.name", new Object[]{"zhangsan"}, "Default message",
            LocaleContextHolder.getLocale());
        // ...
    }
}
```

完整示例见 web 服务 [I18nController.java](../../../../../../../../web/src/main/java/com/company/web/controller/I18nController.java)。

### 2. 异常消息自动翻译

抛出异常时直接把**消息编码**作为 msg，响应出参前 [GrI18nResponseBodyAdvice.java](../globalresponse/gracefulresponse/extend/advice/GrI18nResponseBodyAdvice.java) 会统一翻译：

```java
// 无占位符
ExceptionUtil.throwException("ID不能为空");

// 带占位符参数，译文示例：参数{0}({1})缺失
ExceptionUtil.throwException("参数{0}({1})缺失", "userId", "Integer");
```

处理流程：

1. `ExceptionUtil.throwException` 抛出 `BusinessException` / 携带 args 的 `ArgsBusinessException`
2. `ArgsExceptionAdvice` 将 args 暂存到 `GracefulResponseExceptionArgsContext`
3. `GrI18nResponseBodyAdvice` 取出 `status.msg` 作为 code、args 作为占位符参数，调用 `messageSource.getMessage(msg, args, msg, locale)` 渲染
4. 查不到译文时 defaultMsg 就是 msg 本身，前端始终能拿到兜底文案

> graceful-response 自带的 `GrI18nResponseBodyAdvice` 以 code 作为消息编码，不适用本项目「msg 即 code」的约定，因此框架重写了该 Advice，并在 [application-gracefulresponse.yml](../../../../../resources/application-gracefulresponse.yml) 中固定 `graceful-response.i18n: false` 关闭原版实现。

## Locale 来源与全链路透传

- **入口**：Spring MVC 默认按 HTTP 请求头 `Accept-Language` 解析 Locale，业务代码通过 `LocaleContextHolder.getLocale()` 获取，请求头常量定义为 `HeaderConstants.ACCEPT_LANGUAGE`
- **上下文**：[HeaderContextFilter.java](../context/filter/HeaderContextFilter.java) 将请求头（含 `Accept-Language`）存入 `TransmittableThreadLocal`，线程池场景也可传递
- **Feign 透传**：[HttpHeaderInterceptor.java](../feign/HttpHeaderInterceptor.java) 把上下文中的全部 header 透传给下游服务，下游无需额外处理即可拿到相同 Locale

请求示例：

```http
GET /i8n/accept-language
Accept-Language: en-US
```

## 缓存说明

`MysqlMessageSource` 通过 Spring Cache 缓存译文，配置见 [application-cachemanager.yml](../../../../../resources/application-cachemanager.yml)：

| 环境 | 缓存实现 | TTL |
| --- | --- | --- |
| dev | Caffeine 本地缓存 | 10 分钟 |
| test / pre / prod | Redis 分布式缓存 | 10 分钟 |

- **cacheName**：`i18n`
- **缓存 key**：`i18n:{code}:{locale}`，例如 `i18n:test.hello:en-US`
- **null 缓存**：`cacheNullValues: true`，数据库查不到的译文同样缓存（值为 null），避免缓存穿透
- **失效方式**：当前无主动失效，译文修改后最长等待一个 TTL（10 分钟）生效；对实时性要求高的场景可在译文管理后台更新后主动清除 `i18n` 缓存

## 内置文案资源

framework jar 内置系统级异常文案，位于 [i18n-framework 目录](../../../../../resources/i18n-framework)：

| 文件 | 语言 |
| --- | --- |
| `messages.properties` | 默认（留空，回退用） |
| `messages_zh_CN.properties` | 简体中文 |
| `messages_zh_HK.properties` | 繁体中文 |
| `messages_en_US.properties` | 美式英文 |

内容覆盖框架统一处理的系统异常，例如请求方法不支持、媒体类型不支持、参数缺失、文件超限、系统错误等，文案中的 `{0}`、`{1}` 为运行时参数占位符。新增语言时，在该目录补充对应 `messages_{语言}_{地区}.properties` 文件即可。

framework-edge 模块另有一套边缘侧文案（如「未授权，请登录」），basename 为 `i18n-framework-edge/messages`，仅面向端侧服务（app/web/adminapi）引入。

## 与「实体字段国际化」的区别

本模块解决的是**提示消息/异常消息**的翻译；项目中另有一套**业务实体字段**级翻译（如轮播图标题、APP 版本发布说明按语言返回），二者互不冲突：

| 维度 | 消息编码国际化（本模块） | 实体字段国际化 |
| --- | --- | --- |
| 场景 | 异常提示、系统文案 | 业务数据内容（banner.title、version.release_notes 等） |
| 实现 | `MysqlMessageSource` + `MessageSourceResolver` | mybatis-i18n-spring-boot-starter（由 boot-starter-datasource 引入） |
| 使用方式 | `messageSource.getMessage(...)` / 抛异常自动翻译 | 实体字段标注 `@I18nTable`、`@I18nField`，查询结果自动替换 |
| 数据提供 | 本服务直连或 Feign 调 tool 服务 | 实现 `I18nDataProvider`（如 `CommonI18nDataProvider`），支持独立分表 / 统一表两种方式 |

字段国际化的用法见 [boot-starter/datasource/README.md](../../../../../../../../boot-starter/datasource/README.md) 第 7 节。

## 扩展点

- **自定义消息来源**：实现 [MessageSourceResolver](MessageSourceResolver.java) 即可接入任意存储（配置中心、第三方翻译服务等），注册为 Spring Bean 后自动接管第一级解析
- **新增语言**：补充 properties 文件 + `common_i18n` 表对应 locale 数据，无需改框架代码
- **调整缓存策略**：修改 `application-cachemanager.yml` 中 `spring.cache.type` 与 TTL，或为 `i18n` cacheName 配置独立的缓存规格

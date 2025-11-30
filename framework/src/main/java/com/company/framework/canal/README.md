# Canal 接入指南

Canal 是阿里巴巴开源的一个基于 MySQL 数据库增量日志解析的组件，提供增量数据订阅和消费功能。本文档介绍了如何在本项目中接入和使用 Canal。


## 数据库开启binlog

在使用 Canal 客户端之前，需要确认数据库开启了binlog：

```
-- 查看 binlog 基础配置
SHOW VARIABLES LIKE '%log_bin%';
-- 查看 binlog 格式（Canal 必须为 ROW）
SHOW VARIABLES LIKE 'binlog_format';
-- 查看 MySQL 服务器 ID（需与 Canal 不同）
SHOW VARIABLES LIKE 'server_id';
```

### 如果没有开启，按照以下方式开启：
1. 开启mysql binlog：my.ini增加如下配置
```
# 二进制日志（用于 Canal）
server-id=1
log-bin=/path_to_binlog/mysql-bin
binlog_format=ROW
expire_logs_days=7
max_binlog_size=100M
```
2. 创建canal用户（或直接使用现有用户也行）
```
CREATE USER 'canal'@'%' IDENTIFIED BY 'canal';
GRANT SELECT, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'canal'@'%';
FLUSH PRIVILEGES;
```

## 启动 Canal Server

在使用 Canal 客户端之前，需要先启动 Canal Server：

1. [下载并安装 Canal Server](https://github.com/alibaba/canal/releases) （核心下载canal.deployer即可）
2. Canal Server连接到哪个数据库？修改配置 conf/example/instance.properties：
```
# 修改为你的 MySQL 连接信息
canal.instance.master.address=127.0.0.1:3306
canal.instance.dbUsername=canal
canal.instance.dbPassword=canal
```
3. 启动 Canal Server

## 注意事项

1. 确保 MySQL 已开启 binlog，并设置格式为 ROW 模式
2. Canal Server 和 MySQL 需要在同一网络环境中
3. 处理器中的逻辑应该尽量轻量，避免影响数据同步性能
4. 如需在多个服务中监听相同的数据变更，可以考虑结合消息队列使用

## 客户端接入1：TCP模式
```
Mysql binlog -> Canal Server -> Canal Client
```

#### 服务端配置conf/canal.properties

```properties
canal.serverMode = tcp
```

### 依赖配置，Canal 依赖：
```xml
<dependency>
    <groupId>top.javatool</groupId>
    <artifactId>canal-spring-boot-starter</artifactId>
    <version>1.2.1-RELEASE</version>
</dependency>
```

### 配置文件设置

在不同环境的配置文件中添加 Canal 相关配置：

#### application-dev.yml
```yaml
canal:
  mode: simple # simple,cluster,zk,kafka,rocketMQ，默认：simple 可以使用一个不存在的选项关闭canal
  server: 127.0.0.1:11111 # canal的地址
  destination: example # 数据同步的目的地，默认：example
```

其他环境配置类似，可根据实际需求修改 `server` 和 `destination` 参数。

### 创建数据变更处理器

创建实现了 `EntryHandler<T>` 接口的处理器类来处理数据变更事件。示例代码如下：

```java
@Component
@CanalTable(value = "bu_user_info") // 对应的数据库表名
public class UserInfoHandler implements EntryHandler<UserInfo> {

    @Override
    public void delete(UserInfo t) {
        System.out.println("删除操作: " + JsonUtil.toJsonString(t));
    }

    @Override
    public void insert(UserInfo t) {
        System.out.println("插入操作: " + JsonUtil.toJsonString(t));
    }

    @Override
    public void update(UserInfo before, UserInfo after) {
        System.out.println("更新操作，更新前: " + JsonUtil.toJsonString(before));
        System.out.println("更新操作，更新后: " + JsonUtil.toJsonString(after));
    }
}
```
## 客户端接入2：消息驱动模式
```
Mysql binlog -> Canal Server -> MQ（削峰填谷） -> Canal Client
```

### rabbitMQ

#### 服务端配置conf/canal.properties
```
canal.serverMode = rabbitMQ

rabbitmq.host = localhost:5672
rabbitmq.virtual.host = /
rabbitmq.exchange = fanout-canal
rabbitmq.username = guest
rabbitmq.password = guest
rabbitmq.deliveryMode =
```

### rocketMQ

#### 1. 服务端配置conf/canal.properties
```
canal.serverMode = rocketMQ

rocketmq.producer.group = producer-group
rocketmq.enable.message.trace = false
rocketmq.customized.trace.topic =
rocketmq.namespace =
rocketmq.namesrv.addr = 127.0.0.1:9876
rocketmq.retry.times.when.send.failed = 0
rocketmq.vip.channel.enabled = false
rocketmq.tag =
```

#### 2. 服务端配置conf/example/instance.properties
```
canal.mq.topic=fanout-canal
```

### 接口`EntryHandler<T>` 用法还是清晰简单的，所以将数据转发到EntryHandler处理
1. top.javatool中canal沒有rabbitMQ、rocketMQ的实现，可阅读源码包 top.javatool.canal.client.spring.boot.autoconfigure
2. 项目中已整合消息驱动，包含rabbitMQ、rocketMQ的实现逻辑，所以只需要自定义实现top.javatool.canal.client.handler.MessageHandler，即可以将数据给到top.javatool的canal客户端处理
3. 新增消息驱动实现：具体请看[MessagedrivenClientAutoConfiguration.java](extend/MessagedrivenClientAutoConfiguration.java)

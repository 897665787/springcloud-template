# 三种消息队列实现对比

## 架构对比

### 目录结构一致性

所有三种 MQ 实现都遵循相同的目录结构：

```
framework/src/main/java/com/company/framework/messagedriven/
├── rabbitmq/                    ├── rocketmq/                    ├── redis/
│   ├── RabbitMQAutoConfiguration    ├── RocketMQAutoConfiguration    ├── RedisAutoConfiguration
│   ├── RabbitmqMessageSender        ├── RocketmqMessageSender        ├── RedisMessageSender
│   ├── aspect/                      ├── aspect/                      ├── aspect/
│   │   └── TraceAspect              │   └── TraceAspect              │   └── TraceAspect
│   ├── gracefulshutdown/            ├── gracefulshutdown/            ├── gracefulshutdown/
│   │   └── RabbitmqConsumerComp.    │   └── RocketmqConsumerComp.    │   └── RedisConsumerComponent
│   └── utils/                       └── utils/                       ├── delay/
│       └── ConsumerUtils                └── ConsumerUtils            │   └── DelayQueueScanner
                                                                       └── utils/
                                                                           └── ConsumerUtils
```

## 核心组件对比

### 1. 自动配置类

| 组件 | RabbitMQ | RocketMQ | Redis |
|------|----------|----------|-------|
| 条件1 | `message-driven=rabbitmq` | `message-driven=rocketmq` | `message-driven=redis` |
| 条件2 | `spring.rabbitmq.addresses` | `rocketmq.name-server` | `spring.redis.host` |
| 特殊配置 | RabbitTemplate Callback | 导入官方配置 | RedisMessageListenerContainer |

### 2. 消息发送器

所有实现都实现了 `MessageSender` 接口：

```java
public interface MessageSender {
    void sendNormalMessage(String strategyName, Object toJson, String exchange, String routingKey);
    void sendFanoutMessage(Object toJson, String exchange);
    void sendDelayMessage(String strategyName, Object toJson, String exchange, String routingKey, Integer delaySeconds);
}
```

#### 实现方式对比

| 功能 | RabbitMQ | RocketMQ | Redis |
|------|----------|----------|-------|
| 普通消息 | `rabbitTemplate.convertAndSend()` | `rocketMQTemplate.syncSend()` | `redisTemplate.convertAndSend()` |
| 广播消息 | Fanout Exchange | Topic + tag=* | Pub/Sub 频道 |
| 延时消息 | x-delayed 插件 | delayLevel 机制 | Sorted Set + 定时扫描 |
| 消息格式 | MessageProperties | Headers Map | JSON (headers + body) |

### 3. 消费工具类

所有 `ConsumerUtils` 提供相同的方法：

```java
public static <E> void handleByStrategy(String jsonStrMsg, Map/Message properties)
public static <E> void handleByConsumer(String jsonStrMsg, Map/Message properties, Consumer<E> consumer)
```

#### 参数差异

| MQ | 方法参数 | 消息体类型 |
|------|----------|-----------|
| RabbitMQ | `(String jsonStrMsg, Channel channel, Message message)` | `org.springframework.amqp.core.Message` |
| RocketMQ | `(String jsonStrMsg, Map<String, String> properties)` | `MessageExt` |
| Redis | `(String jsonStrMsg, Map<String, String> properties)` | JSON String |

### 4. 日志追踪切面

所有实现都通过 AOP 实现链路追踪：

| MQ | Before 切点 | 获取 TraceId 方式 |
|------|-------------|------------------|
| RabbitMQ | `@RabbitListener` 注解 | `message.getMessageProperties().getMessageId()` |
| RocketMQ | `RocketMQListener.onMessage()` 方法 | `messageExt.getProperties().get(HEADER_MESSAGE_ID)` |
| Redis | `redis.consumer.*Consumer.onMessage()` | 解析 JSON 的 `headers.message_id` |

### 5. 优雅下线

| MQ | 实现方式 | 核心代码 |
|------|----------|---------|
| RabbitMQ | 停止监听器 | `rabbitListenerEndpointRegistry.stop()` |
| RocketMQ | 停止消费者 | 依赖 Spring 生命周期 |
| Redis | 停止监听容器 | `redisMessageListenerContainer.stop()` |

## 消费者实现对比

### RabbitMQ 消费者
```java
@RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = "queue-name"), 
    exchange = @Exchange(value = "exchange-name"), 
    key = "routing-key"))
public void handle(String jsonStrMsg, Channel channel, Message message) {
    ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
}
```

### RocketMQ 消费者
```java
@RocketMQMessageListener(
    topic = "topic-name",
    consumerGroup = "group-name",
    selectorExpression = "tag-name")
public class Consumer implements RocketMQListener<MessageExt> {
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody(), UTF_8);
        ConsumerUtils.handleByStrategy(msg, messageExt.getProperties());
    }
}
```

### Redis 消费者
```java
@Bean
public MessageListener messageListener() {
    return (message, pattern) -> {
        String msg = new String(message.getBody(), UTF_8);
        onMessage(msg);
    };
}

@Bean
public Object register(RedisMessageListenerContainer container, MessageListener listener) {
    container.addMessageListener(listener, new ChannelTopic("channel-name"));
    return new Object();
}

public void onMessage(String message) {
    Map<String, Object> map = JsonUtil.toEntity(message, Map.class);
    Map<String, String> headers = (Map) map.get("headers");
    String body = (String) map.get("body");
    ConsumerUtils.handleByStrategy(body, headers);
}
```

## 延时消息实现对比

### RabbitMQ
- **x-delayed-message 插件**: 消息在 exchange 暂存，到期后路由到队列
- **x-dead-letter**: 消息先进延时队列，过期后转移到业务队列
- **精度**: 毫秒级
- **可靠性**: 高（持久化）

### RocketMQ
- **DelayLevel**: 18 个固定延时级别（1s~2h）
- **实现**: broker 内部定时任务
- **精度**: 秒级（固定级别）
- **可靠性**: 高（持久化）

### Redis
- **Sorted Set**: 使用时间戳作为 score 存储消息
- **定时扫描**: `@Scheduled(fixedRate = 1000)` 每秒扫描
- **精度**: 秒级（可调整扫描频率）
- **可靠性**: 低（内存存储，重启丢失）

## 性能对比

| 维度 | RabbitMQ | RocketMQ | Redis |
|------|----------|----------|-------|
| 吞吐量 | 1-10万/秒 | 10-100万/秒 | 10-100万/秒 |
| 延迟 | 微秒级 | 毫秒级 | 微秒级 |
| 持久化 | ✅ | ✅ | ❌ |
| 事务 | ✅ | ✅ | ❌ |
| 确认机制 | ✅ | ✅ | ❌ |
| 消息顺序 | ✅ | ✅ | ❌ |

## 适用场景

### RabbitMQ
- ✅ 传统企业应用
- ✅ 需要消息确认机制
- ✅ 复杂路由规则
- ✅ 中小规模系统

### RocketMQ
- ✅ 大规模分布式系统
- ✅ 高吞吐量场景
- ✅ 金融、电商等核心业务
- ✅ 需要顺序消息、事务消息

### Redis
- ✅ 轻量级任务队列
- ✅ 实时通知推送
- ✅ 缓存失效通知
- ✅ 开发测试环境
- ❌ 不适合需要高可靠性的场景

## 切换成本

得益于统一的 `MessageSender` 接口设计，切换 MQ 只需：

1. 修改配置文件中的 `template.enable.message-driven` 值
2. 配置对应 MQ 的连接信息
3. 无需修改任何业务代码

```yaml
# 切换前
template.enable.message-driven: rabbitmq

# 切换后
template.enable.message-driven: redis
```

## 总结

三种 MQ 实现完全遵循相同的设计模式和代码结构：

1. **统一接口**: 所有实现 `MessageSender` 接口
2. **统一目录**: aspect、utils、gracefulshutdown
3. **统一工具**: ConsumerUtils 提供一致的消费方法
4. **统一切面**: TraceAspect 实现链路追踪
5. **统一条件**: 基于配置的条件加载

这种设计使得：
- ✅ 切换 MQ 零成本
- ✅ 开发人员学习成本低
- ✅ 代码维护性强
- ✅ 便于测试和调试

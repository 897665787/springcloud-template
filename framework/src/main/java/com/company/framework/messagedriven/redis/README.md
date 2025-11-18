# Redis 消息队列实现

## 概述
基于 Redis 的发布订阅（Pub/Sub）和有序集合（Sorted Set）实现的消息队列功能，提供与 RabbitMQ、RocketMQ 一致的接口。

## 功能特性
- ✅ 普通消息发送与消费
- ✅ 广播消息（基于 Redis Pub/Sub）
- ✅ 延时消息（基于 Redis Sorted Set + 定时扫描）
- ✅ 消息追踪（MDC 日志追踪）
- ✅ 优雅下线（GracefulShutdown）
- ✅ 策略模式消费（Strategy Pattern）

## 配置说明

### 1. 启用 Redis 消息队列
在 `application.yml` 中配置：

```yaml
# 启用 Redis 消息队列
template:
  enable:
    message-driven: redis

# Redis 配置
spring:
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
```

### 2. 消息发送示例

```java
@Autowired
private MessageSender messageSender;

// 发送普通消息
messageSender.sendNormalMessage("userStrategy", userDto, "web-direct", "web-key-common");

// 发送广播消息
messageSender.sendFanoutMessage(userDto, "web-direct");

// 发送延时消息（30秒后执行）
messageSender.sendDelayMessage("xDelayMessageStrategy", userDto, "web-x-delayed-direct", "web-key-x-delayed", 30);
```

### 3. 消息消费示例

#### 使用 Strategy 消费

```java
@Slf4j
@Component
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class CommonConsumer {
    
    @Bean
    public MessageListener commonMessageListener() {
        return (message, pattern) -> {
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            onMessage(messageBody);
        };
    }

    @Bean
    public Object registerCommonConsumer(RedisMessageListenerContainer container, 
                                         MessageListener commonMessageListener) {
        container.addMessageListener(commonMessageListener, 
                                     new ChannelTopic("mq:channel:web-direct"));
        return new Object();
    }

    public void onMessage(String message) {
        Map<String, Object> messageMap = JsonUtil.toEntity(message, Map.class);
        Map<String, String> headers = (Map<String, String>) messageMap.get("headers");
        String body = (String) messageMap.get("body");
        
        ConsumerUtils.handleByStrategy(body, headers);
    }
}
```

## 实现原理

### 普通消息
- 使用 Redis Pub/Sub 实现
- 发送到频道：`mq:channel:{exchange}`
- 支持路由键过滤

### 延时消息
- 使用 Redis Sorted Set 存储
- Key: `mq:delay:{exchange}`
- Score: 执行时间戳
- 定时扫描器每秒扫描一次，将到期消息发布到对应频道

### 消息格式
```json
{
  "headers": {
    "strategy_name": "userStrategy",
    "params_class": "com.company.web.dto.UserDto",
    "message_id": "trace-id-xxx",
    "correlation_id": "xxx"
  },
  "body": "{\"userId\":1,\"userName\":\"test\"}",
  "routing_key": "web-key-common"
}
```

## 注意事项
1. Redis Pub/Sub 不保证消息持久化，重启后消息会丢失
2. 延时消息精度为秒级（扫描间隔 1 秒）
3. 消费者需要手动注册监听器到 RedisMessageListenerContainer
4. 适合对消息可靠性要求不高的场景
5. 如需高可靠性，建议使用 RabbitMQ 或 RocketMQ

## 对比其他 MQ

| 特性 | Redis | RabbitMQ | RocketMQ |
|------|-------|----------|----------|
| 消息持久化 | ❌ | ✅ | ✅ |
| 消息确认机制 | ❌ | ✅ | ✅ |
| 延时消息 | ✅（自实现） | ✅（插件） | ✅（原生） |
| 广播消息 | ✅ | ✅ | ✅ |
| 性能 | 极高 | 高 | 高 |
| 适用场景 | 轻量级、高性能 | 通用 | 大规模分布式 |

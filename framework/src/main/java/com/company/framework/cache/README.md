# Cache 模块

## 与 CacheManager 模块区别
Cache 模块更强调使用自定义统一缓存接口ICache来实现缓存，缓存的set、get、del更加灵活，**推荐在非方法上使用**，如

```java
@Component
public class SignInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ICache cache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (signConfiguration.nonceValid()) {
            String key = String.format("nonce:%s", noncestr);
            String value = cache.get(key);
            if (StringUtils.isNotBlank(value)) {
                ExceptionUtil.throwException("请求重复");
            }
            cache.set(key, "1", signConfiguration.getReqValidSeconds(), TimeUnit.SECONDS);
        }

        return true;
    }
}
```

## 概述

Cache模块是自定义统一缓存接口ICache多种缓存实现，支持Redis、Guava本地缓存、组合缓存等多种缓存策略。该模块提供了统一的[ICache](ICache.java)接口，便于在不同缓存实现之间切换。

## 与 CacheManager 模块区别
#### 1. 只有部分模块适用的公共代码，放在boot-starter，按需引用
#### 2. 全部模块都适用的公共代码，放在framework，减少引用工作
#### 3. 本质上全部模块都适用的公共代码也可以放在boot-starter，然后在framework中引用boot-starter


## 功能特性

### 1. 多种缓存实现
- **Redis缓存**: 基于Spring Data Redis实现的分布式缓存
- **Guava本地缓存**: 基于Google Guava Cache实现的本地内存缓存
- **组合缓存**: 结合Redis和Guava的组合缓存，Redis为主缓存，Guava为备用缓存
- **CacheManager缓存**: 基于Spring CacheManager的缓存实现

### 2. 统一接口
所有缓存实现都遵循统一的[ICache](ICache.java)接口，包含以下主要方法：
- `set(key, value)`: 设置缓存
- `get(key)`: 获取缓存
- `del(key)`: 删除缓存
- `increment(key, delta)`: 自增操作

### 3. 断路器机制
组合缓存实现了断路器机制，当主缓存(Redis)不可用时，自动降级到备用缓存(Guava)，提高系统可用性。

### 4. 灵活配置
通过配置文件可轻松切换不同的缓存实现，无需修改业务代码。

## 使用方法

### 1. 配置启用缓存
在`application-xxx.yml`中配置启用哪种缓存：

```yaml
# 启用Redis缓存
template:
  enable:
    cache: redis

# 启用Guava缓存
template:
  enable:
    cache: guava

# 启用组合缓存
template:
  enable:
    cache: combination

# 启用CacheManager缓存
template:
  enable:
    cache: cachemanager
```

### 2. 注入使用

```java
@Autowired
private ICache cache;

// 设置缓存
cache.set("key", "value");

// 获取缓存
String value = cache.get("key");

// 删除缓存
cache.del("key");

// 自增操作
long count = cache.increment("counter", 1);
```

### 3. 缓存实现详情

#### Redis缓存
基于Spring Data Redis实现，支持分布式缓存和持久化。

#### Guava本地缓存
基于Google Guava Cache实现的本地内存缓存，适用于单机应用或作为二级缓存。

#### 组合缓存
结合Redis和Guava的组合缓存实现，具有以下特点：
- 主缓存：Redis
- 备用缓存：Guava
- 断路器机制：当Redis不可用时自动降级到Guava缓存
- 高可用性：即使Redis故障也不会影响业务

#### CacheManager缓存
基于Spring CacheManager的缓存实现，可与Spring的[@Cacheable](Cacheable.java)等注解配合使用。

## 核心类说明

### [ICache](ICache.java)
统一缓存接口，定义了缓存的基本操作方法。

### [RedisCache](redis/RedisCache.java)
Redis缓存实现类，基于Spring Data Redis。

### [GuavaCache](guava/GuavaCache.java)
Guava本地缓存实现类。

### [CombinationCache](CombinationCache.java)
组合缓存实现类，支持主备缓存和断路器机制。

### [CacheManagerCache](cachemanger/CacheManagerCache.java)
基于Spring CacheManager的缓存实现类。

### [CacheAutoConfiguration](CacheAutoConfiguration.java)
缓存自动配置类，根据配置创建相应的缓存实例。

## 注意事项

1. 当使用Guava缓存时，某些方法如设置过期时间可能不会生效，因为Guava Cache不支持灵活的过期时间配置。
2. 组合缓存中的断路器机制会在Redis故障时自动切换到Guava缓存，但需要合理设置断路器参数。
3. 在高并发场景下，建议使用带有同步锁的get方法来避免缓存击穿问题。

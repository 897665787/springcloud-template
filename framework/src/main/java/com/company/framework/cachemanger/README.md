# CacheManager 模块

## 与 Cache 模块区别
CacheManager 模块更强调使用Spring Cache规范注解（@Cacheable、@CachePut、@CacheEvict）来实现缓存，**推荐在方法上使用**，如

```java
@Component
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfo> implements IService<UserInfo> {

    @Cacheable(value = "user:userinfo", key = "#id")
    public UserInfo getCacheById(Integer id) {
        return this.getById(id);
    }

    @CacheEvict(value = "user:userinfo", key = "#id")
    public void clearCacheById(Integer id) {
    }
}
```

## 概述

CacheManager 模块是基于 Spring Cache 抽象的缓存管理配置模块，主要用于配置和管理 Spring 的 CacheManager 实现。该模块提供了对缓存（如Redis、Caffeine等）的配置支持，允许应用程序使用 Spring 的标准缓存注解（如 [@Cacheable](org.springframework.cache.annotation.Cacheable.java)、[@CachePut](org.springframework.cache.annotation.CachePut.java)、[@CacheEvict](org.springframework.cache.annotation.CacheEvict.java) 等）来管理缓存。

## 核心组件

### [CacheManagerAutoConfiguration](CacheManagerAutoConfiguration.java)
启用 Spring 缓存支持的基础配置类，通过 [@EnableCaching](org.springframework.cache.annotation.EnableCaching) 注解激活 Spring 的缓存功能。

### [RedisCacheConfiguration](RedisCacheConfiguration.java)
核心将JdkSerializationRedisSerializer修改为GenericJackson2JsonRedisSerializer

## 使用方法

### 1. 配置启用

在 `application.yml` 中导入cachemanager默认配置：

```yaml
spring:
  profiles:
    include: cachemanager
```

### 2. 在业务代码中使用缓存注解

```java
@Service
public class UserService {
    
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        // 从数据库查询用户信息
        return userRepository.findById(id);
    }
    
    @CacheEvict(value = "users", key = "#user.id")
    public void updateUser(User user) {
        // 更新用户信息
        userRepository.save(user);
    }
    
    @CacheEvict(value = "users", allEntries = true)
    public void clearAllUsers() {
        // 清空所有用户缓存
    }
}
```

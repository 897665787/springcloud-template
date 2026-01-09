# Token Starter 使用指南

## 简介

Token Starter 是一个基于 Spring Boot 的自动化配置模块，提供了统一的 Token 管理服务。该 Starter 集成了 Sa-Token 和 JWT 两种 Token 实现方案，默认使用 Sa-Token 作为 Token 服务实现。

## 功能特性

### 1. 多种 Token 实现
- **Sa-Token**：功能强大的权限认证框架，默认启用
- **JWT**：基于 JSON Web Token 的轻量级实现，可选

### 2. 核心功能
- Token 生成
- Token 验证与解析
- Token 失效处理
- 多设备登录支持
- 统一的 Token 服务接口

### 3. 安全特性
- 支持 Token 过期时间配置
- 支持密钥配置
- 支持访问控制开关
- 支持同端互斥登录

## 快速开始

### 1. 添加依赖

在您的模块的 `pom.xml` 中添加以下依赖：

```xml
<!-- token -->
<dependency>
   <groupId>com.company</groupId>
   <artifactId>boot-starter-token</artifactId>
   <version>${boot-starter-token.version}</version>
</dependency>
```

### 2. 配置参数

在 `application.yml` 中导入token默认配置：

```yaml
spring:
  profiles:
    include: token
```

**如需自定义token配置**：复制[application-token.yml](src/main/resources/application-token.yml)到你的模块的 `resources` 目录下，并修改以下内容：

```yaml
token:
   # 密钥
   secret: 52ae521312f6461083435e045900486e
```

### 3. 使用 Token 服务

在需要使用 Token 服务的地方注入 `TokenService`：

```java
@Service
public class UserService {
    
    @Autowired
    private TokenService tokenService;
    
    public LoginResult login(LoginRequest request) {
        // 验证用户身份逻辑...
        User user = validateUser(request);
        
        if (user != null) {
            // 生成token
            String token = tokenService.generate(user.getId().toString(), request.getDevice());
            
            LoginResult result = new LoginResult();
            result.setToken(token);
            result.setUser(user);
            return result;
        }
        
        return null;
    }
    
    public void logout(String token) {
        // 失效token
        tokenService.invalid(token);
    }
    
    public User getCurrentUser(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader("x-token");
        
        // 验证并获取用户ID
        String userId = tokenService.checkAndGet(token);
        
        if (userId != null) {
            return userService.getById(Long.valueOf(userId));
        }
        
        return null;
    }
}
```

## 核心接口说明

### TokenService 接口

TokenService 是统一的 Token 服务接口，提供了三个核心方法：

1. **generate(String userId, String device)**：生成 Token
   - `userId`：用户唯一标识
   - `device`：登录设备类型（如 APP、WEB、MINIPROGRAM 等）
   - 返回值：生成的 Token 字符串

2. **invalid(String token)**：使 Token 失效
   - `token`：要失效的 Token
   - 返回值：登录设备类型

3. **checkAndGet(String token)**：验证 Token 并获取用户 ID
   - `token`：待验证的 Token
   - 返回值：用户 ID，如果验证失败返回 null

## 高级配置

### 1. 切换 Token 实现方案

默认使用 Sa-Token 实现，如需切换为 JWT 实现，可在 [TokenAutoConfiguration.java](src/main/java/com/company/token/TokenAutoConfiguration.java) 中修改：

```java
@Bean
@ConditionalOnMissingBean
public TokenService tokenService() {
    // 使用 JWT 实现
    TokenService tokenService = new JsonWebTokenService();
    // 使用 Sa-Token 实现（默认）
    // TokenService tokenService = new SaTokenService();
    return tokenService;
}
```

### 2. 访问控制配置

可以通过配置项控制是否启用访问控制：

```yaml
template:
  enable:
    access-control: true  # 默认为true，设为false时不会校验token有效性
```

### 3. 多环境密钥配置

不同环境使用不同的密钥：

```yaml
---
# dev环境配置
spring:
  config:
    activate:
      on-profile: dev
token:
  secret: dev-secret-key

---
# prod环境配置
spring:
  config:
    activate:
      on-profile: prod
token:
  secret: prod-secret-key
```

## Sa-Token 特性配置

当使用 Sa-Token 作为实现时，可以配置以下参数：

```yaml
sa-token:
  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒
  active-timeout: -1
  # 是否输出操作日志
  is-log: false
  # jwt秘钥
  jwt-secret-key: ${token.secret}
```

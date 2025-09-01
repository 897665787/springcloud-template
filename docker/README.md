# Spring Cloud 微服务 Docker 部署指南

## 概述

本项目为Spring Cloud微服务架构提供了完整的Docker部署解决方案，包含以下特性：

- ✅ **链路追踪**: 集成SkyWalking Agent
- ✅ **线程上下文传递**: 集成阿里TTL (TransmittableThreadLocal)
- ✅ **JVM优化**: 生产级JVM参数配置
- ✅ **异常处理**: 完善的日志记录和异常处理
- ✅ **健康检查**: 内置健康检查机制
- ✅ **监控集成**: 支持Prometheus JMX Exporter

## 项目结构

```
docker/
├── base/                          # 基础镜像
│   ├── Dockerfile                 # 基础镜像Dockerfile
│   └── scripts/                   # 通用脚本
│       ├── entrypoint.sh          # 启动脚本
│       └── healthcheck.sh         # 健康检查脚本
├── generate-dockerfiles.sh        # 批量生成Dockerfile脚本
├── docker-compose.yml             # 完整服务编排文件
└── README.md                      # 本文档
```

## 快速开始

### 1. 构建基础镜像

```bash
# 进入docker目录
cd docker

# 构建基础镜像
docker build -t your-org/springcloud-base:latest base/
```

### 2. 批量生成服务Dockerfile

```bash
# 给脚本执行权限
chmod +x generate-dockerfiles.sh

# 执行批量生成脚本
./generate-dockerfiles.sh
```

### 3. 构建服务镜像

```bash
# 构建订单服务
docker build -t your-org/order-service:latest ../order/service/

# 构建用户服务
docker build -t your-org/user-service:latest ../user/service/

# 构建系统服务
docker build -t your-org/system-service:latest ../system/service/

# 构建网关服务
docker build -t your-org/gateway-service:latest ../gateway/

# 构建Eureka服务
docker build -t your-org/eureka-server:latest ../eureka/
```

### 4. 使用Docker Compose部署

```bash
# 启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看服务日志
docker-compose logs -f order-service

# 停止所有服务
docker-compose down
```

## 服务列表

| 服务名称 | 端口 | 内存配置 | 说明 |
|---------|------|----------|------|
| eureka-server | 7001 | 1g-2g | 服务注册中心 |
| config-server | 7002 | 1g-2g | 配置中心 |
| gateway-service | 7003 | 2g-4g | API网关 |
| user-service | 7004 | 1g-2g | 用户服务 |
| order-service | 7005 | 2g-4g | 订单服务 |
| system-service | 7006 | 1g-2g | 系统服务 |
| monitor-service | 7007 | 1g-2g | 监控服务 |
| job-service | 7008 | 1g-2g | 定时任务服务 |
| tool-service | 7009 | 1g-2g | 工具服务 |
| im-service | 7010 | 1g-2g | IM服务 |
| admin-service | 7011 | 1g-2g | 管理后台 |
| admin-api-service | 7012 | 1g-2g | 管理API |
| web-service | 7013 | 1g-2g | Web前端 |
| app-service | 7014 | 1g-2g | 移动端API |
| openapi-service | 7015 | 1g-2g | 开放API |
| xxl-job-admin | 7016 | 1g-2g | XXL-Job管理端 |
| xxl-job-executor | 7017 | 512m-1g | XXL-Job执行器 |

## 核心特性详解

### 1. SkyWalking 链路追踪

每个服务都集成了SkyWalking Agent，配置如下：

```bash
-javaagent:/app/plugins/skywalking-agent.jar
-Dskywalking.agent.service_name=${APP_NAME}
-Dskywalking.collector.backend_service=skywalking-oap:11800
-Dskywalking.logging.level=INFO
-Dskywalking.logging.dir=/app/logs
```

### 2. 阿里TTL集成

支持线程池场景下的ThreadLocal上下文传递：

```bash
-Xbootclasspath/a:/app/plugins/transmittable-thread-local-2.14.5.jar
```

### 3. JVM优化参数

#### 内存配置
- **堆内存**: 根据服务类型配置不同的堆大小
- **元空间**: 128m-512m，避免元空间OOM
- **G1GC**: 使用G1垃圾收集器，优化停顿时间

#### GC优化
```bash
-XX:+UseG1GC                           # 使用G1垃圾收集器
-XX:MaxGCPauseMillis=200              # 最大GC停顿时间200ms
-XX:G1HeapRegionSize=16m              # G1区域大小
-XX:G1NewSizePercent=30               # 新生代最小占比
-XX:G1MaxNewSizePercent=40            # 新生代最大占比
```

#### 日志和监控
```bash
-XX:+PrintGCDetails                   # 打印详细GC日志
-XX:+PrintGCDateStamps               # 打印GC时间戳
-Xloggc:/app/logs/gc.log             # GC日志文件路径
-XX:+UseGCLogFileRotation            # 启用GC日志轮转
-XX:NumberOfGCLogFiles=5             # 保留5个GC日志文件
-XX:GCLogFileSize=100M               # 每个GC日志文件100M
```

### 4. 异常处理和日志记录

#### 启动脚本特性
- 启动时间记录
- JAR文件存在性检查
- 环境变量验证
- 启动参数记录
- 异常退出日志记录

#### 日志目录结构
```
/app/logs/
├── startup.log          # 启动日志
├── error.log            # 错误日志
├── health.log           # 健康检查日志
├── gc.log               # GC日志
└── heapdump.hprof       # 堆转储文件
```

### 5. 健康检查

内置健康检查机制：

```bash
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD /app/scripts/healthcheck.sh
```

- **检查间隔**: 30秒
- **超时时间**: 10秒
- **启动等待**: 60秒
- **重试次数**: 3次

## 环境变量配置

### 基础环境变量
```bash
APP_NAME=service-name           # 服务名称
APP_PROFILE=prod               # 环境配置
JVM_OPTS=...                   # JVM参数
SKYWALKING_OPTS=...            # SkyWalking参数
TTL_OPTS=...                   # TTL参数
PROMETHEUS_OPTS=...            # Prometheus参数
```

### 服务特定配置
```bash
# Eureka客户端配置
EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://eureka-server:8080/eureka/

# 数据库配置
DB_HOST=mysql
DB_PORT=3306
DB_NAME=springcloud
DB_USERNAME=root
DB_PASSWORD=password

# Redis配置
REDIS_HOST=redis
REDIS_PORT=6379
REDIS_PASSWORD=
```

## 部署建议

### 1. 生产环境配置

```bash
# 设置生产环境配置
export APP_PROFILE=prod
export JVM_OPTS="-Xms4g -Xmx8g -XX:MaxGCPauseMillis=100"

# 构建生产镜像
docker build --build-arg APP_PROFILE=prod -t your-org/service:prod .
```

### 2. 资源限制

```yaml
# docker-compose.yml 资源限制示例
services:
  order-service:
    deploy:
      resources:
        limits:
          memory: 6G
          cpus: '2.0'
        reservations:
          memory: 4G
          cpus: '1.0'
```

### 3. 网络配置

```yaml
# 自定义网络配置
networks:
  springcloud-network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.20.0.0/16
```

### 4. 存储配置

```yaml
# 持久化存储配置
volumes:
  service-logs:
    driver: local
    driver_opts:
      type: none
      o: bind
      device: /data/logs/service
```

## 监控和运维

### 1. 日志收集

```bash
# 查看实时日志
docker-compose logs -f service-name

# 导出日志
docker-compose logs service-name > service.log

# 清理日志
docker system prune -f
```

### 2. 性能监控

- **JVM监控**: 通过JMX Exporter暴露JVM指标
- **应用监控**: 集成Spring Boot Actuator
- **链路追踪**: SkyWalking UI查看调用链
- **日志分析**: ELK Stack分析日志

### 3. 故障排查

```bash
# 进入容器调试
docker exec -it container-name /bin/bash

# 查看JVM进程
jps -l

# 查看JVM参数
jinfo -sysprops pid

# 生成堆转储
jmap -dump:format=b,file=heapdump.hprof pid
```

## 常见问题

### 1. 内存不足
```bash
# 增加JVM堆内存
export JVM_OPTS="-Xms2g -Xmx4g"

# 检查容器资源使用
docker stats
```

### 2. 启动失败
```bash
# 查看启动日志
docker logs container-name

# 检查健康状态
docker inspect container-name | grep Health
```

### 3. 网络连接问题
```bash
# 检查网络连通性
docker exec container-name ping service-name

# 查看网络配置
docker network inspect springcloud-network
```

## 最佳实践

1. **镜像分层**: 合理利用Docker缓存层，提高构建效率
2. **资源限制**: 为每个服务设置合理的资源限制
3. **健康检查**: 配置合适的健康检查策略
4. **日志管理**: 实现日志轮转和清理策略
5. **监控告警**: 集成监控系统，设置告警规则
6. **备份策略**: 定期备份配置和数据
7. **滚动更新**: 使用滚动更新策略，确保服务可用性

## 技术支持

如有问题，请参考：

- [Spring Boot Docker指南](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker官方文档](https://docs.docker.com/)
- [SkyWalking官方文档](https://skywalking.apache.org/docs/)
- [阿里TTL项目](https://github.com/alibaba/transmittable-thread-local)

---

**注意**: 请根据实际生产环境调整配置参数，特别是内存、CPU和网络配置。

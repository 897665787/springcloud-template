# Spring Cloud微服务基础镜像
FROM openjdk:8-jre-alpine

# 设置元数据
LABEL maintainer="your-email@company.com"
LABEL description="Spring Cloud Microservice Base Image"
LABEL version="1.0.0"

# 安装必要的工具
RUN apk add --no-cache \
    curl \
    tzdata \
    bash \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

# 创建应用用户（安全最佳实践）
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

# 创建必要的目录
RUN mkdir -p /app/logs /app/plugins /app/config /app/tmp /app/scripts && \
    chown -R appuser:appuser /app

# 设置工作目录
WORKDIR /app

# 复制通用脚本
COPY scripts/entrypoint.sh /app/scripts/
COPY scripts/healthcheck.sh /app/scripts/

# 设置脚本权限
RUN chmod +x /app/scripts/*.sh

# 设置环境变量
ENV JAVA_HOME="/usr/lib/jvm/java-1.8-openjdk" \
    PATH="/usr/lib/jvm/java-1.8-openjdk/bin:$PATH" \
    TZ="Asia/Shanghai"

# 切换到应用用户
USER appuser

# 暴露端口
EXPOSE 8080

# 设置卷挂载点
VOLUME ["/app/logs", "/app/config", "/app/tmp"]

# 设置标签
LABEL org.opencontainers.image.source="https://github.com/your-org/springcloud-template" \
      org.opencontainers.image.description="Spring Cloud Microservice Base Image" \
      org.opencontainers.image.licenses="Apache-2.0"

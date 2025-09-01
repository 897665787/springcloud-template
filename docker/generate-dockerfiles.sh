#!/bin/bash

# 批量生成Spring Boot服务的Dockerfile脚本

# 定义服务列表
SERVICES=(
    "config:config-service:1g:2g"
    "monitor:monitor-service:1g:2g"
    "job:job-service:1g:2g"
    "im/service:im-service:1g:2g"
    "adminapi:admin-api-service:1g:2g"
    "web:web-service:1g:2g"
    "app:app-service:1g:2g"
    "admin:admin-service:1g:2g"
    "tool/service:tool-service:1g:2g"
    "xxl-job/xxl-job-admin:xxl-job-admin:1g:2g"
    "xxl-job/xxl-job-executor-samples/xxl-job-executor-sample-springboot:xxl-job-executor:512m:1g"
    "openapi:openapi-service:1g:2g"
)

# 创建Dockerfile模板函数
create_dockerfile() {
    local service_path=$1
    local service_name=$2
    local min_heap=$3
    local max_heap=$4
    
    local dockerfile_path="${service_path}/Dockerfile"
    
    cat > "$dockerfile_path" << EOF
# ${service_name} Dockerfile
FROM maven:3.8.6-openjdk-8 AS builder

# 设置工作目录
WORKDIR /app

# 复制Maven配置文件
COPY pom.xml .
COPY */pom.xml */pom.xml
COPY */*/pom.xml */*/pom.xml

# 下载依赖（利用Docker缓存层）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY . .

# 构建${service_name}
RUN mvn clean package -pl ${service_path} -am -DskipTests

# 运行阶段
FROM your-org/springcloud-base:latest

# 设置服务名称
ENV APP_NAME="${service_name}" \\
    APP_PROFILE="prod"

# 复制SkyWalking Agent
COPY --from=builder /app/plugins/skywalking-agent/skywalking-agent.jar /app/plugins/
COPY --from=builder /app/plugins/ttl/transmittable-thread-local-2.14.5.jar /app/plugins/

# 复制Prometheus JMX Exporter（可选）
COPY --from=builder /app/plugins/prometheus/jmx_prometheus_javaagent-1.0.1.jar /app/plugins/
COPY --from=builder /app/plugins/prometheus/jmx_prometheus_javaagent-config.yaml /app/plugins/

# 复制构建好的JAR文件
COPY --from=builder /app/${service_path}/target/*.jar /app/app.jar

# 设置文件权限
RUN chown -R appuser:appuser /app

# 设置JVM参数
ENV JVM_OPTS="\\
    -server \\
    -Xms${min_heap} \\
    -Xmx${max_heap} \\
    -XX:MetaspaceSize=128m \\
    -XX:MaxMetaspaceSize=256m \\
    -XX:+UseG1GC \\
    -XX:MaxGCPauseMillis=200 \\
    -XX:G1HeapRegionSize=16m \\
    -XX:G1NewSizePercent=30 \\
    -XX:G1MaxNewSizePercent=40 \\
    -XX:G1MixedGCCountTarget=8 \\
    -XX:+UnlockExperimentalVMOptions \\
    -XX:+UseCGroupMemoryLimitForHeap \\
    -XX:+HeapDumpOnOutOfMemoryError \\
    -XX:HeapDumpPath=/app/logs/heapdump.hprof \\
    -XX:+PrintGCDetails \\
    -XX:+PrintGCDateStamps \\
    -XX:+PrintGCTimeStamps \\
    -Xloggc:/app/logs/gc.log \\
    -XX:+UseGCLogFileRotation \\
    -XX:NumberOfGCLogFiles=5 \\
    -XX:GCLogFileSize=100M \\
    -Djava.awt.headless=true \\
    -Dfile.encoding=UTF-8 \\
    -Duser.timezone=Asia/Shanghai \\
    -Djava.security.egd=file:/dev/./urandom \\
    -Dspring.profiles.active=\${APP_PROFILE} \\
    -Dapp.name=\${APP_NAME}"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \\
    CMD /app/scripts/healthcheck.sh

# 设置启动命令
CMD ["/app/scripts/entrypoint.sh"]
EOF

    echo "✅ 已创建: ${dockerfile_path}"
}

# 主函数
main() {
    echo "🚀 开始批量生成Spring Boot服务的Dockerfile..."
    echo ""
    
    # 检查基础镜像是否存在
    if [ ! -f "docker/base/Dockerfile" ]; then
        echo "❌ 错误: 基础镜像Dockerfile不存在，请先创建 docker/base/Dockerfile"
        exit 1
    fi
    
    # 检查脚本文件是否存在
    if [ ! -f "docker/base/scripts/entrypoint.sh" ] || [ ! -f "docker/base/scripts/healthcheck.sh" ]; then
        echo "❌ 错误: 启动脚本不存在，请先创建 docker/base/scripts/ 目录下的脚本文件"
        exit 1
    fi
    
    # 遍历服务列表生成Dockerfile
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_path service_name min_heap max_heap <<< "$service_info"
        
        # 检查服务目录是否存在
        if [ -d "$service_path" ]; then
            create_dockerfile "$service_path" "$service_name" "$min_heap" "$max_heap"
        else
            echo "⚠️  警告: 服务目录不存在，跳过: ${service_path}"
        fi
    done
    
    echo ""
    echo "🎉 批量生成完成！"
    echo ""
    echo "📋 已生成的服务列表:"
    for service_info in "${SERVICES[@]}"; do
        IFS=':' read -r service_path service_name min_heap max_heap <<< "$service_info"
        if [ -d "$service_path" ]; then
            echo "   - ${service_name} (${service_path}/Dockerfile)"
        fi
    done
    echo ""
    echo "🔧 下一步操作:"
    echo "   1. 构建基础镜像: docker build -t your-org/springcloud-base:latest docker/base/"
    echo "   2. 构建各服务镜像: docker build -t your-org/${service_name}:latest ${service_path}/"
    echo "   3. 使用docker-compose或kubernetes部署"
}

# 执行主函数
main "$@"

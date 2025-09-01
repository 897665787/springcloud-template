# 用户服务 Dockerfile
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

# 构建用户服务
RUN mvn clean package -pl user/service -am -DskipTests

# 运行阶段
FROM your-org/springcloud-base:latest

# 设置服务名称
ENV APP_NAME="user-service" \
    APP_PROFILE="prod"

# 复制SkyWalking Agent
COPY --from=builder /app/plugins/skywalking-agent/skywalking-agent.jar /app/plugins/
COPY --from=builder /app/plugins/ttl/transmittable-thread-local-2.14.5.jar /app/plugins/

# 复制Prometheus JMX Exporter（可选）
COPY --from=builder /app/plugins/prometheus/jmx_prometheus_javaagent-1.0.1.jar /app/plugins/
COPY --from=builder /app/plugins/prometheus/jmx_prometheus_javaagent-config.yaml /app/plugins/

# 复制构建好的JAR文件
COPY --from=builder /app/user/service/target/*.jar /app/app.jar

# 设置文件权限
RUN chown -R appuser:appuser /app

# 设置JVM参数（用户服务中等内存需求）
ENV JVM_OPTS="\
    -server \
    -Xms1g \
    -Xmx2g \
    -XX:MetaspaceSize=128m \
    -XX:MaxMetaspaceSize=256m \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=200 \
    -XX:G1HeapRegionSize=16m \
    -XX:G1NewSizePercent=30 \
    -XX:G1MaxNewSizePercent=40 \
    -XX:G1MixedGCCountTarget=8 \
    -XX:+UnlockExperimentalVMOptions \
    -XX:+UseCGroupMemoryLimitForHeap \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/app/logs/heapdump.hprof \
    -XX:+PrintGCDetails \
    -XX:+PrintGCDateStamps \
    -XX:+PrintGCTimeStamps \
    -Xloggc:/app/logs/gc.log \
    -XX:+UseGCLogFileRotation \
    -XX:NumberOfGCLogFiles=5 \
    -XX:GCLogFileSize=100M \
    -Djava.awt.headless=true \
    -Dfile.encoding=UTF-8 \
    -Duser.timezone=Asia/Shanghai \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=${APP_PROFILE} \
    -Dapp.name=${APP_NAME}"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD /app/scripts/healthcheck.sh

# 设置启动命令
CMD ["/app/scripts/entrypoint.sh"]
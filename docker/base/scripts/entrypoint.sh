#!/bin/bash
set -e

# 记录启动时间
echo "$(date '+%Y-%m-%d %H:%M:%S'): Starting ${APP_NAME:-unknown-service}..." >> /app/logs/startup.log

# 检查JAR文件
if [ ! -f /app/app.jar ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S'): ERROR - app.jar not found!" >> /app/logs/startup.log
    echo "$(date '+%Y-%m-%d %H:%M:%S'): ERROR - app.jar not found!" >> /app/logs/error.log
    exit 1
fi

# 检查必要的环境变量
if [ -z "$APP_NAME" ]; then
    echo "$(date '+%Y-%m-%d %H:%M:%S'): WARNING - APP_NAME not set, using default" >> /app/logs/startup.log
    export APP_NAME="unknown-service"
fi

# 设置默认JVM参数
if [ -z "$JVM_OPTS" ]; then
    export JVM_OPTS="\
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
        -XX:+HeapDumpOnOutOfMemoryError \
        -XX:HeapDumpPath=/app/logs/heapdump.hprof \
        -XX:+PrintGCDetails \
        -XX:+PrintGCDateStamps \
        -Xloggc:/app/logs/gc.log \
        -XX:+UseGCLogFileRotation \
        -XX:NumberOfGCLogFiles=5 \
        -XX:GCLogFileSize=100M \
        -Djava.awt.headless=true \
        -Dfile.encoding=UTF-8 \
        -Duser.timezone=Asia/Shanghai \
        -Djava.security.egd=file:/dev/./urandom"
fi

# 设置SkyWalking参数
if [ -z "$SKYWALKING_OPTS" ] && [ -f /app/plugins/skywalking-agent.jar ]; then
    export SKYWALKING_OPTS="\
        -javaagent:/app/plugins/skywalking-agent.jar \
        -Dskywalking.agent.service_name=${APP_NAME} \
        -Dskywalking.collector.backend_service=skywalking-oap:11800 \
        -Dskywalking.logging.level=INFO \
        -Dskywalking.logging.dir=/app/logs \
        -Dskywalking.agent.instance_name=${APP_NAME}-${HOSTNAME:-unknown} \
        -Dskywalking.agent.span_limit_per_segment=300 \
        -Dskywalking.agent.ignore_suffix=.jpg,.jpeg,.js,.css,.png,.bmp,.gif,.ico,.mp3,.mp4,.html,.svg"
fi

# 设置TTL参数
if [ -f /app/plugins/transmittable-thread-local-2.14.5.jar ]; then
    export TTL_OPTS="-Xbootclasspath/a:/app/plugins/transmittable-thread-local-2.14.5.jar"
fi

# 设置Prometheus JMX Exporter参数（可选）
if [ -f /app/plugins/jmx_prometheus_javaagent-1.0.1.jar ] && [ -f /app/plugins/jmx_prometheus_javaagent-config.yaml ]; then
    export PROMETHEUS_OPTS="-javaagent:/app/plugins/jmx_prometheus_javaagent-1.0.1.jar=8080:/app/plugins/jmx_prometheus_javaagent-config.yaml"
fi

# 记录启动参数
echo "$(date '+%Y-%m-%d %H:%M:%S'): JVM_OPTS: $JVM_OPTS" >> /app/logs/startup.log
echo "$(date '+%Y-%m-%d %H:%M:%S'): SKYWALKING_OPTS: $SKYWALKING_OPTS" >> /app/logs/startup.log
echo "$(date '+%Y-%m-%d %H:%M:%S'): TTL_OPTS: $TTL_OPTS" >> /app/logs/startup.log
echo "$(date '+%Y-%m-%d %H:%M:%S'): PROMETHEUS_OPTS: $PROMETHEUS_OPTS" >> /app/logs/startup.log

# 启动应用
echo "$(date '+%Y-%m-%d %H:%M:%S'): Starting application..." >> /app/logs/startup.log

# 使用exec确保进程能够接收信号
exec java $JVM_OPTS $SKYWALKING_OPTS $TTL_OPTS $PROMETHEUS_OPTS $JAVA_OPTS -jar /app/app.jar

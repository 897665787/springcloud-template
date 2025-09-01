#!/bin/bash

# 健康检查脚本
# 检查应用是否正常运行

# 获取健康检查端点（可配置）
HEALTH_ENDPOINT=${HEALTH_ENDPOINT:-"/actuator/health"}
PORT=${PORT:-8080}

# 尝试连接健康检查端点
if curl -f -s "http://localhost:${PORT}${HEALTH_ENDPOINT}" > /dev/null 2>&1; then
    exit 0
else
    # 记录健康检查失败
    echo "$(date '+%Y-%m-%d %H:%M:%S'): Health check failed for ${APP_NAME:-unknown-service}" >> /app/logs/health.log
    exit 1
fi

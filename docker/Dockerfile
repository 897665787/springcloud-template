# 使用官方的OpenJDK 8镜像作为基础镜像
FROM openjdk:8-jre-slim

# 在镜像中创建一个目录存放我们的应用
VOLUME /tmp

# 指定时区
ENV TZ=Asia/Shanghai LANG=C.UTF-8 LC_ALL=C.UTF-8
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 安装一些镜像中没有的软件
RUN apt-get update && apt-get install -y less vim net-tools procps

# 将依赖的插件添加到容器中
RUN mkdir -p /plugins
COPY plugins /plugins

# 使用官方的OpenJDK 8镜像作为基础镜像
FROM openjdk:8-jre-slim

# 在镜像中创建一个目录存放我们的应用
VOLUME /tmp

# 指定时区
ENV TZ=Asia/Shanghai LANG=C.UTF-8 LC_ALL=C.UTF-8
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 将jar文件添加到容器中并更名为app.jar
COPY target/template-*.jar app.jar

# 暴露容器内的端口给外部访问
EXPOSE 6201

# 在容器启动时运行jar包
ENTRYPOINT ["java"\
,"-Djava.security.egd=file:/dev/./urandom"\
,"-jar","/app.jar"\
,"--spring.profiles.active=dev"\
,"--eureka.client.service-url.defaultZone=http://host.docker.internal:7001/eureka/"\
]
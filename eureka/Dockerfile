# 使用官方的OpenJDK 8镜像作为基础镜像
FROM openjdk:8-jdk-alpine

# 在镜像中创建一个目录存放我们的应用
VOLUME /tmp

# 将jar文件添加到容器中并更名为app.jar
COPY target/template-*.jar app.jar

# 暴露容器内的端口给外部访问
EXPOSE 7001

# 在容器启动时运行jar包
ENTRYPOINT ["java"\
,"-Djava.security.egd=file:/dev/./urandom"\
,"-jar","/app.jar"\
,"--spring.profiles.active=dev"\
]


#FROM ：指定基础镜像
#MAINTAINER ：维护者信息
#RUN ：构建镜像时所需要执行的命令
#CMD ：用于容器在真正运行后所需要执行的命令，CMD 和 RUN 其实都是执行命令，但是 CMD 用于最后面 注：每个 Dockerfile 只能有一条 CMD 命令。如果指定了多条 CMD 命令，只有最后一条会被执行。
#ENTRYPOINT ：其实和 CMD 差不多，具体区别：CMD # 指定容器启动时要执行的命令，只有最后一个会生效，可被替代 ；ENTRYPOINT # 指定容器启动时 要执行的命令,可以追加命令。注：每个 Dockerfile 只能有一条 ENTRYPOINT命令，如果指定了多条 ENTRYPOINT 命令，只有最后一条会被执行。
#ADD ：复制文件或目录到容器，如果是压缩文件，会自动解压
#COPY ：拷贝文件到容器内，与 ADD 一样但不能解压
#LABEL ：用于为镜像添加元数据，一个标识
#ENV ：设置环境变量
#EXPOSE ：指定于外界交互的端口
#VOLUME ：文件挂载，容器与宿主机之间的文件共享功能，等同于 docker run 时的参数-v
#WORKDIR ：工作目录相当于 cd
#USER ：指定运行容器时的用户名ARG ：设置变量
#ONBUILD ：该指令只有在当该镜像被用作其他镜像的基础镜像时，才会生效
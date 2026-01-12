# 构建基础镜像
构建命令：docker build -t springcloud-template/base-image:{version} .
```
demo：docker build -t springcloud-template/base-image:v1 .
```

# 版本日志
### v1
```
1. 基于openjdk:8-jre-slim构建基础镜像
2. 在镜像中创建一个目录存放我们的应用
3. 指定时区
4. 安装一些镜像中没有的软件
5. 将依赖的插件添加到容器中
```

# 请先使用根目录docker/Dockerfile构建基础镜像
FROM springcloud-template/base-image:v1

# 将jar文件添加到容器中并更名为app.jar
COPY target/template-*.jar app.jar

# 暴露容器内的端口给外部访问，也可以在docker run中使用‘-p 外端口:内端口’指定
EXPOSE 8020

# 在容器启动时运行jar包
ENTRYPOINT ["java"\
# 避免意外阻塞代码，urandom安全性没有random高，默认：file:/dev/random
,"-Djava.security.egd=file:/dev/./urandom"\

# 最小堆，默认：物理内存1/64（最小1MB），最大堆，默认：物理内存1/4（最大1GB）
,"-Xms512M","-Xmx2048M"\
# 新生代大小，默认：堆内存1/3（需显式设置）
,"-Xmn512M"\
# 新老年代分配比例，默认情况下，新生代与老年代比例为1:2。NewRatio默认值是2。如果NewRatio修改成3，那么新生代与老年代比例就是1:3
,"-XX:NewRatio=2"\
# 新生代分配比例，默认情况下Eden、From、To的比例是8:1:1。SurvivorRatio默认值是8，如果SurvivorRatio修改成4，那么其比例就是4:1:1
,"-XX:SurvivorRatio=8"\
# 新生代对象直接进入老年代阈值，默认：15
,"-XX:MaxTenuringThreshold=15"\

# 每个线程的堆栈大小，默认：1M
,"-Xss1024K"\

# 元空间，默认：21MB
,"-XX:MetaspaceSize=21M"\
# 元空间，最大空间，默认：没有限制
#,"-XX:MaxMetaspaceSize=2048M"\
# 在GC之后，最小的Metaspace剩余空间容量的百分比，默认40%，小于此值增大元空间，最大的Metaspace剩余空间容量的百分比，默认70%，大于此值缩小元空间
,"-XX:MinMetaspaceFreeRatio=40","-XX:MaxMetaspaceFreeRatio=70"\

# 使用CMS垃圾回收器，在HotSpot JVM中，这个参数的默认值通常是9。这意味着默认情况下，JVM会尝试将垃圾收集的时间限制在总运行时间的1/(1+9) = 10%以下。换句话说，JVM会尽可能地让程序运行时间占到总时间的90%，而将剩余的10%用于垃圾收集。
,"-XX:+UseConcMarkSweepGC","-XX:GCTimeRatio=9"\
# 使用G1垃圾回收器，预期停顿时间，默认：200ms，新生代最小占比，默认：5%，新生代最大占比，默认：60%
#,"-XX:+UseG1GC","-XX:MaxGCPauseMillis=200","-XX:+UnlockExperimentalVMOptions","-XX:G1NewSizePercent=5","-XX:G1MaxNewSizePercent=60"\

# 输出详细GC日志
,"-XX:+PrintGCDetails","-XX:+PrintGCDateStamps","-XX:+PrintHeapAtGC","-Xloggc:./logs/gc-%p.log"\
# 发生OOM时生成Dump文件
,"-XX:+HeapDumpOnOutOfMemoryError","-XX:HeapDumpPath=./logs/oom-heapdump-%p.hprof"\
# 发生致命错误时，记录错误信息
,"-XX:ErrorFile=./logs/hs_err_pid-%p.log"\
# 发生致命错误时，记录栈信息、堆信息
,"-XX:OnError=\"jstack %p > ./logs/error-jstack-%p.log;jmap -dump:format=b,file=./logs/error-heapdump-%p.hprof %p\""\

# 阿里TTL，有功能性支持线程池传递上下文
,"-javaagent:plugins/ttl/transmittable-thread-local-2.14.5.jar"\
# skywalking日志追踪
,"-javaagent:plugins/skywalking-agent/skywalking-agent.jar" \
,"-Dskywalking.agent.service_name=springcloud-template::template-tool" \
,"-Dskywalking.collector.backend_service=127.0.0.1:11800" \
# jmx监控
,"-javaagent:plugins/prometheus/jmx_prometheus_javaagent-1.0.1.jar=28020:plugins/prometheus/jmx_prometheus_javaagent-config.yaml" \

,"-jar","/app.jar"\
,"--spring.profiles.active=dev"\
,"--eureka.client.service-url.defaultZone=http://host.docker.internal:7010/eureka/"\
]

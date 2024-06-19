# SpringBoot 中 基于 MDC 实现日志的链路追踪

本示例 演示的是 Slf4j MDC + TTL (TransmittableThreadLocal) 自定义实现链路追踪能力

### 链路追踪能解决什么问题

在实际项目中，一个方法在执行的过程中，往往需要打印多个日志。我们往往需要顺序的区看一些日志才能更好的排查问题

但是，在 spring 项目中，大部分的方法执行都是并发的，致使打印的内容狠杂乱，导致我们无法很快通过日志来排查问题

链路追踪就可以很好的解决这个问题，我们只需要找到 唯一的 traceId，然后通过 traceId 来查询其他日志的

### 什么是 MDC

> [SLF4J 官网](https://www.slf4j.org/)
>
> [SLF4J MDC](https://www.slf4j.org/api/org/slf4j/MDC.html)

MDC（Mapped Diagnostic Context，映射调试上下文）是 slf4j 提供的一种轻量级的日志跟踪工具。

Log4j、Logback或者Log4j2等日志中最常见区分同一个请求的方式是通过线程名，而如果请求量大，线程名在相近的时间内会有很多重复的而无法分辨，因此引出了trace-id，即在接收到的时候生成唯一的请求id，在整个执行链路中带上此唯一id.

MDC.java本身不提供传递traceId的能力，真正提供能力的是MDCAdapter接口的实现。

比如Log4j的是Log4jMDCAdapter，Logback的是LogbackMDCAdapter。

没错，是不是有点那个味了，其实底层还是用的 ThreadLocal

### 问题

正因为上述底层他是基于 ThreadLocal 实现，所以在使用多线程的时候，还是会导致有问题的出现（父子线程传值问题）

那么这时候我们应该怎么做呢？

这时候我们就需要自己实现一个 MDC Adapter 了，借助于 阿里的 ttl

```xml

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>transmittable-thread-local</artifactId>
    <version>2.11.5</version>
    <scope>compile</scope>
</dependency>
```

具体的实现，参考了类 `top.bulk.mdc.adapter.TtlMDCAdapter`

光写完 Adapter 还不算完成，还要配置其生效，参考项目中写的 `org.slf4j.impl.StaticMDCBinder`

### 如何正确的使用

我们需要再程序的入口处，去进行 MDC traceId 的生成或者继承

一般来说，是通过各种组件的拦截器或者过滤器实现

简单理解，就是不管通过什么方式进入到我们的程序，都要生成或者继承 trace-id 到 MDC 中去

```java
// 进入程序，设置 TraceId 到 MDC
MDC.put("TraceId",traceId);

// 执行相关业务逻辑 打印日志
        log.info("Log message");

// 访问完毕 清除 MDC
        MDC.remove("TraceId");
```

servlet，参考类 `top.bulk.mdc.interceptor.LogInterceptor`

还有其他的程序入口，例如

http（openFeign,httpClient restTemplate）、rpc(Dubbo)、mq（RabbitMQ） 等


### 测试

注意本项目启动，需要再启动参数中配置 `-javaagent:E:\tools\maven\repository\com\alibaba\transmittable-thread-local\2.14.5\transmittable-thread-local-2.14.5.jar`

地址改为你自己的

之后访问: `http://localhost:8080/test1?id=1111` 观察控制台输出

### 题外话

这是我们自己实现，也属于造轮子了，毕竟业内有 skywalking zipkin 等优秀的框架

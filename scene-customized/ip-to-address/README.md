# 本示例演示的是如果从请求中获取用户ip并且转换为地址

> 这里使用的是 ip2region.xdb 去做离线计算
>
> 参考地址 https://gitee.com/lionsoul/ip2region/tree/master/binding/java

核心包

```xml

<dependency>
    <groupId>org.lionsoul</groupId>
    <artifactId>ip2region</artifactId>
    <version>2.7.0</version>
</dependency>

```

我们需要做的一般有两步

1. 获取ip
2. 根据ip转地址

### 核心类

参考类
`top.bulk.util.IpAddressUtil`

### 测试

参考类
`IpAddressUtilTest.java`
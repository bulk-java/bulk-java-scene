# Spring Boot 项目中自定义实现限流

在很多业务场景下，我们的接口访问，都是需要频率控制的，然而这一类问题问问有相同的解决方案

### 方案

本示例通过自定义注解，来实现限流 `top.bulk.ratelimit.annotation.RateLimiter`

通过对 `top.bulk.ratelimit.handle.RateLimiterHandler` 接口的不同实现，来匹配多种限流算法

#### 限流维度

基于什么维度去进行限流，比如

1. IP ，比如一个 IP 一秒最多十次
2. 用户，比如一个用户一秒最多十次
3. 资源(接口)，比如一个资源一秒最多十次
4. ...

参考枚类 `top.bulk.ratelimit.constant.KeyStrategyEnum`

#### 限流算法：

市面上，限流的方案有很多(实际生产环境建议使用成熟的中间件或者自己写lua脚本方式)

1. 基于固定窗口限流算法限流

2. 滑动窗口算法限流

3. 基于漏桶算法限流

4. 基于令牌桶算法限流。
    1. Guava RateLimiter 不过是单机的
    2. 基于 redis

### 使用 & 测试

参考 `top.bulk.ratelimit.controller.TestController`

```java
    @GetMapping("t6-2")
    @RateLimiter(max = 1, time = 1, strategy = RateLimiterEnum.TOKEN_BUCKET, keyStrategy = IP_METHOD)
    public String t62() {
        log.info("测试令牌桶算法限流-redisson方式，t62:{}", LocalDateTime.now());
        return "ok";
    }
```
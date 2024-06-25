package top.bulk.ratelimit.annotation;

import org.springframework.core.annotation.AliasFor;
import top.bulk.ratelimit.constant.KeyStrategyEnum;
import top.bulk.ratelimit.constant.RateLimiterEnum;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 *
 * @author 散装java
 * @date 2024-06-24
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface RateLimiter {
    int DEFAULT_LIMIT = 10;

    /**
     * 限流key
     */
    String key() default "";

    /**
     * 限流 key 生成策略
     */
    KeyStrategyEnum keyStrategy() default KeyStrategyEnum.IP;

    /**
     * max 最大请求数
     */
    @AliasFor("max") long value() default DEFAULT_LIMIT;

    /**
     * max 最大请求数
     */
    @AliasFor("value") long max() default DEFAULT_LIMIT;

    /**
     * 限流时间 - 允许请求的时间间隔，默认1分钟
     */
    long time() default 60;

    /**
     * 允许求情的时间间隔，默认 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限流实现策略
     */
    RateLimiterEnum strategy() default RateLimiterEnum.SLIDING_WINDOW;

}

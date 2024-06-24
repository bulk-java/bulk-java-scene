package top.bulk.ratelimit.annotation;

import org.springframework.core.annotation.AliasFor;
import top.bulk.ratelimit.constant.RateLimiterEnum;

import java.util.concurrent.TimeUnit;

/**
 * 限流注解
 *
 * @author 散装java
 * @date 2024-06-24
 */
public @interface RateLimiter {
    int DEFAULT_LIMIT = 10;

    /**
     * max 最大请求数
     */
    @AliasFor("max") long value() default DEFAULT_LIMIT;

    /**
     * max 最大请求数
     */
    @AliasFor("value") long max() default DEFAULT_LIMIT;

    /**
     * 限流key
     */
    String key() default "";

    /**
     * 超时时长，默认1分钟
     */
    long timeout() default 60;

    /**
     * 超时时间单位，默认 秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限流实现策略
     */
    RateLimiterEnum strategy() default RateLimiterEnum.SLIDING_WINDOW;

}

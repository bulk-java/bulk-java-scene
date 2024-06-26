package top.bulk.ratelimit.handle;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * 基于Guava的 RateLimiter 实现的限流
 *
 * <b>这种限流不支持分布式！只支持本地限流</b>
 *
 * @author 散装java
 * @date 2024-06-26
 */
public class GuavaRateLimiterHandler implements RateLimiterHandler {

    @Override
    public boolean limit(String key, Long max, Long time, TimeUnit unit) {
        // 每秒5次
        RateLimiter rateLimiter = RateLimiter.create(5.0);
        return rateLimiter.tryAcquire();
    }
}

package top.bulk.ratelimit.handle;

import top.bulk.ratelimit.annotation.RateLimiter;

/**
 * 限流处理器核心接口
 *
 * @author 散装java
 * @date 2024-06-24
 */
public interface RateLimiterHandler {

    boolean limit(RateLimiter rateLimiter);
}

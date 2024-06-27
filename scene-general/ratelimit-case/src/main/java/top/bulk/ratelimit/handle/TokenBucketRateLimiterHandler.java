package top.bulk.ratelimit.handle;

import org.redisson.api.*;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.constant.RateLimiterConst;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶实现（基于 Redisson）
 * Redisson 中直接提供了基于令牌桶的实现 RRateLimiter
 * 建议生产中使用本种方式！
 *
 * @author 散装java
 * @date 2024-06-25
 */
@Component(RateLimiterConst.TOKEN_BUCKET)
public class TokenBucketRateLimiterHandler implements RateLimiterHandler {
    public static final String KEY_PREFIX = "TokenBucket:";
    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean limit(String key, Long max, Long time, TimeUnit unit) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(KEY_PREFIX + key);

        // 初始化，设置速率模式，速率，间隔，间隔单位
        // rateLimiter.trySetRate(RateType.OVERALL, max, unit.toSeconds(time), RateIntervalUnit.SECONDS);

        // 考虑到配置变更的情况，所以判断下限流规则是否变更
        RateLimiterConfig config = rateLimiter.getConfig();
        if (config == null || !config.getRate().equals(max) || config.getRateInterval() != unit.toMillis(time)) {
            // 设置速率模式，速率，间隔，间隔单位
            rateLimiter.setRate(RateType.OVERALL, max, unit.toSeconds(time), RateIntervalUnit.SECONDS);
        }
        // 获取令牌
        return !rateLimiter.tryAcquire();
    }
}

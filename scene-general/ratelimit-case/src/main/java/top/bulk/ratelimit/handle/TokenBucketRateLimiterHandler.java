package top.bulk.ratelimit.handle;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.constant.RateLimiterConst;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶实现
 * Redisson 中直接提供了基于令牌桶的实现 RRateLimiter
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
        rateLimiter.trySetRate(RateType.OVERALL, max, unit.toSeconds(time), RateIntervalUnit.SECONDS);
        // 获取令牌
        return rateLimiter.tryAcquire();
    }
}

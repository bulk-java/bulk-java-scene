package top.bulk.ratelimit.handle;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.annotation.RateLimiter;
import top.bulk.ratelimit.constant.RateLimiterConst;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 基于滑动窗口实现
 *
 * @author 散装java
 * @date 2024-06-24
 */
@Component(RateLimiterConst.SLIDING_WINDOW)
@Slf4j
public class SlidingWindowRateLimiterHandler implements RateLimiterHandler {
    public static final String PREFIX = "slidingWindowRateLimiter:";
    /**
     * 窗口大小（单位：S）
     */
    @Value("${windowSize:10L}")
    Long windowSize;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean limit(RateLimiter limiter) {
        String key = limiter.key();
        //窗口计数
        RScoredSortedSet<Long> counter = redissonClient.getScoredSortedSet(PREFIX + key);
        //使用分布式锁，避免并发设置初始值的时候，导致窗口计数被覆盖
        RLock rLock = redissonClient.getLock(PREFIX + "LOCK:" + key);
        try {
            rLock.lock(200, TimeUnit.MILLISECONDS);
            // 当前时间戳
            long currentTimestamp = System.currentTimeMillis();
            // 窗口起始时间戳
            long windowStartTimestamp = currentTimestamp - windowSize * 1000;
            // 移除窗口外的时间戳，左闭右开
            counter.removeRangeByScore(0, true, windowStartTimestamp, false);
            // 将当前时间戳作为score,也作为member，
            // TODO:高并发情况下可能没法保证唯一，可以加一个唯一标识
            counter.add(currentTimestamp, currentTimestamp);
            //使用zset的元素个数，作为请求计数
            long count = counter.size();
            // 判断时间戳数量是否超过限流阈值
            if (count > limiter.value()) {
                log.info("SlidingWindowRateLimiterHandler key:{}, count:{},over limit:{}", key, count, limiter.max());
                return true;
            }
            return false;
        } finally {
            rLock.unlock();
        }
    }

}

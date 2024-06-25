package top.bulk.ratelimit.handle;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
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
    public static final String KEY_PREFIX = "SlidingWindow:";

    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean limit(String key, Long max, Long time, TimeUnit unit) {
        String redisKey = KEY_PREFIX + key;
        //窗口计数
        RScoredSortedSet<Long> counter = redissonClient.getScoredSortedSet(redisKey);
        //使用分布式锁，避免并发设置初始值的时候，导致窗口计数被覆盖
        RLock rLock = redissonClient.getLock(KEY_PREFIX + "LOCK:" + key);
        try {
            rLock.lock(200, TimeUnit.MILLISECONDS);
            // 当前时间戳
            long currentTimestamp = System.currentTimeMillis();
            // 窗口起始时间戳 >> 转换为毫秒值
            long windowStartTimestamp = currentTimestamp - unit.toMillis(time);
            // 移除 窗口外的时间戳，左闭右开
            counter.removeRangeByScore(0, true, windowStartTimestamp, false);
            //使用zset的元素个数，作为请求计数
            long count = counter.size();
            // 判断时间戳数量是否超过限流阈值
            if (count >= max) {
                log.info("SlidingWindowRateLimiterHandler key:{}, count:{},over limit:{}", redisKey, count, max);
                return true;
            } else {
                // 将当前时间戳作为score,也作为member，
                counter.add(currentTimestamp, currentTimestamp);
            }
            return false;
        } finally {
            rLock.unlock();
        }
    }

}

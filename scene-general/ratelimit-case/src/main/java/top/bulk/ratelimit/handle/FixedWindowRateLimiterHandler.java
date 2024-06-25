package top.bulk.ratelimit.handle;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.constant.RateLimiterConst;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 固定窗口限流
 * <p>
 * 也可以叫做 计数器算法
 *
 * @author 散装java
 * @date 2024-06-25
 */
@Component(RateLimiterConst.FIXED_WINDOW)
@Slf4j
public class FixedWindowRateLimiterHandler implements RateLimiterHandler {
    public static final String KEY_PREFIX = "FixedWindow:";
    @Resource
    private RedissonClient redissonClient;


    @Override
    public boolean limit(String key, Long max, Long time, TimeUnit unit) {
        // 多指令操作 要加锁
        RLock rLock = redissonClient.getLock(KEY_PREFIX + "LOCK:" + key);
        try {
            rLock.lock(100, TimeUnit.MILLISECONDS);
            String redisKey = KEY_PREFIX + key;
            RAtomicLong counter = redissonClient.getAtomicLong(redisKey);
            // 计数 +1
            long count = counter.incrementAndGet();
            // 如果为1的话，就说明窗口刚初始化,直接设置过期时间，作为窗口
            if (count == 1) {
                counter.expire(Duration.ofNanos(unit.toNanos(time)));
            }
            // 触发限流
            if (count > max) {
                // 触发限流的不记在请求数量中
                counter.decrementAndGet();
                return true;
            }
            return false;
        } finally {
            rLock.unlock();
        }
    }
}

package top.bulk.ratelimit.handle;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.constant.RateLimiterConst;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 基于漏桶算法实现
 *
 * @author 散装java
 * @date 2024-06-25
 */
@Component(RateLimiterConst.LEAKY_BUCKET)
@Slf4j
public class LeakyBucketRateLimiterHandler implements RateLimiterHandler {
    @Resource
    private RedissonClient redissonClient;

    private static final String KEY_PREFIX = "LeakyBucket:";
    /**
     * 需要漏水的 key
     */
    private static final String LEAK_WATER_KEY = KEY_PREFIX + "LeakWaterKey";

    @SuppressWarnings("all")
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    /**
     * 漏水速率，单位:个/秒
     * 这个值，应该是可配置的
     */
    private final Long leakRate = 1L;


    @PostConstruct
    public void init() {
        // 定期执行 漏水方法 ，这里设置的漏水为1s
        executorService.scheduleAtFixedRate(this::leakWater, 0, leakRate, TimeUnit.SECONDS);
    }

    @Override
    public boolean limit(String key, Long max, Long time, TimeUnit unit) {
        // 多条指令操作，加锁，防止并发初始化问题
        RLock rLock = redissonClient.getLock(KEY_PREFIX + "LOCK:" + key);
        try {
            rLock.lock(100, TimeUnit.MILLISECONDS);
            String redisKey = KEY_PREFIX + key;
            RScoredSortedSet<Long> bucket = redissonClient.getScoredSortedSet(redisKey);
            // 存储所有的桶，方便漏水
            RSet<String> pathSet = redissonClient.getSet(LEAK_WATER_KEY);
            pathSet.add(redisKey);

            long now = System.currentTimeMillis();
            // 检查桶是否已满，桶未满，添加一个元素到桶中，满了则说明限流了
            if (bucket.size() < max) {
                bucket.add(now, now);
                return false;
            }
            log.info("桶已满，触发限流 key: {}, bucket size:{}", redisKey, bucket.size());
            return true;
        } finally {
            rLock.unlock();
        }
    }


    /**
     * 漏水
     */
    public void leakWater() {
        RSet<String> pathSet = redissonClient.getSet(LEAK_WATER_KEY);
        //遍历所有path,删除旧请求
        for (String path : pathSet) {
            String redisKey = KEY_PREFIX + path;
            RScoredSortedSet<Long> bucket = redissonClient.getScoredSortedSet(redisKey);
            // 获取当前时间
            long now = System.currentTimeMillis();
            // 删除旧的请求
            bucket.removeRangeByScore(0, true, now - 1000 * leakRate, true);
        }
    }
}

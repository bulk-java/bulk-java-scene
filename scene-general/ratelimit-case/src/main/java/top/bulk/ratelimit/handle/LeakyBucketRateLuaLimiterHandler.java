package top.bulk.ratelimit.handle;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.constant.RateLimiterConst;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 基于漏桶算法实现
 * 思路 : 水（请求）先进入到漏桶里，漏桶以一定的速度出水，当水流入速度过大会直接溢出
 *
 * @author 散装java
 * @date 2024-06-25
 */
@Component(RateLimiterConst.LEAKY_BUCKET_LUA)
@Slf4j
public class LeakyBucketRateLuaLimiterHandler implements RateLimiterHandler {

    private static final String KEY_PREFIX = "LeakyBucketLua:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    DefaultRedisScript<Long> leakyBucketRetaLimiterLua;

    @Override
    public boolean limit(String key, Long max, Long time, TimeUnit unit) {
        String keyName = KEY_PREFIX + key;

        long rateInterval = unit.toMillis(time);
        // 容量，决定最大并发数
        long capacity = max;
        // 水流的速度，即容器中的水量在周期时间流完的速度
        double passRate = (double) max / rateInterval;
        // 每次请求增加的水量，相当于每次请求获取的令牌数
        long addWater = 1L;
        // 请求时间戳，使用毫秒
        long lastTs = System.currentTimeMillis();


        Long result = redisTemplate.execute(leakyBucketRetaLimiterLua,
                Collections.singletonList(keyName),
                capacity, passRate, addWater, lastTs);


        if (result != null) {
            return result == 0L;
        } else {
            return false;
        }
    }
}

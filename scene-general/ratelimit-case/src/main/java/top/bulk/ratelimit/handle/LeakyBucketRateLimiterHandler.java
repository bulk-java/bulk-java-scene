package top.bulk.ratelimit.handle;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.constant.RateLimiterConst;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 基于漏桶算法实现
 * 思路 : 水（请求）先进入到漏桶里，漏桶以一定的速度出水，当水流入速度过大会直接溢出
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

    @Override
    public boolean limit(String key, Long max, Long time, TimeUnit unit) {
        // 多条指令操作，加锁，防止并发初始化问题
        RLock rLock = redissonClient.getLock(KEY_PREFIX + "LOCK:" + key);
        try {
            rLock.lock(1, TimeUnit.SECONDS);

            String redisKey = KEY_PREFIX + key;
            RMap<String, String> limitInfoMap = redissonClient.getMap(redisKey);

            // 初始化漏桶
            if (!limitInfoMap.isExists()) {
                limitInfoMap.fastPut("capacity", String.valueOf(max));
                limitInfoMap.fastPut("passRate", String.valueOf((double) max / unit.toSeconds(time)));
                limitInfoMap.fastPut("addWater", "1");
                limitInfoMap.fastPut("water", "0");
                limitInfoMap.fastPut("lastTs", String.valueOf(System.currentTimeMillis()));
                return true;
            }

            long currentCapacity = Long.parseLong(limitInfoMap.get("capacity"));
            double currentPassRate = Double.parseDouble(limitInfoMap.get("passRate"));
            long currentAddWater = Long.parseLong(limitInfoMap.get("addWater"));
            long currentWater = Long.parseLong(limitInfoMap.get("water"));
            long lastTs = Long.parseLong(limitInfoMap.get("lastTs"));

            long nowTs = System.currentTimeMillis();

            long waterPass = Math.round(currentPassRate * ((nowTs - lastTs) / 1000.0));
            currentWater = Math.max(0L, currentWater - waterPass);

            if ((currentCapacity - currentWater) >= currentAddWater) {
                currentWater += currentAddWater;
                limitInfoMap.fastPut("water", String.valueOf(currentWater));
                limitInfoMap.fastPut("lastTs", String.valueOf(nowTs));
                return true;
            }

            return false;

        } catch (Exception e) {
            log.error("漏桶执行异常：{}", e.getMessage(), e);
            return false;
        } finally {
            rLock.unlock();
        }

    }

}

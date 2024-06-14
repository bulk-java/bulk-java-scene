package top.bulk.handler;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis实现的幂等处理
 *
 * @author 散装java
 * @date 2024-06-14
 */
@Component
@Slf4j
public class RedisIdempotentHandler implements IdempotentHandler {

    private static final String REDIS_MAP_CACHE_KEY = "idempotent";

    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean isIdempotent(String key, long llt, TimeUnit ttlUnit) {
        log.info("{}执行操作",Thread.currentThread().getName());
        // 借助于 RMapCache 来实现操作，不需要判断 rMapCache 是否为空
        RMapCache<String, Object> rMapCache = redissonClient.getMapCache(REDIS_MAP_CACHE_KEY);
        // 以当前时间作为 value,可读性更好一些
        String value = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        Object v1;
        // 如果有当前key，说明不是第一次访问了
        if (null != rMapCache.get(key)) {
            return false;
        }
        synchronized (this) {
            // putIfAbsent 是线程安全的，如果不存在，则添加
            v1 = rMapCache.putIfAbsent(key, value, llt, ttlUnit);
            if (null != v1) {
                return false;
            } else {
                log.info("[isIdempotent]:记录成功 key={},value={},expireTime={}{}", key, value, llt, ttlUnit);
            }
        }

        return true;
    }

    /**
     * 删除幂等标识
     *
     * @param key key
     * @return 成功删除返回ture，没有可以删除的元素，返回 fasle
     */
    @Override
    public boolean delIdempotentSign(String key) {
        RMapCache<Object, Object> mapCache = redissonClient.getMapCache(REDIS_MAP_CACHE_KEY);
        if (mapCache.size() == 0) {
            return false;
        }
        // 如果 obj == null 说明要删除的key不存在
        Object obj = mapCache.remove(key);
        log.info("[delIdempotentSign]:key={},obj={},now:{}", key, obj,System.currentTimeMillis());
        return obj != null;
    }
}

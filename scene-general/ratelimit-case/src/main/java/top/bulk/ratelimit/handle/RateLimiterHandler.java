package top.bulk.ratelimit.handle;

import java.util.concurrent.TimeUnit;

/**
 * 限流处理器核心接口
 *
 * @author 散装java
 * @date 2024-06-24
 */
public interface RateLimiterHandler {
    /**
     * 判断是否限流
     *
     * @param key  限流key ，
     * @param max  允许访问最大次数
     * @param time 时间
     * @param unit 时间单位
     * @return true 限流， false 不限流
     */
    boolean limit(String key, Long max, Long time, TimeUnit unit);
}

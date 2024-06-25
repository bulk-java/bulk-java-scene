package top.bulk.ratelimit.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.constant.RateLimiterConst;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 固定窗口限流 基于 LUA 脚本实现
 * <p>
 * 也可以叫做 计数器算法
 *
 * @author 散装java
 * @date 2024-06-25
 */
@Component(RateLimiterConst.FIXED_WINDOW_LUA)
@Slf4j
public class FixedWindowLuaRateLimiterHandler implements RateLimiterHandler {
    public static final String KEY_PREFIX = "FixedWindowLua:";
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    DefaultRedisScript<Long> fixedWindowRateLimiterLua;

    @Override
    public boolean limit(String key, Long max, Long time, TimeUnit unit) {
        String redisKey = KEY_PREFIX + key;

        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = redisTemplate.execute(fixedWindowRateLimiterLua, Collections.singletonList(redisKey), max, unit.toMillis(time));

        if (result != null) {
            return result == 0L;
        } else {
            return false;
        }
    }
}

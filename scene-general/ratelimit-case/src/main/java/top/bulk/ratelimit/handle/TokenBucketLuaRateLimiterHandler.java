package top.bulk.ratelimit.handle;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.constant.RateLimiterConst;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶实现 - lua 脚本方式实现
 *
 * @author 散装java
 * @date 2024-06-25
 */
@Component(RateLimiterConst.TOKEN_BUCKET_LUA)
public class TokenBucketLuaRateLimiterHandler implements RateLimiterHandler {
    public static final String KEY_PREFIX = "TokenBucketLua:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    DefaultRedisScript<Long> tokenBucketRateLimiterLua;

    @Override
    public boolean limit(String key, Long max, Long time, TimeUnit unit) {
        // keys 参数组装 参考 redisson 格式
        String keyName = KEY_PREFIX + key;
        String valueName = String.format("{%s}.value", keyName);
        String permitsName = String.format("{%s}.permits", keyName);

        // 此处写法参考 org.redisson.RedissonRateLimiter.tryAcquireAsync(org.redisson.client.protocol.RedisCommand<T>, java.lang.Long)
        byte[] random = new byte[8];
        ThreadLocalRandom.current().nextBytes(random);

        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = redisTemplate.execute(tokenBucketRateLimiterLua,
                Arrays.asList(keyName, valueName, permitsName),
                1, System.currentTimeMillis(), random, max, unit.toMillis(time));

        if (result != null) {
            return result == 0L;
        } else {
            return false;
        }
    }
}

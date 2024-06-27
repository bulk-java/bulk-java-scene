package top.bulk.ratelimit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @author 散装java
 * @date 2024-06-25
 */
@Configuration
public class RedisConfig {
    /**
     * 此处有坑，一定要定义一个自己的 redisTemplate
     * 不然会走到 redisson 定义的，这样执行 lua 脚本会有点问题
     * todo 解决 和 redisson 公用一个 redisTemplate
     */
    // @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    // 读取脚本内容扔到容器中，节省加载时间 ------------------------------------

    @Bean
    public DefaultRedisScript<Long> fixedWindowRateLimiterLua() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本 - 固定窗口
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/fixedWindowRateLimiter.lua")));
        // 指定返回值类型
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean
    public DefaultRedisScript<Long> slidingWindowRateLimiterLua() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本 - 滑动窗口
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/slidingWindowRateLimiter.lua")));
        // 指定返回值类型
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean
    public DefaultRedisScript<Long> leakyBucketRetaLimiterLua() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本 - 漏桶算法
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/leakyBucketRetaLimiter.lua")));
        // 指定返回值类型
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean
    public DefaultRedisScript<Long> tokenBucketRateLimiterLua() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 指定 lua 脚本 - 令牌桶算法
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/tokenBucketRateLimiter.lua")));
        // 指定返回值类型
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}

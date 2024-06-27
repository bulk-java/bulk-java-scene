package top.bulk.ratelimit.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 限流方案枚举
 *
 * @author 散装java
 * @date 2024-06-24
 */
@AllArgsConstructor
@Getter
public enum RateLimiterEnum {
    /**
     * 固定窗口
     */
    FIXED_WINDOW,
    /**
     * 固定窗口 lua 脚本实现
     */
    FIXED_WINDOW_LUA,
    /**
     * 滑动窗口实现
     */
    SLIDING_WINDOW,
    /**
     * 漏桶算法
     */
    LEAKY_BUCKET,
    /**
     * 漏桶算法 - lua 实现
     */
    LEAKY_BUCKET_LUA,
    /**
     * 令牌桶
     */
    TOKEN_BUCKET,
    /**
     * 令牌桶 - lua 实现
     */
    TOKEN_BUCKET_LUA,
    ;
}

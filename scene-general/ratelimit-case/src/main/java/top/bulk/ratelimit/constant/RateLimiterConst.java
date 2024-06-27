package top.bulk.ratelimit.constant;

/**
 * 限流方案枚举类
 *
 * @author 散装java
 * @date 2024-06-24
 */
public interface RateLimiterConst {
    /**
     * 基于滑动窗口实现
     */
    String SLIDING_WINDOW = "SLIDING_WINDOW";
    /**
     * 基于固定窗口实现
     */
    String FIXED_WINDOW = "FIXED_WINDOW";
    String FIXED_WINDOW_LUA = "FIXED_WINDOW_LUA";
    /**
     * 基于漏桶算法实现
     */
    String LEAKY_BUCKET = "LEAKY_BUCKET";
    String LEAKY_BUCKET_LUA = "LEAKY_BUCKET_LUA";
    /**
     * 基于令牌桶实现
     */
    String TOKEN_BUCKET = "TOKEN_BUCKET";
    String TOKEN_BUCKET_LUA = "TOKEN_BUCKET_LUA";
}

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
    SLIDING_WINDOW,;
}

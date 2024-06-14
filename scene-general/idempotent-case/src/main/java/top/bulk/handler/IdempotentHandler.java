package top.bulk.handler;

import java.util.concurrent.TimeUnit;

/**
 * 幂等处理接口 - 可以做不通的实现
 *
 * @author 散装java
 * @date 2024-06-14
 */
public interface IdempotentHandler {
    /**
     * 判断当前 key 是否满足幂等
     *
     * @param key     key
     * @param llt     过期时间
     * @param ttlUnit 时间单位
     * @return true and false
     */
    boolean isIdempotent(String key, long llt, TimeUnit ttlUnit);

    /**
     * 删除当前key
     *
     * @param key key
     * @return true and false
     */
    boolean delIdempotentSign(String key);

}

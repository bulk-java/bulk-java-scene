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
     * 保存幂等标识，如果保存失败则返回false
     *
     * @param key     key
     * @param llt     过期时间
     * @param ttlUnit 时间单位
     * @return true and false
     */
    boolean saveIdempotentSign(String key, long llt, TimeUnit ttlUnit);

    /**
     * 删除当前key
     *
     * @param key key
     * @return true and false
     */
    boolean delIdempotentSign(String key);

}

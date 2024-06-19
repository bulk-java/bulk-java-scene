package top.bulk.mdc.util;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * MDC 工具类
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-18
 */
public class MdcUtil {
    public static final String TRACE_ID = "TRACE_ID";

    /**
     * 生成 traceId
     *
     * @return traceId
     */
    public static String generateTraceId() {
        // 这里使用 uuid
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 将 traceId 存储到 MDC 中去
     *
     * @param tid 链路追踪号
     */
    public static void put(String tid) {
        if (StringUtils.hasText(tid)) {
            MDC.put(TRACE_ID, tid);
        }
    }

    /**
     * 从 MDC 中移除 traceId
     */
    public static void remove() {
        MDC.remove(TRACE_ID);
    }
}

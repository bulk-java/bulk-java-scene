package top.bulk.mdc.interceptor;

import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import top.bulk.mdc.util.MdcUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static top.bulk.mdc.util.MdcUtil.TRACE_ID;

/**
 * 基于 拦截器实现，拦截每一次请求，然后获取请求的 traceId，然后设置到 MDC 中，方便后续日志打印
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-18
 */
public class LogInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) {
        String tid;
        // 考虑微服务情况，判断上流有没有传下来 TRACE_ID ，如果有，那么用上流传下来得，额米有自己生成
        if (StringUtils.hasText(request.getHeader(TRACE_ID))) {
            tid = request.getHeader(TRACE_ID);
        } else {
            tid = MdcUtil.generateTraceId();
        }
        MDC.put(TRACE_ID, tid);
        return true;
    }

    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, @Nullable Exception ex) {
        MDC.remove(TRACE_ID);
    }
}

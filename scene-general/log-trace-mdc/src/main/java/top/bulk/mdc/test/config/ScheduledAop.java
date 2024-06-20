package top.bulk.mdc.test.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import top.bulk.mdc.util.MdcUtil;

/**
 * 基于 AOP 直接拦截 @Scheduled 进行增强 MDC
 *
 * @author 散装java
 * @date 2024-06-20
 */
@Aspect
@Component
public class ScheduledAop {
    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void scheduledTask() {
    }

    @Before("scheduledTask()")
    public void beforeScheduledTask() {
        // 在任务执行前设置 MDC 上下文信息
        MdcUtil.setTraceIdIfAbsent();
    }

    @After("scheduledTask()")
    public void afterScheduledTask() {
        // 在任务执行后清除 MDC 上下文信息
        MdcUtil.remove();
    }
}

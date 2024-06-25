package top.bulk.inter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.bulk.inter.annotation.BizLog;
import top.bulk.inter.log.LogManager;
import top.bulk.inter.util.JoinPointUtil;

import java.lang.reflect.Method;

/**
 * 注解处理类
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-25
 */
@Aspect
@Slf4j
@Component
public class BizLogAspect {


    /**
     * 处理请求前执行
     */
    @Before("@annotation(bizLog)")
    public void boBefore(JoinPoint joinPoint, BizLog bizLog) {
        LogManager.setStartTime();
    }

    /**
     * 返回结果后处理
     */
    @AfterReturning(pointcut = "@annotation(bizLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, BizLog bizLog, Object result) {

        // 操作人，一般会去取自业务上下文，这里直接写死
        String userName = "bulk-java";

        //异步记录日志
        LogManager.saveLog(bizLog, userName, joinPoint, JoinPointUtil.toJsonString(result));

        LogManager.removeStartTime();
    }

    /**
     * 操作发生异常记录日志
     */
    @AfterThrowing(pointcut = "@annotation(bizLog)", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, BizLog bizLog, Exception exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        BizLog businessLog = method.getAnnotation(BizLog.class);

        String userName = "bulk-java";
        LogManager.ExceptionLog(businessLog, userName, joinPoint, exception);

        LogManager.removeStartTime();
    }
}

package top.bulk.inter.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.core.NamedThreadLocal;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import top.bulk.inter.annotation.BizLog;
import top.bulk.inter.entity.BizOptLog;
import top.bulk.inter.util.JoinPointUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-25
 */
@Slf4j
public class LogManager {
    private static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(10, new ScheduledExecutorFactoryBean());
    public static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<>("Cost Time");

    /**
     * 记录接口开始处理时间
     */
    public static void setStartTime() {
        TIME_THREADLOCAL.set(System.currentTimeMillis());

    }

    public static void removeStartTime() {
        TIME_THREADLOCAL.remove();
    }

    public static long calCostTime() {
        return System.currentTimeMillis() - TIME_THREADLOCAL.get();
    }

    /**
     * 保存日志
     *
     * @param bizLog    业务日志
     * @param account   操作人
     * @param joinPoint 切点
     * @param result    返回结果
     */
    public static void saveLog(BizLog bizLog, final String account, JoinPoint joinPoint, final String result) {
        long costTime = calCostTime();
        EXECUTOR.schedule(() -> {
            BizOptLog bizOptLog = newBizOptLog(bizLog, account, joinPoint, costTime);
            bizOptLog.setSuccess("是");
            bizOptLog.setMessage("操作成功");
            bizOptLog.setResult(result);
            log.info("模拟保存成功数据:{}", bizOptLog);
        }, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * 保存异常日志
     *
     * @param bizLog    业务日志
     * @param account   操作人
     * @param joinPoint 切点
     * @param exception 异常
     */
    public static void ExceptionLog(BizLog bizLog, final String account, JoinPoint joinPoint, Exception exception) {
        long costTime = calCostTime();
        EXECUTOR.schedule(() -> {
            BizOptLog bizOptLog = newBizOptLog(bizLog, account, joinPoint, costTime);
            bizOptLog.setSuccess("否");
            bizOptLog.setMessage(Arrays.toString(exception.getStackTrace()));
            log.info("模拟保存失败数据:{}", bizOptLog);
        }, 10, TimeUnit.MILLISECONDS);
    }

    private static BizOptLog newBizOptLog(BizLog bizLog, final String account, JoinPoint joinPoint, long costTime) {
        BizOptLog bizOptLog = new BizOptLog();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String param = JoinPointUtil.getArgsJsonString(joinPoint);
        String method = JoinPointUtil.getMethod();

        bizOptLog.setTitle(bizLog.title());
        bizOptLog.setClassName(className);
        bizOptLog.setMethodName(methodName);
        bizOptLog.setParam(param);
        bizOptLog.setReqMethod(method);
        bizOptLog.setOptTime(LocalDateTime.now());
        bizOptLog.setAccount(account);
        bizOptLog.setCostTime(costTime);
        return bizOptLog;
    }
}

package top.bulk.ratelimit.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import top.bulk.ratelimit.annotation.RateLimiter;
import top.bulk.ratelimit.constant.KeyStrategyEnum;
import top.bulk.ratelimit.constant.RateLimiterEnum;
import top.bulk.ratelimit.handle.RateLimiterHandler;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 限流注解处理类
 *
 * @author 散装java
 * @date 2024-06-24
 */
@Aspect
@Slf4j
@Component
public class RateLimiterAspect {
    @Resource
    Map<String, RateLimiterHandler> handlerMap;

    @Pointcut("@annotation(top.bulk.ratelimit.annotation.RateLimiter)")
    public void rateLimit() {
    }

    @Before("rateLimit()")
    public void beforePointCut(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);

        // 获取使用那种算法限流策略
        RateLimiterEnum strategy = rateLimiter.strategy();
        RateLimiterHandler handler = handlerMap.get(strategy.name());

        // 生成key
        KeyStrategyEnum keyStrategy = rateLimiter.keyStrategy();
        String key = keyStrategy.genKey(joinPoint);

        // 判断是否限流
        boolean limit = handler.limit(key, rateLimiter.max(), rateLimiter.time(), rateLimiter.timeUnit());

        if (limit) {
            // 实际项目中要对异常做处理，这里只是为了方便观察结果
            throw new RuntimeException("限流:访问次数过多！" + strategy.name());
        }
    }
}

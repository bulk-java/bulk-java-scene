package top.bulk.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.bulk.annotation.Idempotent;
import top.bulk.exception.IdempotentException;
import top.bulk.expression.ExpressionResolver;
import top.bulk.handler.IdempotentHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * `@Idempotent` 注解处理类
 *
 * @author 散装java
 * @date 2024-06-13
 */
@Aspect
@Slf4j
@Component
public class IdempotentAspect {
    /**
     * 借助 ThreadLocal ，实现 删除能力
     */
    private static final ThreadLocal<Map<String, Object>> THREAD_CACHE = ThreadLocal.withInitial(HashMap::new);

    private static final String KEY = "key";

    private static final String DEL_KEY = "delKey";

    @Autowired
    private ExpressionResolver expressionResolver;

    @Resource
    IdempotentHandler idempotentHandler;
    /**
     * 切点
     */
    @Pointcut("@annotation(top.bulk.annotation.Idempotent)")
    public void pointCut() {
    }

    /**
     * 前置处理，进行幂等的控制
     */
    @Before("pointCut()")
    public void beforePointCut(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();

        // 获取 @Idempotent 注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Idempotent idempotent = method.getAnnotation(Idempotent.class);

        // 获取用于幂等校验的key
        String key = this.getKey(joinPoint, request, idempotent);

        long expireTime = idempotent.expireTime();
        String info = idempotent.info();
        TimeUnit timeUnit = idempotent.timeUnit();
        boolean delKey = idempotent.delKey();

        // 如果不满足幂等校验，则直接抛出异常
        if (!idempotentHandler.saveIdempotentSign(key, expireTime, timeUnit)) {
            throw new IdempotentException(info);
        }


        // 暂存key，如果需要删除的话，在 后置处理 中执行
        Map<String, Object> map = THREAD_CACHE.get();
        map.put(KEY, key);
        map.put(DEL_KEY, delKey);
    }

    /**
     * 后置处理，对于那些需要删除 key 的场景，进行 key 值的删除
     */
    @After("pointCut()")
    public void afterPointCut(JoinPoint joinPoint) {
        Map<String, Object> map = THREAD_CACHE.get();
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        String key = map.get(KEY).toString();
        boolean delKey = (boolean) map.get(DEL_KEY);

        if (delKey) {
            idempotentHandler.delIdempotentSign(key);
        }
        THREAD_CACHE.remove();
    }

    /**
     * 获取用于幂等校验的 redis 存储的 key
     * 若没有配置 幂等 标识编号，则使用 url + 参数列表作为区分
     */
    private String getKey(JoinPoint joinPoint, HttpServletRequest request, Idempotent idempotent) {
        String key;
        // 若没有配置 幂等 标识编号，则使用 url + 参数列表作为区分
        if (!StringUtils.hasLength(idempotent.key())) {
            String url = request.getRequestURI();
            String argString = Arrays.asList(joinPoint.getArgs()).toString();
            key = url + argString;
        } else {
            // 使用jstl 规则区分
            key = expressionResolver.resolver(idempotent, joinPoint);
        }
        // 当配置了el表达式但是所选字段为空时,会抛出异常,兜底使用url做标识
        if (key == null) {
            key = request.getRequestURL().toString();
        }
        return key;
    }


}

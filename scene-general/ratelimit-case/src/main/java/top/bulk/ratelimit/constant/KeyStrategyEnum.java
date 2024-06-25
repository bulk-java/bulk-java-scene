package top.bulk.ratelimit.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import top.bulk.ratelimit.annotation.RateLimiter;
import top.bulk.ratelimit.util.IpUtil;

import java.lang.reflect.Method;

/**
 * 限流 key
 *
 * @author 散装java
 * @date 2024-06-25
 */
@AllArgsConstructor
@Getter
public enum KeyStrategyEnum {
    /**
     * 自定义 key
     */
    CUSTOMIZE {
        @Override
        public String genKey(JoinPoint joinPoint) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
            return rateLimiter.key();
        }
    },
    /**
     * 基于ip 生成key
     */
    IP {
        @Override
        public String genKey(JoinPoint joinPoint) {
            return IpUtil.getIp();
        }
    },
    /**
     * 基于 ip + 方法名， 即一个ip 一个方法，最多访问几次
     */
    IP_METHOD {
        @Override
        public String genKey(JoinPoint joinPoint) {
            // 获取方法名
            String methodName = joinPoint.getSignature().getName();
            // 获取类名
            String className = joinPoint.getSignature().getDeclaringTypeName();
            return String.format("%s:%s:%s", IpUtil.getIp(), className, methodName);
        }
    },
    /**
     * 基于用户生成唯一key
     */
    USER {
        @Override
        public String genKey(JoinPoint joinPoint) {
            // 在项目中，返回用户的 id
            return "";
        }
    },
    ;

    // 定义一个抽象方法，每个枚举值都要实现这个方法
    public abstract String genKey(JoinPoint joinPoint);
}

package top.bulk.inter.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解析切面的参数工具类
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-25
 */
@Slf4j
public class JoinPointUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 获取切面的参数json
     */
    public static String getArgsJsonString(JoinPoint joinPoint) {
        StringBuilder argsJson = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (!isFilterObject(arg)) {
                if (arg != null) {
                    String jsonStr = toJsonString(arg);
                    argsJson.append(jsonStr).append(" ");
                }
            }
        }
        return argsJson.toString().trim();
    }

    /**
     * 判断是否需要拼接参数，过滤掉HttpServletRequest,MultipartFile,HttpServletResponse等类型参数
     *
     * @author xuyuxiang
     * @date 2020/3/16 17:59
     */
    private static boolean isFilterObject(Object arg) {
        return arg instanceof MultipartFile || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse;
    }

    public static String toJsonString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("toJsonString 异常：{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取请求方法
     */
    public static String getMethod() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "-";
        } else {
            return request.getMethod();
        }
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        } else {
            return requestAttributes.getRequest();
        }
    }
}

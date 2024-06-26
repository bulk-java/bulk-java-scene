package top.bulk.ratelimit.util;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * ip工具类
 *
 * @author 散装java
 * @date 2024-06-25
 */
public class IpUtil {
    private static final String LOCAL_REMOTE_HOST = "0:0:0:0:0:0:0:1";
    /**
     * 获取ip
     *
     * @return
     */
    public static String getIp() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        String remoteHost = ServletUtil.getClientIP(request);
        return LOCAL_REMOTE_HOST.equals(remoteHost) ? "127.0.0.1" : remoteHost;
    }
}

package top.bulk.util;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * ip工具类 - 离线方式
 *
 * @author 散装java
 * @date 2024-06-13
 */
@Slf4j
public class IpAddressUtil {
    private static final String LOCAL_REMOTE_HOST = "0:0:0:0:0:0:0:1";
    private static final String LOCAL_IP = "127.0.0.1";
    /**
     *
     */
    private static Searcher SEARCHER;

    static {
        // 加载 resource 目录下的 ip2region.xdb 文件
        // 如果你想更新数据，参考 https://gitee.com/lionsoul/ip2region/blob/master/ReadMe.md#xdb-%E6%95%B0%E6%8D%AE%E6%9B%B4%E6%96%B0
        // 如果你需要更为准确的数据，可以考虑购买一些第三方的商用数据
        ClassPathResource resource = new ClassPathResource("ip2region.xdb");
        try {
            // 获取文件的绝对路径
            String filePath = resource.getFile().getAbsolutePath();
            SEARCHER = Searcher.newWithFileOnly(filePath);
        } catch (Exception e) {
            log.error("IpAddressUtil初始化异常：", e);
        }
    }

    /**
     * 获取请求的 ip
     */
    public static String getIp(HttpServletRequest request) {
        if (request == null) {
            return LOCAL_IP;
        }
        // 借助hutool工具类，其实就是解析请求头中的以下参数
        // "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
        // 为什么不直接 request.getRemoteAddr()? 因为 如果Nginx等反向代理软件， 则不能通过 request.getRemoteAddr() 获取 IP地址
        String remoteHost = ServletUtil.getClientIP(request);
        return LOCAL_REMOTE_HOST.equals(remoteHost) ? "127.0.0.1" : remoteHost;

    }

    /**
     * 根据 IP 地址离线获取城市
     */
    public static String getCityInfo(String ip) {
        try {
            ip = ip.trim();
            // 查询，返回固定格式 国家|区域|省份|城市|ISP
            String region = SEARCHER.search(ip);
            return region.replace("0|", "").replace("|0", "");
        } catch (Exception e) {
            return "未知";
        }
    }
}

package top.bulk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.util.IpAddressUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试
 *
 * @author 散装java
 * @date 2024-06-13
 */
@RestController
@Slf4j
public class TestController {

    @GetMapping("/get")
    public String get(HttpServletRequest request) {
        String ip = IpAddressUtil.getIp(request);
        String info = IpAddressUtil.getCityInfo(ip);
        log.info("get:ip:{},info:{}", ip, info);
        return "ok";
    }
}

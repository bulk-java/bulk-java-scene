package top.bulk.masking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试类
 *
 * @author 散装java
 * @date 2024-06-20
 */
@RestController
@Slf4j
public class TestController {

    @GetMapping("/test")
    public void test() {
        log.info("tel:{},手机号:{}", "13000000000", "13000000000");
    }
}

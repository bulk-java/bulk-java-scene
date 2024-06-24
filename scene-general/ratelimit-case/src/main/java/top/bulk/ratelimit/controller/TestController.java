package top.bulk.ratelimit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 测试
 *
 * @author 散装java
 * @date 2024-06-24
 */
@RestController
@Slf4j
public class TestController {

    @GetMapping("t1")
    public String test1() {
        log.info("t1:{}", LocalDateTime.now());
        return "ok";
    }
}

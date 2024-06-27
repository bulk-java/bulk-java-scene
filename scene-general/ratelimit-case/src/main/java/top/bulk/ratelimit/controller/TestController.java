package top.bulk.ratelimit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.ratelimit.annotation.RateLimiter;
import top.bulk.ratelimit.constant.RateLimiterEnum;

import java.time.LocalDateTime;

import static top.bulk.ratelimit.constant.KeyStrategyEnum.IP_METHOD;

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
    @RateLimiter(max = 5, time = 10, keyStrategy = IP_METHOD)
    public String test1() {
        log.info("t1:{}", LocalDateTime.now());
        return "ok";
    }

    @GetMapping("t2")
    @RateLimiter(max = 5, time = 10, strategy = RateLimiterEnum.FIXED_WINDOW_LUA)
    public String test2() {
        log.info("测试固定窗口限流-lua脚本方式，t2:{}", LocalDateTime.now());
        return "ok";
    }

    @GetMapping("t3")
    @RateLimiter(max = 5, time = 10, strategy = RateLimiterEnum.FIXED_WINDOW)
    public String test3() {
        log.info("测试固定窗口限流-redisson方式，t3:{}", LocalDateTime.now());
        return "ok";
    }

    @GetMapping("t4")
    @RateLimiter(max = 5, time = 10, strategy = RateLimiterEnum.SLIDING_WINDOW)
    public String t4() {
        log.info("测试滑动窗口限流-redisson方式，t4:{}", LocalDateTime.now());
        return "ok";
    }

    @GetMapping("t5")
    @RateLimiter(max = 5, time = 10, strategy = RateLimiterEnum.LEAKY_BUCKET)
    public String t5() {
        log.info("测试漏桶算法限流-redisson方式，t5:{}", LocalDateTime.now());
        return "ok";
    }

    @GetMapping("t5-1")
    @RateLimiter(max = 5, time = 10, strategy = RateLimiterEnum.LEAKY_BUCKET_LUA)
    public String t51() {
        log.info("测试漏桶算法限流-lua脚本，t51:{}", LocalDateTime.now());
        return "ok";
    }

    /**
     * 可以通过更改 max time 的值，看测试结果
     */
    @GetMapping("t6")
    @RateLimiter(max = 4, time = 10, strategy = RateLimiterEnum.TOKEN_BUCKET)
    public String t6() {
        log.info("测试令牌桶算法限流-redisson方式，t6:{}", LocalDateTime.now());
        return "ok";
    }

    @GetMapping("t6-2")
    @RateLimiter(max = 1, time = 1, strategy = RateLimiterEnum.TOKEN_BUCKET, keyStrategy = IP_METHOD)
    public String t62() {
        log.info("测试令牌桶算法限流-redisson方式，t62:{}", LocalDateTime.now());
        return "ok";
    }

    @GetMapping("t6-3")
    @RateLimiter(max = 5, time = 10, strategy = RateLimiterEnum.TOKEN_BUCKET_LUA)
    public String t63() {
        log.info("测试令牌桶算法限流-lua方式，t63:{}", LocalDateTime.now());
        return "ok";
    }
}

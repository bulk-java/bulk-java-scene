package top.bulk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.annotation.Idempotent;
import top.bulk.entity.OrderEntity;

/**
 * @author 散装java
 * @date 2024-06-14
 */
@RestController
@Slf4j
public class OrderController {

    /**
     * 测试 post 请求， 使用 userId + productId 组合为幂等条件
     */
    @PostMapping("/order")
    @Idempotent(key = "#order.userId + ''+ #order.productId", expireTime = 3, info = "请勿重复下单", delKey = true)
    public String order(@RequestBody OrderEntity order) throws InterruptedException {
        Thread.sleep(1000L);
        log.info("order ==> {}", order);
        return "success";
    }

    /**
     * 测试 get 请求， 使用单个入参作为 幂等条件
     */
    @GetMapping("/get")
    @Idempotent(key = "#key", expireTime = 3, info = "请勿重复查询")
    public String get(String key) throws Exception {
        Thread.sleep(2000L);
        return "success";
    }

    /**
     * 测试不制定幂等条件的
     */
    @GetMapping("/noKey")
    @Idempotent(expireTime = 3, info = "请勿重复查询")
    public String noKey(String id) throws Exception {
        log.info("noKey ==> {}", id);
        Thread.sleep(2000L);
        return "success";
    }


}

package top.bulk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.bulk.annotation.Idempotent;
import top.bulk.entity.OrderEntity;
import top.bulk.exception.IdempotentException;
import top.bulk.handler.IdempotentHandler;

import javax.annotation.Resource;

/**
 * @author 散装java
 * @date 2024-06-14
 */
@RestController
@Slf4j
public class OrderController {
    @Resource
    IdempotentHandler idempotentHandler;

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
     * 基于 token 的幂等校验，访问当前接口前，需要先请求 "/getToken" 接口获取到 token
     *
     * @param token token 令牌
     * @param order 下单参数
     * @return res
     * @see TokenController#token()
     */
    @PostMapping("/orderWithToken")
    public String orderWithToken(@RequestHeader("token") String token, @RequestBody OrderEntity order) throws InterruptedException {
        // 如果令牌删除失败，则说明令牌已经被别人使用，那么返回幂等异常
        if (!idempotentHandler.delIdempotentSign(token)) {
            throw new IdempotentException("请勿重复下单");
        }
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

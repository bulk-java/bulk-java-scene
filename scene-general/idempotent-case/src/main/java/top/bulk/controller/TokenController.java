package top.bulk.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.handler.IdempotentHandler;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-15
 */
@RestController
public class TokenController {
    @Resource
    IdempotentHandler idempotentHandler;

    /**
     * 生成令牌，这里使用 uuid
     *
     * @return uuid
     */
    @GetMapping("/getToken")
    public String token() {
        String token = UUID.randomUUID().toString().replace("-", "");
        // 保存token 有效时间 5 min 。建议依据业务场景设定时间
        idempotentHandler.saveIdempotentSign(token, 5, TimeUnit.MINUTES);
        return token;
    }
}

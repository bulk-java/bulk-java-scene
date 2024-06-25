package top.bulk.inter.controller;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.inter.annotation.BizLog;
import top.bulk.inter.entity.UserEntity;
import top.bulk.inter.enums.LogOptTypeEnum;
import top.bulk.inter.service.TestService;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-25
 */
@RestController
public class TestController {
    @Resource
    private TestService testService;

    @GetMapping("t1")
    @BizLog(title = "测试1", optType = LogOptTypeEnum.QUERY)
    public String test(String id) {
        return "test";
    }

    @SneakyThrows
    @PostMapping("t2")
    @BizLog(title = "测试2", optType = LogOptTypeEnum.ADD)
    public String test(@RequestBody UserEntity user) {
        TimeUnit.SECONDS.sleep(1);
        return "ok";
    }

    @PostMapping("t3")
    public String t3(@RequestBody UserEntity user) {
        UserEntity u2 = testService.addUser(user, "bulk-java");
        return "ok";
    }
}

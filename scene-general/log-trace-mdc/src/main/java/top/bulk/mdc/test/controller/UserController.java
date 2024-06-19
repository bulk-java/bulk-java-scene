package top.bulk.mdc.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bulk.mdc.test.service.UserService;

import javax.annotation.Resource;

/**
 * 测试接口
 *
 * @author shixiaodong
 * @date 2024-06-19
 */
@RestController
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/test1")
    public String test1(String id) {
        log.info("UserController.test:{}", id);
        String name = userService.queryNameById(id);
        log.info("UserController.test name :{}", name);
        return "ok";
    }
}

package top.bulk.inter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.bulk.inter.annotation.BizLog;
import top.bulk.inter.entity.UserEntity;
import top.bulk.inter.enums.LogOptTypeEnum;
import top.bulk.inter.service.TestService;

/**
 * 实现类
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-25
 */
@Service
@Slf4j
public class TestServiceImpl implements TestService {
    @Override
    @BizLog(title = "测试service", optType = LogOptTypeEnum.OTHER)
    public UserEntity addUser(UserEntity user, String s) {
        log.info("service测试");
        return new UserEntity("返回参数", 20);
    }
}

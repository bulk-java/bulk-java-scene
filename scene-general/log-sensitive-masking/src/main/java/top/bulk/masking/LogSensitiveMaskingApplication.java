package top.bulk.masking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类 本项目是用于演示敏感数据脱敏的
 *
 * @author 散装java
 * @date 2024-06-20
 */
@SpringBootApplication
@Slf4j
public class LogSensitiveMaskingApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogSensitiveMaskingApplication.class);
        log.info("tel:{},手机号:{},身份证{}", "13000000000", "13000000000", "370681199922222222");
    }
}

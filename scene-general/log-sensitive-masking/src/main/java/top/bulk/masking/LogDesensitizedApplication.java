package top.bulk.masking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类 本项目是用于演示敏感数据脱敏的
 *
 * @author 散装java
 * @date 2024-06-20
 */
@SpringBootApplication
public class LogDesensitizedApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogDesensitizedApplication.class);
    }
}

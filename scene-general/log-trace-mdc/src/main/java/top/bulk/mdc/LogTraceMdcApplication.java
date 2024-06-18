package top.bulk.mdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 基于 mdc 实现  日志的链路追踪能力
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-18
 */
@SpringBootApplication
public class LogTraceMdcApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogTraceMdcApplication.class, args);
    }
}
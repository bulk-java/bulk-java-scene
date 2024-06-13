package top.bulk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author 散装java
 * @date 2024-06-13
 */
@SpringBootApplication
public class IdempotentApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdempotentApplication.class, args);
    }
}

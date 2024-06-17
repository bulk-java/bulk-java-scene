package top.bulk.holiday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author 散装java
 * @date 2022-05-27
 */
@SpringBootApplication
class HolidayApplication {

    public static void main(String[] args) {
        SpringApplication.run(HolidayApplication.class, args);
        System.out.println("http://localhost:8080/");
    }

}

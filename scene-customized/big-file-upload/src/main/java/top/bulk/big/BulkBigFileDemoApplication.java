package top.bulk.big;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BulkBigFileDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BulkBigFileDemoApplication.class, args);
        System.out.println("访问地址: http://localhost:8080/page/index.html");
    }

}

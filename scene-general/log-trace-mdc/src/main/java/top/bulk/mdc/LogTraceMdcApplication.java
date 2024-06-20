package top.bulk.mdc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 基于 mdc 实现  日志的链路追踪能力
 *
 * 需要添加启动参数  -javaagent:E:\tools\maven\repository\com\alibaba\transmittable-thread-local\2.14.5\transmittable-thread-local-2.14.5.jar
 *
 * agent这样对代码无侵入，或者你也可以使用 TtlRunable修饰Runable 来使用多线程调度
 *
 * @author 散装java
 * @version 1.0.0
 * @date 2024-06-18
 */
@SpringBootApplication
@EnableScheduling
public class LogTraceMdcApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogTraceMdcApplication.class, args);
    }
}
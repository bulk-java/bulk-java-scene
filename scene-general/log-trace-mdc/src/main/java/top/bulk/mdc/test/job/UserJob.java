package top.bulk.mdc.test.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.bulk.mdc.test.config.ScheduledAop;

import java.time.LocalDateTime;

/**
 * 定时任务，基于 @Scheduled 注解的定时任务，要向生效，要做AOP的控制，见 ScheduledAop
 *
 * @author 散装java
 * @date 2024-06-19
 * @see ScheduledAop
 */
@Component
@Slf4j
public class UserJob {
    /**
     * 每隔5秒执行一次的定时任务
     * 测试定时任务的日志输出是否正常
     */
    @SneakyThrows
    @Scheduled(fixedRate = 5000)
    public void job1() {
        log.info(">>> UserJob.job1 执行：{}", LocalDateTime.now());
        Thread.sleep(1000);
        log.info(">>> UserJob.job1 执行完毕：{}", LocalDateTime.now());
    }

    @SneakyThrows
    @Scheduled(cron = "0/1 * * * * ?")
    public void job2() {
        log.info(">>> UserJob.job2 执行：{}", LocalDateTime.now());
        Thread.sleep(1000);
        log.info(">>> UserJob.job2 执行完毕：{}", LocalDateTime.now());

    }
}

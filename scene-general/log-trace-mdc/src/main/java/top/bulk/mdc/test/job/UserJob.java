package top.bulk.mdc.test.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author shixiaodong
 * @date 2024-06-19
 */
@Component
@Slf4j
public class UserJob {


    public void job1() {
        log.info(">>> UserJob.job1 执行");
    }
}

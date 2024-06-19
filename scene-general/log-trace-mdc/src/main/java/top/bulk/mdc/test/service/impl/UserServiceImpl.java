package top.bulk.mdc.test.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.bulk.mdc.test.service.UserService;

import java.util.concurrent.*;

/**
 * @author shixiaodong
 * @date 2024-06-19
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * 自定义线程池  参数随便设置的，就是为了测试打印日志是否带 traceId
     */
    private static final ExecutorService BULK_EXECUTOR = new ThreadPoolExecutor(
            5,
            10,
            2,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(200),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public String queryNameById(String id) {
        log.info("UserServiceImpl.queryNameById:{}", id);
        for (int i = 0; i < 3; i++) {
            // 假装开是个线程处理业务
            int finalNum = i;
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("ThreadName:{} => 正在处理业务:{}", Thread.currentThread().getName(), finalNum);
            }).start();

            // 测试线程池情况
            BULK_EXECUTOR.submit(() -> {
                log.info("BULK_EXECUTOR :{} => 正在处理业务:{}", Thread.currentThread().getName(), finalNum);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        return "bulk-java";
    }
}

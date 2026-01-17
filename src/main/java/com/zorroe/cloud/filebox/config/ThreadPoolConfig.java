package com.zorroe.cloud.filebox.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Slf4j
@Configuration
public class ThreadPoolConfig {

    /**
     * 通用业务线程池
     */
    @Bean("FileOperateTaskExecutor")
    public ExecutorService businessTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数：CPU 核数 * 2（适用于 I/O 密集型任务）
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        // 最大线程数：防止资源耗尽
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 4);
        // 队列容量：有界队列，避免内存溢出
        executor.setQueueCapacity(1000);
        // 线程空闲存活时间（秒）
        executor.setKeepAliveSeconds(60);
        // 线程名称前缀
        executor.setThreadNamePrefix("biz-pool-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60); // 等待60秒再强制关闭
        // 拒绝策略：记录日志 + 抛出异常（或可自定义）
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // 记录拒绝任务的日志（建议接入监控/告警）
                log.error("【线程池拒绝任务】线程池已满，任务被拒绝: {}", r);
                throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + executor.toString());
            }
        });

        // 初始化线程池
        executor.initialize();

        return executor.getThreadPoolExecutor();
    }
}

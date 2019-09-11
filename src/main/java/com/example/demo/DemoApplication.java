package com.example.demo;

import com.example.demo.configs.mapper.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@MapperScan(basePackages = "com.example.demo.dao")
@SpringBootApplication(scanBasePackages = {"com.example.demo"})
public class DemoApplication {
    /**
     * 开启WebSocket支持
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 线程池
     */
    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 此方法返回可用处理器的虚拟机的最大数量; 不小于1
        int core = Runtime.getRuntime().availableProcessors();
        // 设置核心线程数
        executor.setCorePoolSize(core);
        // 设置最大线程数
        executor.setMaxPoolSize(core * 2 + 1);
        // 除核心线程外的线程存活时间
        executor.setKeepAliveSeconds(3);
        // 如果传入值大于0，底层队列使用的是LinkedBlockingQueue,否则默认使用SynchronousQueue
        executor.setQueueCapacity(40);
        // 线程名称前缀
        executor.setThreadNamePrefix("thread-execute");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

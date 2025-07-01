package com.SmsCallback.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//@Configuration
public class AsyncConfig {

    @Bean(name = "apiCall")
    public ThreadPoolTaskExecutor apiCallAsyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(400);
        executor.setMaxPoolSize(600);
        executor.setQueueCapacity(6000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("apiCall- ");
//        executor.initialize(); 
        return executor;
    }

    @Bean(name = "callbackInsert")
    public ThreadPoolTaskExecutor callbackInsertTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(400);
        executor.setMaxPoolSize(600);
        executor.setQueueCapacity(6000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("callbackInsert- ");
//        executor.initialize(); // ✅ Initialize executor
        return executor;
    }
}

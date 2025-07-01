package com.SmsCallback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
public class SmsCallbackRecordApplication{

	public static void main(String[] args)  {
		SpringApplication.run(SmsCallbackRecordApplication.class, args);
	}

	@Bean("apiCall")
	 TaskExecutor apiCallAsyncTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(40);
		executor.setMaxPoolSize(60);
		executor.setQueueCapacity(600);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("apiCall- ");
		return executor;
	}

	
	@Bean("callbackInsert")
	 TaskExecutor getAsyncTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(40);
		executor.setMaxPoolSize(60);
		executor.setQueueCapacity(600);
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setThreadNamePrefix("callbackInsert- ");
		return executor;
	}

}
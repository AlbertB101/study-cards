package edu.albert.studycards.authserver.config;

import edu.albert.studycards.authserver.service.AccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@Configuration
public class AppConfig  {

	public static final String TASK_EXECUTOR_NAME = "threadPoolTaskExecutor";
	
	/**
	 * Configure thread pool bean for using it in {@link AccountServiceImpl}.
	 * @return configured ThreadPoolTaskExecutor
	 */
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setBeanName(TASK_EXECUTOR_NAME);
		executor.setCorePoolSize(3);
		executor.setMaxPoolSize(6);
		executor.setQueueCapacity(100);
		executor.setThreadGroupName("ServiceLayerThreadPool");
		executor.initialize();
		return executor;
	}
}
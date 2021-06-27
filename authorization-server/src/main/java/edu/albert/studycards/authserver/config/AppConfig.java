package edu.albert.studycards.authserver.config;

import edu.albert.studycards.authserver.service.UserAccountServiceImpl;
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
	
	/**
	 * Configure thread pool bean for using it in {@link UserAccountServiceImpl}.
	 * @return configured ThreadPoolTaskExecutor
	 */
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(3);
		executor.setMaxPoolSize(6);
		executor.setQueueCapacity(100);
		executor.setThreadGroupName("ServiceLayerThreadPool");
		executor.initialize();
		return executor;
	}
}
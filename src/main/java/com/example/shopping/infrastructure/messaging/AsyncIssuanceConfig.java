package com.example.shopping.infrastructure.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncIssuanceConfig { // v4: 스프링 큐
    // @Async를 사용하면 기본으로 TaskExecutor을 제공하지만 좀더 정교하게 다루기 위해 커스텀

    public static final String ISSUANCE_TASK_EXECUTOR = "issuanceTaskExecutor";

    @Bean(name =ISSUANCE_TASK_EXECUTOR)
    public Executor issuanceTaskExecutor() {
        // 스프링이 제공하는 executor 사용
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(10000);
        // 로그에 기록되는 이름
        executor.setThreadNamePrefix("issuance-async");
        // 앱이 비정상 종료될 때 스레드 풀에서 작업중인 일은 모두 완료하고 shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 종료전 30초 여유를 두고 종료
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }
}

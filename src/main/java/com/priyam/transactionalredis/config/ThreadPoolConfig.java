package com.priyam.transactionalredis.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.*;


@EnableAsync
@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {

    @Bean("threadpool")
    @Override
    public ExecutorService getAsyncExecutor() {
        return new ThreadPoolExecutor(2, 5, 5, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

//    @Bean("threadpool")
//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//        threadPoolTaskExecutor.setCorePoolSize(2);
//        threadPoolTaskExecutor.setMaxPoolSize(5);
//        threadPoolTaskExecutor.setQueueCapacity(10);
//        threadPoolTaskExecutor.setThreadNamePrefix("RedisBoilerPlate-");
//        threadPoolTaskExecutor.initialize();
//        return threadPoolTaskExecutor;
//    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }

    class CustomAsyncExceptionHandler
            implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(
                Throwable throwable, Method method, Object... obj) {

            System.out.println("Exception message - " + throwable.getMessage());
            System.out.println("Method name - " + method.getName());
            for (Object param : obj) {
                System.out.println("Parameter value - " + param);
            }
        }

    }

}



package com.priyam.transactionalredis.service;

import com.priyam.transactionalredis.constant.Constants;
import com.priyam.transactionalredis.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

@Service
public class UserService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final Integer numberOfIncrement = 1000;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Qualifier("threadpool")
    @Autowired
    private ExecutorService executorService;

    public void persistNonTransactional() {
        LOGGER.info("Current Value of userKey : {}", redisTemplate.opsForValue().get(Constants.NONTRANSACTIONAL_USER_KEY));

        ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
        for(int i = 0 ; i < numberOfIncrement ; i++) {
            executorCompletionService.submit(() -> fetchUserKeyAndIncrementNonTransactional());
        }

        for(int i = 0 ; i < numberOfIncrement ; i++) {
            try {
                executorCompletionService.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("Final Value of userKey : {}", redisTemplate.opsForValue().get(Constants.NONTRANSACTIONAL_USER_KEY));
    }

    private Void fetchUserKeyAndIncrementNonTransactional() {
        String value = redisTemplate.opsForValue().get(Constants.NONTRANSACTIONAL_USER_KEY);
        if(value == null) {
            redisTemplate.opsForValue().set(Constants.NONTRANSACTIONAL_USER_KEY, String.valueOf(0));
            return null;
        }
        Integer newValue = Integer.parseInt(value) + 1;
        redisTemplate.opsForValue().set(Constants.NONTRANSACTIONAL_USER_KEY, newValue.toString());
        return null;
    }

    public void persistTransactional() {
    }

    public void addDetails(User user) {
        redisTemplate.opsForValue().set(user.getUserId().toString(), user.getName());
    }

    public String getDetails(Integer userId) {
        return redisTemplate.opsForValue().get(userId.toString());
    }
}

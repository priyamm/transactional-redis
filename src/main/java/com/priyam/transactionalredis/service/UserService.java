package com.priyam.transactionalredis.service;

import com.priyam.transactionalredis.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public void persistNonTransactional() {
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

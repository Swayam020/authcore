package com.swayam.authcore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private static final String PREFIX = "blacklist:";
    private final StringRedisTemplate redisTemplate;   // auto-configured from spring.data.redis.*

    public void blacklist(String jti, long ttlMillis) {
        if (ttlMillis <= 0) return;   // already expired — nothing to store
        redisTemplate.opsForValue().set(PREFIX + jti, "1", Duration.ofMillis(ttlMillis));
    }

    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + jti));
    }
}

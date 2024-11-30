package com.finance.common.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.dto.Cacheable;
import com.finance.common.exception.ExceptionService;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ConditionalOnBean(RedisTemplate.class)
public class CacheServiceBuilder {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ExceptionService exceptionService;

    public <V extends Cacheable> CacheService<V> buildCacheService(final String cacheName, final Class<V> type) {
        return new RedisCacheService<>(redisTemplate, objectMapper, cacheName, type, exceptionService);
    }
}

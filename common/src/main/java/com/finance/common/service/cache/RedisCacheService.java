package com.finance.common.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.dto.Cacheable;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ConditionalOnProperty(name = "enable.redis.cache", havingValue = "true")
public class RedisCacheService<V extends Cacheable> implements CacheService<V> {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final String cacheName;
    private final Class<V> type;
    private final ExceptionService exceptionService;

    @Value("${cache.time-to-live-sec:7200}")
    private int timeToLiveSec;

    @Override
    public void cache(final String key, final V value) {
        redisTemplate.opsForHash().put(this.cacheName, key, value);
        redisTemplate.expire(this.cacheName, this.timeToLiveSec, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public void cacheAll(final Map<String, V> map) {
        redisTemplate.opsForHash().putAll(cacheName, map);
        redisTemplate.expire(cacheName, this.timeToLiveSec, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public V get(final String key) {
        HashOperations<String, String, V> hashOps = redisTemplate.opsForHash();
        Object value = hashOps.get(this.cacheName, key);

        return convertToConcreteClass(value);
    }

    /**
     * Get all values.
     *
     * @return all values
     */
    public List<V> getAll() {
        HashOperations<String, String, V> hashOps = redisTemplate.opsForHash();
        final var values = hashOps.values(this.cacheName);

        List<V> convertedValues = new ArrayList<>();
        for (Object value : values) {
            V convertedValue = convertToConcreteClass(value);
            convertedValues.add(convertedValue);
        }

        return convertedValues;
    }

    /**
     * Convert map to object.
     *
     * @param cachedObject map of key value
     * @return mapped object
     */
    private V convertToConcreteClass(final Object cachedObject) {
        try {
            return objectMapper.convertValue(cachedObject, this.type);
        } catch (Exception e) {
            throw exceptionService.throwInternalException(SharedApplicationError.GENERIC_ERROR);
        }
    }
}

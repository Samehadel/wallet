package com.finance.common.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.dto.Cacheable;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.InternalException;
import com.finance.common.exception.SharedApplicationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.Getter;
import lombok.Setter;

/**
 * AbstractCacheService.
 * @param <V> The value type.
 *
 * @author Sameh.Adel
 * @since 25/11/2024
 */
@Getter
public class CacheService<V extends Cacheable> {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final String cacheName;
    private final Class<V> type;
    private final ExceptionService exceptionService;

    @Setter
    private int timeToLiveSec = 7200; //2 Hours

    protected CacheService(final RedisTemplate<String, Object> redisTemplate,
        final ObjectMapper objectMapper, final String cacheName, final Class<V> type, final ExceptionService exceptionService) {

        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cacheName = cacheName;
        this.type = type;
        this.exceptionService = exceptionService;
    }

    public void cache(final String key, final V value) {
        redisTemplate.opsForHash().put(this.cacheName, key, value);
        redisTemplate.expire(this.cacheName, this.timeToLiveSec, java.util.concurrent.TimeUnit.SECONDS);
    }

    public void cacheAll(final Map<String, V> map) {
        redisTemplate.opsForHash().putAll(cacheName, map);
        redisTemplate.expire(cacheName, this.timeToLiveSec, java.util.concurrent.TimeUnit.SECONDS);
    }


    public V get(final String key) {
        HashOperations<String, String, V> hashOps = redisTemplate.opsForHash();
        Object value = hashOps.get(this.cacheName, key);

        return convertFromMap((Map<String, Object>) value);
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
            V convertedValue = convertFromMap((Map<String, Object>) value);
            convertedValues.add(convertedValue);
        }

        return convertedValues;
    }

    /**
     * Convert map to object.
     *
     * @param map map of key value
     * @return mapped object
     */
    private V convertFromMap(final Map<String, Object> map) {
        try {
            return objectMapper.convertValue(map, this.type);
        } catch (Exception e) {
            throw exceptionService.throwInternalException(SharedApplicationError.GENERIC_ERROR);
        }
    }
}

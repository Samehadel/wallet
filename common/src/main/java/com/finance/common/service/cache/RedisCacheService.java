package com.finance.common.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.dto.Cacheable;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ConditionalOnProperty(name = "enable.redis.cache", havingValue = "true")
public class RedisCacheService<V extends Cacheable> implements CacheService<V> {
    private final RMapCache<String, V> mapCache;
    private final ObjectMapper objectMapper;
    private final Class<V> type;
    private final ExceptionService exceptionService;

    @Value("${cache.time-to-live-sec:7200}")
    private int timeToLiveSec;


    @Override
    public void cache(String key, V value) {
        mapCache.put(key, value, timeToLiveSec, TimeUnit.SECONDS);
    }

    @Override
    public void cacheAll(Map<String, V> map) {
        map.forEach((key, value) -> mapCache.put(key, value, timeToLiveSec, TimeUnit.SECONDS));
    }

    @Override
    public V get(String key) {
        return mapCache.get(key);
    }

    /**
     * Get all values.
     *
     * @return all values
     */
    @Override
    public List<V> getAll() {
        return new ArrayList<>(mapCache.values());
    }
}

package com.finance.common.service.cache;

import com.finance.common.dto.Cacheable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@ConditionalOnProperty(name = "enable.redis.cache", havingValue = "true")
public class RedisCacheService<V extends Cacheable> implements CacheService<V> {
    private final RMapCache<String, V> mapCache;
    private final Class<V> type;
    private final long timeToLiveSec;


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
        V value = mapCache.get(key);

        if (value != null && !type.isInstance(value)) {
            throw new IllegalArgumentException("Cached value is not of the expected type: " + type.getName());
        }

        return value;
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

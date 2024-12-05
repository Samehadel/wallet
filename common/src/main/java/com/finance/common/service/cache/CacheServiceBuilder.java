package com.finance.common.service.cache;

import com.finance.common.dto.Cacheable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "application.cache.enable", havingValue = "true")
public class CacheServiceBuilder {
    private final RedissonClient redissonClient;
    private final Map<String, CacheService<? extends Cacheable>> cacheServiceMap = new ConcurrentHashMap<>();

    @Value("${cache.time-to-live-sec:7200}")
    @Setter
    private int defaultCacheTtl;

    public <V extends Cacheable> CacheService<V> buildCacheInstance(final String cacheName, final Class<V> type) {
        return buildCacheInstance(cacheName, type, defaultCacheTtl);
    }

    /**
     * Build a cache service instance with a custom TTL.
     *
     * @param cacheName     the cache name
     * @param type          the class type of the cacheable object
     * @param timeToLiveSec the custom TTL in seconds
     * @return the cache service instance
     */
    @SuppressWarnings("unchecked")
    public <V extends Cacheable> CacheService<V> buildCacheInstance(final String cacheName, final Class<V> type, final int timeToLiveSec) {
        return (CacheService<V>) cacheServiceMap.computeIfAbsent(cacheName, name -> createCacheService(name, type, timeToLiveSec));
    }

    /**
     * Create a new cache service instance.
     *
     * @param cacheName     the cache name
     * @param type          the class type of the cacheable object
     * @param timeToLiveSec the TTL in seconds
     * @return a new RedisCacheService instance
     */
    private <V extends Cacheable> RedisCacheService<V> createCacheService(final String cacheName, Class<V> type, int timeToLiveSec) {
        RMapCache<String, V> mapCache = redissonClient.getMapCache(cacheName);
        return new RedisCacheService<>(mapCache, type, timeToLiveSec);
    }
}

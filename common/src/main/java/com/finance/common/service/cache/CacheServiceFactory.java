package com.finance.common.service.cache;

import com.finance.common.dto.Cacheable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Service
@ConditionalOnProperty(name = "application.cache.enable", havingValue = "true")
@Log4j2
public class CacheServiceFactory {
    private final RedissonClient redissonClient;
    private final Map<String, CacheService<? extends Cacheable>> cacheServiceMap = new ConcurrentHashMap<>();

    @Value("${cache.time-to-live-sec:7200}")
    @Setter
    private int defaultCacheTtl;

    @Autowired
    public CacheServiceFactory(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        log.info("Cache service factory initialized");
    }

    public <V extends Cacheable> CacheService<V> buildCacheInstance(final String cacheName, final Class<V> type) {
        return buildCacheInstance(cacheName, type, defaultCacheTtl);
    }

    @SuppressWarnings("unchecked")
    public <V extends Cacheable> CacheService<V> buildCacheInstance(final String cacheName, final Class<V> type, final int timeToLiveSec) {
        log.debug("Building cache service for cache name [{}], type [{}], time to live [{}]", cacheName, type, timeToLiveSec);
        return (CacheService<V>) cacheServiceMap.computeIfAbsent(cacheName, name -> createCacheService(name, type, timeToLiveSec));
    }

    private <V extends Cacheable> RedisCacheService<V> createCacheService(final String cacheName, Class<V> type, int timeToLiveSec) {
        RMapCache<String, V> mapCache = redissonClient.getMapCache(cacheName);
        return new RedisCacheService<>(mapCache, type, timeToLiveSec);
    }
}

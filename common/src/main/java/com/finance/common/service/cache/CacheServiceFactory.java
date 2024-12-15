package com.finance.common.service.cache;

import com.finance.common.dto.Cacheable;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.util.ObjectUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@DependsOn("redissonClient")
@ConditionalOnBean(RedissonClient.class)
public class CacheServiceFactory {
    private final RedissonClient redissonClient;
    private final ExceptionService exceptionService;
    private final Map<String, CacheService<? extends Cacheable>> cacheServiceMap = new ConcurrentHashMap<>();

    @Value("${cache.default.time-to-live-sec:172800}")
    private Integer defaultTimeToLiveSeconds;

    public CacheServiceFactory(final RedissonClient redissonClient, final ExceptionService exceptionService) {
        this.redissonClient = redissonClient;
        this.exceptionService = exceptionService;
        log.info("Cache service factory initialized with default ttl [{}]", this.defaultTimeToLiveSeconds);
    }

    public <V extends Cacheable> CacheService<V> buildCacheInstance(final String cacheName, final Class<V> type) {
        if (ObjectUtils.anyNull(cacheName, type)) {
            throw exceptionService.buildInternalExceptionWithReference(SharedApplicationError.MISSING_REQUIRED_FIELD, "cache name or type");
        }
        return buildCacheInstance(cacheName, type, defaultTimeToLiveSeconds);
    }

    @SuppressWarnings("unchecked")
    public <V extends Cacheable> CacheService<V> buildCacheInstance(final String cacheName, final Class<V> type, final long customTtlSeconds) {
        log.debug("Building cache service for cache name [{}], type [{}], time to live [{}]", cacheName, type, customTtlSeconds);

        if (ObjectUtils.anyNull(cacheName, type)) {
            log.error("Invalid cache name or type, cache name [{}], type [{}]", cacheName, type);
            throw exceptionService.buildInternalExceptionWithReference(SharedApplicationError.MISSING_REQUIRED_FIELD, "cache name or type");
        }

        return (CacheService<V>) cacheServiceMap.computeIfAbsent(cacheName, name -> createCacheService(name, type, getTtlOrDefault(customTtlSeconds)));
    }

    private long getTtlOrDefault(final long customTtlSeconds) {
        if (customTtlSeconds <= 0) {
            log.warn("Invalid time to live value [{}], using default value [{}]", customTtlSeconds, defaultTimeToLiveSeconds);
            return defaultTimeToLiveSeconds;
        }

        return customTtlSeconds;
    }

    private <V extends Cacheable> RedisCacheService<V> createCacheService(final String cacheName, Class<V> type, long timeToLiveSec) {
        RMapCache<String, V> mapCache = redissonClient.getMapCache(cacheName);
        return new RedisCacheService<>(mapCache, type, timeToLiveSec);
    }
}

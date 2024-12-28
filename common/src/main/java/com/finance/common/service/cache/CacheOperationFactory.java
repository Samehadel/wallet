package com.finance.common.service.cache;

import com.finance.common.dto.Cacheable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class CacheOperationFactory {
    private final Map<String, CacheOperationService<? extends Cacheable>> cacheOperationServices = new ConcurrentHashMap<>();
    private final CacheServiceFactory cacheServiceFactory;

    public CacheOperationFactory(final Optional<CacheServiceFactory> cacheServiceFactory) {
        this.cacheServiceFactory = cacheServiceFactory.orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <V extends Cacheable> CacheOperationService<V> createInstance(final String cacheName, final Class<V> type, final long timeToLiveSeconds) {
        return (CacheOperationService<V>) cacheOperationServices.computeIfAbsent(
            cacheName, key -> new CacheOperationService<>(cacheServiceFactory, cacheName, type, timeToLiveSeconds)
        );
    }
}

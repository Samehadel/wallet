package com.finance.common.service.cache;

import com.finance.common.dto.Cacheable;

import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
public class CacheOperationService<V extends Cacheable> {
    private final CacheServiceFactory cacheServiceFactory;
    private final String cacheName;
    private final Class<V> type;
    private final long timeToLiveSeconds;

    public void cache(final String key, final V value) {
        CacheService<V> cacheService = buildCacheService();

        if (cacheService == null) {
            logServiceAbsenceWarnMessage();
            return;
        }

        cacheService.cache(key, value);
    }

    public void cacheAll(final Map<String, V> map) {
        CacheService<V> cacheService = buildCacheService();

        if (cacheService == null) {
            logServiceAbsenceWarnMessage();
            return;
        }

        cacheService.cacheAll(map);
    }

    public V get(final String key) {
        CacheService<V> cacheService = buildCacheService();

        if (cacheService == null) {
            logServiceAbsenceWarnMessage();
            return null;
        }

        return cacheService.get(key);
    }

    public List<V> getAll() {
        CacheService<V> cacheService = buildCacheService();

        if (cacheService == null) {
            logServiceAbsenceWarnMessage();
            return null;
        }

        return cacheService.getAll();
    }

    private CacheService<V> buildCacheService() {
        if (cacheServiceFactory == null) {
            log.warn("Cache service factory not found");
            return null;
        }

        return cacheServiceFactory.buildCacheInstance(cacheName, type, timeToLiveSeconds);
    }

    private void logServiceAbsenceWarnMessage() {
        log.warn("Cache service not found for cache name [{}]", this.cacheName);
    }
}

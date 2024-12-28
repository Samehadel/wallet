package com.finance.common.service.cache;

import com.finance.common.dto.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * AbstractCacheService.
 * @param <V> The value type.
 *
 * @author Sameh.Adel
 * @since 25/11/2024
 */
interface CacheService<V extends Cacheable> {
    void cache(final String key, final V value);

    void cacheAll(final Map<String, V> map);

    V get(final String key);

    List<V> getAll();
}

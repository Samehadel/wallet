package com.finance.common.controller;

import com.finance.common.dto.CacheTestClass;
import com.finance.common.service.cache.CacheService;
import com.finance.common.service.cache.CacheServiceBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cache/test")
@Profile("dev")
@RequiredArgsConstructor
@ConditionalOnBean(CacheServiceBuilder.class)
public class CacheTestController {
    private static final String CACHE_NAME = "test-cache";

    private final CacheServiceBuilder cacheServiceBuilder;


    @PostMapping
    public void testCache(@RequestBody CacheTestClass cacheTestClass) {
        CacheService<CacheTestClass> cacheService = cacheServiceBuilder.buildCacheInstance(CACHE_NAME, CacheTestClass.class);

        cacheService.cache(cacheTestClass.getMobile(), cacheTestClass);
    }

    @PostMapping("/ttl/{ttl}")
    public void testCache(@PathVariable("ttl") int ttl, @RequestBody CacheTestClass cacheTestClass) {
        CacheService<CacheTestClass> cacheService = cacheServiceBuilder.buildCacheInstance(CACHE_NAME, CacheTestClass.class, ttl);

        cacheService.cache(cacheTestClass.getMobile(), cacheTestClass);
    }

    @PostMapping("/all")
    public void testAllCache(@RequestBody List<CacheTestClass> cacheTestClasses) {
        CacheService<CacheTestClass> cacheService = cacheServiceBuilder.buildCacheInstance(CACHE_NAME, CacheTestClass.class);

        Map<String, CacheTestClass> cacheMap = cacheTestClasses.stream().collect(Collectors.toMap(CacheTestClass::getMobile, Function.identity()));
        cacheService.cacheAll(cacheMap);
    }

    @GetMapping("/key/{key}")
    public CacheTestClass getCache(@PathVariable("key") final String key) {
        CacheService<CacheTestClass> cacheService = cacheServiceBuilder.buildCacheInstance(CACHE_NAME, CacheTestClass.class);

        return cacheService.get(key);
    }

    @GetMapping("/all")
    public List<CacheTestClass> getAllCache() {
        CacheService<CacheTestClass> cacheService = cacheServiceBuilder.buildCacheInstance(CACHE_NAME, CacheTestClass.class);

        return cacheService.getAll();
    }

}

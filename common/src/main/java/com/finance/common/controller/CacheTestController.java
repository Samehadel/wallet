package com.finance.common.controller;

import com.finance.common.dto.CacheTestClass;
import com.finance.common.service.cache.CacheOperationFactory;
import com.finance.common.service.cache.CacheOperationService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache/test")
@Profile("dev")
public class CacheTestController {
    private static final String CACHE_NAME = "test-cache";

    private final CacheOperationService<CacheTestClass> cacheOperationService;

    public CacheTestController(final CacheOperationFactory cacheOperationFactory) {
        this.cacheOperationService = cacheOperationFactory.createInstance(CACHE_NAME, CacheTestClass.class, 120);
    }

    @PostMapping
    public void testCache(@RequestBody final CacheTestClass cacheTestClass) {
        cacheOperationService.cache(cacheTestClass.getMobile(), cacheTestClass);
    }

    @PostMapping("/all")
    public void testAllCache(@RequestBody final List<CacheTestClass> cacheTestClasses) {
        Map<String, CacheTestClass> cacheMap = cacheTestClasses.stream().collect(Collectors.toMap(CacheTestClass::getMobile, Function.identity()));
        cacheOperationService.cacheAll(cacheMap);
    }

    @GetMapping("/key/{key}")
    public CacheTestClass getCache(@PathVariable("key") final String key) {
        return cacheOperationService.get(key);
    }

    @GetMapping("/all")
    public List<CacheTestClass> getAllCache() {
        return cacheOperationService.getAll();
    }

}

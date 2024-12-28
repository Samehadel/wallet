package com.finance.common.service;

import com.finance.common.dto.ServiceConfigurationDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.persistence.repository.ServiceConfigRepository;
import com.finance.common.service.cache.CacheOperationFactory;
import com.finance.common.service.cache.CacheOperationService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@ConditionalOnProperty(value = "service.config.type", havingValue = "database")
@RequiredArgsConstructor
public class DatabaseServiceConfig implements ServiceConfiguration {
    private final ExceptionService exceptionService;
    private final ServiceConfigRepository serviceConfigurationRepository;
    private final CacheOperationFactory cacheOperationFactory;

    private CacheOperationService<ServiceConfigurationDTO> cacheOperationService;

    @Value("${service.config.cache.enabled:false}")
    private boolean serviceConfigCacheEnabled;

    @Value("${service.config.cache.name:service-config-cache}")
    private String cacheName;

    @PostConstruct
    public void initializeCacheOperation() {
        cacheOperationService = cacheOperationFactory.createInstance(cacheName, ServiceConfigurationDTO.class, 120);
    }

    @Override
    public String getConfiguration(final String key) {
        try {
            log.info("Getting configuration for Key: [{}]", key);
            String value = fetchConfigurationFromCacheIfEnabled(key);

            if (value != null) {
                return value;
            }

            value = serviceConfigurationRepository.findByKey(key).orElseThrow().getValue();
            cacheConfigurationIfEnabled(key, value);

            return value;
        } catch (Exception e) {
            log.error("Error getting configuration: [{}]", key, e);
            throw exceptionService.buildInternalExceptionWithReference(SharedApplicationError.GENERIC_ERROR);
        }
    }

    private String fetchConfigurationFromCacheIfEnabled(final String key) {
        if (serviceConfigCacheEnabled) {
            log.info("Fetching configuration from cache for Key: [{}]", key);
            ServiceConfigurationDTO configuration = cacheOperationService.get(key);

            if (configuration != null) {
                log.info("Configuration found in cache for Key: [{}]", key);
                return configuration.getValue();
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private void cacheConfigurationIfEnabled(final String key, final String value) {
        if (serviceConfigCacheEnabled) {
            log.info("Caching configuration for Key: [{}]", key);
            cacheOperationService.cache(key, new ServiceConfigurationDTO(value));
        }
    }
}

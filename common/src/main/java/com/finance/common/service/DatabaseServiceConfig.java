package com.finance.common.service;

import com.finance.common.dto.ServiceConfigurationDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.persistence.repository.ServiceConfigRepository;
import com.finance.common.service.cache.CacheService;
import com.finance.common.service.cache.CacheServiceFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
@ConditionalOnProperty(value = "enable.database.service.config", havingValue = "true")
public class DatabaseServiceConfig implements ServiceConfiguration {
    private final ExceptionService exceptionService;
    private final ServiceConfigRepository serviceConfigurationRepository;
    private final CacheServiceFactory cacheServiceFactory;

    @Value("${service.config.cache.enabled:false}")
    private boolean cacheEnabled;

    @Value("${service.config.cache.name:service_config_cache}")
    private String cacheName;

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
        if (cacheEnabled) {
            log.info("Fetching configuration from cache for Key: [{}]", key);
            CacheService<ServiceConfigurationDTO> cacheService = getCacheService();
            ServiceConfigurationDTO configuration = cacheService.get(key);

            if (configuration != null) {
                log.info("Configuration found in cache for Key: [{}]", key);
                return configuration.getValue();
            }
        }

        return null;
    }

    private void cacheConfigurationIfEnabled(final String key, final String value) {
        if (cacheEnabled) {
            log.info("Caching configuration for Key: [{}]", key);
            final var cacheService = cacheServiceFactory.buildCacheInstance(cacheName, ServiceConfigurationDTO.class);
            cacheService.cache(key, new ServiceConfigurationDTO(value));
        }
    }

    private CacheService<ServiceConfigurationDTO> getCacheService() {
        return cacheServiceFactory.buildCacheInstance(cacheName, ServiceConfigurationDTO.class);
    }
}

package com.finance.common.service;

import com.finance.common.dto.ServiceConfigurationDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.persistence.repository.ServiceConfigRepository;
import com.finance.common.service.cache.CacheOperationService;
import com.finance.common.service.cache.CacheServiceFactory;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DatabaseServiceConfig implements ServiceConfiguration {
    private final ExceptionService exceptionService;
    private final ServiceConfigRepository serviceConfigurationRepository;
    private final CacheOperationService<ServiceConfigurationDTO> cacheOperationService;

    @Value("${service.config.cache.enabled:false}")
    private boolean serviceConfigCacheEnabled;

    @Value("${service.config.cache.name:service_config_cache}")
    private String cacheName;

    public DatabaseServiceConfig(final ExceptionService exceptionService, final ServiceConfigRepository serviceConfigurationRepository,
        final Optional<CacheServiceFactory> cacheServiceFactoryOptional) {

        this.exceptionService = exceptionService;
        this.serviceConfigurationRepository = serviceConfigurationRepository;
        this.cacheOperationService = CacheOperationService.<ServiceConfigurationDTO>builder()
            .cacheServiceFactory(cacheServiceFactoryOptional.orElse(null))
            .cacheName(cacheName)
            .type(ServiceConfigurationDTO.class)
            .timeToLiveSeconds(86400)
            .build();
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

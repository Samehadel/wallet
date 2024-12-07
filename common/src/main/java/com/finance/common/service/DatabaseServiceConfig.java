package com.finance.common.service;

import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.persistence.repository.ServiceConfigRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class DatabaseServiceConfig implements ServiceConfiguration {
    private final ExceptionService exceptionService;
    private final ServiceConfigRepository serviceConfigurationRepository;

    @Override
    public String getConfiguration(String key) {
        try {
            log.info("Getting configuration for Key: [{}]", key);
            return serviceConfigurationRepository.findByKey(key).orElseThrow().getValue();
        } catch (Exception e) {
            log.error("Error getting configuration: [{}]", key, e);
            throw exceptionService.buildInternalException(SharedApplicationError.GENERIC_ERROR);
        }
    }
}

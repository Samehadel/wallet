package com.finance.common.service;

import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class DatabaseServiceConfig implements ServiceConfiguration {
    private final ExceptionService exceptionService;

    @Override
    public String getConfiguration(String key) {
        try {
            //return serviceConfigurationRepository.findByKey(key).orElseThrow().getValue();
            return null;
        } catch (Exception e) {
            log.error("Error getting configuration: [{}]", key, e);
            throw exceptionService.throwInternalException(SharedApplicationError.GENERIC_ERROR);
        }
    }
}

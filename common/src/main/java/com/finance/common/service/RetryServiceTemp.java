package com.finance.common.service;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RetryServiceTemp {
    private static final int MAX_ATTEMPTS = 5;
    private static final int SUCCESS_AFTER_ATTEMPTS = 6;

    private int attempts = 0;

    @Retryable(retryFor = { RuntimeException.class }, maxAttempts = MAX_ATTEMPTS, backoff = @Backoff(delay = 500))
    public void retryService(final String message) {
        log.info("Retry service called with attempts: [{}]", attempts + 1);

        if (attempts < SUCCESS_AFTER_ATTEMPTS) {
            attempts ++;
            throw new RuntimeException("Service failed");
        }

        log.info("Printing message: [{}]", message);
    }

    @Recover
    private void recoverService(final RuntimeException e, final String message) {
        log.error("Error occurred while calling service for message: [{}]", message);
    }
}

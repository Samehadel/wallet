package com.finance.common.client;

import com.finance.common.dto.AccessUrlDTO;
import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.util.ClientUtils;

import java.util.List;

import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserClientRestTemplate implements UserClient {
    private final RestTemplate restTemplate;
    private final ExceptionService exceptionService;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    private static final String SERVICE_NAME = "user-svc";
    private static final String SERVICE_BASE_URL = "/api/v1/user";
    private static final String USER_CONTROLLER_BASE_URL = "/internal";
    private static final String ENDPOINT_CONTROLLER_BASE_URL = "/internal/endpoint";
    private static final String USER_INFO_CIRCUIT_BREAKER_NAME = "userInfoCircuitBreaker";
    private static final String ENDPOINT_CIRCUIT_BREAKER_NAME = "endpointCircuitBreaker";

    @Override
    public UserDTO getUserByUsername(final String username) {
        String url = ClientUtils.constructUrl(SERVICE_NAME, SERVICE_BASE_URL, USER_CONTROLLER_BASE_URL, "/username/{username}");

        return circuitBreakerFactory.create(USER_INFO_CIRCUIT_BREAKER_NAME)
            .run(() -> callGetForUserObject(url, username),
                throwable -> handleUserFallback(throwable, username));
    }

    @Override
    public UserDTO getUserByMobile(final String mobile) {
        String url = ClientUtils.constructUrl(SERVICE_NAME, SERVICE_BASE_URL, USER_CONTROLLER_BASE_URL, "/mobile/{mobile}");

        return circuitBreakerFactory.create(USER_INFO_CIRCUIT_BREAKER_NAME)
            .run(() -> callGetForUserObject(url, mobile),
                throwable -> handleUserFallback(throwable, mobile));
    }

    private UserDTO callGetForUserObject(final String url, final Object... uriVariables) {
        return restTemplate.getForObject(url, UserDTO.class, uriVariables);
    }

    @Override
    public List<AccessUrlDTO> getPublicEndpoints() {
        String url = ClientUtils.constructUrl(SERVICE_NAME, SERVICE_BASE_URL, ENDPOINT_CONTROLLER_BASE_URL, null);

        return circuitBreakerFactory.create(ENDPOINT_CIRCUIT_BREAKER_NAME)
            .run(() -> callGetForParameterizedType(url, getAccessUrlResponseType()), this::handleEndpointsFallback);
    }

    @Override
    public List<AccessUrlDTO> getUserEndpoints(final String username) {
        String url = ClientUtils.constructUrl(SERVICE_NAME, SERVICE_BASE_URL, ENDPOINT_CONTROLLER_BASE_URL, "/username/{username}");

        return circuitBreakerFactory.create(ENDPOINT_CIRCUIT_BREAKER_NAME)
            .run(() -> callGetForParameterizedType(url, getAccessUrlResponseType(), username),
                this::handleEndpointsFallback);
    }

    private <R> R callGetForParameterizedType(final String url, final ParameterizedTypeReference<R> responseType, final Object... uriVariables) {
        ResponseEntity<R> responseEntity = restTemplate.exchange(url,
            HttpMethod.GET,
            null,
            responseType,
            uriVariables);

        return responseEntity.getBody();
    }

    private ParameterizedTypeReference<List<AccessUrlDTO>> getAccessUrlResponseType() {
        return new ParameterizedTypeReference<>() {
        };
    }

    private UserDTO handleUserFallback(final Throwable throwable, final String identifier) {
        log.error("Circuit breaker fallback for user lookup with identifier: {}", identifier, throwable);

        throw exceptionService.buildServiceUnavailableException(SharedApplicationError.GENERIC_ERROR);
    }

    private List<AccessUrlDTO> handleEndpointsFallback(final Throwable throwable) {
        log.error("Circuit breaker fallback for user endpoints lookup [{}]", throwable.getMessage());

        throw exceptionService.buildServiceUnavailableException(SharedApplicationError.GENERIC_ERROR);
    }
}

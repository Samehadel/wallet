package com.finance.common.client;

import com.finance.common.dto.AccessUrlDTO;
import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.util.ClientUtils;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserClientRestTemplate implements UserClient {
    private final RestTemplate restTemplate;
    private final ExceptionService exceptionService;

    private static final String SERVICE_NAME = "user-svc";
    private static final String SERVICE_BASE_URL = "/api/v1/user";
    private static final String USER_CONTROLLER_BASE_URL = "/internal";
    private static final String ENDPOINT_CONTROLLER_BASE_URL = "/internal/endpoint";

    @Override
    @CircuitBreaker(name = "getUserInfo", fallbackMethod = "fetchUserFallback")
    public UserDTO getUserByUsername(final String username) {
        String url = ClientUtils.constructUrl(SERVICE_NAME, SERVICE_BASE_URL, USER_CONTROLLER_BASE_URL, "/username/{username}");

        return callGetForUserObject(url, username);
    }

    @Override
    @CircuitBreaker(name = "getUserInfo", fallbackMethod = "fetchUserFallback")
    public UserDTO getUserByMobile(final String mobile) {
        String url = ClientUtils.constructUrl(SERVICE_NAME, SERVICE_BASE_URL, USER_CONTROLLER_BASE_URL, "/mobile/{mobile}");

        return callGetForUserObject(url, mobile);
    }

    private UserDTO callGetForUserObject(final String url, final Object... uriVariables) {
        return restTemplate.getForObject(url, UserDTO.class, uriVariables);
    }

    private UserDTO fetchUserFallback(final Exception e) {
        log.error("Error occurred while fetching user due to: [{}]", e.getMessage());
        throw exceptionService.buildInternalException(SharedApplicationError.GENERIC_ERROR);
    }

    @Override
    @CircuitBreaker(name = "getEndpoints", fallbackMethod = "fetchEndpointsFallback")
    public List<AccessUrlDTO> getPublicEndpoints() {
        String url = ClientUtils.constructUrl(SERVICE_NAME, SERVICE_BASE_URL, ENDPOINT_CONTROLLER_BASE_URL, null);

        return callGetForParameterizedType(url, getAccessUrlResponseType());
    }

    @Override
    @CircuitBreaker(name = "getEndpoints", fallbackMethod = "fetchEndpointsFallback")
    public List<AccessUrlDTO> getUserEndpoints(final String username) {
        String url = ClientUtils.constructUrl(SERVICE_NAME, SERVICE_BASE_URL, ENDPOINT_CONTROLLER_BASE_URL, "/username/{username}");

        return callGetForParameterizedType(url, getAccessUrlResponseType(), username);
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

    private List<AccessUrlDTO> fetchEndpointsFallback(final Exception e) {
        log.error("Error occurred while fetching endpoints due to: [{}]", e.getMessage());
        throw exceptionService.buildInternalException(SharedApplicationError.GENERIC_ERROR);
    }
}

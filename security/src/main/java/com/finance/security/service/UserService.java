package com.finance.security.service;

import com.finance.common.client.UserClient;
import com.finance.common.constants.UrlMethodEnum;
import com.finance.common.dto.AccessUriDTO;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserClient userClient;

    public boolean isPublicEndpoint(final String url, final UrlMethodEnum method) {
        List<AccessUriDTO> publicEndpoints = userClient.getPublicEndpoints();
        return anyEndpointMatch(url, method, publicEndpoints);
    }

    public boolean userHasAccess(final String username, final String url, final UrlMethodEnum method) {
        List<AccessUriDTO> userEndpoints = userClient.getUserEndpoints(username);
        return anyEndpointMatch(url, method, userEndpoints);
    }

    private boolean anyEndpointMatch(final String url, final UrlMethodEnum method, final List<AccessUriDTO> userEndpoints) {
        return userEndpoints.stream()
            .anyMatch(endpoint -> endpoint.getUrl().equals(url)
                && endpoint.getMethod().equals(method));
    }
}

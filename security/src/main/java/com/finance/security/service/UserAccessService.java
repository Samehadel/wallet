package com.finance.security.service;

import com.finance.common.client.UserClient;
import com.finance.common.constants.UrlMethodEnum;
import com.finance.common.dto.AccessUrlDTO;
import com.finance.common.service.cache.CacheOperationService;
import com.finance.security.dto.PathPatternAccessUri;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.util.pattern.PathPatternParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserAccessService {
    private final UserClient userClient;

    public boolean isPublicEndpoint(final String url, final UrlMethodEnum method) {
        List<AccessUrlDTO> publicEndpoints = getPublicEndpoints();
        return anyEndpointMatch(url, method, publicEndpoints);
    }

    private List<AccessUrlDTO> getPublicEndpoints() {
        // TODO: use cache
        return userClient.getPublicEndpoints();
    }

    public boolean userHasAccess(final String username, final String url, final UrlMethodEnum method) {
        List<AccessUrlDTO> userEndpoints = getUserEndpoints(username);
        return anyEndpointMatch(url, method, userEndpoints);
    }

    private List<AccessUrlDTO> getUserEndpoints(final String username) {
        // TODO: use cache
        return userClient.getUserEndpoints(username);
    }

    private List<PathPatternAccessUri> convertToPathPattern(final List<AccessUrlDTO> accessUris) {
        return accessUris
            .stream()
            .map(accessUri ->
                new PathPatternAccessUri(PathPatternParser.defaultInstance.parse(accessUri.getUrl()), accessUri.getMethod().getName()))
            .toList();
    }

    private boolean anyEndpointMatch(final String url, final UrlMethodEnum httpMethod, final List<AccessUrlDTO> accessUrls) {
        final List<PathPatternAccessUri> accessUrlsPathPatterns = convertToPathPattern(accessUrls);
        final PathContainer path = PathContainer.parsePath(url);
        HttpMethod httpMethod1 = HttpMethod.valueOf(httpMethod.name());

        return accessUrlsPathPatterns
            .stream()
            .anyMatch(userAccessibleUrl -> userAccessibleUrl.pathPattern().matches(path) && httpMethod1.matches(userAccessibleUrl.httpMethod()));
    }
}

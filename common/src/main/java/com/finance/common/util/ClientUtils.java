package com.finance.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientUtils {

    public static String constructUrl(final String serviceName,
        final String serviceBaseUrl,
        final String controllerBaseUrl,
        final String endpointPath) {

        StringBuilder url = new StringBuilder("http://");
        url.append(serviceName);

        if (appendable(serviceBaseUrl)) {
            url.append(serviceBaseUrl);
        }

        if (appendable(controllerBaseUrl)) {
            url.append(controllerBaseUrl);
        }

        if (appendable(endpointPath)) {
            url.append(endpointPath);
        }

        return url.toString();
    }

    private static boolean appendable(final String url) {
        return !StringUtil.isNullOrEmpty(url) && !hasOnlySlash(url);
    }

    private static boolean hasOnlySlash(final String serviceBaseUrl) {
        return serviceBaseUrl.startsWith("/") && serviceBaseUrl.length() == 1;
    }
}

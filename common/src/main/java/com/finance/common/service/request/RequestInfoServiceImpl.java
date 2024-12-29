package com.finance.common.service.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.constants.CommonHeaders;
import com.finance.common.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RequestInfoServiceImpl implements RequestInfoService {
    private final HttpServletRequest httpServletRequest;
    private final ObjectMapper objectMapper;

    public final UserDTO getRequestUser() {
        String userJson = httpServletRequest.getHeader(CommonHeaders.X_USER);
        if (userJson == null) {
            return null;
        }

        return convertJsonToUser(userJson);
    }

    private UserDTO convertJsonToUser(final String userJson) {
        try {
            return objectMapper.readValue(userJson, UserDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

}

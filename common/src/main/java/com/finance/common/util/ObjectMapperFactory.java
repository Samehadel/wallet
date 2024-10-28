package com.finance.common.util;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectMapperFactory {
    public static final ObjectMapper INSTANCE;
    public static final ObjectMapper SNAKE_CASE_INSTANCE;

    static {
        INSTANCE = JsonMapper
            .builder()
            .disable(FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .findAndAddModules()
            .build();

        SNAKE_CASE_INSTANCE = JsonMapper
            .builder()
            .disable(FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .findAndAddModules()
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build();
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }

    public static ObjectMapper getSnakeCaseInstance() {
        return SNAKE_CASE_INSTANCE;
    }
}

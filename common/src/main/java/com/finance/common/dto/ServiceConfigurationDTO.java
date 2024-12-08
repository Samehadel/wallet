package com.finance.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceConfigurationDTO implements Cacheable {
    private String value;
}

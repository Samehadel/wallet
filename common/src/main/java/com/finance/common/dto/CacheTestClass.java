package com.finance.common.dto;

import lombok.Data;

@Data
public class CacheTestClass implements Cacheable {
    private String mobile;

    private String name;
}

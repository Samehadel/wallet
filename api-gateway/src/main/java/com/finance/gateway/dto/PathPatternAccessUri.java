package com.finance.gateway.dto;

import org.springframework.web.util.pattern.PathPattern;

public record PathPatternAccessUri(PathPattern pathPattern, String httpMethod) {
}
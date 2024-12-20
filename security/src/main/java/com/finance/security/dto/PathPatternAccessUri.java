package com.finance.security.dto;

import org.springframework.web.util.pattern.PathPattern;

public record PathPatternAccessUri(PathPattern pathPattern, String httpMethod) {
}
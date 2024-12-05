package com.finance.common.exception;

import lombok.Builder;

@Builder
public record ErrorDetails(String errorCode, String errorMessage){
}

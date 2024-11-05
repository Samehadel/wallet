package com.finance.common.exception;

import com.finance.common.model.ApiResponse;
import com.finance.common.util.ApiResponseBuilder;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InternalException.class)
	public ResponseEntity<ApiResponse<Void>> handleException(final InternalException e) {
		log.error("Error occurred due to: {}", e.getErrorDetails().errorMessage());

		return ApiResponseBuilder.buildFailedResponse(e);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiResponse<Void>> handleApplicationException(final BadRequestException badRequestException) {
		log.error("Error occurred due to: {}", badRequestException.getErrorDetails().errorMessage());

		return ApiResponseBuilder.buildFailedResponse(badRequestException);
	}

}

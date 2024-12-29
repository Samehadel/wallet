package com.finance.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.model.ApiResponse;
import com.finance.common.util.ApiResponseBuilder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final ObjectMapper objectMapper;

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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(final MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		return ApiResponseBuilder.buildValidationErrorResponse(errors);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(final UnauthorizedException e) {
		log.error("Error code: {}, Error message: {}", e.getErrorDetails().errorCode(), e.getErrorDetails().errorMessage());

		return ApiResponseBuilder.buildFailedResponse(e);
	}
}

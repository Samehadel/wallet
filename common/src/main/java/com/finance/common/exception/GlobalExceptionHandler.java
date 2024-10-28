package com.finance.common.exception;

import com.finance.common.model.ApiResponse;
import com.finance.common.model.ErrorCodes;
import com.finance.common.util.ApiResponseBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleException(final Exception e) {
		log.error("Error occurred due to: {}", e.getMessage(), e);
		final var failedResponse = ApiResponseBuilder.buildFailedResponse(ErrorCodes.SERVER_ERROR, e.getMessage());

		return new ResponseEntity<>(failedResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ApiResponse<Void>> handleApplicationException(final ApplicationException applicationException) {
		log.error("Error occurred due to: {}", applicationException.getErrorMessage(), applicationException);
		final var failedResponse = ApiResponseBuilder.buildFailedResponse(applicationException);

		return new ResponseEntity<>(failedResponse, applicationException.getHttpStatus());
	}

}

package com.finance.common.util;

import com.finance.common.exception.ApplicationException;
import com.finance.common.exception.ErrorDetails;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.model.ApiResponse;
import com.finance.common.model.StatusEnum;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiResponseBuilder {

	public static ApiResponse<Void> buildSuccessResponse() {
		return buildApiResponse(StatusEnum.SUCCESS, null, null);
	}

	public static <R> ResponseEntity<ApiResponse<R>> buildSuccessResponse(R data) {
		ApiResponse<R> apiResponse = buildApiResponse(StatusEnum.SUCCESS, null, data);

		return ResponseEntity
			.ok()
			.body(apiResponse);
	}

	public static <R> ResponseEntity<ApiResponse<R>> buildFailedResponse(final ApplicationException applicationException) {
		ApiResponse<R> apiResponse = buildApiResponse(StatusEnum.FAILED, applicationException.getErrorDetails(), null);

		return ResponseEntity
			.status(applicationException.getStatus())
			.body(apiResponse);
	}

	public static <R> ResponseEntity<ApiResponse<R>> buildValidationErrorResponse(final R data) {
		ErrorDetails errorDetails = ErrorDetails.builder()
			.errorCode(SharedApplicationError.VALIDATION_ERROR.getErrorCode())
			.build();
		ApiResponse<R> apiResponse = buildApiResponse(StatusEnum.FAILED, errorDetails, data);

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(apiResponse);
	}

	public static <R> ApiResponse<R> buildApiResponse(final StatusEnum status, final ErrorDetails errorDetails, final R data) {

		return ApiResponse.<R>builder()
			.date(Instant.now())
			.status(status)
			.errorCode(errorDetails.errorCode())
			.errorMessage(errorDetails.errorMessage())
			.responseBody(data)
			.build();
	}
}

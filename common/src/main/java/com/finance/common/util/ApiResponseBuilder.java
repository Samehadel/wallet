package com.finance.common.util;

import com.finance.common.exception.ApplicationException;
import com.finance.common.model.ApiResponse;
import com.finance.common.model.StatusEnum;

public class ApiResponseBuilder {

	public static ApiResponse<Void> buildSuccessResponse() {
		return buildApiResponse(StatusEnum.SUCCESS, null, null, null);
	}

	public static <D> ApiResponse<D> buildSuccessResponse(D data) {
		return buildApiResponse(StatusEnum.SUCCESS, null, null, data);
	}

	public static ApiResponse<Void> buildFailedResponse(String errorCode, String errorMessage) {
		return buildApiResponse(StatusEnum.FAILED, errorMessage, errorCode, null);
	}

	public static ApiResponse<Void> buildFailedResponse(ApplicationException applicationException) {
		return buildApiResponse(StatusEnum.FAILED, applicationException.getErrorMessage(), applicationException.getApplicationErrorCode(), null);
	}

	public static <D> ApiResponse<D> buildApiResponse(StatusEnum status, String errorMessage, String errorCode, D data) {
		return ApiResponse.<D>builder()
				.status(status)
				.errorCode(errorCode)
				.errorMessage(errorMessage)
				.responseBody(data)
				.build();
	}

}

package com.finance.common.model;

public class ErrorCodes {
	public static final String INVALID_REQUEST = "USER-ERROR-001";
	public static final String INVALID_REQUEST_DATA = "USER-ERROR-002";

	public static final String INVALID_JWT_USER = "AUTH-ERROR-001";
	public static final String UNAUTHORIZED_USER = "AUTH-ERROR-002";

	public static final String SERVER_ERROR = "SERVER-ERROR-001";

	public static final String MISSING_REQUIRED_FIELDS_ERROR = "VAL-ERROR-001";
	public static final String DATA_UNIQUENESS_ERROR = "VAL-ERROR-002";
	public static final String INVALID_DATA_ERROR = "VAL-ERROR-003";

	public static final String DATA_NOT_FOUND_ERROR = "DATA-ERROR-001";
	public static final String ILLEGAL_OPERATION_ERROR = "DATA-ERROR-002";
}

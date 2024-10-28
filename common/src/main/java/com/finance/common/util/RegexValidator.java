package com.finance.common.util;

import com.finance.common.constants.RegexConstants;

import java.util.regex.Pattern;

public class RegexValidator {
	private static final Pattern NATIONAL_ID_PATTERN = Pattern.compile(RegexConstants.NATIONAL_ID_REGEX);
	private static final Pattern PASSPORT_PATTERN = Pattern.compile(RegexConstants.PASSPORT_REGEX);
	private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(RegexConstants.PHONE_NUMBER_REGEX);
	private static final Pattern EMAIL_PATTERN = Pattern.compile(RegexConstants.EMAIL_REGEX);

	public static boolean isValidNationalId(String nationalId) {
		return NATIONAL_ID_PATTERN.matcher(nationalId).matches();
	}

	public static boolean isValidPassportNumber(String passport) {
		return PASSPORT_PATTERN.matcher(passport).matches();
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
	}

	public static boolean isValidEmail(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}
}

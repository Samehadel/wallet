package com.bank.customer.controller;

import com.finance.common.dto.CustomerDTO;
import com.bank.customer.service.CustomerService;
import com.finance.common.controller.BaseController;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.model.ApiResponse;
import com.finance.common.service.BaseService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CustomerController extends BaseController<CustomerDTO> {

	private final CustomerService customerService;
	private final ExceptionService exceptionService;

	@Override
	protected BaseService getService() {
		return customerService;
	}

	@GetMapping("/test/e1")
	public ApiResponse<Void> testE1() {
		exceptionService.throwBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Test Field");

		return null;
	}

	@GetMapping("/test/e2")
	public ApiResponse<Void> testE2() {

		exceptionService.throwLogicalException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Test Field");

		return null;
	}

}

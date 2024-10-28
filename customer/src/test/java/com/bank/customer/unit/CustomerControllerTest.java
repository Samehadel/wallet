package com.bank.customer.unit;


import com.bank.customer.controller.CustomerController;
import com.bank.customer.service.CustomerService;

import com.finance.common.dto.CustomerDTO;
import com.finance.common.model.ApiResponse;
import com.finance.common.model.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.bank.customer.util.MockingUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CustomerControllerTest {
	@InjectMocks
	private CustomerController customerController;

	@Mock
	private CustomerService customerService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateCustomer() {
		when(customerService.create(getAnyCustomerDTO())).thenReturn(getSuccessBankResponseCustomerDTO());
		ApiResponse<CustomerDTO> response = customerController.create(createValidCustomer());
		assertEquals(StatusEnum.SUCCESS, response.getStatus());
		assertNotNull(response.getResponseBody());
		assertNull(response.getErrorCode());
		assertNull(response.getErrorMessage());
	}

	@Test
	public void testCreateAccount_FailedResponse() {
		when(customerService.create(getAnyCustomerDTO())).thenReturn(getFailedBankResponse());
		ApiResponse<CustomerDTO> response = customerController.create(createValidCustomer());
		assertEquals(StatusEnum.FAILED, response.getStatus());
		assertNull(response.getResponseBody());
		assertNotNull(response.getErrorCode());
		assertNotNull(response.getErrorMessage());
	}


	@Test
	public void testGetAccounts() {
		when(customerService.get(getAnyString())).thenReturn(getSuccessBankResponseCustomerDTO());
		ApiResponse<CustomerDTO> response = customerController.get("123");
		assertEquals(StatusEnum.SUCCESS, response.getStatus());
		assertNotNull(response.getResponseBody());
		assertNull(response.getErrorCode());
		assertNull(response.getErrorMessage());
	}

	@Test
	public void testGetCustomer_FailedResponse() {
		when(customerService.get(getAnyString())).thenReturn(getFailedBankResponse());
		ApiResponse<CustomerDTO> response = customerController.get("123");
		assertEquals(StatusEnum.FAILED, response.getStatus());
		assertNull(response.getResponseBody());
		assertNotNull(response.getErrorCode());
		assertNotNull(response.getErrorMessage());
	}

	@Test
	public void testBlockCustomer() {
		when(customerService.block(getAnyString())).thenReturn(getSuccessBankResponseCustomerDTO());
		ApiResponse<Void> response = customerController.block("123");
		assertEquals(StatusEnum.SUCCESS, response.getStatus());
		assertNotNull(response.getResponseBody());
		assertNull(response.getErrorCode());
		assertNull(response.getErrorMessage());
	}

	@Test
	public void testBlockAccount_FailedResponse() {
		when(customerService.block(getAnyString())).thenReturn(getFailedBankResponse());
		ApiResponse<Void> response = customerController.block("123");
		assertEquals(StatusEnum.FAILED, response.getStatus());
		assertNull(response.getResponseBody());
		assertNotNull(response.getErrorCode());
		assertNotNull(response.getErrorMessage());
	}

}

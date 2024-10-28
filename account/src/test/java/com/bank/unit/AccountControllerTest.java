package com.bank.unit;

import com.bank.account.controller.AccountController;
import com.bank.account.service.AccountService;
import com.finance.common.dto.AccountDTO;
import com.finance.common.model.ApiResponse;
import com.finance.common.model.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.bank.util.MockingUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AccountControllerTest {
	@InjectMocks
	private AccountController accountController;

	@Mock
	private AccountService accountService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateAccount() {
		when(accountService.create(getAnyAccountDTO())).thenReturn(getSuccessBankResponseAccountDTO());
		ApiResponse<AccountDTO> response = accountController.create(createValidAccountDTO());
		assertEquals(StatusEnum.SUCCESS, response.getStatus());
		assertNotNull(response.getResponseBody());
		assertNull(response.getErrorCode());
		assertNull(response.getErrorMessage());
	}

	@Test
	public void testCreateAccount_FailedResponse() {
		when(accountService.create(getAnyAccountDTO())).thenReturn(getFailedBankResponseAccountDTO());
		ApiResponse<AccountDTO> response = accountController.create(createValidAccountDTO());
		assertEquals(StatusEnum.FAILED, response.getStatus());
		assertNull(response.getResponseBody());
		assertNotNull(response.getErrorCode());
		assertNotNull(response.getErrorMessage());
	}


	@Test
	public void testGetAccounts() {
		when(accountService.get(getAnyString())).thenReturn(getSuccessBankResponseAccountDTO());
		ApiResponse<AccountDTO> response = accountController.get("123");
		assertEquals(StatusEnum.SUCCESS, response.getStatus());
		assertNotNull(response.getResponseBody());
		assertNull(response.getErrorCode());
		assertNull(response.getErrorMessage());
	}

	@Test
	public void testGetAccounts_FailedResponse() {
		when(accountService.get(getAnyString())).thenReturn(getFailedBankResponseAccountDTO());
		ApiResponse<AccountDTO> response = accountController.get("123");
		assertEquals(StatusEnum.FAILED, response.getStatus());
		assertNull(response.getResponseBody());
		assertNotNull(response.getErrorCode());
		assertNotNull(response.getErrorMessage());
	}

	@Test
	public void testBlockAccount() {
		when(accountService.block(getAnyString())).thenReturn(getSuccessBankResponseAccountDTO());
		ApiResponse<Void> response = accountController.block("123");
		assertEquals(StatusEnum.SUCCESS, response.getStatus());
		assertNotNull(response.getResponseBody());
		assertNull(response.getErrorCode());
		assertNull(response.getErrorMessage());
	}

	@Test
	public void testBlockAccount_FailedResponse() {
		when(accountService.block(getAnyString())).thenReturn(getFailedBankResponseAccountDTO());
		ApiResponse<Void> response = accountController.block("123");
		assertEquals(StatusEnum.FAILED, response.getStatus());
		assertNull(response.getResponseBody());
		assertNotNull(response.getErrorCode());
		assertNotNull(response.getErrorMessage());
	}

}

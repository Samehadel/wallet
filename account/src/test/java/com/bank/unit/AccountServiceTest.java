package com.bank.unit;

import com.bank.account.AccountRepository;
import com.bank.account.clients.CustomerServiceClient;
import com.bank.account.configuration.AppConfig;
import com.bank.account.entity.AccountEntity;
import com.bank.account.service.AccountServiceImpl;
import com.finance.common.dto.AccountDTO;
import com.finance.common.dto.CustomerDTO;
import com.bank.shared.exceptions.IllegalOperationException;
import com.bank.shared.exceptions.MissingRequiredFieldsException;
import com.finance.common.model.ApiResponse;
import com.finance.common.model.StatusEnum;
import com.bank.util.MockingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.bank.util.MockingUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
	@InjectMocks
	private AccountServiceImpl accountService;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private CustomerServiceClient customerServiceClient;

	@Mock
	private AppConfig appConfig;


	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateAccount_ValidAccount() {
		AccountDTO validAccount = createValidAccountDTO();

		mockCustomerClientResponse(MockingUtil.getSuccessBankResponseCustomerDTO());
		mockCustomerNumberOfAccounts(0L);
		mockCustomerNumberOfAccountsWithSameType(0L);
		mockMaximumNumberOfAccounts(10L);
		mockAccountRepositorySave(new AccountEntity());

		accountService.create(validAccount);
		verify(accountRepository, times(1)).save(any(AccountEntity.class));
	}

	@Test
	public void testCreateAccount_InValidAccount_ExceededMaxNumberOfAccounts() {
		AccountDTO validAccount = createValidAccountDTO();

		mockCustomerClientResponse(MockingUtil.getSuccessBankResponseCustomerDTO());
		mockCustomerNumberOfAccounts(3L);
		mockCustomerNumberOfAccountsWithSameType(0L);
		mockMaximumNumberOfAccounts(3L);

		try {
			accountService.create(validAccount);
		} catch (IllegalOperationException e) {
			verify(accountRepository, times(0)).save(any(AccountEntity.class));
		}
	}

	@Test
	public void testCreateAccount_InValidAccount_CustomerHasSameAccountType() {
		AccountDTO validAccount = createValidAccountDTO();

		mockCustomerClientResponse(MockingUtil.getSuccessBankResponseCustomerDTO());
		mockCustomerNumberOfAccounts(3L);
		mockCustomerNumberOfAccountsWithSameType(1L);
		mockMaximumNumberOfAccounts(10L);

		try {
			accountService.create(validAccount);
			assertEquals(1, 2);
		} catch (IllegalOperationException e) {
			verify(accountRepository, times(0)).save(any(AccountEntity.class));
		}
	}

	@Test
	public void testCreateAccount_InValidAccountInvalidOfficialID() {
		AccountDTO validAccount = createValidAccountDTO();

		mockCustomerClientResponse(MockingUtil.getBankResponseCustomerDTO_InvalidOfficialID());
		mockCustomerNumberOfAccounts(0L);
		mockCustomerNumberOfAccountsWithSameType(0L);
		mockMaximumNumberOfAccounts(10L);

		try {
			accountService.create(validAccount);
			assertEquals(1, 2);
		} catch (IllegalOperationException e) {
			verify(accountRepository, times(0)).save(getAnyAccountEntity());
		}
	}

	@Test
	public void testCreateAccount_InvalidAccountDTO() {
		AccountDTO validAccount = createInValidAccount();

		try {
			accountService.create(validAccount);
			assertEquals(1, 2);
		} catch (MissingRequiredFieldsException e) {
			verify(accountRepository, times(0)).save(getAnyAccountEntity());
		}
	}

	@Test
	public void testBlockAccount_ValidAccountNumber() {
		mockFindAccountByAccountNumber(new AccountEntity());
		accountService.block("01212112");
		verify(accountRepository, times((1))).save(getAnyAccountEntity());
	}

	@Test
	public void testBlockAccount_InValidAccountNumber() {
		try {
			accountService.block("");
			assertEquals(1, 2);
		} catch (MissingRequiredFieldsException e) {
			verify(accountRepository, times((0))).save(getAnyAccountEntity());
		}
	}

	@Test
	public void testGetAccounts() {
		mockFindAccountByAccountNumber(List.of(new AccountEntity()));
		ApiResponse<AccountDTO> response = accountService.get("CUSTOMER-1");
		assertEquals(StatusEnum.SUCCESS, response.getStatus());
	}

	@Test
	public void testGetAccounts_InvalidCustomerCode() {
		try {
			accountService.get("");
		} catch (MissingRequiredFieldsException e) {
			assertEquals(1, 1);
		}
	}

	private void mockAccountRepositorySave(AccountEntity savedEntity) {
		when(accountRepository.save(getAnyAccountEntity())).thenReturn(savedEntity);
	}

	private void mockMaximumNumberOfAccounts(long maxNumberAccounts) {
		when(appConfig.getMaxNumberAccounts()).thenReturn(maxNumberAccounts);
	}

	private void mockCustomerNumberOfAccounts(long mockingNumber) {
		when(accountRepository.countByCustomerCode(getAnyString(), any(List.class))).thenReturn(mockingNumber);
	}

	private void mockCustomerNumberOfAccountsWithSameType(long mockingNumber) {
		when(accountRepository.countByCustomerCodeAndAccountType(getAnyString(), getAnyAccountType(), getAnyAccountStatus())).thenReturn(mockingNumber);
	}

	private void mockCustomerClientResponse(ApiResponse<CustomerDTO> customerResponse) {
		when(customerServiceClient.get(getAnyString())).thenReturn(customerResponse);
	}

	private void mockFindAccountByAccountNumber(AccountEntity mockingEntity) {
		when(accountRepository.findByAccountNumber(getAnyString())).thenReturn(mockingEntity);
	}

	private void mockFindAccountByAccountNumber(List<AccountEntity> mockingList) {
		when(accountRepository.findByCustomerCode(getAnyString())).thenReturn(mockingList);
	}
}

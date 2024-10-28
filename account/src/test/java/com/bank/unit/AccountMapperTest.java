package com.bank.unit;

import com.bank.account.entity.AccountEntity;
import com.bank.account.mapper.AccountMapper;
import com.finance.common.dto.AccountDTO;
import com.finance.common.enums.AccountStatusEnum;
import com.finance.common.enums.AccountTypeEnum;
import com.finance.common.mapper.MapperTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountMapperTest implements MapperTest {
	private final AccountMapper accountMapper = new AccountMapper();

	@Test
	@Override
	public void testMapToEntity() {
		AccountDTO accountDTO = getAccountDTO();
		AccountEntity mappedEntity = accountMapper.mapToEntity(accountDTO);
		assertDTOMatchEntity(accountDTO, mappedEntity);
	}

	private AccountDTO getAccountDTO() {
		return AccountDTO.builder()
				.accountType(AccountTypeEnum.CERTIFICATE_OF_DEPOSIT)
				.customerCode("123456789")
				.accountHolderName("John Doe")
				.accountStatus(AccountStatusEnum.ACTIVE)
				.monthlyLimit(BigDecimal.TEN)
				.dailyLimit(BigDecimal.TWO)
				.balance(BigDecimal.ONE)
				.build();
	}

	private void assertDTOMatchEntity(AccountDTO accountDTO, AccountEntity mappedEntity) {
		assertEquals(accountDTO.getAccountType(), mappedEntity.getAccountType());
		assertEquals(accountDTO.getAccountHolderName(), mappedEntity.getAccountHolderName());

		assertEquals(accountDTO.getCustomerCode(), mappedEntity.getCustomerCode());
		assertNull(mappedEntity.getId());
		assertNull(mappedEntity.getAccountNumber());
		assertNull(mappedEntity.getAccountStatus());
		assertNull(mappedEntity.getBalance());
		assertNull(mappedEntity.getDailyLimit());
		assertNull(mappedEntity.getMonthlyLimit());
		assertNull(mappedEntity.getActive());
		assertNull(mappedEntity.getBlocked());
	}

	@Test
	@Override
	public void testMapToDTO() {
		AccountEntity accountEntity = getAccountEntity();
		AccountDTO mappedDTO = accountMapper.mapToDTO(accountEntity);
		assertEntityMatchDTO(accountEntity, mappedDTO);
	}


	private AccountEntity getAccountEntity() {
		AccountEntity accountEntity = AccountEntity.builder()
				.id(15L)
				.customerCode("CUST-022")
				.accountHolderName("John Smith")
				.accountNumber("01444002251003")
				.accountType(AccountTypeEnum.MONEY_MARKET)
				.accountStatus(AccountStatusEnum.ACTIVE)
				.dailyLimit(BigDecimal.TEN)
				.monthlyLimit(BigDecimal.ONE)
				.balance(BigDecimal.TEN)
				.build();

		return accountEntity;
	}

	private void assertEntityMatchDTO(AccountEntity accountEntity, AccountDTO mappedDTO) {
		assertEquals(accountEntity.getCustomerCode(), mappedDTO.getCustomerCode());
		assertEquals(accountEntity.getAccountNumber(), mappedDTO.getAccountNumber());
		assertEquals(accountEntity.getAccountType(), mappedDTO.getAccountType());
		assertEquals(accountEntity.getAccountStatus(), mappedDTO.getAccountStatus());
		assertEquals(accountEntity.getAccountHolderName(), mappedDTO.getAccountHolderName());
		assertEquals(accountEntity.getMonthlyLimit(), mappedDTO.getMonthlyLimit());
		assertEquals(accountEntity.getDailyLimit(), mappedDTO.getDailyLimit());
		assertEquals(accountEntity.getBalance(), mappedDTO.getBalance());
	}
}

package com.bank.util;

import com.bank.account.entity.AccountEntity;
import com.finance.common.dto.AccountDTO;
import com.finance.common.dto.CustomerDTO;
import com.finance.common.dto.OfficialIdDTO;
import com.finance.common.enums.AccountStatusEnum;
import com.finance.common.enums.AccountTypeEnum;
import com.finance.common.enums.OfficialIdTypeEnum;
import com.finance.common.model.ApiResponse;
import com.finance.common.model.StatusEnum;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;

public class MockingUtil {

	public static ApiResponse getFailedBankResponseAccountDTO() {
		return ApiResponse.builder()
				.status(StatusEnum.FAILED)
				.errorCode("ERR-001")
				.errorMessage("Invalid data")
				.build();
	}
	public static AccountDTO createValidAccountDTO() {
		return createAccountDTO("John Doe", AccountTypeEnum.CHECKING, "123456789");
	}

	public static AccountDTO createInValidAccount() {
		return createAccountDTO("John Doe", AccountTypeEnum.CHECKING, null);
	}

	private static AccountDTO createAccountDTO(String accountHolderName, AccountTypeEnum type, String customerCode) {
		return AccountDTO.builder()
				.accountHolderName(accountHolderName)
				.accountType(type)
				.customerCode(customerCode)
				.build();
	}

	public static ApiResponse getSuccessBankResponseCustomerDTO() {
		return ApiResponse.builder()
				.status(StatusEnum.SUCCESS)
				.responseBody(createValidCustomer())

				.build();
	}
	public static CustomerDTO createValidCustomer() {
		return CustomerDTO.builder()
				.active(true)
				.officialIDs(getValidOfficialIDs())
				.build();
	}

	public static Set<OfficialIdDTO> getValidOfficialIDs() {
		return Set.of(OfficialIdDTO.builder()
				.value("123456789")
				.type(OfficialIdTypeEnum.NATIONAL_ID)
				.expiryDate(getDateAfterToday(365))
				.build());
	}

	public static ApiResponse getBankResponseCustomerDTO_InvalidOfficialID() {
		return ApiResponse.builder()
				.status(StatusEnum.SUCCESS)
				.responseBody(createInValidCustomer())
				.build();
	}

	public static CustomerDTO createInValidCustomer() {
		return CustomerDTO.builder()
				.active(true)
				.officialIDs(getInValidOfficialIDs())
				.build();
	}

	public static Set<OfficialIdDTO> getInValidOfficialIDs() {
		return Set.of(OfficialIdDTO.builder()
				.type(OfficialIdTypeEnum.NATIONAL_ID)
				.expiryDate(getDateAfterToday(365))
				.build());
	}

	public static ApiResponse getSuccessBankResponseAccountDTO() {
		return ApiResponse.builder()
				.status(StatusEnum.SUCCESS)
				.responseBody(createValidAccountDTO())
				.build();
	}

	public static Date getDateAfterToday(int daysToAdd) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, daysToAdd); // Add days to the current date

		return calendar.getTime();
	}

	public static String getAnyString() {
		return any(String.class);
	}

	public static AccountEntity getAnyAccountEntity() {
		return any(AccountEntity.class);
	}

	public static AccountStatusEnum getAnyAccountStatus() {
		return any(AccountStatusEnum.class);
	}

	public static AccountTypeEnum getAnyAccountType() {
		return any(AccountTypeEnum.class);
	}

	public static AccountDTO getAnyAccountDTO() {
		return any(AccountDTO.class);
	}

}

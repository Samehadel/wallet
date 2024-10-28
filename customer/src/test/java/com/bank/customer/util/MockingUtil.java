package com.bank.customer.util;

import com.bank.customer.entity.CustomerEntity;
import com.finance.common.dto.AccountDTO;
import com.finance.common.dto.AddressDTO;
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

	public static ApiResponse getFailedBankResponse() {
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
		return createCustomerDTO(null,
				"John",
				"Doe",
				"John.Doe@example.com",
				"01245987452",
				createValidAddress(),
				createValidOfficialIDs(),
				true,
				false);
	}

	private static AddressDTO createValidAddress() {
		return AddressDTO.builder()
				.street("123 Main St")
				.region("Apt 1")
				.city("New York")
				.zipCode("10001")
				.buildingNumber("123")
				.build();
	}

	public static Set<OfficialIdDTO> createValidOfficialIDs() {
		return Set.of(createValidOfficialID());
	}

	private static OfficialIdDTO createValidOfficialID() {
		return OfficialIdDTO.builder()
				.value(getOfficialIdValue())
				.type(OfficialIdTypeEnum.NATIONAL_ID)
				.expiryDate(getDateAfterToday(365))
				.build();
	}

	public static CustomerDTO createInValidCustomer_MissingFirstName() {
		return createCustomerDTO(null, null, "Doe", "John.Doe@example.com", "01245987452", createValidAddress(), createValidOfficialIDs(), true, false);
	}

	public static CustomerDTO createInValidCustomer_MissingLastName() {
		return createCustomerDTO(null, "John", null, "John.Doe@example.com", "01245987452", createValidAddress(), createValidOfficialIDs(), true, false);
	}

	public static CustomerDTO createInValidCustomer_MissingEmailAndPhoneNumber() {
		return createCustomerDTO(null, "John", "Doe", null, null, createValidAddress(), createValidOfficialIDs(), true, false);
	}

	public static CustomerDTO createInValidCustomer_MissingOfficialId() {
		return createCustomerDTO(null, "John", "Doe", "John.Doe@example.com", "01245987452", createValidAddress(), null, true, false);
	}

	public static CustomerDTO createInValidCustomer_MissingOfficialIdValue() {
		return createCustomerDTO(null, "John", "Doe", "John.Doe@example.com", "01245987452", createValidAddress(), Set.of(createInValidOfficialID_MissingValue()), true, false);
	}

	public static CustomerDTO createInValidCustomer_MissingOfficialIdType() {
		return createCustomerDTO(null, "John", "Doe", "John.Doe@example.com", "01245987452", createValidAddress(), Set.of(createInValidOfficialID_MissingType()), true, false);
	}

	public static CustomerDTO createInValidCustomer_MissingOfficialIdExpiryDate() {
		return createCustomerDTO(null, "John", "Doe", "John.Doe@example.com", "01245987452", createValidAddress(), Set.of(createInValidOfficialID_MissingExpiryDate()), true, false);
	}

	public static CustomerDTO createInValidCustomer_OfficialIDExpired() {
		return createCustomerDTO(null, "John", "Doe", "John.Doe@example.com", "01245987452", createValidAddress(), Set.of(createInValidOfficialID_Expired()), true, false);
	}

	private static CustomerDTO createCustomerDTO(String customerCode, String firstName, String lastName, String email, String phoneNumber, AddressDTO addressDTO, Set<OfficialIdDTO> officialIDs, Boolean active, Boolean blocked) {
		return CustomerDTO.builder()
				.customerCode(customerCode)
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.phoneNumber(phoneNumber)
				.addressDTO(addressDTO)
				.officialIDs(officialIDs)
				.active(active)
				.blocked(blocked)
				.build();
	}

	public static Set<OfficialIdDTO> createInValidOfficialIDs() {
		return Set.of(createInValidOfficialID_MissingType());
	}

	public static Set<OfficialIdDTO> createOfficialIDs(OfficialIdDTO officialIdDTO) {
		return Set.of(officialIdDTO);
	}

	private static OfficialIdDTO createInValidOfficialID_MissingValue() {
		return OfficialIdDTO.builder()
				.type(OfficialIdTypeEnum.NATIONAL_ID)
				.expiryDate(getDateAfterToday(365))
				.build();
	}

	private static OfficialIdDTO createInValidOfficialID_MissingType() {
		return OfficialIdDTO.builder()
				.value(getOfficialIdValue())
				.expiryDate(getDateAfterToday(365))
				.build();
	}

	private static OfficialIdDTO createInValidOfficialID_MissingExpiryDate() {
		return OfficialIdDTO.builder()
				.type(OfficialIdTypeEnum.NATIONAL_ID)
				.value(getOfficialIdValue())
				.build();
	}

	private static OfficialIdDTO createInValidOfficialID_Expired() {
		return OfficialIdDTO.builder()
				.type(OfficialIdTypeEnum.NATIONAL_ID)
				.value(getOfficialIdValue())
				.expiryDate(getDateAfterToday(-2))
				.build();
	}

	private static String getOfficialIdValue() {
		return "29701012244333";
	}

	public static Date getDateAfterToday(int daysToAdd) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, daysToAdd); // Add days to the current date

		return calendar.getTime();
	}

	public static String getAnyString() {
		return any(String.class);
	}

	public static AccountStatusEnum getAnyAccountStatus() {
		return any(AccountStatusEnum.class);
	}

	public static OfficialIdTypeEnum getAnyOfficialIdType() {
		return any(OfficialIdTypeEnum.class);
	}

	public static AccountDTO getAnyAccountDTO() {
		return any(AccountDTO.class);
	}

	public static CustomerDTO getAnyCustomerDTO() {
		return any(CustomerDTO.class);
	}

	public static CustomerEntity getAnyCustomerEntity() {
		return any(CustomerEntity.class);
	}
}

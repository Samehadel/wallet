package com.bank.account.service;

import com.bank.account.AccountRepository;
import com.bank.account.clients.CustomerServiceClient;
import com.bank.account.configuration.AppConfig;
import com.bank.account.entity.AccountEntity;
import com.bank.account.mapper.AccountMapper;
import com.finance.common.constants.EventsConstants;
import com.finance.common.dto.AccountDTO;
import com.finance.common.dto.CustomerDTO;
import com.finance.common.dto.NotificationDTO;
import com.finance.common.dto.OfficialIdDTO;
import com.finance.common.enums.AccountStatusEnum;
import com.finance.common.enums.NotificationTypeEnum;
import com.finance.common.model.ApiResponse;
import com.finance.common.util.ApiResponseBuilder;
import com.finance.common.util.CollectionUtil;
import com.finance.common.util.StringUtil;

import java.util.Date;
import java.util.Set;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountMapper accountMapper = new AccountMapper();

	private final AppConfig appConfig;
	private final AccountRepository accountRepository;
	private final CustomerServiceClient customerServiceClient;
	private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;

	@Override
	public ApiResponse<AccountDTO> create(AccountDTO dto) {
		try {
			log.info("Starting create account for customer {}", dto.getCustomerCode());
			validateAccountRequiredInfo(dto);
			CustomerDTO customerDTO = findCustomerByCode(dto.getCustomerCode());
			validateCustomer(customerDTO);
			validateCustomerAccounts(dto);
			ApiResponse<AccountDTO> response = createAccount(dto);
			pushNotification(dto, customerDTO);
			return response;
		} finally {
			log.info("Finished create account for customer {}", dto.getCustomerCode());
		}
	}

	private void pushNotification(AccountDTO dto, CustomerDTO customerDTO) {
		try {
			log.info("Starting push notification for customer {}", dto.getCustomerCode());
			if(!StringUtil.isNullOrEmpty(customerDTO.getEmail())) {
				kafkaTemplate.send(EventsConstants.NOTIFICATION_MAIL_TOPIC, createEmailNotification(dto.getAccountNumber(), customerDTO));
			} else if(!StringUtil.isNullOrEmpty(customerDTO.getPhoneNumber())) {
				kafkaTemplate.send(EventsConstants.NOTIFICATION_SMS_TOPIC, createSMSNotification(dto.getAccountNumber(), customerDTO));
			}
		} catch (Exception e) {
			log.error("Error while push notification for customer {}", dto.getCustomerCode(), e);
		} finally {
			log.info("Finished push notification for customer {}", dto.getCustomerCode());
		}
	}

	private NotificationDTO createEmailNotification(String accountNumber, CustomerDTO customerDTO) {
		return NotificationDTO.builder()
				.message("Account created successfully with number " + accountNumber)
				.receiverIdentifier(customerDTO.getEmail())
				.type(NotificationTypeEnum.ACCOUNT_REGISTRATION)
				.build();
	}

	private NotificationDTO createSMSNotification(String accountNumber, CustomerDTO customerDTO) {
		return NotificationDTO.builder()
				.message("Account created successfully with number " + accountNumber)
				.receiverIdentifier(customerDTO.getPhoneNumber())
				.type(NotificationTypeEnum.ACCOUNT_REGISTRATION)
				.build();
	}

	private void validateAccountRequiredInfo(AccountDTO dto) {
		log.info("Starting validate account required info {}", dto);
		if (StringUtil.isNullOrEmpty(dto.getCustomerCode())) {
			throw new MissingRequiredFieldsException("Customer code is required");
		}
		if (null == dto.getAccountType()) {
			throw new MissingRequiredFieldsException("Account type is required");
		}
	}

	private void validateCustomer(CustomerDTO customerDTO) {
		log.info("Starting validate customer {}", customerDTO.getCustomerCode());

		if(!Boolean.TRUE.equals(customerDTO.getActive()) ||
				Boolean.TRUE.equals(customerDTO.getBlocked())) {
			throw new IllegalOperationException("Customer is not active");
		}
		validateCustomerOfficialIDs(customerDTO.getOfficialIDs());
	}

	private CustomerDTO findCustomerByCode(String customerCode) {
		ApiResponse<CustomerDTO> response = customerServiceClient.get(customerCode);
		if (null == response || null == response.getResponseBody()) {
			throw new MissingRequiredFieldsException("Customer not found");
		}
		CustomerDTO customerDTO = response.getResponseBody();
		return customerDTO;
	}

	private void validateCustomerAccounts(AccountDTO accountDTO) {
		log.info("Starting validate customer accounts {}", accountDTO.getCustomerCode());
		String customerCode = accountDTO.getCustomerCode();
		long customerNumberOfAccount = accountRepository.countByCustomerCode(customerCode, AccountStatusEnum.getPendingList());
		if (customerNumberOfAccount >= appConfig.getMaxNumberAccounts()) {
			throw new IllegalOperationException("Customer reached the maximum number of accounts");
		}
		long customerExistingAccountWithSameType = accountRepository.countByCustomerCodeAndAccountType(customerCode, accountDTO.getAccountType(), AccountStatusEnum.PENDING_APPROVAL);
		if (customerExistingAccountWithSameType > 0) {
			throw new IllegalOperationException("Customer already has an account with the same type");
		}
	}

	private void validateCustomerOfficialIDs(Set<OfficialIdDTO> officialIDs) {
		log.info("Starting Validating customer official IDs");
		if (CollectionUtil.isNullOrEmpty(officialIDs)) {
			throw new IllegalOperationException("At least one official ID is required");
		}
		for (OfficialIdDTO officialIdDTO : officialIDs) {
			if (StringUtil.isNullOrEmpty(officialIdDTO.getValue())) {
				throw new IllegalOperationException("Official ID number is required");
			}
			if (null == officialIdDTO.getType()) {
				throw new IllegalOperationException("Official ID type is required");
			}
			if (null == officialIdDTO.getExpiryDate()) {
				throw new IllegalOperationException("Official ID expiry date is required");
			}
			Date today = new Date();
			if (officialIdDTO.getExpiryDate().before(today)) {
				throw new InvalidDataException("Official ID is expired");
			}
		}
	}

	private ApiResponse<AccountDTO> createAccount(AccountDTO dto) {
		log.info("Starting create account {}", dto);
		AccountEntity entity = accountMapper.mapToEntity(dto);
		entity.setAccountStatus(AccountStatusEnum.PENDING_APPROVAL);
		entity = accountRepository.save(entity);
		dto = accountMapper.mapToDTO(entity);
		return ApiResponseBuilder.buildSuccessResponse(dto);
	}

	@Override
	public ApiResponse<AccountDTO> get(final String code) {
		try {
			log.info("Starting get accounts {}", code);
			if(StringUtil.isNullOrEmpty(code)) {
				throw new MissingRequiredFieldsException("Customer code is required");
			}
			AccountEntity accountEntity = accountRepository.findByCode(code)
				.orElseThrow(() -> new DataNotFoundException("Account not found"));
			AccountDTO accountDTO = accountMapper.mapToDTO(accountEntity);
			return ApiResponseBuilder.buildSuccessResponse(accountDTO);
		} finally {
			log.info("Finished get account {}", code);
		}
	}

	@Override
	public ApiResponse<Void> block(final String accountNumber) {
		try {
			log.info("Starting block account for accountNumber {}", accountNumber);
			if(StringUtil.isNullOrEmpty(accountNumber)) {
				throw new MissingRequiredFieldsException("Customer accountNumber is required");
			}

			AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);

			accountEntity.setBlocked(Boolean.TRUE);
			accountEntity.setActive(Boolean.FALSE);

			accountRepository.save(accountEntity);
			return ApiResponseBuilder.buildSuccessResponse();
		} finally {
			log.info("Finished block account for accountNumber {}", accountNumber);
		}
	}
}

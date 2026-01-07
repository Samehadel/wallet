package com.finance.account.service;

import com.finance.account.AccountRepository;
import com.finance.account.configuration.AppConfig;
import com.finance.account.entity.AccountEntity;
import com.finance.account.mapper.AccountMapper;
import com.finance.common.client.CustomerServiceClient;
import com.finance.common.constants.EventsConstants;
import com.finance.common.dto.AccountDTO;
import com.finance.common.dto.CustomerDTO;
import com.finance.common.dto.NotificationDTO;
import com.finance.common.dto.OfficialIdDTO;
import com.finance.common.enums.AccountStatusEnum;
import com.finance.common.enums.NotificationTypeEnum;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.model.ApiResponse;
import com.finance.common.util.CollectionUtil;
import com.finance.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountMapper accountMapper = new AccountMapper();

	private final AppConfig appConfig;
	private final AccountRepository accountRepository;
	private final CustomerServiceClient customerServiceClient;
	private final KafkaTemplate<String, NotificationDTO> kafkaTemplate;
	private final ExceptionService exceptionService;

	@Override
	public AccountDTO create(AccountDTO dto) {
		try {
			log.info("Starting create account for customer {}", dto.getCustomerCode());
			validateAccountRequiredInfo(dto);
			CustomerDTO customerDTO = findCustomerByCode(dto.getCustomerCode());
			validateCustomer(customerDTO);
			validateCustomerAccounts(dto);
			AccountDTO response = createAccount(dto);
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
			throw exceptionService.buildBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "customerCode");
		}
		if (null == dto.getAccountType()) {
			throw exceptionService.buildBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "accountType");
		}
	}

	private void validateCustomer(CustomerDTO customerDTO) {
		log.info("Starting validate customer {}", customerDTO.getCustomerCode());

		if(!Boolean.TRUE.equals(customerDTO.getActive()) ||
				Boolean.TRUE.equals(customerDTO.getBlocked())) {
			throw exceptionService.buildBadRequestException(SharedApplicationError.VALIDATION_ERROR, "customer not active");

		}
		validateCustomerOfficialIDs(customerDTO.getOfficialIDs());
	}

	private CustomerDTO findCustomerByCode(String customerCode) {
		ApiResponse<CustomerDTO> response = customerServiceClient.findCustomerByCode(customerCode);
		if (null == response || null == response.getResponseBody()) {
			throw exceptionService.buildBadRequestException(SharedApplicationError.USER_NOT_FOUND);
		}
        return response.getResponseBody();
	}

	private void validateCustomerAccounts(AccountDTO accountDTO) {
		log.info("Starting validate customer accounts {}", accountDTO.getCustomerCode());
		String customerCode = accountDTO.getCustomerCode();
		long customerNumberOfAccount = accountRepository.countByCustomerCode(customerCode, AccountStatusEnum.getPendingList());
		if (customerNumberOfAccount >= appConfig.getMaxNumberAccounts()) {
			throw exceptionService.buildBadRequestException(SharedApplicationError.VALIDATION_ERROR, "Customer reached the maximum number of accounts");
		}
		long customerExistingAccountWithSameType = accountRepository.countByCustomerCodeAndAccountType(customerCode, accountDTO.getAccountType(), AccountStatusEnum.PENDING_APPROVAL);
		if (customerExistingAccountWithSameType > 0) {
			throw exceptionService.buildBadRequestException(SharedApplicationError.VALIDATION_ERROR, "Customer already has an account with the same type");

		}
	}

	private void validateCustomerOfficialIDs(Set<OfficialIdDTO> officialIDs) {
		log.info("Starting Validating customer official IDs");
		if (CollectionUtil.isNullOrEmpty(officialIDs)) {
			throw exceptionService.buildBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Official ID(s)");
		}
		for (OfficialIdDTO officialIdDTO : officialIDs) {
			if (StringUtil.isNullOrEmpty(officialIdDTO.getValue())) {
				throw exceptionService.buildBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Official ID number");
			}
			if (null == officialIdDTO.getType()) {
				throw exceptionService.buildBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Official ID type is required");
			}
			if (null == officialIdDTO.getExpiryDate()) {
				throw exceptionService.buildBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Official ID expiry date");
			}
			Date today = new Date();
			if (officialIdDTO.getExpiryDate().before(today)) {
				throw exceptionService.buildBadRequestException(SharedApplicationError.VALIDATION_ERROR, "Official ID is expired");
			}
		}
	}

	private AccountDTO createAccount(AccountDTO dto) {
		log.info("Starting create account {}", dto);
		AccountEntity entity = accountMapper.mapToEntity(dto);
		entity.setAccountStatus(AccountStatusEnum.PENDING_APPROVAL);
		entity = accountRepository.save(entity);
		return accountMapper.mapToDTO(entity);
	}

	@Override
	public AccountDTO get(final String code) {
		try {
			log.info("Starting get accounts {}", code);
			if(StringUtil.isNullOrEmpty(code)) {
				throw exceptionService.buildBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "account code");

			}
			AccountEntity accountEntity = accountRepository.findByCode(code)
				.orElseThrow(() -> exceptionService.buildBadRequestException(SharedApplicationError.RESOURCE_NOT_FOUND, "Account", "code", code));
            return accountMapper.mapToDTO(accountEntity);
		} finally {
			log.info("Finished get account {}", code);
		}
	}

	@Override
	public void block(final String accountNumber) {
		try {
			log.info("Starting block account for accountNumber {}", accountNumber);
			if(StringUtil.isNullOrEmpty(accountNumber)) {
				throw exceptionService.buildBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "accountNumber");
			}

			AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber);

			accountEntity.setBlocked(Boolean.TRUE);
			accountEntity.setActive(Boolean.FALSE);

			accountRepository.save(accountEntity);
		} finally {
			log.info("Finished block account for accountNumber {}", accountNumber);
		}
	}
}

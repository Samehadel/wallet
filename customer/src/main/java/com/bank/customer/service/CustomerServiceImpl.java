package com.bank.customer.service;

import com.bank.customer.CustomerRepository;
import com.bank.customer.constatnts.CustomerErrors;
import com.bank.customer.entity.CustomerEntity;
import com.bank.customer.mapper.CustomerMapper;
import com.finance.common.dto.CustomerDTO;
import com.finance.common.dto.OfficialIdDTO;
import com.finance.common.enums.OfficialIdTypeEnum;
import com.finance.common.exception.ApplicationBadRequestException;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.model.ApiResponse;
import com.finance.common.util.ApiResponseBuilder;
import com.finance.common.util.CollectionUtil;
import com.finance.common.util.RegexValidator;
import com.finance.common.util.StringUtil;

import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;
	private final ExceptionService exceptionService;

	private final CustomerMapper customerMapper = new CustomerMapper();


	@Override
	public ApiResponse<CustomerDTO> create(CustomerDTO dto) {
		try {
			log.info("Starting Creating customer {}", dto);
			doValidateBeforeSave(dto);
			CustomerEntity customerEntity = customerMapper.mapToEntity(dto);
			customerEntity = customerRepository.save(customerEntity);
			return ApiResponseBuilder.getSuccessResponse(customerMapper.mapToDTO(customerEntity));
		} finally {
			log.info("Finished create customer {}", dto);
		}
	}

	private void doValidateBeforeSave(CustomerDTO dto) {
		log.info("Starting Validating customer before save {}", dto);
		validateCustomerRequiredInfo(dto);
		validateInfoFormat(dto);
		validateInfoUniqueness(dto);
	}

	private void validateCustomerRequiredInfo(final CustomerDTO dto) {
		log.info("Starting Validating customer required info");
		if (StringUtil.isNullOrEmpty(dto.getFirstName())) {
			exceptionService.throwBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "First Name");
		}
		if (StringUtil.isNullOrEmpty(dto.getLastName())) {
			exceptionService.throwBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Last Name");
		}
		if (StringUtil.isNullOrEmpty(dto.getPhoneNumber()) && StringUtil.isNullOrEmpty(dto.getEmail())) {
			exceptionService.throwBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Phone Number or Email");
		}

		validateCustomerOfficialIDs(dto.getOfficialIDs());
	}

	private void validateCustomerOfficialIDs(Set<OfficialIdDTO> officialIDs) {
		log.info("Starting Validating customer official IDs");
		if (CollectionUtil.isNullOrEmpty(officialIDs)) {
			throw new ApplicationBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Official IDs");
		}

		for (OfficialIdDTO officialIdDTO : officialIDs) {
			if (StringUtil.isNullOrEmpty(officialIdDTO.getValue())) {
				exceptionService.throwBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Official ID Value");
			}
			if (null == officialIdDTO.getType()) {
				exceptionService.throwBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Official ID Type");
			}
			if (null == officialIdDTO.getExpiryDate()) {
				exceptionService.throwBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Official ID Expiry Date");
			}
			Date today = new Date();
			if (officialIdDTO.getExpiryDate().before(today)) {
				exceptionService.throwBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Official ID Expiry Date");
			}
		}
	}

	private void validateInfoFormat(CustomerDTO dto) {
		log.info("Starting Validating customer info format {}", dto);
		if (!StringUtil.isNullOrEmpty(dto.getEmail()) && !RegexValidator.isValidEmail(dto.getEmail())) {
			exceptionService.throwBadRequestException(SharedApplicationError.INVALID_EMAIL);
		}
		if (!StringUtil.isNullOrEmpty(dto.getPhoneNumber()) && !RegexValidator.isValidPhoneNumber(dto.getPhoneNumber())) {
			exceptionService.throwBadRequestException(SharedApplicationError.INVALID_PHONE_NUMBER);
		}
		for (OfficialIdDTO officialIdDTO : dto.getOfficialIDs()) {
			if (officialIdDTO.getType() == OfficialIdTypeEnum.NATIONAL_ID && !RegexValidator.isValidNationalId(officialIdDTO.getValue())) {
				exceptionService.throwBadRequestException(SharedApplicationError.INVALID_NATIONAL_ID);
			} else if (officialIdDTO.getType() == OfficialIdTypeEnum.PASSPORT && !RegexValidator.isValidPassportNumber(officialIdDTO.getValue())) {
				exceptionService.throwBadRequestException(SharedApplicationError.INVALID_PASSPORT_NUMBER);
			}
		}
	}

	private void validateInfoUniqueness(CustomerDTO dto) {
		log.info("Starting Validating customer info uniqueness {}", dto);

		long countByEmail = customerRepository.countByEmail(dto.getEmail());
		if (countByEmail > 0) {
			exceptionService.throwBadRequestException(SharedApplicationError.EMAIL_ALREADY_EXISTS);
		}

		long countByPhoneNumber = customerRepository.countByPhoneNumber(dto.getPhoneNumber());
		if (countByPhoneNumber > 0) {
			exceptionService.throwBadRequestException(SharedApplicationError.PHONE_NUMBER_ALREADY_EXISTS);
		}

		for (OfficialIdDTO officialIdDTO : dto.getOfficialIDs()) {
			long countByOfficialId = customerRepository.countByOfficialId(officialIdDTO.getValue(), officialIdDTO.getType());
			if (countByOfficialId > 0) {
				exceptionService.throwBadRequestException(SharedApplicationError.OFFICIAL_ID_ALREADY_EXISTS);
			}
		}
	}


	@Override
	public ApiResponse<CustomerDTO> get(String code) {
		try {
			log.info("Starting get customer by code {}", code);
			if(StringUtil.isNullOrEmpty(code)) {
				exceptionService.throwBadRequestException(SharedApplicationError.MISSING_REQUIRED_FIELD, "Customer code");
			}

			CustomerEntity customerEntity = findCustomerByCustomerCode(code);
			CustomerDTO customerDTO = customerMapper.mapToDTO(customerEntity);
			return ApiResponseBuilder.getSuccessResponse(customerDTO);
		} finally {
			log.info("Finished get customer by code {}", code);
		}
	}

	@Override
	public ApiResponse<Void> block(String code) {
		try {
			log.info("Starting block customer by code {}", code);
			if(StringUtil.isNullOrEmpty(code)) {
				exceptionService.throwBadRequestException(CustomerErrors.REQUIRED_CUSTOMER_CODE);
			}

			CustomerEntity customerEntity = findCustomerByCustomerCode(code);

			customerEntity.setBlocked(Boolean.TRUE);
			customerEntity.setActive(Boolean.FALSE);

			customerRepository.save(customerEntity);
			return ApiResponseBuilder.getSuccessResponse();
		} finally {
			log.info("Finished block customer by code {}", code);
		}
	}

	private CustomerEntity findCustomerByCustomerCode(String code) {
		return customerRepository
				.findByCustomerCode(code)
				.orElseThrow(() -> exceptionService.buildException(CustomerErrors.CUSTOMER_NOT_FOUND, ApplicationBadRequestException.class, code));
	}
}

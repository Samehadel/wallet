package com.bank.customer.entity;

import com.bank.customer.CustomerRepository;
import com.finance.common.util.StringUtil;
import jakarta.persistence.PrePersist;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class CustomerEntityListener {
	private final String CUSTOMER_CODE_PREFIX = "CUSTOMER-";
	private static CustomerRepository customerRepository;

	@PrePersist
	public void onPrePersist(CustomerEntity customer) {
		log.info("CustomerEntityListener onPrePersist is called");
		if(StringUtil.isNullOrEmpty(customer.getCustomerCode())) {
			Long nextSequenceValue = customerRepository.getNextSequenceValue();
			customer.setCustomerCode(CUSTOMER_CODE_PREFIX + nextSequenceValue);
		}
		if(customer.getActive() == null) {
			customer.setActive(Boolean.TRUE);
		}
		if(customer.getBlocked() ==  null) {
			customer.setBlocked(Boolean.FALSE);
		}
	}

	@Autowired
	public void setCustomerRepository(CustomerRepository customerRepository) {
		CustomerEntityListener.customerRepository = customerRepository;
	}

}

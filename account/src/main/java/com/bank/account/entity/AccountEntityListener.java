package com.bank.account.entity;

import com.finance.common.util.StringUtil;
import jakarta.persistence.PrePersist;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.security.SecureRandom;


@Log4j2
public class AccountEntityListener {
	private final int ACCOUNT_NUMBER_LENGTH = 12;
	private final String ACCOUNT_NUMBER_CHARS = "0123456789";

	@PrePersist
	public void prePersist(AccountEntity accountEntity) {
		if (StringUtil.isNullOrEmpty(accountEntity.getAccountNumber())) {
			accountEntity.setAccountNumber(generateAccountNumber());
		}

		if(accountEntity.getDailyLimit() == null) {
			accountEntity.setDailyLimit(BigDecimal.valueOf(10000));
		}

		if(accountEntity.getMonthlyLimit() == null) {
			accountEntity.setMonthlyLimit(BigDecimal.valueOf(100000));
		}

		if(null == accountEntity.getActive()) {
			accountEntity.setActive(false);
		}

		if (null == accountEntity.getBlocked()) {
			accountEntity.setBlocked(false);
		}

		if (null == accountEntity.getBalance()) {
			accountEntity.setBalance(BigDecimal.ZERO);
		}
	}

	public String generateAccountNumber() {
		log.info("Starting generate account number");
		SecureRandom random = new SecureRandom();
		StringBuilder accountNumber = new StringBuilder(ACCOUNT_NUMBER_LENGTH);
		for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
			int randomIndex = random.nextInt(ACCOUNT_NUMBER_CHARS.length());
			char randomChar = ACCOUNT_NUMBER_CHARS.charAt(randomIndex);
			accountNumber.append(randomChar);
		}
		return accountNumber.toString();
	}
}

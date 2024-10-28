package com.bank.account.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Value("${account.max.number.accounts}")
	private long maxNumberAccounts;

	public long getMaxNumberAccounts() {
		return maxNumberAccounts;
	}
}
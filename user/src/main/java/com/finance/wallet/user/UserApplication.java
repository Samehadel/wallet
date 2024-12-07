package com.finance.wallet.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"com.finance.wallet.user", "com.finance.common"})
@EnableJpaRepositories(basePackages = {"com.finance.wallet.user.persistence.repository", "com.finance.common.persistence.repository"})
@EnableDiscoveryClient
public class UserApplication {
	public static void main(final String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
}

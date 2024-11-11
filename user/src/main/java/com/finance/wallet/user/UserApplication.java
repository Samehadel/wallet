package com.finance.wallet.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication(scanBasePackages = {"com.finance.wallet.user", "com.finance.common"})
@EnableDiscoveryClient
public class UserApplication {
	public static void main(final String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
}

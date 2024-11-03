package com.finance.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.finance.authentication"})
@EnableFeignClients
public class AuthenticationApplication {
    public static void main(final String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }
}

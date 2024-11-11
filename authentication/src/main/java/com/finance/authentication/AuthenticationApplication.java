package com.finance.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.finance.authentication", "com.finance.common"})
@EnableFeignClients(basePackages = {"com.finance.common.client"})
@EnableDiscoveryClient
public class AuthenticationApplication {
    public static void main(final String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }
}

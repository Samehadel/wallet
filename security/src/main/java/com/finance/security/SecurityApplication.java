package com.finance.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.finance.security", "com.finance.common"})
@EnableFeignClients(basePackages = {"com.finance.common.client"})
@EnableDiscoveryClient
@EnableAsync
public class SecurityApplication {
    public static void main(final String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}

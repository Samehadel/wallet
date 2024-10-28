package com.finance.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {
	@Value("${service.customer.route.name}")
	private String customerServiceRouteName;
	@Value("${service.customer.baseUrl}")
	private String customerBaseUrl;
	@Value("${service.customer.name}")
	private String customerServiceName;

	@Value("${service.account.route.name}")
	private String accountServiceRouteName;
	@Value("${service.account.baseUrl}")
	private String accountBaseUrl;
	@Value("${service.account.name}")
	private String accountServiceName;

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder locatorBuilder) {
		return locatorBuilder.routes()
				.route(customerServiceRouteName, r -> r.path(customerBaseUrl).uri("lb://" + customerServiceName))
				.route(accountServiceRouteName, r -> r.path(accountBaseUrl).uri("lb://" + accountServiceName))
				.build();
	}
}

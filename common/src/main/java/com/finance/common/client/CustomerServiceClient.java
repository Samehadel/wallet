package com.finance.common.client;

import com.finance.common.dto.CustomerDTO;
import com.finance.common.model.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "CUSTOMER-SERVICE", path = "customer-service/api")
public interface CustomerServiceClient {

	@GetMapping("/{code}")
    ApiResponse<CustomerDTO> findCustomerByCode(@PathVariable("code") String code);
}

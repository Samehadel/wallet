package com.bank.customer.controller;

import com.bank.customer.service.CustomerService;
import com.finance.common.controller.BaseController;
import com.finance.common.dto.CustomerDTO;
import com.finance.common.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CustomerController extends BaseController<CustomerDTO> {

    private final CustomerService customerService;

    @Override
    protected BaseService<CustomerDTO> getService() {
        return customerService;
    }
}

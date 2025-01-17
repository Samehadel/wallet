package com.finance.wallet.user.controller;

import com.finance.common.dto.AccessUrlDTO;
import com.finance.wallet.user.service.EndpointService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/endpoint")
@RequiredArgsConstructor
public class InternalEndpointController {
    private final EndpointService endpointService;

    @GetMapping
    public List<AccessUrlDTO> getPublicEndpoints() {
        return endpointService.getPublicEndpoints();
    }

    @GetMapping("/username/{username}")
    public List<AccessUrlDTO> getUserEndpoints(@PathVariable("username") final String username) {
        return endpointService.getUserEndpoints(username);
    }
}

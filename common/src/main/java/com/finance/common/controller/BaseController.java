package com.finance.common.controller;

import com.finance.common.dto.BaseDTO;
import com.finance.common.model.ApiResponse;
import com.finance.common.service.BaseService;

import org.springframework.web.bind.annotation.*;

@RestController
public abstract class BaseController<D extends BaseDTO> {
	protected abstract BaseService<D> getService();

	@PostMapping("/create")
	public ApiResponse<D> create(@RequestBody D request) {
		return getService().create(request);
	}

	@GetMapping("/get/{code}")
	public ApiResponse<D> get(@PathVariable("code") String code) {
		return getService().get(code);
	}

	@PutMapping("/block/{code}")
	public ApiResponse<Void> block(@PathVariable("code") String code) {
		return getService().block(code);
	}

}

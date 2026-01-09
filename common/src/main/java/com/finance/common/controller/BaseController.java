package com.finance.common.controller;

import com.finance.common.dto.BaseDTO;
import com.finance.common.model.ApiResponse;
import com.finance.common.service.BaseService;
import com.finance.common.util.ApiResponseBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
public abstract class BaseController<D extends BaseDTO> {
	protected abstract BaseService<D> getService();

	@PostMapping
	public ApiResponse<D> create(@RequestBody D request) {
		D dto = getService().create(request);
		return ApiResponseBuilder.buildSuccessResponse(dto);
	}

	@GetMapping("/{code}")
	public ApiResponse<D> get(@PathVariable("code") String code) {
		return ApiResponseBuilder.buildSuccessResponse(getService().get(code));
	}

	@PutMapping("/block/{code}")
	public ApiResponse<Void> block(@PathVariable("code") String code) {
		getService().block(code);
		return ApiResponseBuilder.buildSuccessResponse(null);
	}

}

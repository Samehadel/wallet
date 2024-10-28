package com.finance.common.service;

import com.finance.common.model.ApiResponse;

public interface BaseService <D>{
	ApiResponse<D> create(D dto);
	ApiResponse<D> get(String code);
	ApiResponse<Void> block(String code);
}

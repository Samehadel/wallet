package com.finance.common.service;

public interface BaseService <D>{
	D create(D dto);
	D get(String code);
	void block(String code);
}

package com.finance.common.mapper;

import com.finance.common.dto.BaseDTO;
import com.finance.common.entity.BaseEntity;

import java.util.List;

public interface GlobalMapper <E extends BaseEntity, D extends BaseDTO> {
	E mapToEntity(D dto);
	List<E> mapToEntity(List<D> dto);

	D mapToDTO(E entity);
	List<D> mapToDTO(List<E> entity);
}

package com.bank.customer.mapper;

import com.finance.common.dto.OfficialIdDTO;
import com.bank.customer.entity.OfficialIdEntity;
import com.finance.common.mapper.GlobalMapper;

public class OfficialIdMapper implements GlobalMapper<OfficialIdEntity, OfficialIdDTO> {
	protected OfficialIdMapper() {
	}

	public OfficialIdEntity mapToEntity(OfficialIdDTO dto) {
		OfficialIdEntity entity = new OfficialIdEntity();
		entity.setType(dto.getType());
		entity.setValue(dto.getValue());
		entity.setExpiryDate(dto.getExpiryDate());
		return entity;
	}

	public OfficialIdDTO mapToDTO(OfficialIdEntity entity) {
		OfficialIdDTO dto = OfficialIdDTO.builder()
				.type(entity.getType())
				.expiryDate(entity.getExpiryDate())
				.value(entity.getValue())
				.build();
		return dto;
	}
}

package com.bank.customer.mapper;

import com.finance.common.dto.AddressDTO;
import com.bank.customer.entity.AddressComponent;
import com.finance.common.mapper.GlobalMapper;

public class AddressMapper implements GlobalMapper<AddressComponent, AddressDTO> {
	protected AddressMapper() {
	}
	@Override
	public AddressComponent mapToEntity(AddressDTO dto) {
		AddressComponent entity = new AddressComponent();
		entity.setStreet(dto.getStreet());
		entity.setCity(dto.getCity());
		entity.setZipCode(dto.getZipCode());
		entity.setBuildingNumber(dto.getBuildingNumber());
		entity.setCountry(dto.getCountry());
		entity.setRegion(dto.getRegion());
		return entity;
	}

	@Override
	public AddressDTO mapToDTO(AddressComponent entity) {
		AddressDTO dto = AddressDTO.builder()
				.street(entity.getStreet())
				.city(entity.getCity())
				.zipCode(entity.getZipCode())
				.buildingNumber(entity.getBuildingNumber())
				.country(entity.getCountry())
				.region(entity.getRegion())
				.build();
		return dto;
	}
}

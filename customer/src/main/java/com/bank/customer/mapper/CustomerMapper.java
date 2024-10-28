package com.bank.customer.mapper;

import com.finance.common.dto.AddressDTO;
import com.finance.common.dto.CustomerDTO;
import com.finance.common.dto.OfficialIdDTO;
import com.bank.customer.entity.AddressComponent;
import com.bank.customer.entity.CustomerEntity;
import com.bank.customer.entity.OfficialIdEntity;
import com.finance.common.mapper.GlobalMapper;

public class CustomerMapper implements GlobalMapper<CustomerEntity, CustomerDTO> {

	private final AddressMapper addressMapper = new AddressMapper();
	private final OfficialIdMapper officialIdMapper = new OfficialIdMapper();

	@Override
	public CustomerEntity mapToEntity(CustomerDTO dto) {
		CustomerEntity entity = new CustomerEntity();
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		entity.setPhoneNumber(dto.getPhoneNumber());
		AddressComponent addressComponent = addressMapper.mapToEntity(dto.getAddressDTO());
		entity.setAddressComponent(addressComponent);
		for (OfficialIdDTO officialIdDTO : dto.getOfficialIDs()) {
			OfficialIdEntity officialIdEntity = officialIdMapper.mapToEntity(officialIdDTO);
			entity.addOfficialId(officialIdEntity);
		}

		return entity;
	}

	@Override
	public CustomerDTO mapToDTO(CustomerEntity entity) {
		AddressDTO addressDTO = addressMapper.mapToDTO(entity.getAddressComponent());
		CustomerDTO dto = CustomerDTO.builder()
				.customerCode(entity.getCustomerCode())
				.firstName(entity.getFirstName())
				.lastName(entity.getLastName())
				.email(entity.getEmail())
				.phoneNumber(entity.getPhoneNumber())
				.active(entity.getActive())
				.blocked(entity.getBlocked())
				.addressDTO(addressDTO)
				.build();
		for (OfficialIdEntity officialIdEntity : entity.getOfficialIDs()) {
			OfficialIdDTO officialIdDTO = officialIdMapper.mapToDTO(officialIdEntity);
			dto.addOfficialId(officialIdDTO);
		}

		return dto;
	}
}

package com.bank.customer.unit;

import com.finance.common.dto.AddressDTO;
import com.finance.common.dto.CustomerDTO;
import com.finance.common.dto.OfficialIdDTO;
import com.bank.customer.entity.AddressComponent;
import com.bank.customer.entity.CustomerEntity;
import com.bank.customer.entity.OfficialIdEntity;
import com.finance.common.enums.OfficialIdTypeEnum;
import com.bank.customer.mapper.CustomerMapper;
import com.finance.common.mapper.MapperTest;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CustomerMapperTest implements MapperTest {
	private final CustomerMapper customerMapper = new CustomerMapper();

	@Test
	@Override
	public void testMapToEntity() {
		CustomerDTO customerDTO = getCustomerDTO();
		OfficialIdDTO officialIdDTO = getOfficialIdDTO();
		customerDTO.addOfficialId(officialIdDTO);
		CustomerEntity mappedEntity = customerMapper.mapToEntity(customerDTO);

		assertDTOMatchEntity(customerDTO, mappedEntity);
	}

	private void assertDTOMatchEntity(CustomerDTO customerDTO, CustomerEntity customerEntity) {
		assertEquals(customerDTO.getFirstName(), customerEntity.getFirstName());
		assertEquals(customerDTO.getLastName(), customerEntity.getLastName());
		assertEquals(customerDTO.getEmail(), customerEntity.getEmail());
		assertEquals(customerDTO.getPhoneNumber(), customerEntity.getPhoneNumber());
		assertNull(customerEntity.getCustomerCode());
		assertAddressDTOMatchEntity(customerDTO.getAddressDTO(), customerEntity.getAddressComponent());
		assertNull(customerEntity.getActive());
		assertNull(customerEntity.getBlocked());
		assertOfficialIdDTOMatchEntity(customerDTO.getOfficialIDs(), customerEntity.getOfficialIDs());
	}

	private void assertOfficialIdDTOMatchEntity(Set<OfficialIdDTO> officialIdDTOSet, Set<OfficialIdEntity> officialIdEntitySet) {
		List<OfficialIdDTO> officialIdDTOList = officialIdDTOSet.stream().collect(Collectors.toList());
		List<OfficialIdEntity> officialIdEntityList = officialIdEntitySet.stream().collect(Collectors.toList());

		for (int i = 0; i < officialIdDTOList.size(); i++) {
			OfficialIdDTO officialIdDTO = officialIdDTOList.get(i);
			OfficialIdEntity officialIdEntity = officialIdEntityList.get(i);
			assertEquals(officialIdDTO.getType(), officialIdEntity.getType());
			assertEquals(officialIdDTO.getValue(), officialIdEntity.getValue());
			assertEquals(officialIdDTO.getExpiryDate(), officialIdEntity.getExpiryDate());
		}

	}

	private void assertAddressDTOMatchEntity(AddressDTO addressDTO, AddressComponent addressComponent) {
		assertEquals(addressDTO.getStreet(), addressComponent.getStreet());
		assertEquals(addressDTO.getCity(), addressComponent.getCity());
		assertEquals(addressDTO.getZipCode(), addressComponent.getZipCode());
		assertEquals(addressDTO.getBuildingNumber(), addressComponent.getBuildingNumber());
		assertEquals(addressDTO.getCountry(), addressComponent.getCountry());
		assertEquals(addressDTO.getRegion(), addressComponent.getRegion());
	}

	private OfficialIdDTO getOfficialIdDTO() {
		return OfficialIdDTO.builder()
				.type(OfficialIdTypeEnum.NATIONAL_ID)
				.value("Value")
				.expiryDate(new Date(12222000L))
				.build();
	}

	private CustomerDTO getCustomerDTO() {
		return CustomerDTO.builder()
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@example.com")
				.phoneNumber("123456789")
				.active(true)
				.blocked(false)
				.addressDTO(AddressDTO.builder()
						.street("Street")
						.city("City")
						.zipCode("12345")
						.buildingNumber("123")
						.country("Country")
						.region("Region")
						.build())
				.build();
	}

	@Override
	@Test
	public void testMapToDTO() {
		CustomerEntity customerEntity = getCustomerEntity();
		OfficialIdEntity officialIdEntity = getOfficialIdEntity();
		customerEntity.addOfficialId(officialIdEntity);
		CustomerDTO mappedDTO = customerMapper.mapToDTO(customerEntity);

		assertEntityMatchDTO(customerEntity, mappedDTO);
	}

	private OfficialIdEntity getOfficialIdEntity() {
		OfficialIdEntity officialIdEntity = new OfficialIdEntity();
		officialIdEntity.setType(OfficialIdTypeEnum.NATIONAL_ID);
		officialIdEntity.setValue("Value");
		officialIdEntity.setExpiryDate(new Date(12222000L));
		return officialIdEntity;
	}

	private void assertEntityMatchDTO(CustomerEntity customerEntity, CustomerDTO mappedDTO) {
		assertEquals(customerEntity.getCustomerCode(), mappedDTO.getCustomerCode());
		assertEquals(customerEntity.getFirstName(), mappedDTO.getFirstName());
		assertEquals(customerEntity.getLastName(), mappedDTO.getLastName());
		assertEquals(customerEntity.getEmail(), mappedDTO.getEmail());
		assertEquals(customerEntity.getPhoneNumber(), mappedDTO.getPhoneNumber());
		assertNull(mappedDTO.getActive());
		assertNull(mappedDTO.getBlocked());
		assertAddressDTOMatchEntity(mappedDTO.getAddressDTO(), customerEntity.getAddressComponent());
		assertOfficialIdDTOMatchEntity(mappedDTO.getOfficialIDs(), customerEntity.getOfficialIDs());
	}


	private CustomerEntity getCustomerEntity() {
		return CustomerEntity.builder()
				.customerCode("CUST_0211")
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@example.com")
				.phoneNumber("123456789")
				.addressComponent(AddressComponent.builder()
						.street("Street")
						.city("City")
						.zipCode("12345")
						.buildingNumber("123")
						.country("Country")
						.region("Region")
						.build())
				.build();
	}
}

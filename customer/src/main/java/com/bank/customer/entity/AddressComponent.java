package com.bank.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressComponent {
	@Column(name = "ZIP_CODE")
	private String zipCode;

	@Column(name = "BUILDING_NUMBER")
	private String buildingNumber;

	@Column(name = "STREET")
	private String street;

	@Column(name = "CITY")
	private String city;

	@Column(name = "Region")
	private String region;

	@Column(name = "COUNTRY")
	private String country;
}

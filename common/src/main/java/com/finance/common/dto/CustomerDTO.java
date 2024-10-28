package com.finance.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO extends BaseDTO {

	private String customerCode;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private AddressDTO addressDTO;
	private Set<OfficialIdDTO> officialIDs;
	private Boolean active;
	private Boolean blocked;

	public void addOfficialId(OfficialIdDTO officialIdDTO) {
		if (officialIDs == null) {
			officialIDs = new HashSet<>();
		}
		officialIDs.add(officialIdDTO);
	}

	@Override
	public String toString() {
		return "CustomerDTO{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", email='" + email + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				'}';
	}
}

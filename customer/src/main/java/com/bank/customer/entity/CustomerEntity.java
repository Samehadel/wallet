package com.bank.customer.entity;

import com.finance.common.persistence.ActiveEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "CUSTOMER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(CustomerEntityListener.class)
public class CustomerEntity extends ActiveEntity {

	@Id
	@Column(name = "CUSTOMER_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_ID_SEQ")
	@SequenceGenerator(name = "CUSTOMER_ID_SEQ", sequenceName = "CUSTOMER_ID_SEQ", allocationSize = 1)
	private Long customerId;

	@Column(name = "CUSTOMER_CODE", nullable = false, unique = true)
	private String customerCode;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "EMAIL", unique = true, nullable = false)
	private String email;

	@Column(name = "PHONE_NUMBER", unique = true, nullable = false)
	private String phoneNumber;

	@Column(name = "ADDRESS")
	private AddressComponent addressComponent;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "CUSTOMER_ID")
	private Set<OfficialIdEntity> officialIDs;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public AddressComponent getAddressComponent() {
		return addressComponent;
	}

	public void setAddressComponent(AddressComponent addressComponent) {
		this.addressComponent = addressComponent;
	}

	public Set<OfficialIdEntity> getOfficialIDs() {
		return officialIDs;
	}

	public void setOfficialIDs(Set<OfficialIdEntity> officialIDs) {
		this.officialIDs = officialIDs;
	}

	public void addOfficialId(OfficialIdEntity officialId) {
		if(officialIDs == null) {
			officialIDs = new HashSet<>();
		}
		officialIDs.add(officialId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		CustomerEntity that = (CustomerEntity) o;
		return Objects.equals(customerId, that.customerId) && Objects.equals(customerCode, that.customerCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), customerId, customerCode);
	}
}

package com.bank.customer.entity;

import com.finance.common.persistence.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "CUSTOMER")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(CustomerEntityListener.class)
public class CustomerEntity extends AuditableEntity {

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

	@Column(name = "BLOCKED")
	private Boolean blocked;

	@Column(name = "ACTIVE")
	private Boolean active;

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

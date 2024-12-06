package com.bank.customer.entity;

import com.finance.common.enums.OfficialIdTypeEnum;
import com.finance.common.persistence.BaseEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "OFFICIAL_ID")
public class OfficialIdEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OFFICIAL_ID_SEQ")
	@SequenceGenerator(name = "OFFICIAL_ID_SEQ", sequenceName = "OFFICIAL_ID_SEQ", allocationSize = 1)
	@Column(name = "OFFICIAL_ID")
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE")
	private OfficialIdTypeEnum type;

	@Column(name = "VALUE")
	private String value;

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRY_DATE")
	private Date expiryDate;

	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	private CustomerEntity customerEntity;

	public OfficialIdTypeEnum getType() {
		return type;
	}

	public void setType(OfficialIdTypeEnum type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		OfficialIdEntity that = (OfficialIdEntity) o;
		return type == that.type && value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), type, value);
	}
}

package com.finance.account.entity;

import com.finance.common.enums.AccountStatusEnum;
import com.finance.common.enums.AccountTypeEnum;
import com.finance.common.persistence.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "ACCOUNT")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AccountEntityListener.class)
public class AccountEntity extends AuditableEntity {

	@Id
	@GeneratedValue(generator = "ACCOUNT_ID_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ACCOUNT_ID_SEQ", sequenceName = "ACCOUNT_ID_SEQ", allocationSize = 1)
	@Setter(AccessLevel.PRIVATE)
	private Long id;

	@Column(name = "CODE", nullable = false)
	private String code;

	@Column(name = "CUSTOMER_CODE", nullable = false)
	private String customerCode;

	@Column(name = "ACCOUNT_NUMBER", nullable = false, unique = true)
	private String accountNumber;

	@Column(name = "ACCOUNT_HOLDER_NAME", nullable = false)
	private String accountHolderName;

	@Enumerated(EnumType.STRING)
	@Column(name = "ACCOUNT_TYPE", nullable = false)
	private AccountTypeEnum accountType;

	@Enumerated(EnumType.STRING)
	@Column(name = "ACCOUNT_STATUS", nullable = false)
	private AccountStatusEnum accountStatus;

	@Column(name = "BALANCE", nullable = false)
	private BigDecimal balance;

	@Column(name = "DAILY_LIMIT", nullable = false)
	private BigDecimal dailyLimit;

	@Column(name = "MONTHLY_LIMIT", nullable = false)
	private BigDecimal monthlyLimit;

	@Column(name = "BLOCKED")
	private Boolean blocked;

	@Column(name = "ACTIVE")
	private Boolean active;
}

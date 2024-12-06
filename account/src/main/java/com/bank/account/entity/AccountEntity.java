package com.bank.account.entity;

import com.finance.common.persistence.AuditableEntity;
import com.finance.common.enums.AccountStatusEnum;
import com.finance.common.enums.AccountTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "ACCOUNT")
@Builder
@AllArgsConstructor
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


	public AccountEntity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public AccountTypeEnum getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountTypeEnum accountType) {
		this.accountType = accountType;
	}

	public AccountStatusEnum getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatusEnum accountStatus) {
		this.accountStatus = accountStatus;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(BigDecimal dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public BigDecimal getMonthlyLimit() {
		return monthlyLimit;
	}

	public void setMonthlyLimit(BigDecimal monthlyLimit) {
		this.monthlyLimit = monthlyLimit;
	}
}

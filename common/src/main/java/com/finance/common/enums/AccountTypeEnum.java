package com.finance.common.enums;

public enum AccountTypeEnum {
	CHECKING(1, "Checking"),
	SAVINGS(2, "Savings"),
	CREDIT(3, "Credit"),
	MONEY_MARKET(4, "Money Market"),
	CERTIFICATE_OF_DEPOSIT(5, "Certificate of Deposit");

	public final int id;
	public final String name;

	AccountTypeEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static AccountTypeEnum fromId(int id) {
		for (AccountTypeEnum type : AccountTypeEnum.values()) {
			if (type.id == id) {
				return type;
			}
		}
		return null;
	}

	public static AccountTypeEnum fromName(String name) {
		for (AccountTypeEnum type : AccountTypeEnum.values()) {
			if (type.name.equals(name)) {
				return type;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
}

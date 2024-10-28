package com.finance.common.enums;

import java.util.List;

public enum AccountStatusEnum {
	ACTIVE, INACTIVE, CLOSED, BLOCKED, PENDING_APPROVAL;

	public static List<AccountStatusEnum> getPendingList() {
		return List.of(ACTIVE, INACTIVE, PENDING_APPROVAL);
	}
}

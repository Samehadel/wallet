package com.finance.account.mapper;

import com.finance.account.entity.AccountEntity;
import com.finance.common.dto.AccountDTO;
import com.finance.common.mapper.GlobalMapper;

import java.util.List;

public class AccountMapper implements GlobalMapper <AccountEntity, AccountDTO> {

	@Override
	public AccountEntity mapToEntity(AccountDTO dto) {
        return AccountEntity.builder()
                .customerCode(dto.getCustomerCode())
                .accountType(dto.getAccountType())
                .accountHolderName(dto.getAccountHolderName())
                .build();
	}

	@Override
	public List<AccountEntity> mapToEntity(List<AccountDTO> dto) {
		return dto.stream()
				.map(this::mapToEntity)
				.toList();
	}

	@Override
	public AccountDTO mapToDTO(AccountEntity entity) {
        return AccountDTO.builder()
                .customerCode(entity.getCustomerCode())
                .accountNumber(entity.getAccountNumber())
                .accountType(entity.getAccountType())
                .accountStatus(entity.getAccountStatus())
                .accountHolderName(entity.getAccountHolderName())
                .monthlyLimit(entity.getMonthlyLimit())
                .dailyLimit(entity.getDailyLimit())
                .balance(entity.getBalance())
                .build();
	}

	@Override
	public List<AccountDTO> mapToDTO(List<AccountEntity> entity) {
		return entity.stream()
				.map(this::mapToDTO)
				.toList();
	}
}

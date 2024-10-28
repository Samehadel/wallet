package com.bank.account;

import com.bank.account.entity.AccountEntity;
import com.finance.common.enums.AccountStatusEnum;
import com.finance.common.enums.AccountTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

	@Query("SELECT COUNT(a) FROM AccountEntity a " +
			"WHERE a.customerCode = :customerCode " +
			"AND a.accountStatus IN :accountStatusList ")
	long countByCustomerCode(@Param("customerCode") String customerCode, @Param("accountStatusList") List<AccountStatusEnum> accountStatusList);

	@Query("SELECT COUNT(a) FROM AccountEntity a " +
			"WHERE a.customerCode = :customerCode AND a.accountStatus = :accountStatus " +
			"AND a.accountType = :accountType")
	long countByCustomerCodeAndAccountType(@Param("customerCode") String customerCode, @Param("accountType")AccountTypeEnum accountType, @Param("accountStatus") AccountStatusEnum accountStatus);

	List<AccountEntity> findByCustomerCode(String code);

	AccountEntity findByAccountNumber(String accountNumber);

	Optional<AccountEntity> findByCode(String code);
}

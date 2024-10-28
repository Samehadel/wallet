package com.bank.customer;

import com.bank.customer.entity.CustomerEntity;
import com.finance.common.enums.OfficialIdTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

	Optional<CustomerEntity> findByCustomerCode(String customerCode);

	@Query("SELECT COUNT(c) FROM CustomerEntity c WHERE c.email = :email")
	long countByEmail(@Param("email") String email);

	@Query("SELECT COUNT(customer) FROM CustomerEntity customer WHERE customer.phoneNumber = :phoneNumber")
	long countByPhoneNumber(@Param("phoneNumber") String phoneNumber);

	@Query("SELECT COUNT(customer) FROM CustomerEntity customer " +
			"JOIN OfficialIdEntity OFF_ID ON OFF_ID.customerEntity.customerId = customer.customerId " +
			"WHERE OFF_ID.value = :value AND OFF_ID.type = :type ")
	long countByOfficialId(@Param("value") String value, @Param("type") OfficialIdTypeEnum type);

	@Query(value = "SELECT nextval('CUSTOMER_ID_SEQ')", nativeQuery = true)
	Long getNextSequenceValue();
}

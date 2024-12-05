package com.finance.wallet.user.persistence.repository;

import com.finance.wallet.user.persistence.entity.RoleEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query(value = "SELECT r.* FROM ROLE r "
        + "JOIN USER_ROLE userRole ON r.id = userRole.role_id "
        + "WHERE userRole.user_id = :userId", nativeQuery = true)
    List<RoleEntity> findByUserId(Long userId);

}

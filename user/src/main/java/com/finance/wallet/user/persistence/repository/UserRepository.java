package com.finance.wallet.user.persistence.repository;

import com.finance.wallet.user.persistence.entity.UserEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByMobile(String mobile);

    Optional<UserEntity> findByCif(String cif);

    boolean existsByUsername(String username);

    boolean existsByMobile(String mobile);
}

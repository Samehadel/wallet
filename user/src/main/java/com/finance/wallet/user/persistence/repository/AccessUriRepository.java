package com.finance.wallet.user.persistence.repository;

import com.finance.wallet.user.persistence.entity.AccessUrlEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessUriRepository extends JpaRepository<AccessUrlEntity, Long> {
    List<AccessUrlEntity> findByIsPrivate(boolean isPrivate);

    @Query("SELECT a FROM UserEntity u "
               + "JOIN u.roles r "
               + "JOIN r.accessUrls a "
               + "WHERE u.username = :username")
    List<AccessUrlEntity> findByUserRoles(String username);
}

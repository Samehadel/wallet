package com.finance.common.persistence.repository;

import com.finance.common.persistence.entity.ServiceConfigurationsEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceConfigRepository extends JpaRepository<ServiceConfigurationsEntity, Integer> {
    Optional<ServiceConfigurationsEntity> findByKey(String key);
}

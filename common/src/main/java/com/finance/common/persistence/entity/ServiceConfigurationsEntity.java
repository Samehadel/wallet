package com.finance.common.persistence.entity;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name = "SERVICE_CONFIGURATIONS")
@Data
public class ServiceConfigurationsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CONFIG_KEY", nullable = false, unique = true, length = 50)
    private String key;

    @Column(name = "CONFIG_VALUE", nullable = false)
    private String value;
}

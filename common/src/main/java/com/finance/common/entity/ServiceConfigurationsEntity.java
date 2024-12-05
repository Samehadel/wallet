package com.finance.common.entity;

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

    @Column(name = "NAME", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "VALUE", nullable = false)
    private String value;
}

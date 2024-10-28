package com.finance.wallet.user.persistence.entity;

import com.finance.common.constants.UserStatus;
import com.finance.common.entity.AuditableEntity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="USERS")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "LAST_LOGIN_DATE")
    private LocalDateTime lastLoginDate;

    @Column(name = "LOGIN_TRIES", nullable = false)
    private Integer loginTries;

    @Column(name = "LOCKED", nullable = false)
    private Boolean locked;

}


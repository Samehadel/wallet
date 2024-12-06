package com.finance.wallet.user.persistence.entity;

import com.finance.common.constants.UserStatusEnum;
import com.finance.common.persistence.entity.AuditableEntity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="USERS")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@EntityListeners(UserEntityListener.class)
public class UserEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "CIF", nullable = false, length = 20, unique = true)
    private String cif;

    @Column(name = "MOBILE", nullable = false, unique = true, length = 15)
    private String mobile;

    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "STATUS", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserStatusEnum status;

    @Column(name = "LAST_LOGIN_DATE")
    private LocalDateTime lastLoginDate;

    @Column(name = "LOGIN_TRIALS", nullable = false)
    private Integer loginTrials = 0;

    @Column(name = "STATUS_UPDATED_AT")
    private LocalDateTime statusUpdatedAt;

    @Column(name = "ACTIVATED", nullable = false)
    private Boolean activated;

    @Column(name = "ACTIVATED_AT")
    private LocalDateTime activatedAt;

    @ManyToMany
    @JoinTable(
        name = "USER_ROLE",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<RoleEntity> roles = new HashSet<>();
}


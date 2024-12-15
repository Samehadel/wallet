package com.finance.wallet.user.persistence.entity;

import com.finance.common.constants.UrlMethodEnum;
import com.finance.common.persistence.entity.BaseEntity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ACCESS_URL")
@Data
public class AccessUrlEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "URL", nullable = false)
    private String url;

    @Column(name = "METHOD", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private UrlMethodEnum method;

    @Column(name = "PRIVATE", nullable = false)
    private Boolean isPrivate;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AccessUrlEntity that = (AccessUrlEntity) o;
        return Objects.equals(url, that.url) && method == that.method && Objects.equals(isPrivate, that.isPrivate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, method, isPrivate);
    }
}

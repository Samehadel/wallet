package com.finance.wallet.user.mapper;

import com.finance.common.dto.UserDTO;
import com.finance.common.mapper.GlobalMapper;
import com.finance.wallet.user.persistence.entity.UserEntity;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper extends GlobalMapper<UserEntity, UserDTO> {

    @Override
    @Named("fullMapping")
    UserDTO mapToDTO(UserEntity entity);

    @Override
    @IterableMapping(qualifiedByName = "fullMapping")
    List<UserDTO> mapToDTO(List<UserEntity> entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastLoginDate", ignore = true)
    @Mapping(target = "activated", ignore = true)
    @Mapping(target = "activatedAt", ignore = true)
    @Mapping(target = "accessUris", ignore = true)
    @Mapping(target = "statusUpdatedAt", ignore = true)
    @Named("excludeSensitive")
    UserDTO mapToDTOExcludeSensitiveInfo(UserEntity entity);

    default java.lang.String map(final char[] value) {
        if (value == null) {
            return null;
        }

        return new java.lang.String(value);
    }

    default char[] map(final java.lang.String value) {
        if (value == null) {
            return new char[0];
        }
        return value.toCharArray();
    }
}

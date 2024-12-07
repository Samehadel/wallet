package com.finance.wallet.user.mapper;

import com.finance.common.dto.UserDTO;
import com.finance.common.mapper.GlobalMapper;
import com.finance.wallet.user.persistence.entity.UserEntity;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends GlobalMapper<UserEntity, UserDTO> {

    @Override
    UserDTO mapToDTO(UserEntity entity);

    @Override
    List<UserDTO> mapToDTO(List<UserEntity> entity);

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

package com.finance.wallet.user.mapper;

import com.finance.common.dto.UserDTO;
import com.finance.common.mapper.GlobalMapper;
import com.finance.wallet.user.persistence.entity.UserEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends GlobalMapper<UserEntity, UserDTO> {

    default String map(final char[] value) {
        if (value == null) {
            return null;
        }

        return new String(value);
    }

    default char[] map(final String value) {
        if (value == null) {
            return null;
        }
        return value.toCharArray();
    }
}

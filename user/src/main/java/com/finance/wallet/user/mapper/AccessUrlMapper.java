package com.finance.wallet.user.mapper;

import com.finance.common.dto.AccessUriDTO;
import com.finance.common.mapper.GlobalMapper;
import com.finance.wallet.user.persistence.entity.AccessUrlEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccessUrlMapper extends GlobalMapper<AccessUrlEntity, AccessUriDTO> {
}

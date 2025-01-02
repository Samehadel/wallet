package com.finance.common.service.request;

import com.finance.common.dto.UserDTO;

public interface RequestInfoService {

    UserDTO getRequestUser();

    String getRequestUsername();
}

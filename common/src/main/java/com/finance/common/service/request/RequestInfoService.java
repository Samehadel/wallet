package com.finance.common.service.request;

import com.finance.common.dto.UserDTO;

public interface RequestInfoService {

    boolean internalRequest();

    UserDTO getRequestUser();

    String getRequestUsername();

    String getRequestMobile();

    String getRequestCif();
}

package com.finance.common.client;


import com.finance.common.dto.AccessUrlDTO;
import com.finance.common.dto.UserDTO;

import java.util.List;

public interface UserClient {
    UserDTO getUserByUsername(String username);

    UserDTO getUserByMobile(String mobile);

    List<AccessUrlDTO> getPublicEndpoints();

    List<AccessUrlDTO> getUserEndpoints(final String username);
}

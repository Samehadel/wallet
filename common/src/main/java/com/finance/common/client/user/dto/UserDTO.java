package com.finance.common.client.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.finance.common.constants.RegexConstants;
import com.finance.common.constants.UserStatus;
import com.finance.common.dto.BaseDTO;
import com.finance.common.service.CharArrayDeserializer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDTO extends BaseDTO {

    @NotEmpty
    private String username;

    @Pattern(regexp = RegexConstants.EMAIL_REGEX)
    private String email;

    @JsonDeserialize(using = CharArrayDeserializer.class)
    private char[] password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer loginTries;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean locked;
}


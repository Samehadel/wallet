package com.finance.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.finance.common.constants.RegexConstants;
import com.finance.common.constants.UserStatus;
import com.finance.common.service.CharArrayDeserializer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDTO extends BaseDTO {

    private String mobile;

    private String firstName;

    private String lastName;

    @NotEmpty
    private String username;

    @Pattern(regexp = RegexConstants.EMAIL_REGEX)
    private String email;

    @JsonDeserialize(using = CharArrayDeserializer.class)
    private char[] password;

    private UserStatus status;

    private Integer loginTries;
}

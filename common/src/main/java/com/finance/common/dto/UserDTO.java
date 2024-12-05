package com.finance.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.finance.common.constants.RegexConstants;
import com.finance.common.constants.RestGroups;
import com.finance.common.constants.UserStatusEnum;
import com.finance.common.service.CharArrayDeserializer;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDTO extends BaseDTO {

    private Long id;

    @NotEmpty
    private String username;

    @Null(groups = {RestGroups.Create.class, RestGroups.Update.class})
    private String cif;

    @NotEmpty(groups = {RestGroups.Create.class, RestGroups.Update.class})
    private String mobile;

    @NotEmpty(groups = {RestGroups.Create.class, RestGroups.Update.class})
    private String firstName;

    @NotEmpty(groups = {RestGroups.Create.class, RestGroups.Update.class})
    private String lastName;

    @Pattern(regexp = RegexConstants.EMAIL_REGEX)
    private String email;

    @NotEmpty(groups = {RestGroups.Create.class})
    @Size(min = 6, max = 36, groups = {RestGroups.Create.class})
    @JsonDeserialize(using = CharArrayDeserializer.class)
    private char[] password;

    @Null(groups = {RestGroups.Create.class, RestGroups.Update.class})
    private UserStatusEnum status;

    private LocalDateTime lastLoginDate;

    private Integer loginTrials;

    private LocalDateTime statusUpdatedAt;

    @Null(groups = {RestGroups.Create.class, RestGroups.Update.class})
    private Boolean activated;

    private LocalDateTime activatedAt;

    private List<RoleDTO> roles;

    private List<AccessUriDTO> accessUris;
}

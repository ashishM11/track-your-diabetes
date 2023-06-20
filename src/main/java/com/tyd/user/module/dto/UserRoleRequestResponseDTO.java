package com.tyd.user.module.dto;

public record UserRoleRequestResponseDTO(
        Long userRoleId,
        String userRoleName,
        java.util.Collection<UserRolePrivilegeRequestResponseDTO> privileges
) {
}

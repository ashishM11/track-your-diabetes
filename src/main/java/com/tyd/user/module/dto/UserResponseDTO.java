package com.tyd.user.module.dto;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDTO(
        Long userId,
        String userFName,
        String userLName,
        String userMobile,
        String userEmail,
        LocalDate userDOB,
        LocalDate userCreationDT,
        PasswordResponseDTO password,
        List<UserRoleRequestResponseDTO> userRoles
) {

}

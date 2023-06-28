package com.tyd.user.module.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordRequestDTO(
        @NotEmpty(message = "Email cannot be empty")
        @NotBlank(message = "Email cannot be blank.")
        @NotNull(message = "Email cannot be Null.")
        String userEmail,
        @NotEmpty(message = "Old password filed cannot be Empty.")
        @NotBlank(message = "Old password field cannot be Blank.")
        @NotNull(message = "Old password field cannot be Null.")
        String oldPassword,
        @NotEmpty(message = "New password cannot be Empty.")
        @NotBlank(message = "New password cannot be Blank.")
        @NotNull(message = "New password cannot be Null.")
        String newPassword,
        @NotEmpty(message = "Confirm password filed cannot be Empty.")
        @NotBlank(message = "Confirm password field cannot be Blank.")
        @NotNull(message = "Confirm password field cannot be Null.")
        String confirmPassword
) {
}

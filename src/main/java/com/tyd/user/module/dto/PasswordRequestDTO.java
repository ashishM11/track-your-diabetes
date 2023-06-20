package com.tyd.user.module.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PasswordRequestDTO(
        @NotEmpty(message = "Password cannot be Empty.")
        @NotBlank(message = "Password cannot be Blank.")
        @NotNull(message = "Password cannot be Null.")
        String password,
        @NotEmpty(message = "Retype Password filed cannot be Empty.")
        @NotBlank(message = "Retype Password field cannot be Blank.")
        @NotNull(message = "Retype Password field cannot be Null.")
        String retypePassword
) { }

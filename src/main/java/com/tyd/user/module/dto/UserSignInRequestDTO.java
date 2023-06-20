package com.tyd.user.module.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserSignInRequestDTO(
        @NotEmpty(message = "Email Id Or Mobile Number cannot be empty.")
        @NotBlank(message = "Email Id Or Mobile Number cannot be Blank.")
        @NotNull(message = "Email Id Or Mobile Number cannot be Null.")
        String userEmailOrMobile,
        @NotEmpty(message = "Password field cannot be Empty.")
        @NotBlank(message = "Password field cannot be Blank.")
        @NotNull(message = "Password field cannot be Null.")
        String userPassword) {
}

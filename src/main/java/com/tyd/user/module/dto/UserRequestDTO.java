package com.tyd.user.module.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record UserRequestDTO(
        @NotEmpty(message = "First Name cannot be Empty.")
        @NotBlank(message = "First Name cannot be Blank.")
        @NotNull(message = "First Name cannot be Null.")
        String userFName,
        @NotEmpty(message = "Last Name cannot be Empty.")
        @NotBlank(message = "Last Name cannot be Blank.")
        @NotNull(message = "Last Name cannot be Null.")
        String userLName,
        @NotEmpty(message = "Mobile number cannot be Empty.")
        @NotBlank(message = "Mobile number cannot be Blank.")
        @NotNull(message = "Mobile number cannot be Null.")
        @Length(max = 10, min = 10, message = "Mobile Number should be of 10 digit.")
        String userMobile,
        @NotEmpty(message = "EMail Id cannot be Empty.")
        @NotBlank(message = "EMail Id cannot be Blank.")
        @NotNull(message = "EMail Id cannot be Null.")
        @Email(message = "Email Id must be in correct format.")
        String userEmail,
        @NotEmpty(message = "Please share your gender.")
        @NotBlank(message = "Please share your gender.")
        @NotNull(message = "Please share your gender.")
        String userGender,
        @NotEmpty(message = "Date of Birth is required field.")
        @NotBlank(message = "Date of Birth is required field.")
        @NotNull(message = "Date of Birth is required field.")
        LocalDate userDOB,
        @NotNull(message = "Password & Retype password field cannot be Null.")
        PasswordRequestDTO password
) {
}

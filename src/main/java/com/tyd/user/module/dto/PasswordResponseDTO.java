package com.tyd.user.module.dto;

import java.time.LocalDate;

public record PasswordResponseDTO(Long passwordId,String encryptedPassword,LocalDate passwordCreationDT) {
}

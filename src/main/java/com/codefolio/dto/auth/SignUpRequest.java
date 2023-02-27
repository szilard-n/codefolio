package com.codefolio.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank
        String username,

        @NotBlank
        @Email
        String email,

        @NotNull
        @Size(min = 8)
        String password) {
}

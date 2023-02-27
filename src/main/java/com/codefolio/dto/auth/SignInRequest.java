package com.codefolio.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank
        String credential,

        @NotBlank
        String password) {
}

package com.codefolio.dto.user;

public record NewUserRequest(
        String username,
        String email,
        String password) {
}

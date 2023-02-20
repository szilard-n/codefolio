package com.codefolio.dto.user;

public record NewUserDto(
        String username,
        String email,
        String password) {
}

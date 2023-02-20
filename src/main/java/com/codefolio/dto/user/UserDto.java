package com.codefolio.dto.user;

import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email) {
}

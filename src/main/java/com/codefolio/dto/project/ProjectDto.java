package com.codefolio.dto.project;

import java.sql.Timestamp;
import java.util.UUID;

public record ProjectDto(
        UUID id,
        String category,
        String title,
        String description,
        UUID createdBy,
        Timestamp createdAt,
        Timestamp updatedAt) {
}

package com.codefolio.dto.project;

import com.codefolio.dto.task.TaskDto;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record ProjectPreviewResponse(
        UUID id,
        String category,
        String title,
        String description,
        UUID createdBy,
        Timestamp createdAt,
        Timestamp updatedAt,
        List<TaskDto> tasks) {
}

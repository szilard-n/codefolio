package com.codefolio.dto.project;

import com.codefolio.dto.task.TaskDto;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record NewProjectRequest(
        @NotBlank
        String category,

        @NotBlank
        String title,

        @NotBlank
        String description,
        List<TaskDto> tasks) {
}

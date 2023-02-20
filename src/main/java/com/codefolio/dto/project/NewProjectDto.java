package com.codefolio.dto.project;

import com.codefolio.dto.task.TaskDto;

import java.util.List;

public record NewProjectDto(
        String category,
        String title,
        String description,
        List<TaskDto> tasks) {
}

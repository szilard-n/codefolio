package com.codefolio.dto.user;

import com.codefolio.dto.project.ProjectDto;

import java.util.List;
import java.util.UUID;

public record UserPreviewDto(
        UUID id,
        String username,
        List<ProjectDto> createdProjects,
        List<ProjectDto> workList) {
}

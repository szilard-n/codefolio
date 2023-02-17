package com.codefolio.dto;

import com.codefolio.entity.Project;
import com.codefolio.entity.Task;
import com.codefolio.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private UUID id;
    private String category;
    private String title;
    private String description;
    private UUID createdBy;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<Task> tasks = new ArrayList<>();

    public static ProjectDto map(Project project, List<Task> tasks) {
        return ProjectDto.builder()
                .id(project.getId())
                .category(project.getCategory())
                .title(project.getTitle())
                .description(project.getDescription())
                .createdBy(project.getCreatedBy())
                .updatedAt(project.getUpdatedAt())
                .tasks(tasks)
                .build();
    }
}

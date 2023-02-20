package com.codefolio.controller;

import com.codefolio.dto.project.NewProjectDto;
import com.codefolio.dto.project.ProjectDto;
import com.codefolio.dto.project.ProjectPreviewDto;
import com.codefolio.entity.Project;
import com.codefolio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    // TODO: Remove userIds after auth is implemented

    private final ProjectService projectService;

    @PostMapping("/{userId}")
    public ResponseEntity<ProjectDto> createProject(@RequestBody NewProjectDto newProjectDto, @PathVariable UUID userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(newProjectDto, userId));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("/assign/{projectId}/{userId}")
    public ResponseEntity<Void> assignProjectToUser(@PathVariable UUID projectId, @PathVariable UUID userId) {
        projectService.assignProjectToUser(projectId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{projectId}/{userId}")
    public ResponseEntity<ProjectPreviewDto> previewProject(@PathVariable UUID projectId, @PathVariable UUID userId) {
        return ResponseEntity.ok(projectService.projectPreview(projectId, userId));
    }

}

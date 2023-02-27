package com.codefolio.controller;

import com.codefolio.dto.project.NewProjectRequest;
import com.codefolio.dto.project.ProjectDto;
import com.codefolio.dto.project.ProjectPreviewResponse;
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
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody NewProjectRequest newProjectDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(newProjectDto));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("/assign/{projectId}")
    public ResponseEntity<Void> assignProjectToUser(@PathVariable UUID projectId) {
        projectService.assignProjectToUser(projectId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectPreviewResponse> previewProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(projectService.projectPreview(projectId));
    }

}

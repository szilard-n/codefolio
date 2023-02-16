package com.codefolio.controller;

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

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(project));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("/assign/{projectId}/{userId}")
    public ResponseEntity<Void> assignProjectToUser(@PathVariable UUID projectId, @PathVariable UUID userId) {
        projectService.assignProjectToUser(projectId, userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

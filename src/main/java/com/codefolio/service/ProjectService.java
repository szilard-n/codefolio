package com.codefolio.service;

import com.codefolio.dto.project.NewProjectRequest;
import com.codefolio.dto.project.ProjectDto;
import com.codefolio.dto.project.ProjectPreviewResponse;
import com.codefolio.entity.Project;
import com.codefolio.entity.Task;
import com.codefolio.entity.User;
import com.codefolio.exception.ExceptionFactory;
import com.codefolio.exception.exceptions.ResourceNotFoundException;
import com.codefolio.mapper.ProjectMapper;
import com.codefolio.repostiory.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final String PROJECT_NOT_FOUND = "exceptions.project.projectNotFount";

    private final ProjectRepository projectRepository;
    private final AuthService authService;
    private final TaskService taskService;
    private final ProjectMapper projectMapper;

    @Transactional
    public ProjectDto createProject(NewProjectRequest newProjectDto) {
        User loggedInUser = authService.getAuthenticatedUser();
        Timestamp now = Timestamp.from(Instant.now());
        Project project = Project.builder()
                .createdAt(now)
                .updatedAt(now)
                .createdBy(loggedInUser.getId())
                .category(newProjectDto.category())
                .description(newProjectDto.description())
                .title(newProjectDto.title())
                .build();

        projectRepository.save(project);

        if (!CollectionUtils.isEmpty(newProjectDto.tasks())) {
            taskService.createTasks(newProjectDto.tasks(), project.getId(), loggedInUser.getId());
        }

        return projectMapper.map(project);
    }

    public List<ProjectDto> getAllProjects() {
        return projectMapper.map(projectRepository.findAll());
    }

    @Transactional
    public void assignProjectToUser(UUID projectId) {
        Project project = getProjectById(projectId);
        User currentUser = authService.getAuthenticatedUser();
        currentUser.getWorkList().add(project);
    }

    public ProjectPreviewResponse projectPreview(UUID projectId) {
        Project project = getProjectById(projectId);
        User currentUser = authService.getAuthenticatedUser();

        List<Task> tasks = taskService.getTasksForUser(project.getId(), currentUser.getId(), project.getCreatedBy());
        return projectMapper.map(project, tasks);
    }

    private Project getProjectById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> ExceptionFactory.create(ResourceNotFoundException.class, PROJECT_NOT_FOUND));
    }
}

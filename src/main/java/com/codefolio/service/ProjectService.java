package com.codefolio.service;

import com.codefolio.dto.ProjectDto;
import com.codefolio.entity.Project;
import com.codefolio.entity.Task;
import com.codefolio.entity.User;
import com.codefolio.repostiory.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final TaskService taskService;

    @Transactional
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public void assignProjectToUser(UUID projectId, UUID userId) {
        User user = userService.findById(userId);
        Project project = getProjectById(projectId);

        user.getWorkList().add(project);
    }

    public ProjectDto projectPreview(UUID projectId, UUID userId) {
        User user = userService.findById(userId);
        Project project = getProjectById(projectId);

        List<Task> tasks = taskService.getTasksForUser(project.getId(), user.getId(), project.getCreatedBy());
        return ProjectDto.map(project, tasks);
    }

    public ProjectDto getProject(UUID projectId, UUID userId) {
        Project project = getProjectById(projectId);
        List<Task> tasks = taskService.findByCreatorId(project.getCreatedBy(), userId);
        return ProjectDto.map(project, tasks);
    }

    private Project getProjectById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project Not Found"));
    }
}

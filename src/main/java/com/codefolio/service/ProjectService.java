package com.codefolio.service;

import com.codefolio.dto.project.NewProjectDto;
import com.codefolio.dto.project.ProjectDto;
import com.codefolio.dto.project.ProjectPreviewDto;
import com.codefolio.entity.Project;
import com.codefolio.entity.Task;
import com.codefolio.entity.User;
import com.codefolio.mapper.ProjectMapper;
import com.codefolio.mapper.TaskMapper;
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

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final ProjectMapper projectMapper;

    @Transactional
    public ProjectDto createProject(NewProjectDto newProjectDto, UUID createBy) {
        Timestamp now = Timestamp.from(Instant.now());
        Project project = Project.builder()
                .createdAt(now)
                .updatedAt(now)
                .createdBy(createBy)
                .category(newProjectDto.category())
                .description(newProjectDto.description())
                .title(newProjectDto.title())
                .build();

        projectRepository.save(project);

        if (!CollectionUtils.isEmpty(newProjectDto.tasks())) {
            taskService.createTasks(newProjectDto.tasks(), project.getId(), createBy);
        }

        return projectMapper.map(project);
    }

    public List<ProjectDto> getAllProjects() {
        return projectMapper.map(projectRepository.findAll());
    }

    @Transactional
    public void assignProjectToUser(UUID projectId, UUID userId) {
        User user = userService.findById(userId);
        Project project = getProjectById(projectId);

        user.getWorkList().add(project);
    }

    public ProjectPreviewDto projectPreview(UUID projectId, UUID userId) {
        User user = userService.findById(userId);
        Project project = getProjectById(projectId);

        List<Task> tasks = taskService.getTasksForUser(project.getId(), user.getId(), project.getCreatedBy());
        return projectMapper.map(project, tasks);
    }

    private Project getProjectById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project Not Found"));
    }
}

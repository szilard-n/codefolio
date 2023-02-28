package com.codefolio.service;

import com.codefolio.dto.task.TaskDto;
import com.codefolio.entity.Task;
import com.codefolio.entity.User;
import com.codefolio.repostiory.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final AuthService authService;

    protected List<Task> getTasksForUser(UUID projectId, UUID workerId, UUID creatorId) {
        return taskRepository.findByCreatorOrWorkerId(projectId, workerId, creatorId);
    }

    @Transactional
    public void createTasks(List<TaskDto> taskDtos, UUID projectId) {
        final User loggedInUser = authService.getAuthenticatedUser();
        createTasks(taskDtos, projectId, loggedInUser.getId());
    }

    @Transactional
    public void createTasks(List<TaskDto> taskDtos, UUID projectId, UUID createdBy) {
        List<Task> newTasks = taskDtos.stream()
                .map(taskDto -> Task.builder()
                        .title(taskDto.title())
                        .description(taskDto.description())
                        .index(taskDto.index())
                        .projectId(projectId)
                        .createdBy(createdBy)
                        .build())
                .toList();
        taskRepository.saveAll(newTasks);
    }
}

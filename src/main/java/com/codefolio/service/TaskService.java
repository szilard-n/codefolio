package com.codefolio.service;

import com.codefolio.entity.Task;
import com.codefolio.repostiory.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getTasksForUser(UUID project, UUID workerId, UUID creatorId) {
        return taskRepository.findByCreatorOrWorkerId(project, workerId, creatorId);
    }

    public List<Task> findByCreatorId(UUID projectId, UUID creatorId) {
        return taskRepository.findByProjectIdAndCreatedBy(projectId, creatorId);
    }

    public void createTasks(Task task, UUID projectId, UUID createdBy) {
        task.setProjectId(projectId);
        task.setCreatedBy(createdBy);
        taskRepository.save(task);
    }
}

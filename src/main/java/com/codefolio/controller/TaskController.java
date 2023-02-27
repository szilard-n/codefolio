package com.codefolio.controller;

import com.codefolio.dto.task.CreateTasksRequest;
import com.codefolio.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/{projectId}")
    public ResponseEntity<Void> createTasks(@RequestBody CreateTasksRequest tasksWrapperDto, @PathVariable UUID projectId) {
        taskService.createTasks(tasksWrapperDto.tasks(), projectId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

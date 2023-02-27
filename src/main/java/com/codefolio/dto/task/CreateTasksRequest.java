package com.codefolio.dto.task;

import java.util.List;

public record CreateTasksRequest(List<TaskDto> tasks) {
}

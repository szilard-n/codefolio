package com.codefolio.mapper;

import com.codefolio.dto.task.TaskDto;
import com.codefolio.entity.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {

    TaskDto map(Task task);

    Task map(TaskDto taskDto);

    List<Task> map(List<TaskDto> taskDtos);
}

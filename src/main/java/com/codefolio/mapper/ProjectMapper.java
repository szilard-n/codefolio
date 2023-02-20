package com.codefolio.mapper;

import com.codefolio.dto.project.ProjectDto;
import com.codefolio.dto.project.ProjectPreviewDto;
import com.codefolio.entity.Project;
import com.codefolio.entity.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = TaskMapper.class)
public interface ProjectMapper {

    ProjectDto map(Project project);

    List<ProjectDto> map(List<Project> projects);

    ProjectPreviewDto map(Project project, List<Task> tasks);
}

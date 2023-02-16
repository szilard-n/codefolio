package com.codefolio.service;

import com.codefolio.entity.Project;
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
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new RuntimeException("Project Not Found"));

        user.getWorkList().add(project);
    }
}

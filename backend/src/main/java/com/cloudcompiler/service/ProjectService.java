package com.cloudcompiler.service;

import com.cloudcompiler.dto.FileDtos;
import com.cloudcompiler.dto.ProjectDtos;
import com.cloudcompiler.entity.Project;
import com.cloudcompiler.entity.SourceFile;
import com.cloudcompiler.entity.User;
import com.cloudcompiler.exception.ResourceNotFoundException;
import com.cloudcompiler.repository.ProjectRepository;
import com.cloudcompiler.repository.SourceFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SourceFileRepository sourceFileRepository;
    private final AuthService authService;

    public ProjectService(ProjectRepository projectRepository, SourceFileRepository sourceFileRepository, AuthService authService) {
        this.projectRepository = projectRepository;
        this.sourceFileRepository = sourceFileRepository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<ProjectDtos.ProjectResponse> getUserProjects() {
        User user = authService.getCurrentAuthenticatedUser();
        if (user == null) {
            return List.of();
        }
        return projectRepository.findByUserOrderByUpdatedAtDesc(user).stream()
                .map(this::mapToProjectResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProjectDtos.ProjectResponse getProjectById(UUID projectId) {
        User user = authService.getCurrentAuthenticatedUser();
        Project project = projectRepository.findByIdAndUser(projectId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return mapToProjectResponse(project);
    }

    @Transactional
    public ProjectDtos.ProjectResponse createProject(ProjectDtos.ProjectRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        if (user == null) {
            throw new ResourceNotFoundException("User must be logged in to create projects.");
        }

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .build();

        Project saved = projectRepository.save(project);

        // Add a default main.c file
        SourceFile defaultFile = SourceFile.builder()
                .name("main.c")
                .language("c")
                .content("""
                        // Sample C-like Program
                        int main() {
                            int a = 10;
                            int b = 20;
                            int c = a + b * 2;
                            
                            if (c > 30) {
                                print(c);
                            } else {
                                print(0);
                            }
                            
                            return c;
                        }
                        """)
                .project(saved)
                .build();

        sourceFileRepository.save(defaultFile);
        saved.setFiles(List.of(defaultFile));

        return mapToProjectResponse(saved);
    }

    @Transactional
    public ProjectDtos.ProjectResponse updateProject(UUID projectId, ProjectDtos.ProjectRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        Project project = projectRepository.findByIdAndUser(projectId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        project.setName(request.getName());
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }

        return mapToProjectResponse(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(UUID projectId) {
        User user = authService.getCurrentAuthenticatedUser();
        Project project = projectRepository.findByIdAndUser(projectId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        projectRepository.delete(project);
    }

    public ProjectDtos.ProjectResponse mapToProjectResponse(Project project) {
        List<FileDtos.FileResponse> fileResponses = new ArrayList<>();
        if (project.getFiles() != null) {
            fileResponses = project.getFiles().stream()
                    .map(f -> FileDtos.FileResponse.builder()
                            .id(f.getId())
                            .projectId(project.getId())
                            .name(f.getName())
                            .content(f.getContent())
                            .language(f.getLanguage())
                            .createdAt(f.getCreatedAt())
                            .updatedAt(f.getUpdatedAt())
                            .build())
                    .collect(Collectors.toList());
        }

        return ProjectDtos.ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .files(fileResponses)
                .build();
    }
}

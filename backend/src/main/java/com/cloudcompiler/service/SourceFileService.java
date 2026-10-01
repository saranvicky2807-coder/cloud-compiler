package com.cloudcompiler.service;

import com.cloudcompiler.dto.FileDtos;
import com.cloudcompiler.entity.Project;
import com.cloudcompiler.entity.SourceFile;
import com.cloudcompiler.entity.User;
import com.cloudcompiler.exception.ResourceNotFoundException;
import com.cloudcompiler.repository.ProjectRepository;
import com.cloudcompiler.repository.SourceFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SourceFileService {

    private final SourceFileRepository sourceFileRepository;
    private final ProjectRepository projectRepository;
    private final AuthService authService;

    public SourceFileService(SourceFileRepository sourceFileRepository, ProjectRepository projectRepository, AuthService authService) {
        this.sourceFileRepository = sourceFileRepository;
        this.projectRepository = projectRepository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<FileDtos.FileResponse> getProjectFiles(UUID projectId) {
        User user = authService.getCurrentAuthenticatedUser();
        Project project = projectRepository.findByIdAndUser(projectId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        return sourceFileRepository.findByProjectOrderByCreatedAtAsc(project).stream()
                .map(this::mapToFileResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FileDtos.FileResponse createFile(UUID projectId, FileDtos.FileRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        Project project = projectRepository.findByIdAndUser(projectId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        SourceFile file = SourceFile.builder()
                .name(request.getName())
                .content(request.getContent() != null ? request.getContent() : "")
                .language(request.getLanguage() != null ? request.getLanguage() : "c")
                .project(project)
                .build();

        return mapToFileResponse(sourceFileRepository.save(file));
    }

    @Transactional
    public FileDtos.FileResponse updateFileContent(UUID fileId, FileDtos.FileContentUpdateRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        SourceFile file = sourceFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + fileId));

        if (!file.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("File not found or unauthorized: " + fileId);
        }

        file.setContent(request.getContent());
        return mapToFileResponse(sourceFileRepository.save(file));
    }

    @Transactional
    public FileDtos.FileResponse renameFile(UUID fileId, FileDtos.FileRequest request) {
        User user = authService.getCurrentAuthenticatedUser();
        SourceFile file = sourceFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + fileId));

        if (!file.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("File not found or unauthorized: " + fileId);
        }

        file.setName(request.getName());
        if (request.getLanguage() != null) {
            file.setLanguage(request.getLanguage());
        }
        return mapToFileResponse(sourceFileRepository.save(file));
    }

    @Transactional
    public void deleteFile(UUID fileId) {
        User user = authService.getCurrentAuthenticatedUser();
        SourceFile file = sourceFileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found: " + fileId));

        if (!file.getProject().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("File not found or unauthorized: " + fileId);
        }

        sourceFileRepository.delete(file);
    }

    private FileDtos.FileResponse mapToFileResponse(SourceFile file) {
        return FileDtos.FileResponse.builder()
                .id(file.getId())
                .projectId(file.getProject().getId())
                .name(file.getName())
                .content(file.getContent())
                .language(file.getLanguage())
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .build();
    }
}

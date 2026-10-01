package com.cloudcompiler.service;

import com.cloudcompiler.compiler.CompilerEngine;
import com.cloudcompiler.compiler.dto.CompilerRequest;
import com.cloudcompiler.compiler.dto.CompilerResponse;
import com.cloudcompiler.entity.CompilationHistory;
import com.cloudcompiler.entity.Project;
import com.cloudcompiler.entity.SourceFile;
import com.cloudcompiler.entity.User;
import com.cloudcompiler.repository.CompilationHistoryRepository;
import com.cloudcompiler.repository.ProjectRepository;
import com.cloudcompiler.repository.SourceFileRepository;
import org.springframework.stereotype.Service;

@Service
public class CompilerService {

    private final CompilerEngine compilerEngine;
    private final CompilationHistoryRepository historyRepository;
    private final ProjectRepository projectRepository;
    private final SourceFileRepository sourceFileRepository;
    private final AuthService authService;

    public CompilerService(CompilerEngine compilerEngine, CompilationHistoryRepository historyRepository, ProjectRepository projectRepository, SourceFileRepository sourceFileRepository, AuthService authService) {
        this.compilerEngine = compilerEngine;
        this.historyRepository = historyRepository;
        this.projectRepository = projectRepository;
        this.sourceFileRepository = sourceFileRepository;
        this.authService = authService;
    }

    public CompilerResponse compile(CompilerRequest request) {
        CompilerResponse response = compilerEngine.executePipeline(request);

        try {
            User user = authService.getCurrentAuthenticatedUser();
            if (user != null) {
                String projectName = null;
                if (request.getProjectId() != null) {
                    projectName = projectRepository.findById(request.getProjectId())
                            .map(Project::getName).orElse(null);
                }

                String fileName = request.getFileName();
                if (fileName == null && request.getFileId() != null) {
                    fileName = sourceFileRepository.findById(request.getFileId())
                            .map(SourceFile::getName).orElse(null);
                }

                CompilationHistory history = CompilationHistory.builder()
                        .user(user)
                        .projectId(request.getProjectId())
                        .projectName(projectName != null ? projectName : "Playground")
                        .fileId(request.getFileId())
                        .fileName(fileName != null ? fileName : "code.c")
                        .sourceCode(request.getSourceCode())
                        .success(response.isSuccess())
                        .totalErrors(response.getTotalErrors())
                        .executionTimeMs(response.getTotalExecutionTimeMs())
                        .summary(response.getSummary())
                        .build();

                historyRepository.save(history);
            }
        } catch (Exception ignored) {
            // Ignore history recording failures in playground mode
        }

        return response;
    }
}

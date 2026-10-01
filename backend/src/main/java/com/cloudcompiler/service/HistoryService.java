package com.cloudcompiler.service;

import com.cloudcompiler.dto.HistoryDtos;
import com.cloudcompiler.entity.CompilationHistory;
import com.cloudcompiler.entity.User;
import com.cloudcompiler.exception.ResourceNotFoundException;
import com.cloudcompiler.repository.CompilationHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final CompilationHistoryRepository historyRepository;
    private final AuthService authService;

    public HistoryService(CompilationHistoryRepository historyRepository, AuthService authService) {
        this.historyRepository = historyRepository;
        this.authService = authService;
    }

    @Transactional(readOnly = true)
    public List<HistoryDtos.HistoryResponse> getUserHistory() {
        User user = authService.getCurrentAuthenticatedUser();
        if (user == null) {
            return List.of();
        }

        return historyRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToHistoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistoryDtos.HistoryResponse getHistoryById(UUID historyId) {
        User user = authService.getCurrentAuthenticatedUser();
        CompilationHistory history = historyRepository.findByIdAndUser(historyId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Compilation history not found: " + historyId));

        return mapToHistoryResponse(history);
    }

    @Transactional
    public void deleteHistory(UUID historyId) {
        User user = authService.getCurrentAuthenticatedUser();
        CompilationHistory history = historyRepository.findByIdAndUser(historyId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Compilation history not found: " + historyId));

        historyRepository.delete(history);
    }

    @Transactional
    public void clearUserHistory() {
        User user = authService.getCurrentAuthenticatedUser();
        if (user != null) {
            historyRepository.deleteByUser(user);
        }
    }

    public HistoryDtos.HistoryResponse mapToHistoryResponse(CompilationHistory history) {
        return HistoryDtos.HistoryResponse.builder()
                .id(history.getId())
                .projectId(history.getProjectId())
                .projectName(history.getProjectName())
                .fileId(history.getFileId())
                .fileName(history.getFileName())
                .sourceCode(history.getSourceCode())
                .success(history.isSuccess())
                .totalErrors(history.getTotalErrors())
                .executionTimeMs(history.getExecutionTimeMs())
                .summary(history.getSummary())
                .createdAt(history.getCreatedAt())
                .build();
    }
}

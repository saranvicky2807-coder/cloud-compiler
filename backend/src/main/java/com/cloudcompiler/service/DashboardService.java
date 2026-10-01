package com.cloudcompiler.service;

import com.cloudcompiler.dto.DashboardStatsDto;
import com.cloudcompiler.dto.HistoryDtos;
import com.cloudcompiler.dto.ProjectDtos;
import com.cloudcompiler.entity.CompilationHistory;
import com.cloudcompiler.entity.User;
import com.cloudcompiler.repository.CompilationHistoryRepository;
import com.cloudcompiler.repository.ProjectRepository;
import com.cloudcompiler.repository.SourceFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final SourceFileRepository sourceFileRepository;
    private final CompilationHistoryRepository historyRepository;
    private final AuthService authService;
    private final ProjectService projectService;
    private final HistoryService historyService;

    public DashboardService(ProjectRepository projectRepository, SourceFileRepository sourceFileRepository, CompilationHistoryRepository historyRepository, AuthService authService, ProjectService projectService, HistoryService historyService) {
        this.projectRepository = projectRepository;
        this.sourceFileRepository = sourceFileRepository;
        this.historyRepository = historyRepository;
        this.authService = authService;
        this.projectService = projectService;
        this.historyService = historyService;
    }

    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        User user = authService.getCurrentAuthenticatedUser();
        if (user == null) {
            return DashboardStatsDto.builder().build();
        }

        long totalProjects = projectRepository.countByUser(user);
        long totalFiles = sourceFileRepository.countByProject_User(user);
        long totalCompilations = historyRepository.countByUser(user);
        long successfulCompilations = historyRepository.countByUserAndSuccess(user, true);
        long failedCompilations = totalCompilations - successfulCompilations;

        double successRate = totalCompilations > 0 ? ((double) successfulCompilations / totalCompilations) * 100.0 : 0.0;

        List<CompilationHistory> allHistory = historyRepository.findByUserOrderByCreatedAtDesc(user);
        double avgExecTime = allHistory.stream()
                .mapToLong(CompilationHistory::getExecutionTimeMs)
                .average()
                .orElse(0.0);

        List<ProjectDtos.ProjectResponse> recentProjects = projectRepository.findByUserOrderByUpdatedAtDesc(user).stream()
                .limit(5)
                .map(projectService::mapToProjectResponse)
                .collect(Collectors.toList());

        List<HistoryDtos.HistoryResponse> recentCompilations = allHistory.stream()
                .limit(5)
                .map(historyService::mapToHistoryResponse)
                .collect(Collectors.toList());

        return DashboardStatsDto.builder()
                .totalProjects(totalProjects)
                .totalFiles(totalFiles)
                .totalCompilations(totalCompilations)
                .successfulCompilations(successfulCompilations)
                .failedCompilations(failedCompilations)
                .successRatePercentage(Math.round(successRate * 10.0) / 10.0)
                .averageExecutionTimeMs(Math.round(avgExecTime * 10.0) / 10.0)
                .recentProjects(recentProjects)
                .recentCompilations(recentCompilations)
                .build();
    }
}

package com.cloudcompiler.dto;

import java.util.List;

public class DashboardStatsDto {
    private long totalProjects;
    private long totalFiles;
    private long totalCompilations;
    private long successfulCompilations;
    private long failedCompilations;
    private double successRatePercentage;
    private double averageExecutionTimeMs;
    private List<ProjectDtos.ProjectResponse> recentProjects;
    private List<HistoryDtos.HistoryResponse> recentCompilations;

    public DashboardStatsDto() {}

    public DashboardStatsDto(long totalProjects, long totalFiles, long totalCompilations, long successfulCompilations, long failedCompilations, double successRatePercentage, double averageExecutionTimeMs, List<ProjectDtos.ProjectResponse> recentProjects, List<HistoryDtos.HistoryResponse> recentCompilations) {
        this.totalProjects = totalProjects;
        this.totalFiles = totalFiles;
        this.totalCompilations = totalCompilations;
        this.successfulCompilations = successfulCompilations;
        this.failedCompilations = failedCompilations;
        this.successRatePercentage = successRatePercentage;
        this.averageExecutionTimeMs = averageExecutionTimeMs;
        this.recentProjects = recentProjects;
        this.recentCompilations = recentCompilations;
    }

    public static DashboardStatsDtoBuilder builder() {
        return new DashboardStatsDtoBuilder();
    }

    public static class DashboardStatsDtoBuilder {
        private long totalProjects;
        private long totalFiles;
        private long totalCompilations;
        private long successfulCompilations;
        private long failedCompilations;
        private double successRatePercentage;
        private double averageExecutionTimeMs;
        private List<ProjectDtos.ProjectResponse> recentProjects;
        private List<HistoryDtos.HistoryResponse> recentCompilations;

        public DashboardStatsDtoBuilder totalProjects(long totalProjects) { this.totalProjects = totalProjects; return this; }
        public DashboardStatsDtoBuilder totalFiles(long totalFiles) { this.totalFiles = totalFiles; return this; }
        public DashboardStatsDtoBuilder totalCompilations(long totalCompilations) { this.totalCompilations = totalCompilations; return this; }
        public DashboardStatsDtoBuilder successfulCompilations(long successfulCompilations) { this.successfulCompilations = successfulCompilations; return this; }
        public DashboardStatsDtoBuilder failedCompilations(long failedCompilations) { this.failedCompilations = failedCompilations; return this; }
        public DashboardStatsDtoBuilder successRatePercentage(double successRatePercentage) { this.successRatePercentage = successRatePercentage; return this; }
        public DashboardStatsDtoBuilder averageExecutionTimeMs(double averageExecutionTimeMs) { this.averageExecutionTimeMs = averageExecutionTimeMs; return this; }
        public DashboardStatsDtoBuilder recentProjects(List<ProjectDtos.ProjectResponse> recentProjects) { this.recentProjects = recentProjects; return this; }
        public DashboardStatsDtoBuilder recentCompilations(List<HistoryDtos.HistoryResponse> recentCompilations) { this.recentCompilations = recentCompilations; return this; }

        public DashboardStatsDto build() {
            return new DashboardStatsDto(totalProjects, totalFiles, totalCompilations, successfulCompilations, failedCompilations, successRatePercentage, averageExecutionTimeMs, recentProjects, recentCompilations);
        }
    }

    public long getTotalProjects() { return totalProjects; }
    public void setTotalProjects(long totalProjects) { this.totalProjects = totalProjects; }
    public long getTotalFiles() { return totalFiles; }
    public void setTotalFiles(long totalFiles) { this.totalFiles = totalFiles; }
    public long getTotalCompilations() { return totalCompilations; }
    public void setTotalCompilations(long totalCompilations) { this.totalCompilations = totalCompilations; }
    public long getSuccessfulCompilations() { return successfulCompilations; }
    public void setSuccessfulCompilations(long successfulCompilations) { this.successfulCompilations = successfulCompilations; }
    public long getFailedCompilations() { return failedCompilations; }
    public void setFailedCompilations(long failedCompilations) { this.failedCompilations = failedCompilations; }
    public double getSuccessRatePercentage() { return successRatePercentage; }
    public void setSuccessRatePercentage(double successRatePercentage) { this.successRatePercentage = successRatePercentage; }
    public double getAverageExecutionTimeMs() { return averageExecutionTimeMs; }
    public void setAverageExecutionTimeMs(double averageExecutionTimeMs) { this.averageExecutionTimeMs = averageExecutionTimeMs; }
    public List<ProjectDtos.ProjectResponse> getRecentProjects() { return recentProjects; }
    public void setRecentProjects(List<ProjectDtos.ProjectResponse> recentProjects) { this.recentProjects = recentProjects; }
    public List<HistoryDtos.HistoryResponse> getRecentCompilations() { return recentCompilations; }
    public void setRecentCompilations(List<HistoryDtos.HistoryResponse> recentCompilations) { this.recentCompilations = recentCompilations; }
}

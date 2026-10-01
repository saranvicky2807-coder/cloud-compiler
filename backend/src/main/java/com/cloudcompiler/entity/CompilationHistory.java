package com.cloudcompiler.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "compilation_history")
public class CompilationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private UUID projectId;
    private String projectName;

    private UUID fileId;
    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String sourceCode;

    @Column(nullable = false)
    private boolean success;

    private int totalErrors;
    private long executionTimeMs;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public CompilationHistory() {}

    public CompilationHistory(UUID id, User user, UUID projectId, String projectName, UUID fileId, String fileName, String sourceCode, boolean success, int totalErrors, long executionTimeMs, String summary, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.projectId = projectId;
        this.projectName = projectName;
        this.fileId = fileId;
        this.fileName = fileName;
        this.sourceCode = sourceCode;
        this.success = success;
        this.totalErrors = totalErrors;
        this.executionTimeMs = executionTimeMs;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    public static CompilationHistoryBuilder builder() {
        return new CompilationHistoryBuilder();
    }

    public static class CompilationHistoryBuilder {
        private UUID id;
        private User user;
        private UUID projectId;
        private String projectName;
        private UUID fileId;
        private String fileName;
        private String sourceCode;
        private boolean success;
        private int totalErrors;
        private long executionTimeMs;
        private String summary;
        private LocalDateTime createdAt;

        public CompilationHistoryBuilder id(UUID id) { this.id = id; return this; }
        public CompilationHistoryBuilder user(User user) { this.user = user; return this; }
        public CompilationHistoryBuilder projectId(UUID projectId) { this.projectId = projectId; return this; }
        public CompilationHistoryBuilder projectName(String projectName) { this.projectName = projectName; return this; }
        public CompilationHistoryBuilder fileId(UUID fileId) { this.fileId = fileId; return this; }
        public CompilationHistoryBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public CompilationHistoryBuilder sourceCode(String sourceCode) { this.sourceCode = sourceCode; return this; }
        public CompilationHistoryBuilder success(boolean success) { this.success = success; return this; }
        public CompilationHistoryBuilder totalErrors(int totalErrors) { this.totalErrors = totalErrors; return this; }
        public CompilationHistoryBuilder executionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; return this; }
        public CompilationHistoryBuilder summary(String summary) { this.summary = summary; return this; }
        public CompilationHistoryBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public CompilationHistory build() {
            return new CompilationHistory(id, user, projectId, projectName, fileId, fileName, sourceCode, success, totalErrors, executionTimeMs, summary, createdAt);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public UUID getFileId() { return fileId; }
    public void setFileId(UUID fileId) { this.fileId = fileId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public int getTotalErrors() { return totalErrors; }
    public void setTotalErrors(int totalErrors) { this.totalErrors = totalErrors; }
    public long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

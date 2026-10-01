package com.cloudcompiler.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class HistoryDtos {

    public static class HistoryResponse {
        private UUID id;
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

        public HistoryResponse() {}

        public HistoryResponse(UUID id, UUID projectId, String projectName, UUID fileId, String fileName, String sourceCode, boolean success, int totalErrors, long executionTimeMs, String summary, LocalDateTime createdAt) {
            this.id = id;
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

        public static HistoryResponseBuilder builder() {
            return new HistoryResponseBuilder();
        }

        public static class HistoryResponseBuilder {
            private UUID id;
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

            public HistoryResponseBuilder id(UUID id) { this.id = id; return this; }
            public HistoryResponseBuilder projectId(UUID projectId) { this.projectId = projectId; return this; }
            public HistoryResponseBuilder projectName(String projectName) { this.projectName = projectName; return this; }
            public HistoryResponseBuilder fileId(UUID fileId) { this.fileId = fileId; return this; }
            public HistoryResponseBuilder fileName(String fileName) { this.fileName = fileName; return this; }
            public HistoryResponseBuilder sourceCode(String sourceCode) { this.sourceCode = sourceCode; return this; }
            public HistoryResponseBuilder success(boolean success) { this.success = success; return this; }
            public HistoryResponseBuilder totalErrors(int totalErrors) { this.totalErrors = totalErrors; return this; }
            public HistoryResponseBuilder executionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; return this; }
            public HistoryResponseBuilder summary(String summary) { this.summary = summary; return this; }
            public HistoryResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public HistoryResponse build() {
                return new HistoryResponse(id, projectId, projectName, fileId, fileName, sourceCode, success, totalErrors, executionTimeMs, summary, createdAt);
            }
        }

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
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
}

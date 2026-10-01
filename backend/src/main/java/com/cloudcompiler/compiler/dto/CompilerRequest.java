package com.cloudcompiler.compiler.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CompilerRequest {
    @NotNull(message = "Source code cannot be null")
    private String sourceCode;
    private String fileName;
    private UUID projectId;
    private UUID fileId;
    private String targetStage;

    public CompilerRequest() {}

    public CompilerRequest(String sourceCode, String fileName, UUID projectId, UUID fileId, String targetStage) {
        this.sourceCode = sourceCode;
        this.fileName = fileName;
        this.projectId = projectId;
        this.fileId = fileId;
        this.targetStage = targetStage;
    }

    public static CompilerRequestBuilder builder() {
        return new CompilerRequestBuilder();
    }

    public static class CompilerRequestBuilder {
        private String sourceCode;
        private String fileName;
        private UUID projectId;
        private UUID fileId;
        private String targetStage;

        public CompilerRequestBuilder sourceCode(String sourceCode) { this.sourceCode = sourceCode; return this; }
        public CompilerRequestBuilder fileName(String fileName) { this.fileName = fileName; return this; }
        public CompilerRequestBuilder projectId(UUID projectId) { this.projectId = projectId; return this; }
        public CompilerRequestBuilder fileId(UUID fileId) { this.fileId = fileId; return this; }
        public CompilerRequestBuilder targetStage(String targetStage) { this.targetStage = targetStage; return this; }

        public CompilerRequest build() {
            return new CompilerRequest(sourceCode, fileName, projectId, fileId, targetStage);
        }
    }

    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public UUID getProjectId() { return projectId; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
    public UUID getFileId() { return fileId; }
    public void setFileId(UUID fileId) { this.fileId = fileId; }
    public String getTargetStage() { return targetStage; }
    public void setTargetStage(String targetStage) { this.targetStage = targetStage; }
}

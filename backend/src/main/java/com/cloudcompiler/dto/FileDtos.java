package com.cloudcompiler.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

public class FileDtos {

    public static class FileRequest {
        @NotBlank(message = "File name is required")
        private String name;
        private String content;
        private String language;

        public FileRequest() {}

        public FileRequest(String name, String content, String language) {
            this.name = name;
            this.content = content;
            this.language = language;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
    }

    public static class FileContentUpdateRequest {
        private String content;

        public FileContentUpdateRequest() {}
        public FileContentUpdateRequest(String content) { this.content = content; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    public static class FileResponse {
        private UUID id;
        private UUID projectId;
        private String name;
        private String content;
        private String language;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public FileResponse() {}

        public FileResponse(UUID id, UUID projectId, String name, String content, String language, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.projectId = projectId;
            this.name = name;
            this.content = content;
            this.language = language;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public static FileResponseBuilder builder() {
            return new FileResponseBuilder();
        }

        public static class FileResponseBuilder {
            private UUID id;
            private UUID projectId;
            private String name;
            private String content;
            private String language;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public FileResponseBuilder id(UUID id) { this.id = id; return this; }
            public FileResponseBuilder projectId(UUID projectId) { this.projectId = projectId; return this; }
            public FileResponseBuilder name(String name) { this.name = name; return this; }
            public FileResponseBuilder content(String content) { this.content = content; return this; }
            public FileResponseBuilder language(String language) { this.language = language; return this; }
            public FileResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public FileResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public FileResponse build() {
                return new FileResponse(id, projectId, name, content, language, createdAt, updatedAt);
            }
        }

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public UUID getProjectId() { return projectId; }
        public void setProjectId(UUID projectId) { this.projectId = projectId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    }
}

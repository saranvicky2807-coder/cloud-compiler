package com.cloudcompiler.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProjectDtos {

    public static class ProjectRequest {
        @NotBlank(message = "Project name is required")
        private String name;
        private String description;

        public ProjectRequest() {}

        public ProjectRequest(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class ProjectResponse {
        private UUID id;
        private String name;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<FileDtos.FileResponse> files;

        public ProjectResponse() {}

        public ProjectResponse(UUID id, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt, List<FileDtos.FileResponse> files) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.files = files;
        }

        public static ProjectResponseBuilder builder() {
            return new ProjectResponseBuilder();
        }

        public static class ProjectResponseBuilder {
            private UUID id;
            private String name;
            private String description;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;
            private List<FileDtos.FileResponse> files;

            public ProjectResponseBuilder id(UUID id) { this.id = id; return this; }
            public ProjectResponseBuilder name(String name) { this.name = name; return this; }
            public ProjectResponseBuilder description(String description) { this.description = description; return this; }
            public ProjectResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public ProjectResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
            public ProjectResponseBuilder files(List<FileDtos.FileResponse> files) { this.files = files; return this; }

            public ProjectResponse build() {
                return new ProjectResponse(id, name, description, createdAt, updatedAt, files);
            }
        }

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
        public List<FileDtos.FileResponse> getFiles() { return files; }
        public void setFiles(List<FileDtos.FileResponse> files) { this.files = files; }
    }
}

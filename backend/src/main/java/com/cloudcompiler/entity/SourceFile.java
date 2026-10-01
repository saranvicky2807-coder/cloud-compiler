package com.cloudcompiler.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "source_files")
public class SourceFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String language = "c";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public SourceFile() {}

    public SourceFile(UUID id, String name, String content, String language, Project project, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.language = language != null ? language : "c";
        this.project = project;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SourceFileBuilder builder() {
        return new SourceFileBuilder();
    }

    public static class SourceFileBuilder {
        private UUID id;
        private String name;
        private String content;
        private String language = "c";
        private Project project;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public SourceFileBuilder id(UUID id) { this.id = id; return this; }
        public SourceFileBuilder name(String name) { this.name = name; return this; }
        public SourceFileBuilder content(String content) { this.content = content; return this; }
        public SourceFileBuilder language(String language) { this.language = language; return this; }
        public SourceFileBuilder project(Project project) { this.project = project; return this; }
        public SourceFileBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SourceFileBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public SourceFile build() {
            return new SourceFile(id, name, content, language, project, createdAt, updatedAt);
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

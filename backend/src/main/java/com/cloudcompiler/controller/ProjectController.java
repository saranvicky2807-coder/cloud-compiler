package com.cloudcompiler.controller;

import com.cloudcompiler.dto.ProjectDtos;
import com.cloudcompiler.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Endpoints for managing user compiler projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Get all projects for the authenticated user")
    public ResponseEntity<List<ProjectDtos.ProjectResponse>> getUserProjects() {
        return ResponseEntity.ok(projectService.getUserProjects());
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    public ResponseEntity<ProjectDtos.ProjectResponse> createProject(@Valid @RequestBody ProjectDtos.ProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID")
    public ResponseEntity<ProjectDtos.ProjectResponse> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project metadata")
    public ResponseEntity<ProjectDtos.ProjectResponse> updateProject(
            @PathVariable UUID id,
            @Valid @RequestBody ProjectDtos.ProjectRequest request
    ) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}

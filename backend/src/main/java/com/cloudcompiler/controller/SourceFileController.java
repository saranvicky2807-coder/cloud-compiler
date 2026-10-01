package com.cloudcompiler.controller;

import com.cloudcompiler.dto.FileDtos;
import com.cloudcompiler.service.SourceFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Source Files", description = "Endpoints for managing source code files in projects")
public class SourceFileController {

    private final SourceFileService sourceFileService;

    public SourceFileController(SourceFileService sourceFileService) {
        this.sourceFileService = sourceFileService;
    }

    @GetMapping("/api/projects/{projectId}/files")
    @Operation(summary = "Get all files for a project")
    public ResponseEntity<List<FileDtos.FileResponse>> getProjectFiles(@PathVariable UUID projectId) {
        return ResponseEntity.ok(sourceFileService.getProjectFiles(projectId));
    }

    @PostMapping("/api/projects/{projectId}/files")
    @Operation(summary = "Create a new source file inside a project")
    public ResponseEntity<FileDtos.FileResponse> createFile(
            @PathVariable UUID projectId,
            @Valid @RequestBody FileDtos.FileRequest request
    ) {
        return ResponseEntity.ok(sourceFileService.createFile(projectId, request));
    }

    @PutMapping("/api/files/{fileId}")
    @Operation(summary = "Update file content")
    public ResponseEntity<FileDtos.FileResponse> updateFileContent(
            @PathVariable UUID fileId,
            @RequestBody FileDtos.FileContentUpdateRequest request
    ) {
        return ResponseEntity.ok(sourceFileService.updateFileContent(fileId, request));
    }

    @PutMapping("/api/files/{fileId}/rename")
    @Operation(summary = "Rename a source file")
    public ResponseEntity<FileDtos.FileResponse> renameFile(
            @PathVariable UUID fileId,
            @Valid @RequestBody FileDtos.FileRequest request
    ) {
        return ResponseEntity.ok(sourceFileService.renameFile(fileId, request));
    }

    @DeleteMapping("/api/files/{fileId}")
    @Operation(summary = "Delete a source file")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID fileId) {
        sourceFileService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}

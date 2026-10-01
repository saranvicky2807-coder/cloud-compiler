package com.cloudcompiler.controller;

import com.cloudcompiler.dto.HistoryDtos;
import com.cloudcompiler.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/history")
@Tag(name = "Compilation History", description = "Endpoints for viewing and managing compilation runs and history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    @Operation(summary = "Get user compilation history")
    public ResponseEntity<List<HistoryDtos.HistoryResponse>> getUserHistory() {
        return ResponseEntity.ok(historyService.getUserHistory());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get compilation history record by ID")
    public ResponseEntity<HistoryDtos.HistoryResponse> getHistoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(historyService.getHistoryById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete compilation history record")
    public ResponseEntity<Void> deleteHistory(@PathVariable UUID id) {
        historyService.deleteHistory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Clear all compilation history for current user")
    public ResponseEntity<Void> clearAllHistory() {
        historyService.clearUserHistory();
        return ResponseEntity.noContent().build();
    }
}

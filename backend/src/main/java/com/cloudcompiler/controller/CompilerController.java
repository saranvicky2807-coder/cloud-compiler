package com.cloudcompiler.controller;

import com.cloudcompiler.compiler.dto.CompilerRequest;
import com.cloudcompiler.compiler.dto.CompilerResponse;
import com.cloudcompiler.service.CompilerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compiler")
@Tag(name = "Compiler Engine", description = "Endpoints for running lexical, syntax, semantic, and IR compilation pipelines")
public class CompilerController {

    private final CompilerService compilerService;

    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @PostMapping("/compile")
    @Operation(summary = "Run full end-to-end compiler pipeline (Lexer, Parser, AST, Semantic, Symbol Table, TAC, Quadruples, Triples)")
    public ResponseEntity<CompilerResponse> compile(@Valid @RequestBody CompilerRequest request) {
        request.setTargetStage("ALL");
        return ResponseEntity.ok(compilerService.compile(request));
    }

    @PostMapping("/tokenize")
    @Operation(summary = "Run Lexical Analysis and return tokens")
    public ResponseEntity<CompilerResponse> tokenize(@Valid @RequestBody CompilerRequest request) {
        request.setTargetStage("LEXICAL");
        return ResponseEntity.ok(compilerService.compile(request));
    }

    @PostMapping("/parse")
    @Operation(summary = "Run Syntax Analysis and return AST and syntax errors")
    public ResponseEntity<CompilerResponse> parse(@Valid @RequestBody CompilerRequest request) {
        request.setTargetStage("SYNTAX");
        return ResponseEntity.ok(compilerService.compile(request));
    }

    @PostMapping("/analyze")
    @Operation(summary = "Run Semantic Analysis and return Symbol Table and type errors")
    public ResponseEntity<CompilerResponse> analyze(@Valid @RequestBody CompilerRequest request) {
        request.setTargetStage("SEMANTIC");
        return ResponseEntity.ok(compilerService.compile(request));
    }

    @PostMapping("/generate-ir")
    @Operation(summary = "Run Intermediate Code Generation (TAC, Quadruples, Triples)")
    public ResponseEntity<CompilerResponse> generateIr(@Valid @RequestBody CompilerRequest request) {
        request.setTargetStage("IR");
        return ResponseEntity.ok(compilerService.compile(request));
    }
}

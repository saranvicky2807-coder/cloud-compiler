package com.cloudcompiler.compiler;

import com.cloudcompiler.compiler.ast.ProgramNode;
import com.cloudcompiler.compiler.dto.CompilerRequest;
import com.cloudcompiler.compiler.dto.CompilerResponse;
import com.cloudcompiler.compiler.dto.StageLog;
import com.cloudcompiler.compiler.ir.IrGenerator;
import com.cloudcompiler.compiler.lexer.Lexer;
import com.cloudcompiler.compiler.lexer.Token;
import com.cloudcompiler.compiler.lexer.TokenType;
import com.cloudcompiler.compiler.parser.Parser;
import com.cloudcompiler.compiler.parser.SyntaxError;
import com.cloudcompiler.compiler.semantic.SemanticAnalyzer;
import com.cloudcompiler.compiler.semantic.SemanticError;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompilerEngine {

    public CompilerResponse executePipeline(CompilerRequest request) {
        long startTime = System.currentTimeMillis();
        String sourceCode = request.getSourceCode() != null ? request.getSourceCode() : "";
        String targetStage = request.getTargetStage() != null ? request.getTargetStage().toUpperCase() : "ALL";

        List<StageLog> stages = new ArrayList<>();
        List<Token> tokens = new ArrayList<>();
        List<SyntaxError> syntaxErrors = new ArrayList<>();
        List<SemanticError> semanticErrors = new ArrayList<>();
        ProgramNode ast = null;

        // Stage 1: Lexical Analysis
        long lexStart = System.currentTimeMillis();
        Lexer lexer = new Lexer(sourceCode);
        tokens = lexer.tokenize();
        long lexDuration = System.currentTimeMillis() - lexStart;

        int invalidTokenCount = 0;
        for (Token t : tokens) {
            if (t.getType() == TokenType.INVALID) {
                invalidTokenCount++;
            }
        }

        stages.add(StageLog.builder()
                .stageName("Lexical Analysis")
                .description("Tokenizing source stream, identifying keywords, literals, and identifiers")
                .status(invalidTokenCount > 0 ? "WARNING" : "SUCCESS")
                .durationMs(lexDuration)
                .message("Generated " + tokens.size() + " tokens" + (invalidTokenCount > 0 ? " (" + invalidTokenCount + " invalid tokens detected)" : ""))
                .build());

        if ("LEXICAL".equals(targetStage)) {
            return buildResponse(tokens, syntaxErrors, semanticErrors, null, null, stages, startTime);
        }

        // Stage 2: Syntax Analysis & AST Generation
        long parseStart = System.currentTimeMillis();
        Parser parser = new Parser(tokens);
        ast = parser.parse();
        syntaxErrors = parser.getErrors();
        long parseDuration = System.currentTimeMillis() - parseStart;

        stages.add(StageLog.builder()
                .stageName("Syntax Analysis")
                .description("Recursive descent parsing and AST grammar tree construction")
                .status(syntaxErrors.isEmpty() ? "SUCCESS" : "FAILED")
                .durationMs(parseDuration)
                .message(syntaxErrors.isEmpty() ? "Abstract Syntax Tree constructed successfully" : "Encountered " + syntaxErrors.size() + " syntax error(s)")
                .build());

        if ("SYNTAX".equals(targetStage) || !syntaxErrors.isEmpty()) {
            return buildResponse(tokens, syntaxErrors, semanticErrors, ast, null, stages, startTime);
        }

        // Stage 3: Semantic Analysis & Symbol Table Generation
        long semStart = System.currentTimeMillis();
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
        semanticAnalyzer.analyze(ast);
        semanticErrors = semanticAnalyzer.getErrors();
        long semDuration = System.currentTimeMillis() - semStart;

        stages.add(StageLog.builder()
                .stageName("Semantic Analysis")
                .description("Hierarchical scope binding, type checking, and symbol table generation")
                .status(semanticErrors.isEmpty() ? "SUCCESS" : "FAILED")
                .durationMs(semDuration)
                .message(semanticErrors.isEmpty() ? "Type validation passed, generated symbol table" : "Encountered " + semanticErrors.size() + " semantic error(s)")
                .build());

        if ("SEMANTIC".equals(targetStage) || !semanticErrors.isEmpty()) {
            return buildResponse(tokens, syntaxErrors, semanticErrors, ast, semanticAnalyzer, stages, startTime);
        }

        // Stage 4: Intermediate Code Generation (TAC, Quadruples, Triples)
        long irStart = System.currentTimeMillis();
        IrGenerator irGenerator = new IrGenerator();
        irGenerator.generate(ast);
        long irDuration = System.currentTimeMillis() - irStart;

        stages.add(StageLog.builder()
                .stageName("Intermediate Code Generation")
                .description("Translating AST into Three-Address Code, Quadruples, and Triples")
                .status("SUCCESS")
                .durationMs(irDuration)
                .message("Generated " + irGenerator.getTacInstructions().size() + " TAC instructions, " + irGenerator.getQuadruples().size() + " quadruples, " + irGenerator.getTriples().size() + " triples")
                .build());

        CompilerResponse response = buildResponse(tokens, syntaxErrors, semanticErrors, ast, semanticAnalyzer, stages, startTime);
        response.setThreeAddressCode(irGenerator.getTacInstructions());
        response.setQuadruples(irGenerator.getQuadruples());
        response.setTriples(irGenerator.getTriples());
        response.setIndirectTriples(irGenerator.getIndirectTriples());

        StringBuilder rawTac = new StringBuilder();
        for (var tac : irGenerator.getTacInstructions()) {
            rawTac.append(tac.getFormatted()).append("\n");
        }
        response.setTacRaw(rawTac.toString().trim());

        return response;
    }

    private CompilerResponse buildResponse(
            List<Token> tokens,
            List<SyntaxError> syntaxErrors,
            List<SemanticError> semanticErrors,
            ProgramNode ast,
            SemanticAnalyzer semanticAnalyzer,
            List<StageLog> stages,
            long startTime
    ) {
        long totalDuration = System.currentTimeMillis() - startTime;
        int totalErrors = (syntaxErrors != null ? syntaxErrors.size() : 0) + (semanticErrors != null ? semanticErrors.size() : 0);
        boolean success = totalErrors == 0;

        String summary = success
                ? "Compilation completed successfully in " + totalDuration + " ms."
                : "Compilation failed with " + totalErrors + " error(s).";

        return CompilerResponse.builder()
                .success(success)
                .totalErrors(totalErrors)
                .summary(summary)
                .totalExecutionTimeMs(totalDuration)
                .tokens(tokens != null ? tokens : new ArrayList<>())
                .syntaxErrors(syntaxErrors != null ? syntaxErrors : new ArrayList<>())
                .semanticErrors(semanticErrors != null ? semanticErrors : new ArrayList<>())
                .symbolTable(semanticAnalyzer != null ? semanticAnalyzer.getSymbolTable().getAllSymbols() : new ArrayList<>())
                .ast(ast)
                .stages(stages)
                .build();
    }
}

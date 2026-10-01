package com.cloudcompiler.compiler.dto;

import com.cloudcompiler.compiler.ast.ProgramNode;
import com.cloudcompiler.compiler.ir.IndirectTriple;
import com.cloudcompiler.compiler.ir.Quadruple;
import com.cloudcompiler.compiler.ir.TacInstruction;
import com.cloudcompiler.compiler.ir.Triple;
import com.cloudcompiler.compiler.lexer.Token;
import com.cloudcompiler.compiler.parser.SyntaxError;
import com.cloudcompiler.compiler.semantic.SemanticError;
import com.cloudcompiler.compiler.symboltable.Symbol;

import java.util.ArrayList;
import java.util.List;

public class CompilerResponse {
    private boolean success;
    private int totalErrors;
    private String summary;
    private long totalExecutionTimeMs;
    private List<Token> tokens = new ArrayList<>();
    private List<SyntaxError> syntaxErrors = new ArrayList<>();
    private List<SemanticError> semanticErrors = new ArrayList<>();
    private List<Symbol> symbolTable = new ArrayList<>();
    private ProgramNode ast;
    private List<TacInstruction> threeAddressCode = new ArrayList<>();
    private String tacRaw;
    private List<Quadruple> quadruples = new ArrayList<>();
    private List<Triple> triples = new ArrayList<>();
    private List<IndirectTriple> indirectTriples = new ArrayList<>();
    private List<StageLog> stages = new ArrayList<>();

    public CompilerResponse() {}

    public CompilerResponse(boolean success, int totalErrors, String summary, long totalExecutionTimeMs, List<Token> tokens, List<SyntaxError> syntaxErrors, List<SemanticError> semanticErrors, List<Symbol> symbolTable, ProgramNode ast, List<TacInstruction> threeAddressCode, String tacRaw, List<Quadruple> quadruples, List<Triple> triples, List<IndirectTriple> indirectTriples, List<StageLog> stages) {
        this.success = success;
        this.totalErrors = totalErrors;
        this.summary = summary;
        this.totalExecutionTimeMs = totalExecutionTimeMs;
        this.tokens = tokens != null ? tokens : new ArrayList<>();
        this.syntaxErrors = syntaxErrors != null ? syntaxErrors : new ArrayList<>();
        this.semanticErrors = semanticErrors != null ? semanticErrors : new ArrayList<>();
        this.symbolTable = symbolTable != null ? symbolTable : new ArrayList<>();
        this.ast = ast;
        this.threeAddressCode = threeAddressCode != null ? threeAddressCode : new ArrayList<>();
        this.tacRaw = tacRaw;
        this.quadruples = quadruples != null ? quadruples : new ArrayList<>();
        this.triples = triples != null ? triples : new ArrayList<>();
        this.indirectTriples = indirectTriples != null ? indirectTriples : new ArrayList<>();
        this.stages = stages != null ? stages : new ArrayList<>();
    }

    public static CompilerResponseBuilder builder() {
        return new CompilerResponseBuilder();
    }

    public static class CompilerResponseBuilder {
        private boolean success;
        private int totalErrors;
        private String summary;
        private long totalExecutionTimeMs;
        private List<Token> tokens = new ArrayList<>();
        private List<SyntaxError> syntaxErrors = new ArrayList<>();
        private List<SemanticError> semanticErrors = new ArrayList<>();
        private List<Symbol> symbolTable = new ArrayList<>();
        private ProgramNode ast;
        private List<TacInstruction> threeAddressCode = new ArrayList<>();
        private String tacRaw;
        private List<Quadruple> quadruples = new ArrayList<>();
        private List<Triple> triples = new ArrayList<>();
        private List<IndirectTriple> indirectTriples = new ArrayList<>();
        private List<StageLog> stages = new ArrayList<>();

        public CompilerResponseBuilder success(boolean success) { this.success = success; return this; }
        public CompilerResponseBuilder totalErrors(int totalErrors) { this.totalErrors = totalErrors; return this; }
        public CompilerResponseBuilder summary(String summary) { this.summary = summary; return this; }
        public CompilerResponseBuilder totalExecutionTimeMs(long totalExecutionTimeMs) { this.totalExecutionTimeMs = totalExecutionTimeMs; return this; }
        public CompilerResponseBuilder tokens(List<Token> tokens) { this.tokens = tokens; return this; }
        public CompilerResponseBuilder syntaxErrors(List<SyntaxError> syntaxErrors) { this.syntaxErrors = syntaxErrors; return this; }
        public CompilerResponseBuilder semanticErrors(List<SemanticError> semanticErrors) { this.semanticErrors = semanticErrors; return this; }
        public CompilerResponseBuilder symbolTable(List<Symbol> symbolTable) { this.symbolTable = symbolTable; return this; }
        public CompilerResponseBuilder ast(ProgramNode ast) { this.ast = ast; return this; }
        public CompilerResponseBuilder threeAddressCode(List<TacInstruction> threeAddressCode) { this.threeAddressCode = threeAddressCode; return this; }
        public CompilerResponseBuilder tacRaw(String tacRaw) { this.tacRaw = tacRaw; return this; }
        public CompilerResponseBuilder quadruples(List<Quadruple> quadruples) { this.quadruples = quadruples; return this; }
        public CompilerResponseBuilder triples(List<Triple> triples) { this.triples = triples; return this; }
        public CompilerResponseBuilder indirectTriples(List<IndirectTriple> indirectTriples) { this.indirectTriples = indirectTriples; return this; }
        public CompilerResponseBuilder stages(List<StageLog> stages) { this.stages = stages; return this; }

        public CompilerResponse build() {
            return new CompilerResponse(success, totalErrors, summary, totalExecutionTimeMs, tokens, syntaxErrors, semanticErrors, symbolTable, ast, threeAddressCode, tacRaw, quadruples, triples, indirectTriples, stages);
        }
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public int getTotalErrors() { return totalErrors; }
    public void setTotalErrors(int totalErrors) { this.totalErrors = totalErrors; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public long getTotalExecutionTimeMs() { return totalExecutionTimeMs; }
    public void setTotalExecutionTimeMs(long totalExecutionTimeMs) { this.totalExecutionTimeMs = totalExecutionTimeMs; }
    public List<Token> getTokens() { return tokens; }
    public void setTokens(List<Token> tokens) { this.tokens = tokens; }
    public List<SyntaxError> getSyntaxErrors() { return syntaxErrors; }
    public void setSyntaxErrors(List<SyntaxError> syntaxErrors) { this.syntaxErrors = syntaxErrors; }
    public List<SemanticError> getSemanticErrors() { return semanticErrors; }
    public void setSemanticErrors(List<SemanticError> semanticErrors) { this.semanticErrors = semanticErrors; }
    public List<Symbol> getSymbolTable() { return symbolTable; }
    public void setSymbolTable(List<Symbol> symbolTable) { this.symbolTable = symbolTable; }
    public ProgramNode getAst() { return ast; }
    public void setAst(ProgramNode ast) { this.ast = ast; }
    public List<TacInstruction> getThreeAddressCode() { return threeAddressCode; }
    public void setThreeAddressCode(List<TacInstruction> threeAddressCode) { this.threeAddressCode = threeAddressCode; }
    public String getTacRaw() { return tacRaw; }
    public void setTacRaw(String tacRaw) { this.tacRaw = tacRaw; }
    public List<Quadruple> getQuadruples() { return quadruples; }
    public void setQuadruples(List<Quadruple> quadruples) { this.quadruples = quadruples; }
    public List<Triple> getTriples() { return triples; }
    public void setTriples(List<Triple> triples) { this.triples = triples; }
    public List<IndirectTriple> getIndirectTriples() { return indirectTriples; }
    public void setIndirectTriples(List<IndirectTriple> indirectTriples) { this.indirectTriples = indirectTriples; }
    public List<StageLog> getStages() { return stages; }
    public void setStages(List<StageLog> stages) { this.stages = stages; }
}

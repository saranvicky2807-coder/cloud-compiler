package com.cloudcompiler.compiler;

import com.cloudcompiler.compiler.dto.CompilerRequest;
import com.cloudcompiler.compiler.dto.CompilerResponse;
import com.cloudcompiler.compiler.lexer.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CompilerEngineTest {

    private CompilerEngine compilerEngine;

    @BeforeEach
    void setUp() {
        compilerEngine = new CompilerEngine();
    }

    @Test
    @DisplayName("Should successfully compile complete valid program with TAC and Quadruples")
    void testValidProgramCompilation() {
        String code = """
                int calculate(int x, int y) {
                    int result = x * y + 10;
                    return result;
                }
                
                int main() {
                    int a = 5;
                    int b = 4;
                    int res = calculate(a, b);
                    if (res > 20) {
                        print(res);
                    } else {
                        print(0);
                    }
                    return 0;
                }
                """;

        CompilerResponse response = compilerEngine.executePipeline(CompilerRequest.builder()
                .sourceCode(code)
                .build());

        assertTrue(response.isSuccess(), "Compilation should succeed for valid code");
        assertEquals(0, response.getTotalErrors(), "Should have 0 errors");
        assertNotNull(response.getAst(), "AST should be generated");
        assertFalse(response.getTokens().isEmpty(), "Tokens list should not be empty");
        assertFalse(response.getSymbolTable().isEmpty(), "Symbol table should contain symbols");
        assertFalse(response.getThreeAddressCode().isEmpty(), "TAC instructions should be generated");
        assertFalse(response.getQuadruples().isEmpty(), "Quadruples should be generated");
        assertFalse(response.getTriples().isEmpty(), "Triples should be generated");
        assertEquals(4, response.getStages().size(), "Should have 4 pipeline stages logged");
    }

    @Test
    @DisplayName("Should detect and report lexical invalid tokens without crashing")
    void testLexicalInvalidTokens() {
        String code = "int a = 10 @# 20;";

        CompilerResponse response = compilerEngine.executePipeline(CompilerRequest.builder()
                .sourceCode(code)
                .build());

        boolean hasInvalidToken = response.getTokens().stream()
                .anyMatch(t -> t.getType() == TokenType.INVALID);
        assertTrue(hasInvalidToken, "Should detect invalid tokens like '@'");
    }

    @Test
    @DisplayName("Should detect syntax error for missing semicolon and invalid syntax")
    void testSyntaxErrorDetection() {
        String code = """
                int main() {
                    int a = 10
                    int b = 20;
                    return 0;
                }
                """;

        CompilerResponse response = compilerEngine.executePipeline(CompilerRequest.builder()
                .sourceCode(code)
                .build());

        assertFalse(response.isSuccess(), "Compilation should fail on syntax error");
        assertFalse(response.getSyntaxErrors().isEmpty(), "Syntax errors should be populated");
        assertTrue(response.getSyntaxErrors().stream().anyMatch(e -> e.getMessage().contains("Expected ';'") || e.getLine() > 0));
    }

    @Test
    @DisplayName("Should detect undeclared variable semantic error")
    void testUndeclaredVariableSemanticError() {
        String code = """
                int main() {
                    a = 10;
                    return 0;
                }
                """;

        CompilerResponse response = compilerEngine.executePipeline(CompilerRequest.builder()
                .sourceCode(code)
                .build());

        assertFalse(response.isSuccess(), "Compilation should fail on undeclared variable");
        assertFalse(response.getSemanticErrors().isEmpty(), "Semantic errors should be populated");
        assertTrue(response.getSemanticErrors().stream()
                .anyMatch(e -> "UNDECLARED_VARIABLE".equals(e.getErrorType()) && "a".equals(e.getIdentifier())));
    }

    @Test
    @DisplayName("Should detect duplicate variable declaration in same scope")
    void testDuplicateVariableSemanticError() {
        String code = """
                int main() {
                    int a = 10;
                    int a = 20;
                    return a;
                }
                """;

        CompilerResponse response = compilerEngine.executePipeline(CompilerRequest.builder()
                .sourceCode(code)
                .build());

        assertFalse(response.isSuccess(), "Compilation should fail on duplicate declaration");
        assertTrue(response.getSemanticErrors().stream()
                .anyMatch(e -> "DUPLICATE_DECLARATION".equals(e.getErrorType()) && "a".equals(e.getIdentifier())));
    }

    @Test
    @DisplayName("Should detect type mismatch semantic error")
    void testTypeMismatchSemanticError() {
        String code = """
                int main() {
                    int age = "hello";
                    return 0;
                }
                """;

        CompilerResponse response = compilerEngine.executePipeline(CompilerRequest.builder()
                .sourceCode(code)
                .build());

        assertFalse(response.isSuccess(), "Compilation should fail on type mismatch");
        assertTrue(response.getSemanticErrors().stream()
                .anyMatch(e -> "TYPE_MISMATCH".equals(e.getErrorType())));
    }

    @Test
    @DisplayName("Should generate Three-Address Code with temporary variables and labels for expressions and loops")
    void testIntermediateCodeGeneration() {
        String code = """
                int main() {
                    int i = 0;
                    int sum = 0;
                    while (i < 10) {
                        sum = sum + i;
                        i = i + 1;
                    }
                    return sum;
                }
                """;

        CompilerResponse response = compilerEngine.executePipeline(CompilerRequest.builder()
                .sourceCode(code)
                .build());

        assertTrue(response.isSuccess());
        assertNotNull(response.getTacRaw());
        assertTrue(response.getTacRaw().contains("IF_FALSE"), "TAC should contain conditional jump");
        assertTrue(response.getTacRaw().contains("GOTO"), "TAC should contain loop back-jump");
        assertTrue(response.getTacRaw().contains("L"), "TAC should contain generated labels");
        assertTrue(response.getTacRaw().contains("t"), "TAC should contain generated temporaries");
        assertFalse(response.getQuadruples().isEmpty(), "Quadruples should be generated");
        assertFalse(response.getTriples().isEmpty(), "Triples should be generated");
    }
}

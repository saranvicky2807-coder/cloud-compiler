package com.cloudcompiler.compiler.semantic;

import com.cloudcompiler.compiler.ast.*;
import com.cloudcompiler.compiler.symboltable.Scope;
import com.cloudcompiler.compiler.symboltable.Symbol;
import com.cloudcompiler.compiler.symboltable.SymbolTable;

import java.util.*;

public class SemanticAnalyzer implements AstVisitor<String> {
    private final SymbolTable symbolTable;
    private Scope currentScope;
    private final List<SemanticError> errors = new ArrayList<>();
    private String currentFunctionReturnType = null;
    private int blockCounter = 0;

    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
        this.currentScope = symbolTable.getRootScope();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public List<SemanticError> getErrors() {
        return errors;
    }

    public void analyze(ProgramNode program) {
        if (program != null) {
            program.accept(this);
        }
    }

    @Override
    public String visit(ProgramNode node) {
        for (AstNode decl : node.getDeclarations()) {
            decl.accept(this);
        }
        return "void";
    }

    @Override
    public String visit(FunctionDeclNode node) {
        // Register function in current (global) scope
        List<String> paramTypes = new ArrayList<>();
        for (ParameterNode p : node.getParameters()) {
            paramTypes.add(p.getType());
        }

        Symbol funcSymbol = Symbol.builder()
                .name(node.getName())
                .type(node.getReturnType())
                .scopeName(currentScope.getName())
                .scopeLevel(currentScope.getLevel())
                .line(node.getLine())
                .column(node.getColumn())
                .isFunction(true)
                .parameterTypes(paramTypes)
                .sizeBytes(0)
                .isInitialized(true)
                .build();

        if (!currentScope.define(funcSymbol)) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("DUPLICATE_FUNCTION")
                    .identifier(node.getName())
                    .message("Function '" + node.getName() + "' is already declared in this scope.")
                    .build());
        } else {
            symbolTable.addSymbol(funcSymbol);
        }

        // Enter function scope
        Scope funcScope = new Scope("function:" + node.getName(), currentScope.getLevel() + 1, currentScope);
        Scope previousScope = currentScope;
        String prevFuncReturnType = currentFunctionReturnType;
        currentScope = funcScope;
        currentFunctionReturnType = node.getReturnType();

        for (ParameterNode param : node.getParameters()) {
            param.accept(this);
        }

        if (node.getBody() != null) {
            // Body statements inside function scope
            for (AstNode stmt : node.getBody().getStatements()) {
                stmt.accept(this);
            }
        }

        currentScope = previousScope;
        currentFunctionReturnType = prevFuncReturnType;
        return node.getReturnType();
    }

    @Override
    public String visit(ParameterNode node) {
        Symbol paramSymbol = Symbol.builder()
                .name(node.getName())
                .type(node.getType())
                .line(node.getLine())
                .column(node.getColumn())
                .isFunction(false)
                .sizeBytes(SymbolTable.getSizeBytesForType(node.getType()))
                .isInitialized(true)
                .build();

        if (!currentScope.define(paramSymbol)) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("DUPLICATE_PARAMETER")
                    .identifier(node.getName())
                    .message("Parameter '" + node.getName() + "' is duplicated in function declaration.")
                    .build());
        } else {
            symbolTable.addSymbol(paramSymbol);
        }
        return node.getType();
    }

    @Override
    public String visit(VarDeclNode node) {
        String initType = null;
        if (node.getInitializer() != null) {
            initType = node.getInitializer().accept(this);
        }

        Symbol varSymbol = Symbol.builder()
                .name(node.getName())
                .type(node.getType())
                .line(node.getLine())
                .column(node.getColumn())
                .isFunction(false)
                .sizeBytes(SymbolTable.getSizeBytesForType(node.getType()))
                .isInitialized(node.getInitializer() != null)
                .build();

        if (!currentScope.define(varSymbol)) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("DUPLICATE_DECLARATION")
                    .identifier(node.getName())
                    .message("Variable '" + node.getName() + "' is already declared in scope '" + currentScope.getName() + "'.")
                    .build());
        } else {
            symbolTable.addSymbol(varSymbol);
        }

        // Type check initializer
        if (initType != null && !isAssignable(node.getType(), initType)) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("TYPE_MISMATCH")
                    .identifier(node.getName())
                    .expectedType(node.getType())
                    .foundType(initType)
                    .message("Type mismatch: Cannot initialize variable '" + node.getName() + "' of type '" + node.getType() + "' with value of type '" + initType + "'.")
                    .build());
        }

        return node.getType();
    }

    @Override
    public String visit(BlockNode node) {
        Scope blockScope = new Scope("block_" + (++blockCounter), currentScope.getLevel() + 1, currentScope);
        Scope prev = currentScope;
        currentScope = blockScope;

        for (AstNode stmt : node.getStatements()) {
            stmt.accept(this);
        }

        currentScope = prev;
        return "void";
    }

    @Override
    public String visit(IfStmtNode node) {
        if (node.getCondition() != null) {
            String condType = node.getCondition().accept(this);
            if (condType != null && !condType.equals("bool") && !condType.equals("int")) {
                errors.add(SemanticError.builder()
                        .line(node.getLine())
                        .column(node.getColumn())
                        .errorType("INVALID_CONDITION_TYPE")
                        .expectedType("bool")
                        .foundType(condType)
                        .message("If condition must evaluate to a boolean or integer, found: '" + condType + "'.")
                        .build());
            }
        }
        if (node.getThenBranch() != null) node.getThenBranch().accept(this);
        if (node.getElseBranch() != null) node.getElseBranch().accept(this);
        return "void";
    }

    @Override
    public String visit(WhileStmtNode node) {
        if (node.getCondition() != null) {
            String condType = node.getCondition().accept(this);
            if (condType != null && !condType.equals("bool") && !condType.equals("int")) {
                errors.add(SemanticError.builder()
                        .line(node.getLine())
                        .column(node.getColumn())
                        .errorType("INVALID_CONDITION_TYPE")
                        .expectedType("bool")
                        .foundType(condType)
                        .message("While condition must evaluate to a boolean or integer, found: '" + condType + "'.")
                        .build());
            }
        }
        if (node.getBody() != null) node.getBody().accept(this);
        return "void";
    }

    @Override
    public String visit(ForStmtNode node) {
        Scope forScope = new Scope("for_loop_" + (++blockCounter), currentScope.getLevel() + 1, currentScope);
        Scope prev = currentScope;
        currentScope = forScope;

        if (node.getInit() != null) node.getInit().accept(this);
        if (node.getCondition() != null) {
            String condType = node.getCondition().accept(this);
            if (condType != null && !condType.equals("bool") && !condType.equals("int")) {
                errors.add(SemanticError.builder()
                        .line(node.getLine())
                        .column(node.getColumn())
                        .errorType("INVALID_CONDITION_TYPE")
                        .expectedType("bool")
                        .foundType(condType)
                        .message("For condition must evaluate to boolean or integer, found: '" + condType + "'.")
                        .build());
            }
        }
        if (node.getUpdate() != null) node.getUpdate().accept(this);
        if (node.getBody() != null) node.getBody().accept(this);

        currentScope = prev;
        return "void";
    }

    @Override
    public String visit(ReturnStmtNode node) {
        String valType = "void";
        if (node.getValue() != null) {
            valType = node.getValue().accept(this);
        }

        if (currentFunctionReturnType != null) {
            if (!isAssignable(currentFunctionReturnType, valType)) {
                errors.add(SemanticError.builder()
                        .line(node.getLine())
                        .column(node.getColumn())
                        .errorType("RETURN_TYPE_MISMATCH")
                        .expectedType(currentFunctionReturnType)
                        .foundType(valType)
                        .message("Return type mismatch: Function expects return type '" + currentFunctionReturnType + "' but returned '" + valType + "'.")
                        .build());
            }
        }
        return valType;
    }

    @Override
    public String visit(PrintStmtNode node) {
        if (node.getExpression() != null) {
            node.getExpression().accept(this);
        }
        return "void";
    }

    @Override
    public String visit(ReadStmtNode node) {
        Symbol sym = currentScope.resolve(node.getTargetVar());
        if (sym == null) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("UNDECLARED_VARIABLE")
                    .identifier(node.getTargetVar())
                    .message("Cannot read into undeclared variable '" + node.getTargetVar() + "'.")
                    .build());
        } else {
            sym.setInitialized(true);
        }
        return "void";
    }

    @Override
    public String visit(ExpressionStmtNode node) {
        if (node.getExpression() != null) {
            return node.getExpression().accept(this);
        }
        return "void";
    }

    @Override
    public String visit(AssignStmtNode node) {
        Symbol sym = currentScope.resolve(node.getName());
        if (sym == null) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("UNDECLARED_VARIABLE")
                    .identifier(node.getName())
                    .message("Semantic Error: Variable '" + node.getName() + "' is not declared.")
                    .build());
            return "unknown";
        }

        sym.setReferenceCount(sym.getReferenceCount() + 1);
        sym.setInitialized(true);

        String valType = "unknown";
        if (node.getValue() != null) {
            valType = node.getValue().accept(this);
        }

        if (!valType.equals("unknown") && !isAssignable(sym.getType(), valType)) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("TYPE_MISMATCH")
                    .identifier(node.getName())
                    .expectedType(sym.getType())
                    .foundType(valType)
                    .message("Type mismatch. Cannot assign '" + valType + "' to variable '" + node.getName() + "' of type '" + sym.getType() + "'.")
                    .build());
        }

        return sym.getType();
    }

    @Override
    public String visit(BinaryExprNode node) {
        String leftType = node.getLeft() != null ? node.getLeft().accept(this) : "unknown";
        String rightType = node.getRight() != null ? node.getRight().accept(this) : "unknown";
        String op = node.getOperator();

        if (leftType.equals("unknown") || rightType.equals("unknown")) {
            return "unknown";
        }

        // Relational operators
        if (op.equals("==") || op.equals("!=") || op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">=")) {
            return "bool";
        }

        // Logical operators
        if (op.equals("&&") || op.equals("||")) {
            if (!leftType.equals("bool") || !rightType.equals("bool")) {
                errors.add(SemanticError.builder()
                        .line(node.getLine())
                        .column(node.getColumn())
                        .errorType("INVALID_LOGICAL_OPERANDS")
                        .message("Logical operator '" + op + "' requires boolean operands, found '" + leftType + "' and '" + rightType + "'.")
                        .build());
            }
            return "bool";
        }

        // Arithmetic operators
        if (op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/") || op.equals("%")) {
            if (leftType.equals("string") && op.equals("+")) {
                return "string";
            }
            if (leftType.equals("float") || rightType.equals("float")) {
                return "float";
            }
            if (leftType.equals("int") && rightType.equals("int")) {
                return "int";
            }

            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("INVALID_ARITHMETIC_OPERANDS")
                    .message("Arithmetic operator '" + op + "' cannot be applied to types '" + leftType + "' and '" + rightType + "'.")
                    .build());
            return "int";
        }

        return "unknown";
    }

    @Override
    public String visit(UnaryExprNode node) {
        String operandType = node.getOperand() != null ? node.getOperand().accept(this) : "unknown";
        String op = node.getOperator();

        if (op.equals("!")) {
            if (!operandType.equals("bool") && !operandType.equals("unknown")) {
                errors.add(SemanticError.builder()
                        .line(node.getLine())
                        .column(node.getColumn())
                        .errorType("INVALID_UNARY_OPERAND")
                        .expectedType("bool")
                        .foundType(operandType)
                        .message("Logical NOT operator '!' requires boolean operand, found: '" + operandType + "'.")
                        .build());
            }
            return "bool";
        }

        if (op.equals("-") || op.equals("+") || op.equals("++") || op.equals("--")) {
            if (!operandType.equals("int") && !operandType.equals("float") && !operandType.equals("unknown")) {
                errors.add(SemanticError.builder()
                        .line(node.getLine())
                        .column(node.getColumn())
                        .errorType("INVALID_UNARY_OPERAND")
                        .expectedType("numeric")
                        .foundType(operandType)
                        .message("Unary operator '" + op + "' requires numeric operand, found: '" + operandType + "'.")
                        .build());
            }
            return operandType;
        }

        return operandType;
    }

    @Override
    public String visit(LiteralExprNode node) {
        return node.getValueType();
    }

    @Override
    public String visit(IdentifierExprNode node) {
        Symbol sym = currentScope.resolve(node.getName());
        if (sym == null) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("UNDECLARED_VARIABLE")
                    .identifier(node.getName())
                    .message("Semantic Error: Variable '" + node.getName() + "' is not declared.")
                    .build());
            return "unknown";
        }

        sym.setReferenceCount(sym.getReferenceCount() + 1);
        return sym.getType();
    }

    @Override
    public String visit(FunctionCallExprNode node) {
        Symbol sym = currentScope.resolve(node.getName());
        if (sym == null || !sym.isFunction()) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("UNDECLARED_FUNCTION")
                    .identifier(node.getName())
                    .message("Semantic Error: Function '" + node.getName() + "' is not declared.")
                    .build());
            return "unknown";
        }

        sym.setReferenceCount(sym.getReferenceCount() + 1);
        List<String> expectedParams = sym.getParameterTypes() != null ? sym.getParameterTypes() : Collections.emptyList();
        List<AstNode> args = node.getArguments();

        if (args.size() != expectedParams.size()) {
            errors.add(SemanticError.builder()
                    .line(node.getLine())
                    .column(node.getColumn())
                    .errorType("ARGUMENT_COUNT_MISMATCH")
                    .identifier(node.getName())
                    .message("Function '" + node.getName() + "' expects " + expectedParams.size() + " argument(s) but got " + args.size() + ".")
                    .build());
        } else {
            for (int i = 0; i < args.size(); i++) {
                String argType = args.get(i).accept(this);
                String expected = expectedParams.get(i);
                if (!isAssignable(expected, argType)) {
                    errors.add(SemanticError.builder()
                            .line(node.getLine())
                            .column(node.getColumn())
                            .errorType("ARGUMENT_TYPE_MISMATCH")
                            .identifier(node.getName())
                            .expectedType(expected)
                            .foundType(argType)
                            .message("Argument " + (i + 1) + " of function '" + node.getName() + "' expects type '" + expected + "', got '" + argType + "'.")
                            .build());
                }
            }
        }

        return sym.getType();
    }

    private boolean isAssignable(String targetType, String sourceType) {
        if (targetType == null || sourceType == null) return true;
        if (targetType.equals(sourceType)) return true;
        if (targetType.equals("float") && sourceType.equals("int")) return true; // implicit int -> float widening
        return false;
    }
}

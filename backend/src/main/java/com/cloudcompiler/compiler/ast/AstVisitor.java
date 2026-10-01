package com.cloudcompiler.compiler.ast;

public interface AstVisitor<R> {
    R visit(ProgramNode node);
    R visit(FunctionDeclNode node);
    R visit(ParameterNode node);
    R visit(VarDeclNode node);
    R visit(BlockNode node);
    R visit(IfStmtNode node);
    R visit(WhileStmtNode node);
    R visit(ForStmtNode node);
    R visit(ReturnStmtNode node);
    R visit(PrintStmtNode node);
    R visit(ReadStmtNode node);
    R visit(ExpressionStmtNode node);
    R visit(AssignStmtNode node);
    R visit(BinaryExprNode node);
    R visit(UnaryExprNode node);
    R visit(LiteralExprNode node);
    R visit(IdentifierExprNode node);
    R visit(FunctionCallExprNode node);
}

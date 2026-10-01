package com.cloudcompiler.compiler.ast;

public class IfStmtNode extends AstNode {
    private AstNode condition;
    private AstNode thenBranch;
    private AstNode elseBranch;

    public IfStmtNode(AstNode condition, AstNode thenBranch, AstNode elseBranch, int line, int column) {
        super("IfStatement", "If Statement", line, column);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
        if (condition != null) addChild(condition);
        if (thenBranch != null) addChild(thenBranch);
        if (elseBranch != null) addChild(elseBranch);
    }

    public AstNode getCondition() {
        return condition;
    }

    public void setCondition(AstNode condition) {
        this.condition = condition;
    }

    public AstNode getThenBranch() {
        return thenBranch;
    }

    public void setThenBranch(AstNode thenBranch) {
        this.thenBranch = thenBranch;
    }

    public AstNode getElseBranch() {
        return elseBranch;
    }

    public void setElseBranch(AstNode elseBranch) {
        this.elseBranch = elseBranch;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

package com.cloudcompiler.compiler.ast;

public class WhileStmtNode extends AstNode {
    private AstNode condition;
    private AstNode body;

    public WhileStmtNode(AstNode condition, AstNode body, int line, int column) {
        super("WhileStatement", "While Loop", line, column);
        this.condition = condition;
        this.body = body;
        if (condition != null) addChild(condition);
        if (body != null) addChild(body);
    }

    public AstNode getCondition() {
        return condition;
    }

    public void setCondition(AstNode condition) {
        this.condition = condition;
    }

    public AstNode getBody() {
        return body;
    }

    public void setBody(AstNode body) {
        this.body = body;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

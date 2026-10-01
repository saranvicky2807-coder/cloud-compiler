package com.cloudcompiler.compiler.ast;

public class PrintStmtNode extends AstNode {
    private AstNode expression;

    public PrintStmtNode(AstNode expression, int line, int column) {
        super("PrintStatement", "Print", line, column);
        this.expression = expression;
        if (expression != null) {
            addChild(expression);
        }
    }

    public AstNode getExpression() {
        return expression;
    }

    public void setExpression(AstNode expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

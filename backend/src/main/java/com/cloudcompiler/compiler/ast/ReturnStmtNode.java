package com.cloudcompiler.compiler.ast;

public class ReturnStmtNode extends AstNode {
    private AstNode value;

    public ReturnStmtNode(AstNode value, int line, int column) {
        super("ReturnStatement", "Return", line, column);
        this.value = value;
        if (value != null) {
            addChild(value);
        }
    }

    public AstNode getValue() {
        return value;
    }

    public void setValue(AstNode value) {
        this.value = value;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

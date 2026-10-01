package com.cloudcompiler.compiler.ast;

public class IdentifierExprNode extends AstNode {
    private String name;

    public IdentifierExprNode(String name, int line, int column) {
        super("Identifier", "Identifier: " + name, line, column);
        this.name = name;
        setAttribute("name", name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

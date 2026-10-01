package com.cloudcompiler.compiler.ast;

public class ParameterNode extends AstNode {
    private String type;
    private String name;

    public ParameterNode(String type, String name, int line, int column) {
        super("Parameter", type + " " + name, line, column);
        this.type = type;
        this.name = name;
        setAttribute("type", type);
        setAttribute("name", name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

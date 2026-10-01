package com.cloudcompiler.compiler.ast;

public class VarDeclNode extends AstNode {
    private String type;
    private String name;
    private AstNode initializer;

    public VarDeclNode(String type, String name, AstNode initializer, int line, int column) {
        super("VariableDeclaration", "VarDecl: " + type + " " + name, line, column);
        this.type = type;
        this.name = name;
        this.initializer = initializer;
        setAttribute("type", type);
        setAttribute("name", name);
        if (initializer != null) {
            this.addChild(initializer);
        }
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

    public AstNode getInitializer() {
        return initializer;
    }

    public void setInitializer(AstNode initializer) {
        this.initializer = initializer;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

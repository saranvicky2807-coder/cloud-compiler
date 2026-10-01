package com.cloudcompiler.compiler.ast;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallExprNode extends AstNode {
    private String name;
    private List<AstNode> arguments = new ArrayList<>();

    public FunctionCallExprNode(String name, int line, int column) {
        super("FunctionCall", "Call: " + name + "()", line, column);
        this.name = name;
        setAttribute("name", name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AstNode> getArguments() {
        return arguments;
    }

    public void setArguments(List<AstNode> arguments) {
        this.arguments = arguments;
    }

    public void addArgument(AstNode arg) {
        this.arguments.add(arg);
        this.addChild(arg);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

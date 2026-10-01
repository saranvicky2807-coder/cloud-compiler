package com.cloudcompiler.compiler.ast;

import java.util.ArrayList;
import java.util.List;

public class FunctionDeclNode extends AstNode {
    private String returnType;
    private String name;
    private List<ParameterNode> parameters = new ArrayList<>();
    private BlockNode body;

    public FunctionDeclNode(String returnType, String name, int line, int column) {
        super("FunctionDeclaration", "Function: " + returnType + " " + name, line, column);
        this.returnType = returnType;
        this.name = name;
        setAttribute("returnType", returnType);
        setAttribute("name", name);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterNode> parameters) {
        this.parameters = parameters;
    }

    public BlockNode getBody() {
        return body;
    }

    public void setBody(BlockNode body) {
        this.body = body;
    }

    public void addParameter(ParameterNode param) {
        this.parameters.add(param);
        this.addChild(param);
    }

    public void setBodyNode(BlockNode body) {
        this.body = body;
        this.addChild(body);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

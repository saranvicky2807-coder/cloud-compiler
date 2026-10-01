package com.cloudcompiler.compiler.ast;

public class AssignStmtNode extends AstNode {
    private String name;
    private String operator;
    private AstNode value;

    public AssignStmtNode(String name, String operator, AstNode value, int line, int column) {
        super("Assignment", name + " " + operator + " <expr>", line, column);
        this.name = name;
        this.operator = operator;
        this.value = value;
        setAttribute("target", name);
        setAttribute("operator", operator);
        if (value != null) {
            addChild(value);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

package com.cloudcompiler.compiler.ast;

public class UnaryExprNode extends AstNode {
    private String operator;
    private AstNode operand;
    private boolean isPrefix;

    public UnaryExprNode(String operator, AstNode operand, boolean isPrefix, int line, int column) {
        super("UnaryExpression", "UnaryOp: " + operator, line, column);
        this.operator = operator;
        this.operand = operand;
        this.isPrefix = isPrefix;
        setAttribute("operator", operator);
        setAttribute("isPrefix", isPrefix);
        if (operand != null) addChild(operand);
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public AstNode getOperand() {
        return operand;
    }

    public void setOperand(AstNode operand) {
        this.operand = operand;
    }

    public boolean isPrefix() {
        return isPrefix;
    }

    public void setPrefix(boolean prefix) {
        isPrefix = prefix;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

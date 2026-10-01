package com.cloudcompiler.compiler.ast;

public class BinaryExprNode extends AstNode {
    private AstNode left;
    private String operator;
    private AstNode right;

    public BinaryExprNode(AstNode left, String operator, AstNode right, int line, int column) {
        super("BinaryExpression", "Op: " + operator, line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
        setAttribute("operator", operator);
        if (left != null) addChild(left);
        if (right != null) addChild(right);
    }

    public AstNode getLeft() {
        return left;
    }

    public void setLeft(AstNode left) {
        this.left = left;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public AstNode getRight() {
        return right;
    }

    public void setRight(AstNode right) {
        this.right = right;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

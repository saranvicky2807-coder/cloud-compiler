package com.cloudcompiler.compiler.ast;

public class ReadStmtNode extends AstNode {
    private String targetVar;

    public ReadStmtNode(String targetVar, int line, int column) {
        super("ReadStatement", "Read into " + targetVar, line, column);
        this.targetVar = targetVar;
        setAttribute("targetVar", targetVar);
    }

    public String getTargetVar() {
        return targetVar;
    }

    public void setTargetVar(String targetVar) {
        this.targetVar = targetVar;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

package com.cloudcompiler.compiler.ast;

import java.util.ArrayList;
import java.util.List;

public class BlockNode extends AstNode {
    private List<AstNode> statements = new ArrayList<>();

    public BlockNode(int line, int column) {
        super("Block", "Block Scope", line, column);
    }

    public List<AstNode> getStatements() {
        return statements;
    }

    public void setStatements(List<AstNode> statements) {
        this.statements = statements;
    }

    public void addStatement(AstNode stmt) {
        this.statements.add(stmt);
        this.addChild(stmt);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

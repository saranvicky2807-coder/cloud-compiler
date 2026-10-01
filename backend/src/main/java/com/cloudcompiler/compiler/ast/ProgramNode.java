package com.cloudcompiler.compiler.ast;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends AstNode {
    private List<AstNode> declarations = new ArrayList<>();

    public ProgramNode(int line, int column) {
        super("Program", "Program", line, column);
    }

    public List<AstNode> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<AstNode> declarations) {
        this.declarations = declarations;
    }

    public void addDeclaration(AstNode decl) {
        this.declarations.add(decl);
        this.addChild(decl);
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

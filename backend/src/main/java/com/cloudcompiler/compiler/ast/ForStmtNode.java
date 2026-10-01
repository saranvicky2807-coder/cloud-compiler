package com.cloudcompiler.compiler.ast;

public class ForStmtNode extends AstNode {
    private AstNode init;
    private AstNode condition;
    private AstNode update;
    private AstNode body;

    public ForStmtNode(AstNode init, AstNode condition, AstNode update, AstNode body, int line, int column) {
        super("ForStatement", "For Loop", line, column);
        this.init = init;
        this.condition = condition;
        this.update = update;
        this.body = body;
        if (init != null) addChild(init);
        if (condition != null) addChild(condition);
        if (update != null) addChild(update);
        if (body != null) addChild(body);
    }

    public AstNode getInit() {
        return init;
    }

    public void setInit(AstNode init) {
        this.init = init;
    }

    public AstNode getCondition() {
        return condition;
    }

    public void setCondition(AstNode condition) {
        this.condition = condition;
    }

    public AstNode getUpdate() {
        return update;
    }

    public void setUpdate(AstNode update) {
        this.update = update;
    }

    public AstNode getBody() {
        return body;
    }

    public void setBody(AstNode body) {
        this.body = body;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

package com.cloudcompiler.compiler.ast;

public class LiteralExprNode extends AstNode {
    private String valueType;
    private Object value;
    private String raw;

    public LiteralExprNode(String valueType, Object value, String raw, int line, int column) {
        super("Literal", String.valueOf(value), line, column);
        this.valueType = valueType;
        this.value = value;
        this.raw = raw;
        setAttribute("type", valueType);
        setAttribute("value", value);
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    @Override
    public <R> R accept(AstVisitor<R> visitor) {
        return visitor.visit(this);
    }
}

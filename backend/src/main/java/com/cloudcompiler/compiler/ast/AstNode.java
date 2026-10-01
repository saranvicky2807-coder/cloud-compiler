package com.cloudcompiler.compiler.ast;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AstNode {
    private String id = UUID.randomUUID().toString().substring(0, 8);
    private String nodeType;
    private String label;
    private int line;
    private int column;
    private Map<String, Object> attributes = new LinkedHashMap<>();
    private List<AstNode> children = new ArrayList<>();

    public AstNode(String nodeType, String label, int line, int column) {
        this.nodeType = nodeType;
        this.label = label;
        this.line = line;
        this.column = column;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<AstNode> getChildren() {
        return children;
    }

    public void setChildren(List<AstNode> children) {
        this.children = children;
    }

    public void addChild(AstNode child) {
        if (child != null) {
            this.children.add(child);
        }
    }

    public void setAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    public abstract <R> R accept(AstVisitor<R> visitor);
}

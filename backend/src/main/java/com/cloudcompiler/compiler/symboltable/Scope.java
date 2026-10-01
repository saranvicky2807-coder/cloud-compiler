package com.cloudcompiler.compiler.symboltable;

import java.util.*;

public class Scope {
    private final String name;
    private final int level;
    private final Scope parent;
    private final Map<String, Symbol> symbols = new LinkedHashMap<>();
    private final List<Scope> children = new ArrayList<>();
    private int currentOffset = 0;

    public Scope(String name, int level, Scope parent) {
        this.name = name;
        this.level = level;
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
    }

    public boolean define(Symbol symbol) {
        if (symbols.containsKey(symbol.getName())) {
            return false;
        }
        symbol.setScopeName(this.name);
        symbol.setScopeLevel(this.level);
        symbol.setMemoryOffset(currentOffset);
        currentOffset += symbol.getSizeBytes();
        symbols.put(symbol.getName(), symbol);
        return true;
    }

    public Symbol resolve(String name) {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        }
        if (parent != null) {
            return parent.resolve(name);
        }
        return null;
    }

    public Symbol resolveLocal(String name) {
        return symbols.get(name);
    }

    public String getName() { return name; }
    public int getLevel() { return level; }
    public Scope getParent() { return parent; }
    public Map<String, Symbol> getSymbols() { return symbols; }
    public List<Scope> getChildren() { return children; }
    public int getCurrentOffset() { return currentOffset; }
    public void setCurrentOffset(int currentOffset) { this.currentOffset = currentOffset; }
}

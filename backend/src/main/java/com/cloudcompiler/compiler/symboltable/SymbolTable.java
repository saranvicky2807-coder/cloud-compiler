package com.cloudcompiler.compiler.symboltable;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    private final Scope rootScope;
    private final List<Symbol> allSymbols = new ArrayList<>();

    public SymbolTable() {
        this.rootScope = new Scope("global", 0, null);
    }

    public Scope getRootScope() {
        return rootScope;
    }

    public void addSymbol(Symbol symbol) {
        allSymbols.add(symbol);
    }

    public List<Symbol> getAllSymbols() {
        return allSymbols;
    }

    public static int getSizeBytesForType(String type) {
        if (type == null) return 4;
        return switch (type.toLowerCase()) {
            case "int" -> 4;
            case "float" -> 8;
            case "char" -> 1;
            case "bool" -> 1;
            case "string" -> 8;
            case "void" -> 0;
            default -> 4;
        };
    }
}

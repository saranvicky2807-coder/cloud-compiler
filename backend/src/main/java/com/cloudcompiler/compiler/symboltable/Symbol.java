package com.cloudcompiler.compiler.symboltable;

import java.util.List;

public class Symbol {
    private String name;
    private String type;
    private String scopeName;
    private int scopeLevel;
    private int line;
    private int column;
    private boolean isFunction;
    private List<String> parameterTypes;
    private int sizeBytes;
    private int memoryOffset;
    private int referenceCount;
    private boolean isInitialized;

    public Symbol() {}

    public Symbol(String name, String type, String scopeName, int scopeLevel, int line, int column, boolean isFunction, List<String> parameterTypes, int sizeBytes, int memoryOffset, int referenceCount, boolean isInitialized) {
        this.name = name;
        this.type = type;
        this.scopeName = scopeName;
        this.scopeLevel = scopeLevel;
        this.line = line;
        this.column = column;
        this.isFunction = isFunction;
        this.parameterTypes = parameterTypes;
        this.sizeBytes = sizeBytes;
        this.memoryOffset = memoryOffset;
        this.referenceCount = referenceCount;
        this.isInitialized = isInitialized;
    }

    public static SymbolBuilder builder() {
        return new SymbolBuilder();
    }

    public static class SymbolBuilder {
        private String name;
        private String type;
        private String scopeName;
        private int scopeLevel;
        private int line;
        private int column;
        private boolean isFunction;
        private List<String> parameterTypes;
        private int sizeBytes;
        private int memoryOffset;
        private int referenceCount;
        private boolean isInitialized;

        public SymbolBuilder name(String name) { this.name = name; return this; }
        public SymbolBuilder type(String type) { this.type = type; return this; }
        public SymbolBuilder scopeName(String scopeName) { this.scopeName = scopeName; return this; }
        public SymbolBuilder scopeLevel(int scopeLevel) { this.scopeLevel = scopeLevel; return this; }
        public SymbolBuilder line(int line) { this.line = line; return this; }
        public SymbolBuilder column(int column) { this.column = column; return this; }
        public SymbolBuilder isFunction(boolean isFunction) { this.isFunction = isFunction; return this; }
        public SymbolBuilder parameterTypes(List<String> parameterTypes) { this.parameterTypes = parameterTypes; return this; }
        public SymbolBuilder sizeBytes(int sizeBytes) { this.sizeBytes = sizeBytes; return this; }
        public SymbolBuilder memoryOffset(int memoryOffset) { this.memoryOffset = memoryOffset; return this; }
        public SymbolBuilder referenceCount(int referenceCount) { this.referenceCount = referenceCount; return this; }
        public SymbolBuilder isInitialized(boolean isInitialized) { this.isInitialized = isInitialized; return this; }

        public Symbol build() {
            return new Symbol(name, type, scopeName, scopeLevel, line, column, isFunction, parameterTypes, sizeBytes, memoryOffset, referenceCount, isInitialized);
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getScopeName() { return scopeName; }
    public void setScopeName(String scopeName) { this.scopeName = scopeName; }
    public int getScopeLevel() { return scopeLevel; }
    public void setScopeLevel(int scopeLevel) { this.scopeLevel = scopeLevel; }
    public int getLine() { return line; }
    public void setLine(int line) { this.line = line; }
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
    public boolean isFunction() { return isFunction; }
    public void setFunction(boolean function) { isFunction = function; }
    public List<String> getParameterTypes() { return parameterTypes; }
    public void setParameterTypes(List<String> parameterTypes) { this.parameterTypes = parameterTypes; }
    public int getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(int sizeBytes) { this.sizeBytes = sizeBytes; }
    public int getMemoryOffset() { return memoryOffset; }
    public void setMemoryOffset(int memoryOffset) { this.memoryOffset = memoryOffset; }
    public int getReferenceCount() { return referenceCount; }
    public void setReferenceCount(int referenceCount) { this.referenceCount = referenceCount; }
    public boolean isInitialized() { return isInitialized; }
    public void setInitialized(boolean initialized) { isInitialized = initialized; }
}

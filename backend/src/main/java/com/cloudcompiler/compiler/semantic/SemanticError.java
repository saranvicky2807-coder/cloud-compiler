package com.cloudcompiler.compiler.semantic;

public class SemanticError {
    private int line;
    private int column;
    private String errorType;
    private String message;
    private String identifier;
    private String expectedType;
    private String foundType;

    public SemanticError() {}

    public SemanticError(int line, int column, String errorType, String message, String identifier, String expectedType, String foundType) {
        this.line = line;
        this.column = column;
        this.errorType = errorType;
        this.message = message;
        this.identifier = identifier;
        this.expectedType = expectedType;
        this.foundType = foundType;
    }

    public static SemanticErrorBuilder builder() {
        return new SemanticErrorBuilder();
    }

    public static class SemanticErrorBuilder {
        private int line;
        private int column;
        private String errorType;
        private String message;
        private String identifier;
        private String expectedType;
        private String foundType;

        public SemanticErrorBuilder line(int line) { this.line = line; return this; }
        public SemanticErrorBuilder column(int column) { this.column = column; return this; }
        public SemanticErrorBuilder errorType(String errorType) { this.errorType = errorType; return this; }
        public SemanticErrorBuilder message(String message) { this.message = message; return this; }
        public SemanticErrorBuilder identifier(String identifier) { this.identifier = identifier; return this; }
        public SemanticErrorBuilder expectedType(String expectedType) { this.expectedType = expectedType; return this; }
        public SemanticErrorBuilder foundType(String foundType) { this.foundType = foundType; return this; }

        public SemanticError build() {
            return new SemanticError(line, column, errorType, message, identifier, expectedType, foundType);
        }
    }

    public int getLine() { return line; }
    public void setLine(int line) { this.line = line; }
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
    public String getErrorType() { return errorType; }
    public void setErrorType(String errorType) { this.errorType = errorType; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getExpectedType() { return expectedType; }
    public void setExpectedType(String expectedType) { this.expectedType = expectedType; }
    public String getFoundType() { return foundType; }
    public void setFoundType(String foundType) { this.foundType = foundType; }
}

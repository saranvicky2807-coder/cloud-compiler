package com.cloudcompiler.compiler.lexer;

public class Token {
    private TokenType type;
    private String category;
    private String lexeme;
    private Object literalValue;
    private int line;
    private int column;
    private int length;
    private String errorMessage;

    public Token() {}

    public Token(TokenType type, String category, String lexeme, Object literalValue, int line, int column, int length, String errorMessage) {
        this.type = type;
        this.category = category != null ? category : (type != null ? type.getCategory() : "");
        this.lexeme = lexeme;
        this.literalValue = literalValue;
        this.line = line;
        this.column = column;
        this.length = length;
        this.errorMessage = errorMessage;
    }

    public Token(TokenType type, String lexeme, Object literalValue, int line, int column) {
        this.type = type;
        this.category = type != null ? type.getCategory() : "";
        this.lexeme = lexeme;
        this.literalValue = literalValue;
        this.line = line;
        this.column = column;
        this.length = lexeme != null ? lexeme.length() : 0;
    }

    public static TokenBuilder builder() {
        return new TokenBuilder();
    }

    public static class TokenBuilder {
        private TokenType type;
        private String category;
        private String lexeme;
        private Object literalValue;
        private int line;
        private int column;
        private int length;
        private String errorMessage;

        public TokenBuilder type(TokenType type) { this.type = type; return this; }
        public TokenBuilder category(String category) { this.category = category; return this; }
        public TokenBuilder lexeme(String lexeme) { this.lexeme = lexeme; return this; }
        public TokenBuilder literalValue(Object literalValue) { this.literalValue = literalValue; return this; }
        public TokenBuilder line(int line) { this.line = line; return this; }
        public TokenBuilder column(int column) { this.column = column; return this; }
        public TokenBuilder length(int length) { this.length = length; return this; }
        public TokenBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }

        public Token build() {
            return new Token(type, category, lexeme, literalValue, line, column, length, errorMessage);
        }
    }

    public TokenType getType() { return type; }
    public void setType(TokenType type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLexeme() { return lexeme; }
    public void setLexeme(String lexeme) { this.lexeme = lexeme; }
    public Object getLiteralValue() { return literalValue; }
    public void setLiteralValue(Object literalValue) { this.literalValue = literalValue; }
    public int getLine() { return line; }
    public void setLine(int line) { this.line = line; }
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

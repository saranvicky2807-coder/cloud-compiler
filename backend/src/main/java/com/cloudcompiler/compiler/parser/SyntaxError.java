package com.cloudcompiler.compiler.parser;

public class SyntaxError {
    private int line;
    private int column;
    private String expected;
    private String found;
    private String message;

    public SyntaxError() {}

    public SyntaxError(int line, int column, String expected, String found, String message) {
        this.line = line;
        this.column = column;
        this.expected = expected;
        this.found = found;
        this.message = message;
    }

    public static SyntaxErrorBuilder builder() {
        return new SyntaxErrorBuilder();
    }

    public static class SyntaxErrorBuilder {
        private int line;
        private int column;
        private String expected;
        private String found;
        private String message;

        public SyntaxErrorBuilder line(int line) { this.line = line; return this; }
        public SyntaxErrorBuilder column(int column) { this.column = column; return this; }
        public SyntaxErrorBuilder expected(String expected) { this.expected = expected; return this; }
        public SyntaxErrorBuilder found(String found) { this.found = found; return this; }
        public SyntaxErrorBuilder message(String message) { this.message = message; return this; }

        public SyntaxError build() {
            return new SyntaxError(line, column, expected, found, message);
        }
    }

    public int getLine() { return line; }
    public void setLine(int line) { this.line = line; }
    public int getColumn() { return column; }
    public void setColumn(int column) { this.column = column; }
    public String getExpected() { return expected; }
    public void setExpected(String expected) { this.expected = expected; }
    public String getFound() { return found; }
    public void setFound(String found) { this.found = found; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

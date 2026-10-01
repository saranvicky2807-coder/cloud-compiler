package com.cloudcompiler.compiler.lexer;

import java.util.*;

public class Lexer {
    private final String source;
    private final int length;
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int column = 1;
    private int tokenStartColumn = 1;

    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("int", TokenType.KW_INT);
        KEYWORDS.put("float", TokenType.KW_FLOAT);
        KEYWORDS.put("char", TokenType.KW_CHAR);
        KEYWORDS.put("bool", TokenType.KW_BOOL);
        KEYWORDS.put("void", TokenType.KW_VOID);
        KEYWORDS.put("if", TokenType.KW_IF);
        KEYWORDS.put("else", TokenType.KW_ELSE);
        KEYWORDS.put("while", TokenType.KW_WHILE);
        KEYWORDS.put("for", TokenType.KW_FOR);
        KEYWORDS.put("return", TokenType.KW_RETURN);
        KEYWORDS.put("print", TokenType.KW_PRINT);
        KEYWORDS.put("read", TokenType.KW_READ);
        KEYWORDS.put("true", TokenType.LITERAL_BOOL);
        KEYWORDS.put("false", TokenType.LITERAL_BOOL);
    }

    public Lexer(String source) {
        this.source = source != null ? source : "";
        this.length = this.source.length();
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (!isAtEnd()) {
            start = current;
            tokenStartColumn = column;
            Token token = scanToken();
            if (token != null) {
                tokens.add(token);
            }
        }

        tokens.add(new Token(TokenType.EOF, "", null, line, column));
        return tokens;
    }

    private Token scanToken() {
        char c = advance();

        switch (c) {
            case ' ':
            case '\r':
            case '\t':
                return null;
            case '\n':
                line++;
                column = 1;
                return null;

            // Delimiters
            case '(': return createToken(TokenType.DELIM_LPAREN);
            case ')': return createToken(TokenType.DELIM_RPAREN);
            case '{': return createToken(TokenType.DELIM_LBRACE);
            case '}': return createToken(TokenType.DELIM_RBRACE);
            case '[': return createToken(TokenType.DELIM_LBRACKET);
            case ']': return createToken(TokenType.DELIM_RBRACKET);
            case ',': return createToken(TokenType.DELIM_COMMA);
            case ';': return createToken(TokenType.DELIM_SEMICOLON);

            // Operators
            case '+':
                if (match('+')) return createToken(TokenType.OP_INCREMENT);
                if (match('=')) return createToken(TokenType.OP_PLUS_ASSIGN);
                return createToken(TokenType.OP_PLUS);
            case '-':
                if (match('-')) return createToken(TokenType.OP_DECREMENT);
                if (match('=')) return createToken(TokenType.OP_MINUS_ASSIGN);
                return createToken(TokenType.OP_MINUS);
            case '*':
                if (match('=')) return createToken(TokenType.OP_MUL_ASSIGN);
                return createToken(TokenType.OP_MULTIPLY);
            case '%':
                return createToken(TokenType.OP_MODULO);
            case '/':
                if (match('/')) {
                    // Single-line comment
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                    String commentText = source.substring(start, current);
                    return new Token(TokenType.COMMENT_LINE, commentText, commentText, line, tokenStartColumn);
                } else if (match('*')) {
                    // Multi-line comment
                    int commentStartLine = line;
                    int commentStartCol = tokenStartColumn;
                    while (!isAtEnd()) {
                        if (peek() == '*' && peekNext() == '/') {
                            advance(); // *
                            advance(); // /
                            String commentText = source.substring(start, current);
                            return new Token(TokenType.COMMENT_BLOCK, commentText, commentText, commentStartLine, commentStartCol);
                        }
                        if (peek() == '\n') {
                            line++;
                            column = 0;
                        }
                        advance();
                    }
                    return createErrorToken("Unterminated multi-line comment", commentStartLine, commentStartCol);
                } else if (match('=')) {
                    return createToken(TokenType.OP_DIV_ASSIGN);
                } else {
                    return createToken(TokenType.OP_DIVIDE);
                }

            case '!':
                if (match('=')) return createToken(TokenType.OP_NEQ);
                return createToken(TokenType.OP_LOGICAL_NOT);
            case '=':
                if (match('=')) return createToken(TokenType.OP_EQ);
                return createToken(TokenType.OP_ASSIGN);
            case '<':
                if (match('=')) return createToken(TokenType.OP_LTE);
                return createToken(TokenType.OP_LT);
            case '>':
                if (match('=')) return createToken(TokenType.OP_GTE);
                return createToken(TokenType.OP_GT);
            case '&':
                if (match('&')) return createToken(TokenType.OP_LOGICAL_AND);
                return createErrorToken("Unexpected character '&'. Did you mean '&&'?", line, tokenStartColumn);
            case '|':
                if (match('|')) return createToken(TokenType.OP_LOGICAL_OR);
                return createErrorToken("Unexpected character '|'. Did you mean '||'?", line, tokenStartColumn);

            // Literals: Strings & Chars
            case '"':
                return scanString();
            case '\'':
                return scanChar();

            default:
                if (isDigit(c)) {
                    return scanNumber();
                } else if (isAlpha(c)) {
                    return scanIdentifierOrKeyword();
                }
                return createErrorToken("Unexpected character: '" + c + "'", line, tokenStartColumn);
        }
    }

    private Token scanNumber() {
        while (isDigit(peek())) {
            advance();
        }

        if (peek() == '.' && isDigit(peekNext())) {
            advance(); // consume '.'
            while (isDigit(peek())) {
                advance();
            }
            String numStr = source.substring(start, current);
            try {
                double val = Double.parseDouble(numStr);
                return new Token(TokenType.LITERAL_FLOAT, numStr, val, line, tokenStartColumn);
            } catch (NumberFormatException e) {
                return createErrorToken("Invalid float literal: " + numStr, line, tokenStartColumn);
            }
        }

        String numStr = source.substring(start, current);
        try {
            long val = Long.parseLong(numStr);
            return new Token(TokenType.LITERAL_INT, numStr, (int) val, line, tokenStartColumn);
        } catch (NumberFormatException e) {
            return createErrorToken("Integer literal overflow: " + numStr, line, tokenStartColumn);
        }
    }

    private Token scanString() {
        int strStartLine = line;
        int strStartCol = tokenStartColumn;
        StringBuilder sb = new StringBuilder();

        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                column = 0;
            }
            if (peek() == '\\') {
                advance();
                char escape = advance();
                switch (escape) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    case '\\': sb.append('\\'); break;
                    case '"': sb.append('"'); break;
                    case '\'': sb.append('\''); break;
                    default:
                        sb.append('\\').append(escape);
                        break;
                }
            } else {
                sb.append(advance());
            }
        }

        if (isAtEnd()) {
            return createErrorToken("Unterminated string literal", strStartLine, strStartCol);
        }

        advance(); // consume closing '"'
        String lexeme = source.substring(start, current);
        return new Token(TokenType.LITERAL_STRING, lexeme, sb.toString(), strStartLine, strStartCol);
    }

    private Token scanChar() {
        int charStartLine = line;
        int charStartCol = tokenStartColumn;
        char val = '\0';

        if (isAtEnd() || peek() == '\'') {
            if (!isAtEnd()) advance();
            return createErrorToken("Empty character constant", charStartLine, charStartCol);
        }

        if (peek() == '\\') {
            advance();
            char escape = advance();
            switch (escape) {
                case 'n': val = '\n'; break;
                case 't': val = '\t'; break;
                case 'r': val = '\r'; break;
                case '\\': val = '\\'; break;
                case '\'': val = '\''; break;
                default: val = escape; break;
            }
        } else {
            val = advance();
        }

        if (peek() != '\'') {
            return createErrorToken("Multi-character or unterminated char constant", charStartLine, charStartCol);
        }
        advance(); // consume closing '\''

        String lexeme = source.substring(start, current);
        return new Token(TokenType.LITERAL_CHAR, lexeme, val, charStartLine, charStartCol);
    }

    private Token scanIdentifierOrKeyword() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        String text = source.substring(start, current);
        TokenType type = KEYWORDS.get(text);
        if (type == null) {
            type = TokenType.IDENTIFIER;
            return new Token(type, text, text, line, tokenStartColumn);
        }

        if (type == TokenType.LITERAL_BOOL) {
            return new Token(type, text, Boolean.parseBoolean(text), line, tokenStartColumn);
        }

        return new Token(type, text, null, line, tokenStartColumn);
    }

    private Token createToken(TokenType type) {
        String text = source.substring(start, current);
        return new Token(type, text, null, line, tokenStartColumn);
    }

    private Token createErrorToken(String message, int errLine, int errCol) {
        String text = source.substring(start, current);
        Token t = new Token(TokenType.INVALID, text, null, errLine, errCol);
        t.setErrorMessage(message);
        return t;
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        column++;
        return true;
    }

    private char advance() {
        char c = source.charAt(current++);
        column++;
        return c;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= length) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAtEnd() {
        return current >= length;
    }
}

package com.cloudcompiler.compiler.lexer;

public enum TokenType {
    // Keywords
    KW_INT("KEYWORD", "int"),
    KW_FLOAT("KEYWORD", "float"),
    KW_CHAR("KEYWORD", "char"),
    KW_BOOL("KEYWORD", "bool"),
    KW_VOID("KEYWORD", "void"),
    KW_IF("KEYWORD", "if"),
    KW_ELSE("KEYWORD", "else"),
    KW_WHILE("KEYWORD", "while"),
    KW_FOR("KEYWORD", "for"),
    KW_RETURN("KEYWORD", "return"),
    KW_PRINT("KEYWORD", "print"),
    KW_READ("KEYWORD", "read"),

    // Literals
    LITERAL_INT("INTEGER", "Integer Literal"),
    LITERAL_FLOAT("FLOAT", "Float Literal"),
    LITERAL_CHAR("CHAR", "Char Literal"),
    LITERAL_STRING("STRING", "String Literal"),
    LITERAL_BOOL("BOOLEAN", "Boolean Literal"),

    // Identifiers
    IDENTIFIER("IDENTIFIER", "Identifier"),

    // Operators
    OP_PLUS("OPERATOR", "+"),
    OP_MINUS("OPERATOR", "-"),
    OP_MULTIPLY("OPERATOR", "*"),
    OP_DIVIDE("OPERATOR", "/"),
    OP_MODULO("OPERATOR", "%"),
    OP_INCREMENT("OPERATOR", "++"),
    OP_DECREMENT("OPERATOR", "--"),
    OP_ASSIGN("OPERATOR", "="),
    OP_PLUS_ASSIGN("OPERATOR", "+="),
    OP_MINUS_ASSIGN("OPERATOR", "-="),
    OP_MUL_ASSIGN("OPERATOR", "*="),
    OP_DIV_ASSIGN("OPERATOR", "/="),
    OP_EQ("OPERATOR", "=="),
    OP_NEQ("OPERATOR", "!="),
    OP_LT("OPERATOR", "<"),
    OP_LTE("OPERATOR", "<="),
    OP_GT("OPERATOR", ">"),
    OP_GTE("OPERATOR", ">="),
    OP_LOGICAL_AND("OPERATOR", "&&"),
    OP_LOGICAL_OR("OPERATOR", "||"),
    OP_LOGICAL_NOT("OPERATOR", "!"),

    // Delimiters
    DELIM_SEMICOLON("DELIMITER", ";"),
    DELIM_COMMA("DELIMITER", ","),
    DELIM_LPAREN("DELIMITER", "("),
    DELIM_RPAREN("DELIMITER", ")"),
    DELIM_LBRACE("DELIMITER", "{"),
    DELIM_RBRACE("DELIMITER", "}"),
    DELIM_LBRACKET("DELIMITER", "["),
    DELIM_RBRACKET("DELIMITER", "]"),

    // Comments & Metadata
    COMMENT_LINE("COMMENT", "Single-line Comment"),
    COMMENT_BLOCK("COMMENT", "Multi-line Comment"),
    
    // Special
    INVALID("INVALID", "Invalid Token"),
    EOF("EOF", "End of File");

    private final String category;
    private final String description;

    TokenType(String category, String description) {
        this.category = category;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}

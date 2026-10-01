package com.cloudcompiler.compiler.parser;

import com.cloudcompiler.compiler.ast.*;
import com.cloudcompiler.compiler.lexer.Token;
import com.cloudcompiler.compiler.lexer.TokenType;

import java.util.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private final List<SyntaxError> errors = new ArrayList<>();

    public Parser(List<Token> rawTokens) {
        // Filter out non-error comment tokens for parsing
        this.tokens = new ArrayList<>();
        if (rawTokens != null) {
            for (Token t : rawTokens) {
                if (t.getType() != TokenType.COMMENT_LINE && t.getType() != TokenType.COMMENT_BLOCK) {
                    this.tokens.add(t);
                }
            }
        }
        if (this.tokens.isEmpty() || this.tokens.get(this.tokens.size() - 1).getType() != TokenType.EOF) {
            this.tokens.add(new Token(TokenType.EOF, "", null, 1, 1));
        }
    }

    public List<SyntaxError> getErrors() {
        return errors;
    }

    public ProgramNode parse() {
        ProgramNode program = new ProgramNode(1, 1);

        while (!isAtEnd()) {
            try {
                AstNode decl = parseTopLevelDeclaration();
                if (decl != null) {
                    program.addDeclaration(decl);
                }
            } catch (ParseException e) {
                synchronize();
            }
        }

        return program;
    }

    private AstNode parseTopLevelDeclaration() {
        if (isTypeSpecifier(peek().getType())) {
            int line = peek().getLine();
            int col = peek().getColumn();
            Token typeToken = advance();
            String typeName = typeToken.getLexeme();

            if (!check(TokenType.IDENTIFIER)) {
                error(peek(), "Expected identifier after type '" + typeName + "'");
                return null;
            }

            Token nameToken = advance();
            String name = nameToken.getLexeme();

            // Check if it's a function declaration
            if (match(TokenType.DELIM_LPAREN)) {
                return finishFunctionDeclaration(typeName, name, line, col);
            } else {
                // Global variable declaration
                AstNode init = null;
                if (match(TokenType.OP_ASSIGN)) {
                    init = parseExpression();
                }
                consume(TokenType.DELIM_SEMICOLON, "Expected ';' after variable declaration");
                return new VarDeclNode(typeName, name, init, line, col);
            }
        }

        // Top level statements allowed for script-like execution
        return parseStatement();
    }

    private FunctionDeclNode finishFunctionDeclaration(String returnType, String name, int line, int col) {
        FunctionDeclNode func = new FunctionDeclNode(returnType, name, line, col);

        if (!check(TokenType.DELIM_RPAREN)) {
            do {
                if (!isTypeSpecifier(peek().getType())) {
                    error(peek(), "Expected parameter type in function '" + name + "'");
                    break;
                }
                Token pTypeToken = advance();
                if (!check(TokenType.IDENTIFIER)) {
                    error(peek(), "Expected parameter name after type '" + pTypeToken.getLexeme() + "'");
                    break;
                }
                Token pNameToken = advance();
                func.addParameter(new ParameterNode(pTypeToken.getLexeme(), pNameToken.getLexeme(), pTypeToken.getLine(), pTypeToken.getColumn()));
            } while (match(TokenType.DELIM_COMMA));
        }

        consume(TokenType.DELIM_RPAREN, "Expected ')' after parameters");

        if (check(TokenType.DELIM_LBRACE)) {
            BlockNode body = parseBlock();
            func.setBodyNode(body);
        } else {
            consume(TokenType.DELIM_SEMICOLON, "Expected '{' or ';' after function header");
        }

        return func;
    }

    private AstNode parseStatement() {
        if (match(TokenType.DELIM_LBRACE)) {
            return parseBlockBody(previous().getLine(), previous().getColumn());
        }
        if (match(TokenType.KW_IF)) {
            return parseIfStatement();
        }
        if (match(TokenType.KW_WHILE)) {
            return parseWhileStatement();
        }
        if (match(TokenType.KW_FOR)) {
            return parseForStatement();
        }
        if (match(TokenType.KW_RETURN)) {
            return parseReturnStatement();
        }
        if (match(TokenType.KW_PRINT)) {
            return parsePrintStatement();
        }
        if (match(TokenType.KW_READ)) {
            return parseReadStatement();
        }
        if (isTypeSpecifier(peek().getType())) {
            return parseVarDeclStatement();
        }

        return parseExpressionOrAssignStatement();
    }

    private BlockNode parseBlock() {
        Token open = consume(TokenType.DELIM_LBRACE, "Expected '{' to begin block");
        return parseBlockBody(open.getLine(), open.getColumn());
    }

    private BlockNode parseBlockBody(int line, int col) {
        BlockNode block = new BlockNode(line, col);
        while (!check(TokenType.DELIM_RBRACE) && !isAtEnd()) {
            try {
                AstNode stmt = parseStatement();
                if (stmt != null) {
                    block.addStatement(stmt);
                }
            } catch (ParseException e) {
                synchronize();
            }
        }
        consume(TokenType.DELIM_RBRACE, "Expected '}' after block");
        return block;
    }

    private AstNode parseIfStatement() {
        int line = previous().getLine();
        int col = previous().getColumn();

        consume(TokenType.DELIM_LPAREN, "Expected '(' after 'if'");
        AstNode condition = parseExpression();
        consume(TokenType.DELIM_RPAREN, "Expected ')' after if condition");

        AstNode thenBranch = parseStatement();
        AstNode elseBranch = null;
        if (match(TokenType.KW_ELSE)) {
            elseBranch = parseStatement();
        }

        return new IfStmtNode(condition, thenBranch, elseBranch, line, col);
    }

    private AstNode parseWhileStatement() {
        int line = previous().getLine();
        int col = previous().getColumn();

        consume(TokenType.DELIM_LPAREN, "Expected '(' after 'while'");
        AstNode condition = parseExpression();
        consume(TokenType.DELIM_RPAREN, "Expected ')' after while condition");

        AstNode body = parseStatement();
        return new WhileStmtNode(condition, body, line, col);
    }

    private AstNode parseForStatement() {
        int line = previous().getLine();
        int col = previous().getColumn();

        consume(TokenType.DELIM_LPAREN, "Expected '(' after 'for'");

        AstNode init = null;
        if (match(TokenType.DELIM_SEMICOLON)) {
            init = null;
        } else if (isTypeSpecifier(peek().getType())) {
            init = parseVarDeclStatement();
        } else {
            init = parseExpressionOrAssignStatement();
        }

        AstNode condition = null;
        if (!check(TokenType.DELIM_SEMICOLON)) {
            condition = parseExpression();
        }
        consume(TokenType.DELIM_SEMICOLON, "Expected ';' after for loop condition");

        AstNode update = null;
        if (!check(TokenType.DELIM_RPAREN)) {
            update = parseExpressionWithoutSemicolon();
        }
        consume(TokenType.DELIM_RPAREN, "Expected ')' after for loop clauses");

        AstNode body = parseStatement();
        return new ForStmtNode(init, condition, update, body, line, col);
    }

    private AstNode parseReturnStatement() {
        int line = previous().getLine();
        int col = previous().getColumn();

        AstNode value = null;
        if (!check(TokenType.DELIM_SEMICOLON)) {
            value = parseExpression();
        }
        consume(TokenType.DELIM_SEMICOLON, "Expected ';' after return value");
        return new ReturnStmtNode(value, line, col);
    }

    private AstNode parsePrintStatement() {
        int line = previous().getLine();
        int col = previous().getColumn();

        boolean hasParen = match(TokenType.DELIM_LPAREN);
        AstNode expr = parseExpression();
        if (hasParen) {
            consume(TokenType.DELIM_RPAREN, "Expected ')' after print expression");
        }
        consume(TokenType.DELIM_SEMICOLON, "Expected ';' after print statement");
        return new PrintStmtNode(expr, line, col);
    }

    private AstNode parseReadStatement() {
        int line = previous().getLine();
        int col = previous().getColumn();

        boolean hasParen = match(TokenType.DELIM_LPAREN);
        Token varToken = consume(TokenType.IDENTIFIER, "Expected variable name to read into");
        if (hasParen) {
            consume(TokenType.DELIM_RPAREN, "Expected ')' after read variable");
        }
        consume(TokenType.DELIM_SEMICOLON, "Expected ';' after read statement");
        return new ReadStmtNode(varToken.getLexeme(), line, col);
    }

    private AstNode parseVarDeclStatement() {
        Token typeToken = advance();
        String typeName = typeToken.getLexeme();
        Token nameToken = consume(TokenType.IDENTIFIER, "Expected identifier after type '" + typeName + "'");

        AstNode init = null;
        if (match(TokenType.OP_ASSIGN)) {
            init = parseExpression();
        }
        consume(TokenType.DELIM_SEMICOLON, "Expected ';' after variable declaration");
        return new VarDeclNode(typeName, nameToken.getLexeme(), init, typeToken.getLine(), typeToken.getColumn());
    }

    private AstNode parseExpressionOrAssignStatement() {
        AstNode expr = parseExpressionWithoutSemicolon();
        consume(TokenType.DELIM_SEMICOLON, "Expected ';' after statement");
        return expr;
    }

    private AstNode parseExpressionWithoutSemicolon() {
        return parseAssignment();
    }

    public AstNode parseExpression() {
        return parseAssignment();
    }

    private AstNode parseAssignment() {
        AstNode expr = parseLogicalOr();

        if (match(TokenType.OP_ASSIGN, TokenType.OP_PLUS_ASSIGN, TokenType.OP_MINUS_ASSIGN, TokenType.OP_MUL_ASSIGN, TokenType.OP_DIV_ASSIGN)) {
            Token op = previous();
            AstNode value = parseAssignment();

            if (expr instanceof IdentifierExprNode idExpr) {
                return new AssignStmtNode(idExpr.getName(), op.getLexeme(), value, idExpr.getLine(), idExpr.getColumn());
            }

            error(op, "Invalid assignment target");
        }

        return expr;
    }

    private AstNode parseLogicalOr() {
        AstNode expr = parseLogicalAnd();
        while (match(TokenType.OP_LOGICAL_OR)) {
            Token op = previous();
            AstNode right = parseLogicalAnd();
            expr = new BinaryExprNode(expr, op.getLexeme(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private AstNode parseLogicalAnd() {
        AstNode expr = parseEquality();
        while (match(TokenType.OP_LOGICAL_AND)) {
            Token op = previous();
            AstNode right = parseEquality();
            expr = new BinaryExprNode(expr, op.getLexeme(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private AstNode parseEquality() {
        AstNode expr = parseComparison();
        while (match(TokenType.OP_EQ, TokenType.OP_NEQ)) {
            Token op = previous();
            AstNode right = parseComparison();
            expr = new BinaryExprNode(expr, op.getLexeme(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private AstNode parseComparison() {
        AstNode expr = parseTerm();
        while (match(TokenType.OP_LT, TokenType.OP_LTE, TokenType.OP_GT, TokenType.OP_GTE)) {
            Token op = previous();
            AstNode right = parseTerm();
            expr = new BinaryExprNode(expr, op.getLexeme(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private AstNode parseTerm() {
        AstNode expr = parseFactor();
        while (match(TokenType.OP_PLUS, TokenType.OP_MINUS)) {
            Token op = previous();
            AstNode right = parseFactor();
            expr = new BinaryExprNode(expr, op.getLexeme(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private AstNode parseFactor() {
        AstNode expr = parseUnary();
        while (match(TokenType.OP_MULTIPLY, TokenType.OP_DIVIDE, TokenType.OP_MODULO)) {
            Token op = previous();
            AstNode right = parseUnary();
            expr = new BinaryExprNode(expr, op.getLexeme(), right, op.getLine(), op.getColumn());
        }
        return expr;
    }

    private AstNode parseUnary() {
        if (match(TokenType.OP_LOGICAL_NOT, TokenType.OP_MINUS, TokenType.OP_PLUS, TokenType.OP_INCREMENT, TokenType.OP_DECREMENT)) {
            Token op = previous();
            AstNode operand = parseUnary();
            return new UnaryExprNode(op.getLexeme(), operand, true, op.getLine(), op.getColumn());
        }
        return parsePostfix();
    }

    private AstNode parsePostfix() {
        AstNode expr = parsePrimary();

        while (true) {
            if (match(TokenType.OP_INCREMENT, TokenType.OP_DECREMENT)) {
                Token op = previous();
                expr = new UnaryExprNode(op.getLexeme(), expr, false, op.getLine(), op.getColumn());
            } else if (match(TokenType.DELIM_LPAREN)) {
                if (expr instanceof IdentifierExprNode idExpr) {
                    expr = finishFunctionCall(idExpr.getName(), idExpr.getLine(), idExpr.getColumn());
                } else {
                    error(peek(), "Only identifiers can be called as functions");
                }
            } else {
                break;
            }
        }

        return expr;
    }

    private FunctionCallExprNode finishFunctionCall(String name, int line, int col) {
        FunctionCallExprNode call = new FunctionCallExprNode(name, line, col);
        if (!check(TokenType.DELIM_RPAREN)) {
            do {
                call.addArgument(parseExpression());
            } while (match(TokenType.DELIM_COMMA));
        }
        consume(TokenType.DELIM_RPAREN, "Expected ')' after function arguments");
        return call;
    }

    private AstNode parsePrimary() {
        if (match(TokenType.LITERAL_BOOL)) {
            Token t = previous();
            return new LiteralExprNode("bool", t.getLiteralValue(), t.getLexeme(), t.getLine(), t.getColumn());
        }
        if (match(TokenType.LITERAL_INT)) {
            Token t = previous();
            return new LiteralExprNode("int", t.getLiteralValue(), t.getLexeme(), t.getLine(), t.getColumn());
        }
        if (match(TokenType.LITERAL_FLOAT)) {
            Token t = previous();
            return new LiteralExprNode("float", t.getLiteralValue(), t.getLexeme(), t.getLine(), t.getColumn());
        }
        if (match(TokenType.LITERAL_CHAR)) {
            Token t = previous();
            return new LiteralExprNode("char", t.getLiteralValue(), t.getLexeme(), t.getLine(), t.getColumn());
        }
        if (match(TokenType.LITERAL_STRING)) {
            Token t = previous();
            return new LiteralExprNode("string", t.getLiteralValue(), t.getLexeme(), t.getLine(), t.getColumn());
        }
        if (match(TokenType.IDENTIFIER)) {
            Token t = previous();
            return new IdentifierExprNode(t.getLexeme(), t.getLine(), t.getColumn());
        }
        if (match(TokenType.DELIM_LPAREN)) {
            AstNode expr = parseExpression();
            consume(TokenType.DELIM_RPAREN, "Expected ')' after expression");
            return expr;
        }

        Token currentToken = peek();
        throw error(currentToken, "Expected expression, found: " + (currentToken.getLexeme().isEmpty() ? currentToken.getType().name() : "'" + currentToken.getLexeme() + "'"));
    }

    private boolean isTypeSpecifier(TokenType type) {
        return type == TokenType.KW_INT || type == TokenType.KW_FLOAT ||
               type == TokenType.KW_CHAR || type == TokenType.KW_BOOL ||
               type == TokenType.KW_VOID;
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        if (current >= tokens.size()) return tokens.get(tokens.size() - 1);
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseException error(Token token, String message) {
        SyntaxError err = SyntaxError.builder()
                .line(token.getLine())
                .column(token.getColumn())
                .expected(token.getType().name())
                .found(token.getLexeme().isEmpty() ? token.getType().name() : token.getLexeme())
                .message(message)
                .build();
        errors.add(err);
        return new ParseException(message);
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().getType() == TokenType.DELIM_SEMICOLON) return;

            switch (peek().getType()) {
                case KW_INT:
                case KW_FLOAT:
                case KW_CHAR:
                case KW_BOOL:
                case KW_VOID:
                case KW_IF:
                case KW_WHILE:
                case KW_FOR:
                case KW_RETURN:
                case KW_PRINT:
                case KW_READ:
                case DELIM_RBRACE:
                    return;
                default:
                    advance();
            }
        }
    }

    private static class ParseException extends RuntimeException {
        public ParseException(String message) {
            super(message);
        }
    }
}

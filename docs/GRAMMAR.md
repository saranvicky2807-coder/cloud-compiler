# Compiler Grammar Specification & Intermediate Representations

## 1. Lexical Grammar

```ebnf
Program        ::= Declaration*
Declaration    ::= FunctionDecl | VarDecl | Statement

Type           ::= "int" | "float" | "char" | "bool" | "void"
Identifier     ::= [a-zA-Z_][a-zA-Z0-9_]*
IntegerLiteral ::= [0-9]+
FloatLiteral   ::= [0-9]+ "." [0-9]+
StringLiteral  ::= '"' [^"]* '"'
CharLiteral    ::= "'" [^'] "'"
BooleanLiteral ::= "true" | "false"
```

## 2. Syntax Grammar (EBNF)

```ebnf
FunctionDecl   ::= Type Identifier "(" ParameterList? ")" ( Block | ";" )
ParameterList  ::= Parameter ( "," Parameter )*
Parameter      ::= Type Identifier

VarDecl        ::= Type Identifier ( "=" Expression )? ";"

Statement      ::= Block
                 | IfStatement
                 | WhileStatement
                 | ForStatement
                 | ReturnStatement
                 | PrintStatement
                 | ReadStatement
                 | VarDecl
                 | ExpressionStatement

Block          ::= "{" Statement* "}"

IfStatement    ::= "if" "(" Expression ")" Statement ( "else" Statement )?
WhileStatement ::= "while" "(" Expression ")" Statement
ForStatement   ::= "for" "(" ( VarDecl | ExpressionStatement | ";" ) Expression? ";" Expression? ")" Statement

ReturnStatement ::= "return" Expression? ";"
PrintStatement  ::= "print" "("? Expression ")"? ";"
ReadStatement   ::= "read" "("? Identifier ")"? ";"
ExpressionStatement ::= Expression ";"

Expression     ::= Assignment
Assignment     ::= LogicalOr ( ( "=" | "+=" | "-=" | "*=" | "/=" ) Assignment )?
LogicalOr      ::= LogicalAnd ( "||" LogicalAnd )*
LogicalAnd     ::= Equality ( "&&" Equality )*
Equality       ::= Relational ( ( "==" | "!=" ) Relational )*
Relational     ::= Additive ( ( "<" | "<=" | ">" | ">=" ) Additive )*
Additive       ::= Multiplicative ( ( "+" | "-" ) Multiplicative )*
Multiplicative ::= Unary ( ( "*" | "/" | "%" ) Unary )*
Unary          ::= ( "!" | "-" | "+" | "++" | "--" ) Unary | Postfix
Postfix        ::= Primary ( "++" | "--" | "(" ArgumentList? ")" )*
ArgumentList   ::= Expression ( "," Expression )*

Primary        ::= Identifier
                 | IntegerLiteral
                 | FloatLiteral
                 | StringLiteral
                 | CharLiteral
                 | BooleanLiteral
                 | "(" Expression ")"
```

## 3. Intermediate Code Generation Formats

### Three-Address Code (TAC)
- `t1 = a + b`
- `t2 = c * d`
- `IF_FALSE t1 GOTO L1`
- `GOTO L2`
- `PARAM t2`
- `CALL func, 1`
- `RETURN t3`

### Quadruples
Records of `(Operator, Arg1, Arg2, Result)`:
```text
Op   | Arg1 | Arg2 | Result
+    | a    | b    | t1
*    | c    | d    | t2
=    | t1   |      | x
```

### Triples
Records of `(Index, Operator, Arg1, Arg2)` where arguments reference previous Triple indices `(0)`, `(1)`:
```text
Index | Op | Arg1 | Arg2
(0)   | +  | a    | b
(1)   | *  | (0)  | c
```

### Indirect Triples
Array of instruction pointers indexing the Triple definitions, enabling easy compiler optimization and code reordering.

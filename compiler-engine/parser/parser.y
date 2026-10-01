%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

extern int yylex();
extern int line_num;
extern int col_num;
void yyerror(const char *s);

%}

%union {
    int int_val;
    float float_val;
    int bool_val;
    char* str_val;
}

%token KW_INT KW_FLOAT KW_CHAR KW_BOOL KW_VOID
%token KW_IF KW_ELSE KW_WHILE KW_FOR KW_RETURN KW_PRINT KW_READ
%token <int_val> LIT_INT
%token <float_val> LIT_FLOAT
%token <bool_val> LIT_BOOL
%token <str_val> IDENTIFIER

%token OP_PLUS OP_MINUS OP_MUL OP_DIV OP_MOD OP_ASSIGN
%token OP_EQ OP_NEQ OP_LT OP_LTE OP_GT OP_GTE OP_AND OP_OR OP_NOT
%token DELIM_SEMI DELIM_COMMA DELIM_LPAREN DELIM_RPAREN DELIM_LBRACE DELIM_RBRACE

%left OP_OR
%left OP_AND
%left OP_EQ OP_NEQ
%left OP_LT OP_LTE OP_GT OP_GTE
%left OP_PLUS OP_MINUS
%left OP_MUL OP_DIV OP_MOD
%right OP_NOT

%%

program:
    declaration_list
    ;

declaration_list:
    declaration_list declaration
    | /* empty */
    ;

declaration:
    var_decl
    | func_decl
    | stmt
    ;

type_spec:
    KW_INT
    | KW_FLOAT
    | KW_CHAR
    | KW_BOOL
    | KW_VOID
    ;

var_decl:
    type_spec IDENTIFIER DELIM_SEMI
    | type_spec IDENTIFIER OP_ASSIGN expr DELIM_SEMI
    ;

func_decl:
    type_spec IDENTIFIER DELIM_LPAREN param_list DELIM_RPAREN block
    ;

param_list:
    param_list_nonempty
    | /* empty */
    ;

param_list_nonempty:
    param_list_nonempty DELIM_COMMA param
    | param
    ;

param:
    type_spec IDENTIFIER
    ;

block:
    DELIM_LBRACE stmt_list DELIM_RBRACE
    ;

stmt_list:
    stmt_list stmt
    | /* empty */
    ;

stmt:
    block
    | var_decl
    | assign_stmt
    | if_stmt
    | while_stmt
    | for_stmt
    | return_stmt
    | print_stmt
    | read_stmt
    | expr DELIM_SEMI
    ;

assign_stmt:
    IDENTIFIER OP_ASSIGN expr DELIM_SEMI
    ;

if_stmt:
    KW_IF DELIM_LPAREN expr DELIM_RPAREN stmt
    | KW_IF DELIM_LPAREN expr DELIM_RPAREN stmt KW_ELSE stmt
    ;

while_stmt:
    KW_WHILE DELIM_LPAREN expr DELIM_RPAREN stmt
    ;

for_stmt:
    KW_FOR DELIM_LPAREN for_init DELIM_SEMI for_cond DELIM_SEMI for_update DELIM_RPAREN stmt
    ;

for_init:
    var_decl
    | assign_stmt
    | /* empty */
    ;

for_cond:
    expr
    | /* empty */
    ;

for_update:
    IDENTIFIER OP_ASSIGN expr
    | expr
    | /* empty */
    ;

return_stmt:
    KW_RETURN expr DELIM_SEMI
    | KW_RETURN DELIM_SEMI
    ;

print_stmt:
    KW_PRINT DELIM_LPAREN expr DELIM_RPAREN DELIM_SEMI
    | KW_PRINT expr DELIM_SEMI
    ;

read_stmt:
    KW_READ DELIM_LPAREN IDENTIFIER DELIM_RPAREN DELIM_SEMI
    | KW_READ IDENTIFIER DELIM_SEMI
    ;

expr:
    IDENTIFIER
    | LIT_INT
    | LIT_FLOAT
    | LIT_BOOL
    | expr OP_PLUS expr
    | expr OP_MINUS expr
    | expr OP_MUL expr
    | expr OP_DIV expr
    | expr OP_MOD expr
    | expr OP_EQ expr
    | expr OP_NEQ expr
    | expr OP_LT expr
    | expr OP_LTE expr
    | expr OP_GT expr
    | expr OP_GTE expr
    | expr OP_AND expr
    | expr OP_OR expr
    | OP_NOT expr
    | DELIM_LPAREN expr DELIM_RPAREN
    | IDENTIFIER DELIM_LPAREN arg_list DELIM_RPAREN
    ;

arg_list:
    arg_list_nonempty
    | /* empty */
    ;

arg_list_nonempty:
    arg_list_nonempty DELIM_COMMA expr
    | expr
    ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Syntax Error at line %d, col %d: %s\n", line_num, col_num, s);
}

int main() {
    printf("Parsing C-like language with Bison/Yacc...\n");
    return yyparse();
}

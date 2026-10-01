%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "y.tab.h"

int line_num = 1;
int col_num = 1;

void update_col(const char* text) {
    col_num += strlen(text);
}
%}

%option noyywrap

DIGIT       [0-9]
LETTER      [a-zA-Z_]
ID          {LETTER}({LETTER}|{DIGIT})*
INT_LIT     {DIGIT}+
FLOAT_LIT   {DIGIT}+"."{DIGIT}+
WS          [ \t\r]+

%%

\n                  { line_num++; col_num = 1; }
{WS}                { update_col(yytext); }

"//".*              { /* Single line comment */ }
"/*"([^*]|\*+[^*/])*\*+"/" { /* Multi-line comment */ }

"int"               { update_col(yytext); return KW_INT; }
"float"             { update_col(yytext); return KW_FLOAT; }
"char"              { update_col(yytext); return KW_CHAR; }
"bool"              { update_col(yytext); return KW_BOOL; }
"void"              { update_col(yytext); return KW_VOID; }
"if"                { update_col(yytext); return KW_IF; }
"else"              { update_col(yytext); return KW_ELSE; }
"while"             { update_col(yytext); return KW_WHILE; }
"for"               { update_col(yytext); return KW_FOR; }
"return"            { update_col(yytext); return KW_RETURN; }
"print"             { update_col(yytext); return KW_PRINT; }
"read"              { update_col(yytext); return KW_READ; }

"true"              { update_col(yytext); yylval.bool_val = 1; return LIT_BOOL; }
"false"             { update_col(yytext); yylval.bool_val = 0; return LIT_BOOL; }

{FLOAT_LIT}         { update_col(yytext); yylval.float_val = atof(yytext); return LIT_FLOAT; }
{INT_LIT}           { update_col(yytext); yylval.int_val = atoi(yytext); return LIT_INT; }
{ID}                { update_col(yytext); yylval.str_val = strdup(yytext); return IDENTIFIER; }

"+"                 { update_col(yytext); return OP_PLUS; }
"-"                 { update_col(yytext); return OP_MINUS; }
"*"                 { update_col(yytext); return OP_MUL; }
"/"                 { update_col(yytext); return OP_DIV; }
"%"                 { update_col(yytext); return OP_MOD; }
"="                 { update_col(yytext); return OP_ASSIGN; }
"=="                { update_col(yytext); return OP_EQ; }
"!="                { update_col(yytext); return OP_NEQ; }
"<"                 { update_col(yytext); return OP_LT; }
"<="                { update_col(yytext); return OP_LTE; }
">"                 { update_col(yytext); return OP_GT; }
">="                { update_col(yytext); return OP_GTE; }
"&&"                { update_col(yytext); return OP_AND; }
"||"                { update_col(yytext); return OP_OR; }
"!"                 { update_col(yytext); return OP_NOT; }

";"                 { update_col(yytext); return DELIM_SEMI; }
","                 { update_col(yytext); return DELIM_COMMA; }
"("                 { update_col(yytext); return DELIM_LPAREN; }
")"                 { update_col(yytext); return DELIM_RPAREN; }
"{"                 { update_col(yytext); return DELIM_LBRACE; }
"}"                 { update_col(yytext); return DELIM_RBRACE; }

.                   { printf("Lexical Error: Unexpected character '%s' at line %d, col %d\n", yytext, line_num, col_num); update_col(yytext); }

%%

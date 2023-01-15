%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1
%}

%token INT;
%token CHAR;
%token PANA;
%token DACA;
%token ALTFEL;
%token REPETA;
%token CONSOLA;
%token PROGRAM;

%token IDENTIFIER;
%token INT_CONSTANT;
%token CHAR_CONSTANT;

%token PLUS;
%token MINUS;
%token TIMES;
%token DIVIDE;
%token MOD;
%token EGAL;
%token GREATER;
%token GREATER_EQUAL;
%token LESS;
%token LESS_EQUAL;
%token EQUAL;
%token NOT_EQUAL;
%token AND;
%token OR;

%token SEMICOLON;
%token OPEN;
%token CLOSE;
%token SQUARE_OPEN;
%token SQUARE_CLOSE;
%token CURLY_OPEN;
%token CURLY_CLOSE;
%token COMMA;
%token COLON;
%token DOUBLE_ARROW_RIGHT;
%token DOUBLE_ARROW_LEFT;

%start Program 

%%
Program: PROGRAM CURLY_OPEN StmtList CURLY_CLOSE {};

StmtList: Stmt | Stmt StmtList {};
Stmt: SimpleStmt; | StructStmt {};
SimpleStmt: DeclarationStmt | IoStmt | AssignStmt {};
SimpleType: INT | CHAR {};
ArrayIdentifier: IDENTIFIER SQUARE_OPEN INT_CONSTANT SQUARE_CLOSE {};  
Identifiers: IDENTIFIER | ArrayIdentifier {};
DeclarationStmt: SimpleType Identifiers {};

AssignStmt: Identifiers EGAL Expression {};
Expression: Expression PLUS Term | Expression MINUS Term | Term {};
Term: Term TIMES Factor | Term DIVIDE Factor | Term MOD Factor | Factor {};
Factor: OPEN Expression CLOSE | Identifiers | INT_CONSTANT | CHAR_CONSTANT {};

IoStmt: CONSOLA DOUBLE_ARROW_RIGHT Identifiers | CONSOLA DOUBLE_ARROW_LEFT Identifiers | CONSOLA DOUBLE_ARROW_LEFT Expression {};

StructStmt: PanaStmt | DacaStmt | RepetaStmt {};

Relation: GREATER | GREATER_EQUAL | LESS | LESS_EQUAL | EQUAL | NOT_EQUAL {};
Condition: Expression Relation Expression {};

PanaStmt: PANA Condition CURLY_OPEN StmtList CURLY_CLOSE {};
DacaStmt: DACA Condition CURLY_OPEN StmtList CURLY_CLOSE | DACA Condition CURLY_OPEN StmtList CURLY_CLOSE ALTFEL CURLY_OPEN StmtList CURLY_CLOSE {};
RepetaStmt: REPETA IDENTIFIER EGAL Expression COLON Expression CURLY_OPEN StmtList CURLY_CLOSE {};
%%
yyerror(char *s)
{	
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin =  fopen(argv[1],"r");
	if(!yyparse()) fprintf(stderr, "\tOK\n");
} 
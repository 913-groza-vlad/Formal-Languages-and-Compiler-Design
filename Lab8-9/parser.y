%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1 
%}

%token DEF
%token INT
%token STRING
%token WHETHER
%token OTHERWISE
%token ARRAY
%token WHILE
%token FOR
%token CONST
%token INPUT
%token OUT

%token plus
%token minus
%token multiply
%token division
%token assign
%token assign2
%token more
%token less
%token equal
%token different
%token moreOrEqual
%token lessOrEqual
%token equalPlus
%token equalMinus
%token equalMultiply
%token equalDivision
%token and
%token or

%token endline
%token semicolon
%token colon
%token leftRoundBracket
%token rightRoundBracket
%token leftCurlyBracket
%token rightCurlyBracket
%token leftSquareBracket
%token rightSquareBracket

%token IDENTIFIER
%token NUMBER_CONST
%token STRING_CONST

%left '+' '-'
%left '*' '/'
%left OR
%left AND

%start program

%%

program : leftCurlyBracket cmpdstmt rightCurlyBracket ;
declaration_stmt : decllist | constlist ;
decllist : DEF decl ;
decl : declaration semicolon decllist | /* empty */ ;
declaration: id_declaration | array_declaration ;
id_declaration : IDENTIFIER colon type ;
array_declaration : IDENTIFIER leftSquareBracket NUMBER_CONST rightSquareBracket colon ARRAY leftRoundBracket type rightRoundBracket ;
type : INT | STRING ;
constlist : CONST const_declaration semicolon constlist | /* empty */ ;
const_declaration : IDENTIFIER assign NUMBER_CONST | STRING_CONST ;
cmpdstmt : stmt cmpdstmt | /* empty */ ;
int_term : NUMBER_CONST | IDENTIFIER ;
string_term : STRING_CONST | IDENTIFIER ;
expression : int_expression | string_expression ;
int_expression : int_term sign_and_expression | leftRoundBracket int_term sign_and_expression rightRoundBracket ;
sign_and_expression : sign int_expression | /* empty */ ;
sign : plus | minus | multiply | division ;
string_expression : string_term plus_and_string_expression ;
plus_and_string_expression : plus string_expression | /* empty */ ;
stmt : declaration_stmt endline | simplestmt endline | structstmt ;
simplestmt : assignstmt | iostmt | cmpdassignstmt ;
structstmt : ifstmt | whilestmt | forstmt ;
assignstmt : IDENTIFIER assign expression ;
cmpdassignstmt : IDENTIFIER equalAndSign expression ;
equalAndSign : equalPlus | equalMinus | equalMultiply | equalDivision ;
iostmt : readstmt | writestmt ;
readstmt : IDENTIFIER assign INPUT leftRoundBracket rightRoundBracket ;
writestmt : IDENTIFIER assign2 OUT leftRoundBracket rightRoundBracket ;
ifstmt : WHETHER leftRoundBracket condition rightRoundBracket stmt_body elsestmt ;
stmt_body : leftCurlyBracket stmt rightCurlyBracket | stmt ;
elsestmt : OTHERWISE stmt_body | /* empty */ ;
whilestmt : WHILE leftRoundBracket condition rightRoundBracket stmt_body ;
forstmt : FOR leftRoundBracket forstmt_parts rightRoundBracket stmt_body ;
forstmt_parts: assignstmt semicolon condition semicolon assignment | /* empty */ ;
assignment : assignstmt | cmpdassignstmt | /* empty */ ;
condition : expression RELATION expression ;
RELATION : less | lessOrEqual | equal | different | moreOrEqual | more | and | or ;

%%

yyerror(char *s)
{
  	printf("%s\n", s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
  	if(argc>1) yyin = fopen(argv[1], "r");
  	if((argc>2)&&(!strcmp(argv[2],"-d"))) yydebug = 1;
  	if(!yyparse()) fprintf(stderr,"\tO.K.\n");
}
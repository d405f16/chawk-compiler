grammar chawk;

program
    : body? setup body? route body?
    ;

body
    : (statement | function_expression)+
    ;

setup
    : 'setup' '=' '{' body?'}'
    ;

route
    : 'route' '=' '{' body? '}'
    ;

statement
    : variable_statement
    | function_statement
    | selection_statement
    | iteration_statement
    | return_statement
    ;

variable_statement
    : IDENTIFIER '=' expression                                 #variableStatement
    | IDENTIFIER '=' '[' (expression (',' expression)*)? ']'    #arrayStatement
    ;

function_statement
    : IDENTIFIER '=' '{' body? '}'                              #functionStatement
    ;

selection_statement
    : 'if' '(' expression ')' '{' body? '}'                         #ifStatement
    | 'if' '(' expression ')' '{' body? '}' 'else' '{' body? '}'    #ifElseStatement
    ;

iteration_statement
    : 'for' '(' IDENTIFIER '=' expression 'to' expression 'by' expression ')' '{' body? '}'     #forStatement
    | 'while' '(' expression ')' '{' body? '}'                                                  #whileStatement
    ;

return_statement
    : 'return' expression
    ;

expression
    : VALUE                                                 #valueExpression
    | '(' expression ')'                                    #parenthesisExpression
    | variable_expression                                   #variableExpression
    | function_expression                                   #functionExpression
    | expression op=('*' | '/' | '%') expression               #mathematicalExpression
    | expression op=('+' | '-') expression                     #mathematicalExpression
    | expression op=('<' | '<=' | '>' | '>=') expression       #relationalExpression
    | expression op=('==' | '!=' ) expression                  #equalityExpression
    | expression op='&&' expression                            #logicalExpression
    | expression op='||' expression                            #logicalExpression
    ;

variable_expression
    : IDENTIFIER
    | IDENTIFIER '[' expression? ']'
    ;

function_expression
    : IDENTIFIER '(' (named_parameter (',' named_parameter)*)? ')'
    ;

named_parameter
    : variable_statement
    | function_statement
    ;

VALUE
    : BOOLEAN
    | NUMBER
    | STRING
    ;

BOOLEAN: 'true' | 'false';
STRING: ANY_EXCEPT_DOUBLEQUOTE | ANY_EXCEPT_SINGLEQUOTE;
NUMBER: DIGIT+ ('.' DIGIT+)?;
IDENTIFIER: CHARACTER+ (CHARACTER | DIGIT | '.')*;

fragment DIGIT: ('0'..'9');
fragment CHARACTER: ('a'..'z'|'A'..'Z');
fragment ANY_EXCEPT_DOUBLEQUOTE: '"' ~('"' | '\n' | '\r')* '"';
fragment ANY_EXCEPT_SINGLEQUOTE: '\'' ~('\'' | '\n' | '\r')* '\'';

WHITESPACE: (' ' | '\t' | '\r' | '\n') -> skip;
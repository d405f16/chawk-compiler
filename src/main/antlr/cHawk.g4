grammar cHawk;

program
    : statement* setup statement* route statement*
    ;

body
    : (statement | function_expression)+
    ;

setup
    : 'setup' '=' '{' body? '}'
    ;

route
    : 'route' '=' '{' body? '}'
    ;

// Statements
statement
    : variable_statement
    | function_statement
    | selection_statement
    | iteration_statement
    | print_statement
    ;

variable_statement
    : IDENTIFIER '=' expression                                                             #variableStatement
    | IDENTIFIER '=' '[' (expression (',' expression)*)? ']'                                #arrayStatement
    ;

function_statement
    : IDENTIFIER '=' '{' body? return_statement? '}'                                        #functionStatement
    ;

selection_statement
    : 'if' '(' expression ')' '{' body? '}'                                                 #ifStatement
    | 'if' '(' expression ')' '{' body? '}' 'else' '{' body? '}'                            #ifElseStatement
    ;

iteration_statement
    : 'for' '(' IDENTIFIER '=' expression 'to' expression 'by' expression ')' '{' body? '}' #forStatement
    | 'while' '(' expression ')' '{' body? '}'                                              #whileStatement
    ;

return_statement
    : 'return' expression                                                                   #returnStatement
    ;

print_statement
    : 'print' expression                                                                    #printStatement
    ;

// Expressions
primary_expression
    : value_expression
    | variable_expression
    | function_expression
    | parenthesis_expression
    ;

value_expression
    : VALUE                                                         #valueExpression
    ;

variable_expression
    : IDENTIFIER                                                    #variableExpression
    | IDENTIFIER '[' expression? ']'                                #arrayExpression
    | 'drone.' variable_expression                                  #systemVariableExpression
    | 'param.' variable_expression                                 #variableParameterExpression
    ;

function_expression
    : IDENTIFIER '(' (named_parameter (',' named_parameter)*)? ')'  #functionExpression
    | 'drone.' function_expression                                  #systemFunctionExpression
    | 'param.' function_expression                                 #functionParameterExpression
    ;

parenthesis_expression
    : '(' expression ')'                                            #parenthesisExpression
    ;

named_parameter
    : variable_statement                                            #namedVariableParameter
    | function_statement                                            #namedFunctionParameter
    ;

mathematical_expression
    : primary_expression                                            #primaryExpression
    | mathematical_expression op=('*' | '/' | '%') expression       #multiplicativeExpression
    | mathematical_expression op=('+' | '-') expression             #additiveExpression
    ;

logical_expression
    : mathematical_expression                                       #mathematicalExpression
    | logical_expression op=('<' | '<=' | '>' | '>=') expression    #relationalExpression
    | logical_expression op=('==' | '!=' ) expression               #equalityExpression
    | logical_expression op='&&' expression                         #andExpression
    | logical_expression op='||' expression                         #orExpression
    ;

expression
    : logical_expression                                            #logicalExpression
    ;

VALUE
    : BOOLEAN
    | NUMBER
    | STRING
    ;

BOOLEAN: 'true' | 'false';
STRING: ANY_EXCEPT_DOUBLEQUOTE | ANY_EXCEPT_SINGLEQUOTE;
NUMBER: DIGIT+ ('.' DIGIT+)?;
IDENTIFIER: CHARACTER+ (CHARACTER | DIGIT)*;

fragment DIGIT: ('0'..'9');
fragment CHARACTER: ('a'..'z'|'A'..'Z');
fragment ANY_EXCEPT_DOUBLEQUOTE: '"' ~('"' | '\n' | '\r')* '"';
fragment ANY_EXCEPT_SINGLEQUOTE: '\'' ~('\'' | '\n' | '\r')* '\'';

WHITESPACE: (' ' | '\t' | '\r' | '\n') -> skip;
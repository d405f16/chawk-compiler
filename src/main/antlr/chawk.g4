grammar chawk;

program
    : statement_expression? setup statement_expression? route statement_expression?
    ;

statement_expression
    : (statement | expression)+
    ;

setup
    : 'setup' '=' '{' statement_expression? '}'
    ;

route
    : 'route' '=' '{' statement_expression? '}'
    ;

/* STATEMENTS */
statement
    : variable_statement
    | function_statement
    | selection_statement
    | iteration_statement
    ;

variable_statement
    : id=IDENTIFIER '=' expr=expression                             #varDcl
    | id1=IDENTIFIER '.' id2=IDENTIFIER '=' expr=expression         #varDotDcl
    | id=IDENTIFIER '=' '[' (expression (',' expression)*)? ']'     #varArrayDcl
    ;

function_statement
    : id=IDENTIFIER '=' '{' statement_expression? '}'                        #funcDcl
    | id1=IDENTIFIER '.' id2=IDENTIFIER '=' '{' statement_expression? '}'    #funcDotDcl
    ;

selection_statement
    : 'if' '(' expr=expression ')' '{' statement_expression? '}'
    | 'if' '(' expr=expression ')' '{' statement_expression? '}' 'else' '{' statement_expression? '}'
    ;

iteration_statement // TODO ret number til expression
    : 'for' '(' variable_statement 'to' num1=NUMBER 'by' num2=NUMBER ')' '{' statement_expression? '}'       #forSmt
    | 'while' '(' expression ')' '{' statement_expression? '}'                                               #whileSmt
    ;


/* EXPRESSIONS */
expression
    : value                                                                 #valueExpression
    //| sign=('!' | '+' | '-') expr=expression                              #signExpr
    | op1='(' expression op2=')'                                            #parenthesisExpression
    | left=expression op=('*' | '/' | '%') right=expression                 #mathematicalExpression
    | left=expression op=('+' | '-') right=expression                       #mathematicalExpression
    | left=expression op=('<' | '<=' | '>' | '>=') right=expression         #relationalExpression
    | left=expression op=('==' | '!=' ) right=expression                    #logicalExpression
    | left=expression op='&&' right=expression                              #logicalExpression
    | left=expression op='||' right=expression                              #logicalExpression
    | function_expression                                                   #functionExpression
    | variable_expression                                                   #variableExpression
    ;
//
variable_expression
    : id=IDENTIFIER                             #id
    | id=IDENTIFIER '[' expression? ']'         #array
    | id=IDENTIFIER '.' variable_expression     #dotId
    ;

function_expression
    : id=IDENTIFIER '(' (named_parameter (',' named_parameter)*)? ')'   #funcCall
    | id=IDENTIFIER '.' function_expression                             #funcDotCall
    ;

named_parameter
    : variable_statement
    | function_statement
    ;


/* DATATYPES */
value
    : BOOLEAN
    | NUMBER
    | STRING
    ;

BOOLEAN
    : ('true' | 'TRUE')
    | ('false' | 'FALSE')
    ;

NUMBER
    : DIGIT+ ('.' DIGIT+)?
    ;

STRING
    : '"' ~('"' | '\n' | '\r')* '"'
    | '\'' ~('\'' | '\n' | '\r')* '\''
    ;

IDENTIFIER
    : CHARACTER+ (CHARACTER | DIGIT)*
    ;

fragment CHARACTER: ('a'..'z'|'A'..'Z');
fragment DIGIT: ('0'..'9');


/* REMOVE WHITESPACE */
WHITESPACE: (' ' | '\t' | '\r' | '\n') -> skip;
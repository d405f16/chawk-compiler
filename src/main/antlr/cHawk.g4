grammar cHawk;

program
    : body body body
    ;

body
    : statement+
    | functionExpression+
    ;

statement
    : 'statement'
    ;

functionExpression
    : 'function()'
    ;

WHITESPACE: (' ' | '\t' | '\r' | '\n') -> skip;
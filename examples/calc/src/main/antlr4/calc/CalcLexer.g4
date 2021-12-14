lexer grammar CalcLexer;

// fragment rules are used by other rules, but do not produce tokens:
fragment DIGIT : '0' .. '9';
// Actual rule that will produce a token INT when matching the regular
// expression "DIGIT+":
INT : DIGIT+;

PLUS : '+' ;
MINUS : '-' ;
TIMES : '*' ;

// Ignore spaces, tabs, newlines and whitespaces
WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {
              skip(); // avoid producing a token
          }
    ;

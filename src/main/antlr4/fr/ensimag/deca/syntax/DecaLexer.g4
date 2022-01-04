lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.

// Reserved Words
ASM : 'asm';
CLASS : 'class';
EXTENDS : 'extends';
ELSE : 'else';
FALSE : 'false';
IF : 'if';
INSTANCEOF : 'instanceof';
NEW : 'new';
NULL :'null';
READINT : 'readInt';
READFLOAT : 'readFloat';
PRINT : 'print';
PRINTLN : 'println';
PRINTLNX : 'printlnx';
PRINTX : 'printx';
PROTECTED : 'protected';
RETURN : 'return';
THIS : 'this';
TRUE : 'true';
WHILE : 'while';

// Identifiers
fragment LETTER : 'a'..'z'|'A'..'Z';
fragment DIGIT : '0'..'9';
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' + '_')*;

// Symbols
LT : '<';
GT : '>';
EQUALS : '=';
PLUS :  '+';
MINUS : '-';
TIMES : '*';
SLASH : '/';
PERCENT : '%';
DOT : '.';
COMMA : ',';
OPARENT : '(';
CPARENT : ')';
OBRACE :  '{';
CBRACE : '}';
EXCLAM : '!';
SEMI : ';';
EQEQ : '==';
NEQ : '!=';
GEQ : '>=';
LEQ : '<=';
AND : '&&';
OR : '||';

// Integers
fragment POSITIVE_DIGIT : '1'..'9';
INT : '0' | POSITIVE_DIGIT DIGIT*; // A FAIRE (?) : Compilation Error if not represented in 32 bits

// Floats
fragment NUM : DIGIT+;
fragment SIGN : ('+' | '-')? ;
EXP : ('E' | 'e') SIGN NUM;
DEC : NUM '.' NUM;
FLOATDEC : (DEC + DEC EXP) ('F' | 'f')?;
DIGITHEX : DIGIT | 'A'..'F' | 'a'..'f';
NUMHEX : DIGITHEX+;
FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f')?;
FLOAT : FLOATDEC | FLOATHEX;

// Strings
fragment STRING_CAR : (.*?)~('"' | '\\' | '\n');
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOF | '\\"' | '\\\\')* '"';

// Separators (Skips)
WS : (' ' | '\t' | '\r' | '\n') {skip();}; // ignore white spaces
SINGLE_LINE_COMMENT : '//'(.*?)('\n') {skip();}; // ignore comments
MULTI_LINE_COMMENT : '/*' .*? '*/' {skip();};

// Includes
fragment FILENAME : (LETTER | DIGIT | '.' | '-' | '_')*;
INCLUDE : '#include' (' ')* '"' FILENAME '"' {super.doInclude(getText());};
grammar Function;

@parser::members {

}

formula
    : statement EOF
    ;

statement
    : functionStatement
    | {false}? ifStatement
    | caseStatement
    | constant
    | column
    //指数运算右结合，即2^3^4的意义为2^(3^4)
    | <assoc=right> statement op=POWER statement
    | statement op=MOD statement
    | statement op=(MUL | DIV) statement
    | statement op=(PLUS | MINUS) statement
    | statement op=(GREATER | GREATER_EQUAL | LESS | LESS_EQUAL | EQUAL | NOT_EQUAL) statement
    | statement op=(AND | OR) statement
    | L_PARENTHESES statement R_PARENTHESES
    ;

caseStatement
    : CASE
     ( (WHEN predictStatement THEN thenStmt=statement)+ | caseStmt=statement (WHEN constant THEN thenStmt=statement)+ )
     (ELSE elseStmt=statement)?
     END
    ;

ifStatement
    : IF L_PARENTHESES predictStatement R_PARENTHESES L_BRACE thenStmt=statement R_BRACE
    (ELSE ifStatement )*
    (ELSE L_BRACE elseStmt=statement R_BRACE)?
    ;

predictStatement
    : statement
    | statement op=(AND|OR) statement
   ;

functionStatement
    : (IDENTITY | IF)
    L_PARENTHESES
    functionParams?
    R_PARENTHESES
    ;

functionParams
    : statement (COMMA statement)*
    ;

column
    : BUSINESS_COLUMN_ID                                            # COLUMN_ID
    | COLUMN_ID                                                     # COLUMN_ID
    | COLUMN_NAME                                                   # COLUMN_NAME
    | IDENTITY                                                      # IDENTITY
    ;

constant
    : STRING
    | (PLUS|MINUS)? (INTEGER | FLOAT)
    | BOOL
    | NULL
    ;

L_PARENTHESES: '(';
R_PARENTHESES: ')';
L_BRACE:    '{';
R_BRACE:    '}';
COMMA: ',';
COLON:  ':';

MUL: '*';
DIV: '/';
PLUS: '+';
MINUS: '-';
POWER: '^';
MOD: '%';

GREATER: '>';
GREATER_EQUAL: '>=';
EQUAL: '==' | '=';
LESS: '<';
LESS_EQUAL: '<=';
NOT_EQUAL: '<>' | '!=';

AND: '&&' | A N D;
OR: '||'  | O R;

IF: I F;
CASE: C A S E;
WHEN: W H E N;
THEN: T H E N;
ELSE: E L S E;
END: E N D;

BOOL: T R U E | F A L S E;
NULL: N U L L ;
STRING: '"'(ESC_DQUOTE|.)*?'"' | '\''(ESC_SQUOTE|.)*?'\'';
INTEGER: DIGIT+;
FLOAT:  DIGIT+ DOT DIGIT+;
//FUNC_START: FUNC_NAME WS* L_PARENTHESES;
COLUMN_ID: SHARP INTEGER ;
BUSINESS_COLUMN_ID: SHARP INTEGER COLON INTEGER ;
IDENTITY: (ALPHA | DIGIT | CHINESE)+ ;
COLUMN_NAME: BACK_QUOTE ANY BACK_QUOTE;

//COLUMN_NAME: (ALPHA | DIGIT | CHINESE)+;
//FUNC_NAME: ALPHA (ALPHA | DIGIT)+;

WS: BLANK+                      -> skip;
LINE_COMMENT: '//'~[\r\n]*      -> skip;
BLOCK_COMMENT: '/*' .*? '*/'    -> channel(HIDDEN);

fragment DOT:               '.';
fragment BACK_QUOTE:        '`';
fragment SHARP:             '#';
fragment SIGN:              [+-];
fragment ESC_DQUOTE:        '\\"' | '\\\\';
fragment ESC_SQUOTE:        '\\\'' | '\\\\';
fragment ALPHA:             [A-Za-z_];
fragment DIGIT:             [0-9];
fragment CHINESE:           [\u4e00-\u9fa5];
fragment NL:                [\r\n];
fragment BLANK:             [ \t\r\n\u000C];
fragment ANY:               .+?;
fragment A :                [aA] ;
fragment B :                [bB] ;
fragment C :                [cC] ;
fragment D :                [dD] ;
fragment E :                [eE] ;
fragment F :                [fF] ;
fragment G :                [gG] ;
fragment H :                [hH] ;
fragment I :                [iI] ;
fragment J :                [jJ] ;
fragment K :                [kK] ;
fragment L :                [lL] ;
fragment M :                [mM] ;
fragment N :                [nN] ;
fragment O :                [oO] ;
fragment P :                [pP] ;
fragment Q :                [qQ] ;
fragment R :                [rR] ;
fragment S :                [sS] ;
fragment T :                [tT] ;
fragment U :                [uU] ;
fragment V :                [vV] ;
fragment W :                [wW] ;
fragment X :                [xX] ;
fragment Y :                [yY] ;
fragment Z :                [zZ] ;
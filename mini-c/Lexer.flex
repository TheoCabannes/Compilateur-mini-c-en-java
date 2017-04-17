
package mini_c;

import java_cup.runtime.*;
import java.util.*;
import static mini_c.sym.*;

%%

%class Lexer
%unicode
%cup
%cupdebug
%line
%column
%yylexthrow Exception

/* The symbols produced by the lexical analyzer not just integers, but objects
   of type java_cup.runtime.Symbol. To create such an object, one invokes the
   function symbol(), defined below, and supplies an integer constant, which
   identifies a terminal symbol; if necessary, one also supplies a semantic
   value, of an appropriate type -- this must match the type declared for this
   terminal symbol in Parser.cup. */

/* See https://www2.in.tum.de/repos/cup/develop/src/java_cup/runtime/ */

/* Technical note: CUP seems to assume that the two integer parameters
   passed to the Symbol constructor are character counts for the left
   and right positions. Instead, we choose to provide line and column
   information. Accordingly, we will replace CUP's error reporting
   routine with our own. */

%{

    private Symbol symbol(int id)
    {
	return new Symbol(id, yyline, yycolumn);
    }

    private Symbol symbol(int id, Object value)
    {
	return new Symbol(id, yyline, yycolumn, value);
    }

%}

/* debut de la spe c */
LineTerminator     = \r | \n | \r\n
InputCharacter     = [^\r\n]
WhiteSpace         = [ \t\f\n\r]

Comment            = "//" {InputCharacter}* {LineTerminator}
| "/*"( [^*] | (\*+[^*/]) )*\*+\/
					
Chiffre			   = [:digit:] /* 0-9 */
Alpha			   = [:jletter:] /* a-z | A-Z */
Ident			   = ({Alpha} | _) ({Alpha}|{Chiffre}|_)*


Chiffre_octal = [0-7]
Chiffre_hexa = [0-9] | [a-f] | [A-F]
// A FAIRE : Caratere ASCII entre 32 et 127
Caractere = [^\'\"\\] | "\\" | "\'" | "\"" | "\x" {Chiffre_hexa}{Chiffre_hexa}
Entier_octal = "0" {Chiffre_octal}+
Entier_hexa = "0x" {Chiffre_hexa}+
Entier_char = "\'" {Caractere} "\'"
Entier = "0" | [1-9]([0-9])*
/* fin de la spe c */

%%

/* A specification of which regular expressions to recognize and what
   symbols to produce. */

<YYINITIAL> {
/* debut de la spe c */
    
/* opérateurs : faire le uminus */
    "="		{ return symbol(EQUAL); }
    "||"    { return symbol(OR); }
    "&&"    { return symbol(AND); }
    "=="    { return symbol(CMP, Binop.Beq); }
    "!="    { return symbol(CMP, Binop.Bneq); }
    "<"     { return symbol(CMP, Binop.Blt); }
    "<="    { return symbol(CMP, Binop.Ble); }
    ">"     { return symbol(CMP, Binop.Bgt); }
    ">="    { return symbol(CMP, Binop.Bge); }
    "+"     { return symbol(PLUS); }
    "-"     { return symbol(MINUS); }
    "*"     { return symbol(TIMES); }
    "/"     { return symbol(DIV); }
    "!"     { return symbol(NOT); }
    "->"    { return symbol(FLECHE); }

/* identificateurs */
	"int"		{ return symbol(INT); }
	"struct"	{ return symbol(STRUCT); }
    "if"		{ return symbol(IF); }
    "else"  	{ return symbol(ELSE); }
    "while"		{ return symbol(WHILE); }
    "return"    { return symbol(RETURN); }
    "sizeof"	{ return symbol(SIZEOF); }
    
/* ponctuation */
	","    	{ return symbol(COMMA); }
    "("    	{ return symbol(LP); }
    ")"    	{ return symbol(RP); }
    "{"		{ return symbol(LA); }
    "}"		{ return symbol(RA); }
    ";"		{ return symbol(SEMICOLUMN); }
    
    {Ident}
    { return symbol(IDENT, yytext().intern()); }
    // The call to intern() allows identifiers to be compared using == .
    
/* Entiers PB DE LECTURE DE test('d') VOIR ENTIER_CHAR */
    {Entier_char}
    { String s = yytext(); return symbol(CST, ((int) s.charAt(1))); }
    {Entier_octal}
    { String s = yytext(); return symbol(CST, Integer.parseInt(s.substring(1, s.length()), 8)); }
    {Entier_hexa}
    { String s = yytext(); return symbol(CST, Integer.parseInt(s.substring(2, s.length()), 16)); }
    {Entier}
    { return symbol(CST, Integer.parseInt(yytext())); } 
    
    {Comment}		{ /* ignore */ }
    {WhiteSpace}    { /* ignore */ }
    
    .
    { throw new Exception (String.format (
        "Line %d, column %d: illegal character: '%s'\n", yyline + 1, yycolumn + 1, yytext()
      ));
    }
    
/* fin de la spe c */    
}



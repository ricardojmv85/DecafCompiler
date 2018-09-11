/*------------  1ra Area: Codigo de Usuario ---------*/
//------> Paquetes,importaciones
package scanner;
import java_cup.runtime.*;
import java.util.LinkedList;
import parser.Simbolos;
import java.io.FileWriter;
import java.io.PrintWriter;

/*------------  2da Area: Opciones y Declaraciones ---------*/
%%
%{
    //----> Codigo de usuario en sintaxis java
    public static LinkedList<TError> TablaEL = new LinkedList<TError>(); 
%}

//-------> Directivas
%public 
%class Analizador_Lexico
%cupsym Simbolos
%cup
%char
%column
%full
%ignorecase
%line
%unicode

//------> Expresiones Regulares
new_line = \r|\n|\r\n;
white_space = {new_line} | [ \t\f]
numero = 0 | [1-9][0-9]* 
alfanum = [a-zA-Z$_] [a-zA-Z0-9$_]*
//------> Estados

%%
/*------------  3raa Area: Reglas Lexicas ---------*/

//-----> Simbolos
<YYINITIAL> "boolean"         { System.out.println("Reconocio "+yytext()+" BOOLEAN"); return new Symbol(Simbolos.BOOLEAN, yycolumn, yyline, yytext()); }
<YYINITIAL> "break"         { System.out.println("Reconocio "+yytext()+" BREAK"); return new Symbol(Simbolos.BREAK, yycolumn, yyline, yytext()); }
<YYINITIAL> "callout"         { System.out.println("Reconocio "+yytext()+" CALLOUT"); return new Symbol(Simbolos.CALLOUT, yycolumn, yyline, yytext()); }
<YYINITIAL> "class"         { System.out.println("Reconocio "+yytext()+" CLASS"); return new Symbol(Simbolos.CLASS, yycolumn, yyline, yytext()); }
<YYINITIAL> "continue"         { System.out.println("Reconocio "+yytext()+" CONTINUE"); return new Symbol(Simbolos.CONTINUE, yycolumn, yyline, yytext()); }
<YYINITIAL> "else"         { System.out.println("Reconocio "+yytext()+" ELSE"); return new Symbol(Simbolos.ELSE, yycolumn, yyline, yytext()); }
<YYINITIAL> "false"         { System.out.println("Reconocio "+yytext()+" FALSE"); return new Symbol(Simbolos.FALSE, yycolumn, yyline, yytext()); }
<YYINITIAL> "for"         { System.out.println("Reconocio "+yytext()+" FOR"); return new Symbol(Simbolos.FOR, yycolumn, yyline, yytext()); }
<YYINITIAL> "if"         { System.out.println("Reconocio "+yytext()+" IF"); return new Symbol(Simbolos.IF, yycolumn, yyline, yytext()); }
<YYINITIAL> "int"         { System.out.println("Reconocio "+yytext()+" INT"); return new Symbol(Simbolos.INT, yycolumn, yyline, yytext()); }
<YYINITIAL> "return"         { System.out.println("Reconocio "+yytext()+" RETURN"); return new Symbol(Simbolos.RETURN, yycolumn, yyline, yytext()); }
<YYINITIAL> "true"         { System.out.println("Reconocio "+yytext()+" TRUE"); return new Symbol(Simbolos.TRUE, yycolumn, yyline, yytext()); }
<YYINITIAL> "void"         { System.out.println("Reconocio "+yytext()+" VOID"); return new Symbol(Simbolos.VOID, yycolumn, yyline, yytext()); }
<YYINITIAL> "Program"         { System.out.println("Reconocio "+yytext()+" PROGRAM"); return new Symbol(Simbolos.PROGRAM, yycolumn, yyline, yytext()); }

<YYINITIAL> "+"         { System.out.println("Reconocio "+yytext()+" PLUS"); return new Symbol(Simbolos.PLUS, yycolumn, yyline, yytext()); }
<YYINITIAL> "-"         { System.out.println("Reconocio "+yytext()+" MINUS"); return new Symbol(Simbolos.MINUS, yycolumn, yyline, yytext()); }
<YYINITIAL> "*"         { System.out.println("Reconocio "+yytext()+" MORE"); return new Symbol(Simbolos.MORE, yycolumn, yyline, yytext()); }
<YYINITIAL> "/"         { System.out.println("Reconocio "+yytext()+" DIV"); return new Symbol(Simbolos.DIV, yycolumn, yyline, yytext()); }
<YYINITIAL> "//"        { System.out.println("Reconocio "+yytext()+" COMMENT"); return new Symbol(Simbolos.COMMENT, yycolumn, yyline, yytext()); }
<YYINITIAL> "("         { System.out.println("Reconocio "+yytext()+" PARA"); return new Symbol(Simbolos.PARA, yycolumn, yyline, yytext()); }
<YYINITIAL> ")"         { System.out.println("Reconocio "+yytext()+" PARC"); return new Symbol(Simbolos.PARC, yycolumn, yyline, yytext()); }
<YYINITIAL> "!"         { System.out.println("Reconocio "+yytext()+" EXCLA"); return new Symbol(Simbolos.EXCLA, yycolumn, yyline, yytext()); }
<YYINITIAL> "\""        { System.out.println("Reconocio "+yytext()+" QUOTE"); return new Symbol(Simbolos.QUOTE, yycolumn, yyline, yytext()); }
<YYINITIAL> "#"         { System.out.println("Reconocio "+yytext()+" NUMERAL"); return new Symbol(Simbolos.NUMERAL, yycolumn, yyline, yytext()); }
<YYINITIAL> "$"         { System.out.println("Reconocio "+yytext()+" DOLAR"); return new Symbol(Simbolos.DOLAR, yycolumn, yyline, yytext()); }
<YYINITIAL> "%"         { System.out.println("Reconocio "+yytext()+" PERCENT"); return new Symbol(Simbolos.PERCENT, yycolumn, yyline, yytext()); }
<YYINITIAL> "&"         { System.out.println("Reconocio "+yytext()+" CAFE"); return new Symbol(Simbolos.CAFE, yycolumn, yyline, yytext()); }
<YYINITIAL> "\'"        { System.out.println("Reconocio "+yytext()+" SINGLEQUOTE"); return new Symbol(Simbolos.DIAGO, yycolumn, yyline, yytext()); }
<YYINITIAL> "."         { System.out.println("Reconocio "+yytext()+" POINT"); return new Symbol(Simbolos.POINT, yycolumn, yyline, yytext()); }
<YYINITIAL> ","         { System.out.println("Reconocio "+yytext()+" COMMA"); return new Symbol(Simbolos.COMMA, yycolumn, yyline, yytext()); }
<YYINITIAL> ":"         { System.out.println("Reconocio "+yytext()+" DPOINT"); return new Symbol(Simbolos.DPOINT, yycolumn, yyline, yytext()); }
<YYINITIAL> ";"         { System.out.println("Reconocio "+yytext()+" PC"); return new Symbol(Simbolos.PC, yycolumn, yyline, yytext()); }
<YYINITIAL> "<"         { System.out.println("Reconocio "+yytext()+" MINUS2"); return new Symbol(Simbolos.MINUS2, yycolumn, yyline, yytext()); }
<YYINITIAL> "="         { System.out.println("Reconocio "+yytext()+" EQUAL"); return new Symbol(Simbolos.EQUAL, yycolumn, yyline, yytext()); }
<YYINITIAL> ">"         { System.out.println("Reconocio "+yytext()+" MINUS3"); return new Symbol(Simbolos.MINUS3, yycolumn, yyline, yytext()); }
<YYINITIAL> "?"         { System.out.println("Reconocio "+yytext()+" INTER"); return new Symbol(Simbolos.INTER, yycolumn, yyline, yytext()); }
<YYINITIAL> "`"         { System.out.println("Reconocio "+yytext()+" TILD"); return new Symbol(Simbolos.TILD, yycolumn, yyline, yytext()); }
<YYINITIAL> "{"         { System.out.println("Reconocio "+yytext()+" LLAV"); return new Symbol(Simbolos.LLAV, yycolumn, yyline, yytext()); }
<YYINITIAL> "}"         { System.out.println("Reconocio "+yytext()+" LLAC"); return new Symbol(Simbolos.LLAC, yycolumn, yyline, yytext()); }
<YYINITIAL> "|"         { System.out.println("Reconocio "+yytext()+" OR"); return new Symbol(Simbolos.OR, yycolumn, yyline, yytext()); }
<YYINITIAL> "~"         { System.out.println("Reconocio "+yytext()+" EQUALS"); return new Symbol(Simbolos.EQUALS, yycolumn, yyline, yytext()); }
<YYINITIAL> "["         { System.out.println("Reconocio "+yytext()+" CORC"); return new Symbol(Simbolos.CORC, yycolumn, yyline, yytext()); }
<YYINITIAL> "]"         { System.out.println("Reconocio "+yytext()+" CORD"); return new Symbol(Simbolos.CORD, yycolumn, yyline, yytext()); }
<YYINITIAL> "\\"        { System.out.println("Reconocio "+yytext()+" DIAGO"); return new Symbol(Simbolos.DIAGO, yycolumn, yyline, yytext()); }
<YYINITIAL> "^"         { System.out.println("Reconocio "+yytext()+" EXP"); return new Symbol(Simbolos.EXP, yycolumn, yyline, yytext()); }
<YYINITIAL> "_"         { System.out.println("Reconocio "+yytext()+" FLOOR"); return new Symbol(Simbolos.FLOOR, yycolumn, yyline, yytext()); }

<YYINITIAL> {alfanum}        { System.out.println("Reconocio "+yytext()+" ALPHA_NUM"); return new Symbol(Simbolos.alpha_num, yycolumn, yyline, yytext()); }


<YYINITIAL> {white_space} {/*ignore*/}

//-------> Simbolos ER
<YYINITIAL> {numero}    { System.out.println("Reconocio "+yytext()+" NUM"); return new Symbol(Simbolos.NUM, yycolumn, yyline, yytext()); }


//------> Errores Lexicos
.                       { System.out.println("Error Lexico "+yytext()+" Linea "+yyline+" Columna "+yycolumn);
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("D:\\Sexto Semestre\\Compiladores\\Compiler\\src\\scanner\\salida.txt");
            pw = new PrintWriter(fichero);

            
                pw.println("Error Lexico "+yytext()+" Linea "+yyline+" Columna "+yycolumn);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        };
          TError datos = new TError(yytext(),yyline,yycolumn,"Error Lexico","Simbolo no existe en el lenguaje");
          TablaEL.add(datos);}
/*------------  1ra Area: Codigo de Usuario ---------*/
//------> Paquetes,importaciones
package scanner;
import java_cup.runtime.*;
import java.util.LinkedList;
import parser.Simbolos;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
/*------------  2da Area: Opciones y Declaraciones ---------*/
%%
%{
    //----> Codigo de usuario en sintaxis java
    public static LinkedList<TError> TablaEL = new LinkedList<TError>(); 

    //procedimiento que imprime el lexema en archivo
    public void imprimir(String argumento){
       BufferedWriter bw = null;
        FileWriter fw = null;
        try {

            File file = new File("D:\\Sexto Semestre\\Compiladores\\Compiler\\src\\scanner\\salida.txt");
            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            bw.write(argumento);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                            //Cierra instancias de FileWriter y BufferedWriter
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public ArrayList<String> Tokens = new ArrayList<String>();
    public void enlistar(String argumento){
        Tokens.add(argumento);
    }
    
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
new_line =  \r|\n|\r\n;
white_space = {new_line} | [ \t\f]
numero = [0-9]
letra = [a-fA-F]
hex_digit= [numero letra]
hex=[0x hex_digit hex_digit*]
alfanum = [:jletter:] [:jletterdigit:]*
//------> Estados

%%
/*------------  3raa Area: Reglas Lexicas ---------*/

//-----> Simbolos
<YYINITIAL> "boolean"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.BOOLEAN, yycolumn, yyline, yytext()); }
<YYINITIAL> "break"         {enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.BREAK, yycolumn, yyline, yytext()); }
<YYINITIAL> "callout"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.CALLOUT, yycolumn, yyline, yytext()); }
<YYINITIAL> "class"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.CLASS, yycolumn, yyline, yytext()); }
<YYINITIAL> "continue"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.CONTINUE, yycolumn, yyline, yytext()); }
<YYINITIAL> "else"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.ELSE, yycolumn, yyline, yytext()); }
<YYINITIAL> "false"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.FALSE, yycolumn, yyline, yytext()); }
<YYINITIAL> "for"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.FOR, yycolumn, yyline, yytext()); }
<YYINITIAL> "if"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.IF, yycolumn, yyline, yytext()); }
<YYINITIAL> "int"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.INT, yycolumn, yyline, yytext()); }
<YYINITIAL> "return"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.RETURN, yycolumn, yyline, yytext()); }
<YYINITIAL> "true"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.TRUE, yycolumn, yyline, yytext()); }
<YYINITIAL> "void"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.VOID, yycolumn, yyline, yytext()); }
<YYINITIAL> "Program"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.PROGRAM, yycolumn, yyline, yytext()); }
<YYINITIAL> "main"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.MAIN, yycolumn, yyline, yytext()); }


<YYINITIAL> "+"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.PLUS, yycolumn, yyline, yytext()); }
<YYINITIAL> "-"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.MINUS, yycolumn, yyline, yytext()); }
<YYINITIAL> "*"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.MORE, yycolumn, yyline, yytext()); }
<YYINITIAL> "/"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.DIV, yycolumn, yyline, yytext()); }
<YYINITIAL> "//"        { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.COMMENT, yycolumn, yyline, yytext()); }
<YYINITIAL> "("         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.PARA, yycolumn, yyline, yytext()); }
<YYINITIAL> ")"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.PARC, yycolumn, yyline, yytext()); }
<YYINITIAL> "!"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.EXCLA, yycolumn, yyline, yytext()); }
<YYINITIAL> "\""        { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.QUOTE, yycolumn, yyline, yytext()); }
<YYINITIAL> "#"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.NUMERAL, yycolumn, yyline, yytext()); }
<YYINITIAL> "$"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.DOLAR, yycolumn, yyline, yytext()); }
<YYINITIAL> "%"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.PERCENT, yycolumn, yyline, yytext()); }
<YYINITIAL> "&"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.CAFE, yycolumn, yyline, yytext()); }
<YYINITIAL> "\'"        { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.SINGLEQUOTE, yycolumn, yyline, yytext()); }
<YYINITIAL> "."         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.POINT, yycolumn, yyline, yytext()); }
<YYINITIAL> ","         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.COMMA, yycolumn, yyline, yytext()); }
<YYINITIAL> ":"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.DPOINT, yycolumn, yyline, yytext()); }
<YYINITIAL> ";"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.PC, yycolumn, yyline, yytext()); }
<YYINITIAL> "<"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.MINUS2, yycolumn, yyline, yytext()); }
<YYINITIAL> "="         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.EQUAL, yycolumn, yyline, yytext()); }
<YYINITIAL> ">"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.MINUS3, yycolumn, yyline, yytext()); }
<YYINITIAL> "?"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.INTER, yycolumn, yyline, yytext()); }
<YYINITIAL> "`"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.TILD, yycolumn, yyline, yytext()); }
<YYINITIAL> "{"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.LLAV, yycolumn, yyline, yytext()); }
<YYINITIAL> "}"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.LLAC, yycolumn, yyline, yytext()); }
<YYINITIAL> "|"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.OR, yycolumn, yyline, yytext()); }
<YYINITIAL> "~"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.EQUALS, yycolumn, yyline, yytext()); }
<YYINITIAL> "["         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.CORC, yycolumn, yyline, yytext()); }
<YYINITIAL> "]"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.CORD, yycolumn, yyline, yytext()); }
<YYINITIAL> "\\"        { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.DIAGO, yycolumn, yyline, yytext()); }
<YYINITIAL> "^"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.EXP, yycolumn, yyline, yytext()); }
<YYINITIAL> "_"         { enlistar("Type: KW, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+""); return new Symbol(Simbolos.FLOOR, yycolumn, yyline, yytext()); }

<YYINITIAL> {alfanum}        { enlistar("Type: ALPHA_NUM, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+" "); return new Symbol(Simbolos.alpha_num, yycolumn, yyline, yytext()); }


<YYINITIAL> {white_space} {/*ignore*/}

//-------> Simbolos ER
<YYINITIAL> {numero}+    { enlistar("Type: NUM, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+" "); return new Symbol(Simbolos.NUM, yycolumn, yyline, yytext()); }


//------> Errores Lexicos
.   {   enlistar("ERROR LEXICO, Value: "+yytext()+", Column: "+yycolumn+", Line: "+yyline+" ");
        TError datos = new TError(yytext(),yyline,yycolumn,"Error Lexico","Simbolo no existe en el lenguaje");
        TablaEL.add(datos);
    }
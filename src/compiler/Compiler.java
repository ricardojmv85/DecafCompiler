/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import parser.analisis_sintactico;
import scanner.Analizador_Lexico;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import scanner.Analizador_lexicup;



/**
 *
 * @author Mazinger
 */
public class Compiler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        //input de que stage se quiere
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Ingrese Comando: ");
        String stage = reader.nextLine(); 
        stage=stage.toString();
        if("scan".equals(stage)){
            scaner();
        }
        else if("scan -debug".equals(stage)){
            scaner_debug();
        }
        else if("parser".equals(stage)){
            parser();
        }
        else if("-help".equals(stage)){
            System.out.println("Uso: [stage] [-debug] \n "
                + "[stage] Hasta que proceso del compilador se desea ejecutar. \n"
                + "[-debug] Opcion si se desea visualizar en consola los procesos en tiempo real.");
        }
        else {
            System.out.println("'-help' para mostrar la lista de comandos.");
        }
    
    }
    
    public static void scaner() throws Exception{
         FileReader fr = null;
            try {
                //para debug hacer lista en el archivo .lex y que se guarde en el java solo para mandarla a imprimir.
                File archivo = new File ("D:\\Sexto Semestre\\Compiladores\\Compiler\\src\\scanner\\entrada.txt");
                File output = new File ("D:\\Sexto Semestre\\Compiladores\\Compiler\\src\\scanner\\salida.txt");
                //vaciamos el archivo de output
                BufferedWriter bw = new BufferedWriter(new FileWriter(output));
                bw.write(" Fase Scanner \n");
                bw.close();
                //leemos el archivo de input
                fr = new FileReader (archivo);
                BufferedReader br = new BufferedReader(fr);
                Analizador_Lexico lexico = new Analizador_Lexico(new BufferedReader(br));
                Analizador_lexicup sintactico = new Analizador_lexicup(lexico);
                sintactico.parse();
                //imprimir los tokens en el archivo
                for (int i=0;i<lexico.Tokens.size();i++){
                imprimir(lexico.Tokens.get(i)+"\n");
                }
                
            } catch (FileNotFoundException ex) {   
            } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void scaner_debug() throws Exception{
         FileReader fr = null;
            try {
                //para debug hacer lista en el archivo .lex y que se guarde en el java solo para mandarla a imprimir.
                File archivo = new File ("D:\\Sexto Semestre\\Compiladores\\Compiler\\src\\scanner\\entrada.txt");
                File output = new File ("D:\\Sexto Semestre\\Compiladores\\Compiler\\src\\scanner\\salida.txt");
                //vaciamos el archivo de output
                BufferedWriter bw = new BufferedWriter(new FileWriter(output));
                bw.write(" Fase Scanner \n");
                bw.close();
                //leemos el archivo de input
                fr = new FileReader (archivo);
                BufferedReader br = new BufferedReader(fr);
                Analizador_Lexico lexico = new Analizador_Lexico(new BufferedReader(br));
                Analizador_lexicup sintactico = new Analizador_lexicup(lexico);
                sintactico.parse();
                //imprimir los tokens en el archivo
                for (int i=0;i<lexico.Tokens.size();i++){
                imprimir(lexico.Tokens.get(i)+"\n");
                System.out.println(lexico.Tokens.get(i));
                }
                
            } catch (FileNotFoundException ex) {   
            } catch (IOException ex) {
            Logger.getLogger(Compiler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void parser() throws Exception{
     FileReader fr = null;
            try {
                File archivo = new File ("D:\\Sexto Semestre\\Compiladores\\Compiler\\src\\scanner\\entrada.txt");
                fr = new FileReader (archivo);
                BufferedReader br = new BufferedReader(fr);
                Analizador_Lexico lexico = new Analizador_Lexico(new BufferedReader(br));
                analisis_sintactico sintactico = new analisis_sintactico(lexico);
                try {
                    sintactico.parse();
                } catch (IOException e) {              
                }
            } catch (FileNotFoundException ex) {   
            }
    }
    public static void imprimir(String argumento){
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
}

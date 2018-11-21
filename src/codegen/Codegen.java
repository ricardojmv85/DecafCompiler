/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codegen;

import ast.Nodo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import parser.analisis_sintactico;
import scanner.Analizador_Lexico;
import java.util.Collections;
import semantic.Semantic;

/**
 *
 * @author Mazinger
 */
public class Codegen {
    
    public Nodo arbol;
    public ArrayList<List> tablita = new ArrayList<List>();
    public Codegen() throws Exception{
            generar_arbol();
            genTablita(arbol);
            //caller_assign();
            methodos(arbol);
        }
    public Integer num_fields;
    public Integer contador_ts=0;
    public Nodo este;
    public Integer cant_blocks=1;
    public Integer cant_ts=0;
    //arma la tabla de simbolos con solo el tipo, nombre, tamano y direccion de las variables globales osea las caller
    public void genTablita(Nodo hoja){
        num_fields=0;
        if(hoja.getContenido().equals("field_declm")){
            String tamano=null;
            String id;
            String type;
            if ("id[int_literal]".equals(hoja.getHijos().get(0).getContenido())){
                id=hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido();
                tamano=hoja.getHijos().get(0).getHijos().get(1).getHijos().get(0).getContenido();
            }else{
                id=hoja.getHijos().get(0).getHijos().get(0).getContenido();
            }
            type=hoja.getHijos().get(1).getHijos().get(0).getContenido();
            ArrayList<String> simbolo= new ArrayList<String>();
            simbolo.add(type);
            simbolo.add(id);
            simbolo.add(tamano);
            tablita.add(simbolo);
            num_fields++;
        }
        for (Nodo hijo:hoja.getHijos()){
            genTablita(hijo);
        }
    }
    //asigna una direccion de variable a cada variable global $16-$23
    public void caller_assign(){
        System.out.println("");
        Integer contador=0;
        for(List simbolo:tablita){
            simbolo.add("$s"+contador);
            System.out.println("addi $s"+contador+",$zero, 0");
            contador++;
        }
        
    }
    //crea los metodos
    public void methodos(Nodo hoja){
            if("method_declm".equals(hoja.getContenido())){
                Integer ts_cont=0;
                System.out.println(hoja.getHijos().get(1).getHijos().get(0).getContenido()+":");
                //si es main imprime la reserva de los globales
                if("main".equals(hoja.getHijos().get(1).getHijos().get(0).getContenido())){
                    caller_assign();
                }
                // 1 crea una tabla de simbolos local con los parametros, variables locales y variables globales, incluye su ra
                ArrayList<List> tabla_local = gen_tabla_local(hoja);
                System.out.println("# "+tabla_local);
                
                // 2 executa sus statements 
                //compara donde esta el blocque para enviarlo a statements
                System.out.println("");
                if(hoja.getHijos().size()>3){
                    ts_cont=ts_cant(hoja.getHijos().get(3));
                    statements(hoja.getHijos().get(3),ts_cont,tabla_local);
                }else { 
                    ts_cont=ts_cant(hoja.getHijos().get(2));
                    statements(hoja.getHijos().get(2),ts_cont,tabla_local);
                }
                System.out.println("");
                if(hoja.getHijos().get(1).getHijos().get(0).getContenido().equals("main")){
                    System.out.println("li $v0,10");
                    System.out.println("syscall");
                }else{
                    System.out.println("jr $ra");
                }
                System.out.println("");
            }
            for (Nodo hijo:hoja.getHijos()){
                methodos(hijo);
            }
    }
    //funcion que devuelve la cantiada de ts en un methodo 
    public Integer ts_cant(Nodo block){
        Integer num_temporales=0;
        if(block.getHijos().size()>0){
            //si hay variables
            if(block.getHijos().get(0).getContenido().equals("var_decl")){
                
                for(Nodo var_declm:block.getHijos().get(0).getHijos()){
                    num_temporales++;
                }
            }
        }
        return num_temporales;
    }
    public ArrayList gen_tabla_local(Nodo method_declm){
        ArrayList<List> tabla_simbolos=new ArrayList<List>();
        Nodo block;
        //inserta las variables globales
        for(List simbolo:tablita){
            tabla_simbolos.add(simbolo);
        }
        //si tiene parametros
        if(method_declm.getHijos().get(2).getContenido().equals("type_id")){
            Integer num_params=0;
            for(Nodo var:method_declm.getHijos().get(2).getHijos()){
                ArrayList<String> simbolo= new ArrayList<String>();
                String type=var.getHijos().get(0).getHijos().get(0).getContenido();
                String id=var.getHijos().get(1).getHijos().get(0).getContenido();
                String direccion="$a"+num_params;
                simbolo.add(type);
                simbolo.add(id);
                simbolo.add("null");
                simbolo.add(direccion);
                tabla_simbolos.add(simbolo);
                num_params++;
            }
            block=method_declm.getHijos().get(3);
        }else{block=method_declm.getHijos().get(2);}
        //si variables locales
        if(block.getHijos().size()>0){
            //si hay variables
            if(block.getHijos().get(0).getContenido().equals("var_decl")){
                Integer num_temporales=0;
                for(Nodo var_declm:block.getHijos().get(0).getHijos()){
                    ArrayList<String> simbolo= new ArrayList<String>();
                    String type=var_declm.getHijos().get(1).getHijos().get(0).getContenido();
                    String id=var_declm.getHijos().get(0).getHijos().get(0).getContenido();
                    String direccion="$t"+num_temporales;
                    simbolo.add(type);
                    simbolo.add(id);
                    simbolo.add("null");
                    simbolo.add(direccion);
                    tabla_simbolos.add(simbolo);
                    num_temporales++;
                }
            }
        }
        //inserta la direccion ra en la tabla asi se toma en cuanta para guardarla
        ArrayList<String> simbolo= new ArrayList<String>();
        simbolo.add("null");
        simbolo.add("ra");
        simbolo.add("null");
        simbolo.add("$ra");
        tabla_simbolos.add(simbolo);
        return tabla_simbolos;
    }
    
    //asignacion de parametros a los registros de argunmentos, recibe el nodo de method_call
    public void parametros(Nodo method_call,ArrayList<List> tabla_local){
        if(method_call.getHijos().size()>1){
            Integer parametros=0;
            for (Nodo parametro:method_call.getHijos().get(1).getHijos()){
                String direccion1=direccion_simbolo(parametro.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido(),tabla_local);
                System.out.println("addi $a"+parametros+", "+direccion1+", 0");
                parametros++;
            }
        }
    }
    //asginacion de parametros de los registros de argumentos, pero de expresines tipo method_Call, recibe method_call
    public void parametros2(Nodo method_call,ArrayList<List> tabla_local){
        if(method_call.getHijos().size()>1){
            Integer parametros=0;
            for(Nodo expr:method_call.getHijos().get(1).getHijos()){
                String direccion1=direccion_simbolo(expr.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido(),tabla_local);
                System.out.println("addi $a"+parametros+", "+direccion1+", 0");
                parametros++;
            }
        }
    }
    //guarda en stack los registros 
    public void save_regs(ArrayList<List> tabla){
        for (List simbolo:tabla){
            System.out.println("addi $sp, $sp, -4");
            System.out.println("sw "+simbolo.get(3)+", 0($sp)");
        }
    }
    //recupera los registros del stack
    public void load_regs(ArrayList<List> tabla){
        Integer num=0;
        Collections.reverse(tabla);
        for(List simbolo:tabla){
            System.out.println("lw "+simbolo.get(3)+", "+(num)*4+"($sp)");
            num++;
        }
        System.out.println("addi $sp, $sp, "+(num)*4);
    }
    //staments
    public void statements(Nodo hoja,Integer ts,ArrayList<List> tabla_local){
        //si el nodo block tiene statements
        if(hoja.getHijos().size()>0){
            System.out.print("# statements");
            System.out.println("");
            for(Nodo block_hijo:hoja.getHijos()){
                if(block_hijo.getContenido().equals("statement")){
                    //entra al nodo de statement
                    ArrayList<Nodo> statements = block_hijo.getHijos();
                    Collections.reverse(statements);
                    for(Nodo statement:statements){
                        //entra en el nodo statement de location_assign_expr
                        if("location_assign_expr".equals(statement.getContenido())){
                            //le envia el nodo de expr
                            String expr_dir=expresiones(statement.getHijos().get(2),ts,tabla_local);
                            //le envia el id 
                            String location_dir=direccion_simbolo(statement.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido(),tabla_local);
                            System.out.println("addi "+location_dir+", "+expr_dir+", 0");
                        }
                        else if("method_call".equals(statement.getContenido())){
                            //guardar en stack los simbolos de la tabla local
                            System.out.println("");
                            System.out.println("# Guarda en Stack");
                            save_regs(tabla_local);
                            System.out.println("");
                            //asignar los parametros 
                            System.out.println("# Asignacion de parametros");
                            parametros(statement,tabla_local);
                            System.out.println("");
                            //imprime el jump al methodo
                            System.out.println("jal "+statement.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido());
                            //recuperar del stack los simbolos de la tabla local
                            System.out.println("");
                            System.out.println("# Recupera de stack");
                            System.out.println("");
                            load_regs(tabla_local);
                            System.out.println("");
                        }else if("return_expr".equals(statement.getContenido())){
                            //guarda en v0 el valor del registro con direccion de la expresiona devolver
                            String direccion_expr1=expresiones(statement.getHijos().get(1),ts,tabla_local);
                            System.out.println("addi $v0, "+direccion_expr1+", 0");
                            
                        }else if("if_statement".equals(statement.getContenido())){
                            //de que tipo es la comparacion
                            System.out.println("");
                            System.out.println("# Entro en if");
                            expresiones(statement.getHijos().get(1),ts,tabla_local);
                            System.out.println("block"+cant_blocks+":");
                            //se declaran las nuevas variables si hay
                            if(statement.getHijos().get(2).getHijos().size()>0){
                                if(statement.getHijos().get(2).getHijos().get(0).getContenido().equals("var_decl")){
                                    tabla_local=declaracion_variables(statement.getHijos().get(2).getHijos().get(0),tabla_local,ts);
                                }
                            }
                            //se genera el block se supone
                            statements(statement.getHijos().get(0),cant_ts,tabla_local);
                            System.out.println("jr $ra");
                        }

                    }
                }
            }
        }
    }
    //declara variables de un block, recibe var_decl
    public ArrayList<List> declaracion_variables(Nodo var_decl, ArrayList<List> tabla_local,Integer ts){
        for (Nodo var_declm:var_decl.getHijos()){
            Integer sep=0;
            ArrayList<String> simbolo = new ArrayList<String>();
            simbolo.add(var_declm.getHijos().get(1).getHijos().get(0).getContenido());
            simbolo.add(var_declm.getHijos().get(0).getHijos().get(0).getContenido());
            simbolo.add("null");
            simbolo.add("$t"+ts+sep);
            tabla_local.add(simbolo);
            sep++;
            cant_ts=ts+sep;
        }
        return tabla_local;
    }
    public void generar_arbol() throws Exception{
        FileReader fr = null;
                try {
                    File archivo = new File ("D:\\Sexto Semestre\\Compiladores\\Compiler\\src\\scanner\\entrada.txt");
                    fr = new FileReader (archivo);
                    BufferedReader br = new BufferedReader(fr);
                    Analizador_Lexico lexico = new Analizador_Lexico(new BufferedReader(br));
                    analisis_sintactico sintactico = new analisis_sintactico(lexico);                 
                    try {
                        sintactico.parse();
                        //System.out.println(sintactico.Gramaticas);

                    } catch (IOException e) {              
                    }
                    if (sintactico.padre!=null){
                        //imprime(sintactico.padre);
                        arbol=sintactico.padre;
                    }else{
                    System.out.println("Error en parser");}
                } catch (FileNotFoundException ex) {   
                }
               
        }
    //da la direccion del simbolo degun el id
    public String direccion_simbolo(String id, ArrayList<List> tabla_local){
        String direccion=null;
        for (List simbolo:tabla_local){
            if(simbolo.get(1).equals(id)){
                direccion=simbolo.get(3).toString();
            }
        }
        return direccion;
    }
    //devuelven una direccion e imprimen instruccinoes 
    public String expresiones(Nodo hoja,Integer ts,ArrayList<List> tabla_local){
        String dir;
        if("location".equals(hoja.getHijos().get(0).getContenido())){
            dir= direccion_simbolo(hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido(),tabla_local);
            System.out.println("addi $t"+ts+", "+direccion_simbolo(hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido(),tabla_local)+", 0");
            return "$t"+ts;
        }
        else if("literal".equals(hoja.getHijos().get(0).getContenido())){
            String tipo=hoja.getHijos().get(0).getHijos().get(0).getContenido();
            if("int_literal".equals(tipo)){
                System.out.println("addi $t"+ts+", $zero"+", "+hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido());
                return "$t"+ts;
            }
            else if ("bool_literal".equals(tipo)){
                if(hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido().equals("true")){
                    System.out.println("addi $t"+ts+", $zero"+", 1");
                    return "$t"+ts;
                } else { 
                    System.out.println("addi $t"+ts+", $zero"+", 0");
                    return "$t"+ts;
                }
            }
            else if ("char_literal".equals(tipo)){
                return "'hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido()'";
            }
        }else if("method_call".equals(hoja.getHijos().get(0).getContenido())) {
            //guardar en stack los simbolos de la tabla local
            System.out.println("");
            System.out.println("# Guarda en Stack");
            save_regs(tabla_local);
            System.out.println("");
            //asignar los parametros 
            System.out.println("# Asignacion de parametros");
            parametros2(hoja.getHijos().get(0),tabla_local);
            System.out.println("");
            //imprime el jump al methodo
            System.out.println("jal "+hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido());
            //metete el return en un un regustro temporal
            System.out.println("addi $t"+ts+", $v0, 0");
            //recuperar del stack los simbolos de la tabla local
            System.out.println("");
            System.out.println("# Recupera de stack");
            System.out.println("");
            load_regs(tabla_local);
            System.out.println("");
            return "$t"+ts;
        }
        else if (hoja.getHijos().size()==3){
            String signo = hoja.getHijos().get(1).getHijos().get(0).getHijos().get(0).getContenido();
            String direc_expr1=expresiones(hoja.getHijos().get(0),ts+1,tabla_local);
            String direc_expr2=expresiones(hoja.getHijos().get(2),ts+2,tabla_local);
            if(signo.equals("mas")){
                System.out.println("add $t"+ts+", "+direc_expr1+", "+direc_expr2);
                return ("$t"+ts);
            }
            else if(signo.equals("menos")){
                System.out.println("sub $t"+ts+", "+direc_expr1+", "+direc_expr2);
                return ("$t"+ts);
            }
            else if(signo.equals("multi")){
                System.out.println("mult "+direc_expr1+", "+direc_expr2);
                System.out.println("mflo $t"+ts);
                return ("$t"+ts);
            }
            else if(signo.equals("div")){
                System.out.println("div "+direc_expr1+", "+direc_expr2);
                System.out.println("mflo $t"+ts);
                return ("$t"+ts);
            }
            else if(signo.equals("percent")){
                System.out.println("div "+direc_expr1+", "+direc_expr2);
                System.out.println("mfhi $t"+ts);
                return ("$t"+ts);
            }
            else if(signo.equals("mayor")){
                String direcc1=expresiones(hoja.getHijos().get(0),ts,tabla_local);
                String direcc2=expresiones(hoja.getHijos().get(2),ts+1,tabla_local);
                System.out.println("bge "+direcc1+", "+direcc2+", block"+cant_blocks);
            }
        }
        else if (hoja.getHijos().size()==2 && hoja.getHijos().get(0).getContenido().equals("menos")){
            String direc_expr1=expresiones(hoja.getHijos().get(1),ts+1,tabla_local);
            System.out.println("li $t"+ts+", -1");
            System.out.println("mult $t"+ts+", "+direc_expr1);
            System.out.println("mflo $t"+ts);
            return ("$t"+ts);
        }
        else if (hoja.getHijos().size()==2 && hoja.getHijos().get(0).getContenido().equals("excla")){
            String direc_expr1=expresiones(hoja.getHijos().get(1),ts+1,tabla_local);
            System.out.println("addi $t"+(ts+1)+", "+direc_expr1+", -1");
            System.out.println("li $t"+ts+", -1");
            System.out.println("mult $t"+(ts+1)+", $t"+ts);
            System.out.println("mflo $t"+ts);
            return ("$t"+ts);
        }
        else if (hoja.getHijos().get(0).getContenido().equals("para_expr_para")){
            return (expresiones(hoja.getHijos().get(0).getHijos().get(0),ts,tabla_local));
        }
        return "";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semantic;

import ast.Nodo;
import static compiler.Compiler.imprime;
import static compiler.Compiler.imprimir;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import parser.analisis_sintactico;
import scanner.Analizador_Lexico;

/**
 *
 * @author Mazinger
 */
public class Semantic {
        public Nodo arbol;
        public ArrayList<List> tabla = new ArrayList<List>();
        public ArrayList<String> errores = new ArrayList<String>();
        public ArrayList<String> scopes = new ArrayList<String>();
        
        //al ser instanciada la clase semantic construye el arbol 
        public Semantic() throws Exception{
            generar_arbol();
            scopes.add("global");
        }
        
        public ArrayList getTabla(){
            return tabla;
        }
        //funcion de analisis semantico
        public void analisis_semantico(Nodo arbol){
            
        }
        
        //funcion que genera el arbol y pone su raiz en la variable arbol
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
        
        
        //funcion que recorre el arbol y declara cada variables segun su scope
        public void declaracion_variables(Nodo hoja,String scope) throws Exception{
            if("block".equals(hoja.getContenido())){
                scope=hoja.getId().toString();
            }
            else if("var_declm".equals(hoja.getContenido())){
                var_decl(hoja,scope);
            }
            else if("field_declm".equals(hoja.getContenido())){
                field_declaration(hoja);
            }
            else if("method_declm".equals(hoja.getContenido())){
                method_declm(hoja);
            }
            for (Nodo hijo:hoja.getHijos()){
                declaracion_variables(hijo,scope);
            }
            
        }
        
        //veritifica que no se usa una variable sin ser declarda antes (scopes)
        public void location(Nodo hoja,String scops){
            String dinamic_scopes=scops;
            if("block".equals(hoja.getContenido())){   
                dinamic_scopes+=","+hoja.getId().toString();
            }
            if("location".equals(hoja.getContenido())){
                List<String> scopes = new ArrayList<String>(Arrays.asList(scops.split(",")));
                //System.out.println("id "+hoja.getHijos().get(0).getHijos().get(0).getContenido()+" con scopes: "+scopes);
                String id=hoja.getHijos().get(0).getHijos().get(0).getContenido();
                if("id[expr]".equals(hoja.getHijos().get(0).getContenido())){
                    
                    id=hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido();
                }
                //chequea si la variable esta declarada en los scopes que puede ver
                boolean declarada=false;
                for (String scopet:scopes){
                    for (List simbolo:tabla){
                        if ((id.equals(simbolo.get(1)))&& (scopet.equals(simbolo.get(2)))){
                            declarada=true;
                            //verifica si el id corresponde a un tipo array 
                            if(("id[expr]".equals(hoja.getHijos().get(0).getContenido()))&&(id.equals(simbolo.get(1)))&&(scopet.equals(simbolo.get(2)))&&!((simbolo.get(0).equals("INT_ARRAY"))||(simbolo.get(0).equals("BOOL_ARRAY")))){
                                errores.add("Variable: "+id+" no de tipo array");
                            }else{
                            
                            }
                        }
                    }
                }
                if(declarada==false){
                    errores.add("Variable: "+id+", no declarada");
                }
            }
            for (Nodo hijo:hoja.getHijos()){
                location(hijo,dinamic_scopes);
            }
        }
      
        
        //inserta variables en tabla de simbolos en nodo var_decl segun su scope y chequea si no hay repetidas en el mismo scope
        public void var_decl(Nodo hoja,String scope){
            Integer cantidad=hoja.getHijos().size();
            String type=hoja.getHijos().get(cantidad-1).getHijos().get(0).getContenido();
            for (int i=0;i<cantidad-1;i++){
                ArrayList<String> simbolo= new ArrayList<String>();
                String id =hoja.getHijos().get(i).getHijos().get(0).getContenido();
                //hago un simbolo con el tipo, contenido,scope y un boolean de comprobado
                simbolo.add(type);
                simbolo.add(id);
                simbolo.add(scope);
                simbolo.add("false");
                //lleno la tabla de error si esta se encuentra en la lista
                variable_repetida(id,scope);
                //inserto el simbolo en la tabla
                tabla.add(simbolo);
            }
        }
        
        //ingresa las varibles declaras en field_decl
        public void field_declaration(Nodo arbol) throws Exception{
            //compara si encuentra un field_declm
            if("field_declm".equals(arbol.getContenido())){
                //insertar en la tabla cada variable
                    //los ids
                    Integer cantidad=arbol.getHijos().size();
                    //este es el tipo
                    String type=arbol.getHijos().get(cantidad-1).getHijos().get(0).getContenido();
                    for (int i=0;i<cantidad-1;i++){
                        if("id[int_literal]".equals(arbol.getHijos().get(i).getContenido())){
                            //ingreso a la tabla de simbolos
                            ArrayList<String> simbolo= new ArrayList<String>();
                            //hago un simbolo con el tipo, contenido,scope y un boolean de comprobado
                            String id=arbol.getHijos().get(i).getHijos().get(0).getHijos().get(0).getContenido();
                            String int_literal= arbol.getHijos().get(i).getHijos().get(1).getHijos().get(0).getContenido();
                            //hago un simbolo con el tipo, contenido,scope y un boolean de comprobado
                            simbolo.add(type);
                            simbolo.add(id);
                            simbolo.add("global");
                            simbolo.add(int_literal);
                            simbolo.add("false");
                            //lleno la tabla de error si esta se encuentra en la lista
                            variable_repetida(id,"global");
                            //inserto el simbolo en la tabla
                            tabla.add(simbolo);
                            //comprueba el index mayor a 0
                            if (1>Integer.parseInt(arbol.getHijos().get(i).getHijos().get(1).getHijos().get(0).getContenido())){
                                errores.add("Indice no permitido en arreglo "+arbol.getHijos().get(i).getHijos().get(0).getHijos().get(0).getContenido());
                            }
                            
                        }else{
                            //ingreso a la tabla de simbolos
                            ArrayList<String> simbolo= new ArrayList<String>();
                            String id=arbol.getHijos().get(i).getHijos().get(0).getContenido();
                            simbolo.add(type);
                            simbolo.add(id);
                            simbolo.add("global");
                            simbolo.add("");
                            simbolo.add("false");
                            //lleno la tabla de error si esta se encuentra en la lista
                            variable_repetida(id,"global");
                            //inserto el simbolo en la tabla
                            tabla.add(simbolo);
                           
                           
                        }
                        
                    }
                }
            
        }
                //procedimiento de field_declaration
                public void variable_repetida(String id,String scope){
                    for(List simbolo:tabla){
                        if((id.equals(simbolo.get(1)))&&(scope.equals(simbolo.get(2)))){
                            errores.add("Variable repetida: "+id+", Scope: "+scope);
                        }
                    }
                }
        //verifica que los metodos int y bool contengan un return
        public void rev_metodos(Nodo hoja){
            if(hoja.getContenido().equals("method_declm")){
                rev_metodos2(hoja);
            }else{
                for (Nodo hijo:hoja.getHijos()){
                    rev_metodos(hijo);
                }
            }
        }
            //verifica que un metodo tenga return
            public void rev_metodos2(Nodo hoja){
                String type = hoja.getHijos().get(0).getHijos().get(0).getContenido();
                String id = hoja.getHijos().get(1).getHijos().get(0).getContenido();
                Nodo block = hoja.getHijos().get(3);
                //verifica que el metodo sea int o bool
                if(type.equals("INT") || type.equals("BOOL")){
                    switch (block.getHijos().size()) {
                        case 1:
                            if(block.getHijos().get(0).getContenido().equals("statement")){
                                //por cada hijo que encuentre el return
                                Nodo statement=block.getHijos().get(0);
                                boolean bandera=false;
                                for(Nodo hijo:statement.getHijos()){
                                    if (hijo.getContenido().equals("return_expr")){
                                        //revisar que la expresion sea del mismo tipo que el metodo
                                        if(type.equals(expr_type2(hijo.getHijos().get(1),"global"))){bandera=true;}
                                        
                                    }
                                }
                                if(!bandera){
                                    errores.add("return del mismo tipo faltante en metodo "+id);
                                }
                            }else{
                                errores.add("return faltante en metodo: "+id);
                            }   break;
                        case 2:
                            Nodo statement=block.getHijos().get(1);
                            boolean bandera=false;
                            for(Nodo hijo:statement.getHijos()){
                                if (hijo.getContenido().equals("return_expr")){
                                    //falta revisar que la expresion sea del mismo tipo que el metodo
                                    System.out.println(type);
                                    System.out.println(expr_type2(hijo.getHijos().get(1),"global"));
                                    if(type.equals(expr_type2(hijo.getHijos().get(1),"global"))){bandera=true;}
                                }
                            }
                            if(!bandera){
                                errores.add("return del mismo tipo faltante en metodo "+id);
                            }   break;
                        default:
                            errores.add("return faltante en metodo: "+id);
                            break;
                    }
                }
            }
      
        //recore el arbol para ver el tipo de cada expresion
        public void expr_type(Nodo hoja,String scopes){
            if("expr".equals(hoja.getContenido())){
                System.out.print("expr_"+hoja.getId()+expr_type2(hoja,scopes)+"\n");
            }else if("block".equals(hoja.getContenido())){
                scopes+=","+hoja.getId().toString();
            }
            for(Nodo hijo:hoja.getHijos()){
                expr_type(hijo,scopes);
            }
            
        }
        
        //retorna el type de la expresion
        public String expr_type2(Nodo hoja,String scops){
            String type="";
            //cuando la expresion es location
            if(hoja.getHijos().get(0).getContenido().equals("location")){
                String id=hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido();
                boolean bandera=false;
                List<String> scopes = new ArrayList<String>(Arrays.asList(scops.split(",")));
                for(String scope:scopes){
                    for (List simbolo:tabla){
                        if(simbolo.get(1).equals(id)&&simbolo.get(2).equals(scope)){
                            bandera=true;
                            type+=simbolo.get(0);
                        }
                    }
                }
                if(!bandera){
                    type="error";
                }
            }
            //cuando la expresion es literal
            else if(hoja.getHijos().get(0).getContenido().equals("literal")){
                type=hoja.getHijos().get(0).getHijos().get(0).getContenido();
                int longitud=type.length()-1;
                type=type.substring(0,longitud-7).toUpperCase();
            }
            //cuando la expresion es expr bin_op expr
            else if(hoja.getHijos().size()==3){
                String bin_op = hoja.getHijos().get(1).getHijos().get(0).getContenido();
                String expr1_type=expr_type2(hoja.getHijos().get(0),scops);
                String expr2_type=expr_type2(hoja.getHijos().get(2),scops);
                switch (bin_op) {
                    case "arith_op":
                        if(expr1_type.equals("INT")&&expr2_type.equals("INT")){type="INT";}
                        else{type="error";}
                        break;
                    case "rel_op":
                        if(expr1_type.equals("INT")&&expr2_type.equals("INT")){type="INT";}
                        else{type="error";}
                        break;
                    case "eq_op":
                        if((expr1_type.equals("INT")&&expr2_type.equals("INT"))||(expr1_type.equals("BOOL")&&expr2_type.equals("BOOL"))){type=expr2_type;}
                        else{type="error";}
                        break;
                    case "cond_op":
                        if(expr1_type.equals("BOOL")&&expr2_type.equals("BOOL")){type="BOOL";}
                        else{type="error";}
                        break;
                    default:
                        break;
                }
            }
            //cuando la expresion es de tipo !expr
            else if(hoja.getHijos().get(0).getContenido().equals("excla")){
                String expr1_type=expr_type2(hoja.getHijos().get(1),scops);
                if(expr1_type.equals("BOOL")){type="BOOL";}else{type="Error";}
            }
            //cuando la expresion es de tipo method_call
            else if(hoja.getHijos().get(0).getContenido().equals("method_call")){
                String id=hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido();
                boolean bandera=false;
                for(List simbolo:tabla){
                    if(simbolo.get(1).equals(id)){
                        type+=simbolo.get(0);
                        bandera=true;
                    }
                }
                if(!bandera){
                    type="error";
                }
            }
            return type;
        }
        
        //declaracion en tabla de simbolos los metodos y los parametros
        public void method_declm(Nodo hoja){
            ArrayList<String> simbolo= new ArrayList<String>();
            String id =hoja.getHijos().get(1).getHijos().get(0).getContenido();
            String type=hoja.getHijos().get(0).getHijos().get(0).getContenido();
            String parameter_types ="[";
            for (Nodo type_ids:hoja.getHijos().get(2).getHijos()){parameter_types+=type_ids.getHijos().get(0).getHijos().get(0).getContenido()+", ";}
            parameter_types=parameter_types.substring(0,parameter_types.length()-2);
            parameter_types+="]";
            //hago un simbolo con el tipo, contenido,scope y un boolean de comprobado
            simbolo.add(type);
            simbolo.add(id);
            simbolo.add("global");
            simbolo.add("false");
            simbolo.add(parameter_types);
            tabla.add(simbolo);
        }
        
        //chequea los parametros de los method calls con los metodos
        public void parametros_method_call(Nodo hoja,String scopes){
            if(hoja.getContenido().equals("method_call")){
                String parameters1="[";
                boolean bandera=false;
                for(Nodo expr:hoja.getHijos().get(1).getHijos()){parameters1+=expr_type2(expr,scopes)+", ";}
                parameters1=parameters1.substring(0,parameters1.length()-2);
                parameters1+="]";
                String id_method=hoja.getHijos().get(0).getHijos().get(0).getHijos().get(0).getContenido();
                for(List simbolo:tabla){
                    if(simbolo.get(1).equals(id_method)&&simbolo.size()==5){
                        //compruebo que tengan los mismos tipos de parametros y la misma cantidad
                        if(parameters1.equals(simbolo.get(4))){
                            bandera=true;
                        }
                    }
                }
                if(!bandera){errores.add("methodo "+id_method+" no coincide con sus parametros");}
            }else if("block".equals(hoja.getContenido())){   
                scopes+=","+hoja.getId().toString();
            }
            for (Nodo hijo:hoja.getHijos()){parametros_method_call(hijo,scopes);}
        }
        
        //retorna el arbol
        public Nodo getArbol(){
            return arbol;
        }
        
}

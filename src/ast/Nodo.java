/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ast;

import java.util.ArrayList;

/**
 *
 * @author Mazinger
 */
public class Nodo {
    private String contenido;
    private ArrayList<Nodo> hijos;
    private Integer id;
    
    public Nodo(String nombre,Integer id2){
        this.contenido=nombre;
        this.id=id2;
        hijos = new ArrayList<Nodo>();
    }
    public void addHijo(Nodo hijo){
        hijos.add(hijo);
    }
    
    public String getContenido(){
        return contenido;
    }
    public ArrayList<Nodo> getHijos(){
    return hijos;
    }
    public Integer getId(){
    return id;
    }
    
}

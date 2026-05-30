/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.model;
import java.io.Serializable;
/**
 *
 * @author andres
 */
public class Tablero implements Serializable{
    private static final long serialVersionUID=1L;
    private int[][] celdas;
    private int filas;
    private int columnas;
    private int caminosCompletos;
    private int totalColores;
    public Tablero(int filas,int columnas,int totalColores){
        this.filas=filas;
        this.columnas=columnas;
        this.totalColores=totalColores;
        this.caminosCompletos=0;
        this.celdas=new int[filas][columnas];
    }
    public void setCelda(int fila,int columna,int color){
        celdas[fila][columna]=color;
    }
    public int getCelda(int fila,int columna){
        return celdas[fila][columna];
    }
    public boolean isLleno(){
        for(int controlF=0;controlF<filas;controlF++){
            for(int controlC=0;controlC<columnas;controlC++){
                if(celdas[controlF][controlC]==0){
                    return false;
                }
            }
        }
        return true;
    }
    public boolean nivelResuelto(){
        return isLleno() && caminosCompletos==totalColores;
    }
    public void limpiar(){
        celdas=new int[filas][columnas];
        caminosCompletos=0;
    }
    public int getFilas(){
        return filas;
    }
    public int getColumnas(){
        return columnas;
    }
    public int getiCaminosCompletos(){
        return caminosCompletos;
    }
    public int getTotalColores(){
        return totalColores;
    }
    public void setCaminosCompletos(int caminosCompletos){
        this.caminosCompletos=caminosCompletos;
    }
}

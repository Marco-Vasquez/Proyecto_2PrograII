/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.model;

import java.io.Serializable;

/**
 *
 * @author mjosu
 */
public class Nivel implements Serializable{

    private static final long serialVersionUID = 1L;

    private int numNivel;
    private int filas;
    private int columnas;
    private int[][] puntosIniciales;
    private boolean desbloqueado;
    private String dificultad;

    public Nivel(int numNivel, int filas, int columnas, int[][] puntosIniciales, String dificultad) {
        this.numNivel = numNivel;
        this.filas = filas;
        this.columnas = columnas;
        this.puntosIniciales = puntosIniciales;
        this.dificultad = dificultad;
        this.desbloqueado = (numNivel == 1);
    }

    public int getNumNivel() { 
        return numNivel; 
    }
    
    public int getFilas() { 
        return filas; 
    }
    
    public int getColumnas() { 
        return columnas; 
    }
    
    public int[][] getPuntosIniciales() { 
        return puntosIniciales; 
    }
    
    public boolean isDesbloqueado() { 
        return desbloqueado; 
    }
    
    public String getDificultad() { 
        return dificultad; 
    }

    public void desbloquear() { 
        this.desbloqueado = true; 
    }
    
    public Tablero crearTablero(){
        Tablero tablero;
        tablero=new Tablero(filas,columnas,puntosIniciales.length);
        for(int[] punto:puntosIniciales){
            tablero.colocarPuntoOrigen(punto[0],punto[1],punto[4]);
            tablero.colocarPuntoOrigen(punto[2],punto[3],punto[4]);
        }
        return tablero;
    }
}

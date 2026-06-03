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
    public static final int CON_ARRIBA=1;
    public static final int CON_ABAJO=2;
    public static final int CON_IZQ=4;
    public static final int CON_DER=8;
    private int filas;
    private int columnas;
    private int caminosCompletos;
    private int totalColores;
    private int[][] colores;
    private EstadoCelda[][] estados;
    private int[][] conexiones;
    public Tablero(int filas,int columnas,int totalColores){
        this.filas=filas;
        this.columnas=columnas;
        this.totalColores=totalColores;
        this.caminosCompletos=0;
        this.colores=new int[filas][columnas];
        this.estados=new EstadoCelda[filas][columnas];
        this.conexiones=new int[filas][columnas];
        inicializarEstados();
    }
    private void inicializarEstados(){
        for(int fila=0;fila<filas;fila++){
            for(int columna=0;columna<columnas;columna++){
                estados[fila][columna]=EstadoCelda.VACIA;
                conexiones[fila][columna]=0;
            }
        }
    }
    public void colocarPuntoOrigen(int fila,int columna,int color){
        colores[fila][columna]=color;
        estados[fila][columna]=EstadoCelda.PUNTO_ORIGEN;
    }
    public void trazarCamino(int fila,int columna,int color){
        colores[fila][columna]=color;
        estados[fila][columna]=EstadoCelda.CAMINO;
    }
    public void agregarConexion(int fila,int columna,int mascara){
        conexiones[fila][columna]|=mascara;
    }
    public void quitarConexion(int fila,int columna,int mascara){
        conexiones[fila][columna]&=~mascara;
    }
    public boolean tieneConexion(int fila,int columna,int mascara){
        return (conexiones[fila][columna]&mascara)!=0;
    }
    public void limpiarConexiones(int fila,int columna){
        conexiones[fila][columna]=0;
    }
    public void borrarCelda(int fila,int columna){
        if(estados[fila][columna]!=EstadoCelda.PUNTO_ORIGEN){
            colores[fila][columna]=0;
            estados[fila][columna]=EstadoCelda.VACIA;
        }
        conexiones[fila][columna]=0;
    }
    public int getColor(int fila,int columna){return colores[fila][columna];}
    public EstadoCelda getEstado(int fila,int columna){return estados[fila][columna];}
    public boolean isVacia(int fila,int columna){return estados[fila][columna]==EstadoCelda.VACIA;}
    public boolean isPuntoOrigen(int fila,int columna){return estados[fila][columna]==EstadoCelda.PUNTO_ORIGEN;}
    public boolean dentroDelTablero(int fila,int columna){return fila>=0&&fila<filas&&columna>=0&&columna<columnas;}
    public void setCelda(int fila,int columna,int color){colores[fila][columna]=color;}
    public int getCelda(int fila,int columna){return colores[fila][columna];}
    public boolean isLleno(){
        for(int controlF=0;controlF<filas;controlF++){
            for(int controlC=0;controlC<columnas;controlC++){
                if(estados[controlF][controlC]==EstadoCelda.VACIA) return false;
            }
        }
        return true;
    }
    public boolean nivelResuelto(){return isLleno()&&caminosCompletos==totalColores;}
    public void reiniciar(){
        for(int fila=0;fila<filas;fila++){
            for(int columna=0;columna<columnas;columna++){
                if(estados[fila][columna]==EstadoCelda.CAMINO){
                    colores[fila][columna]=0;
                    estados[fila][columna]=EstadoCelda.VACIA;
                    conexiones[fila][columna]=0;
                }
            }
        }
        caminosCompletos=0;
    }
    public int getFilas(){return filas;}
    public int getColumnas(){return columnas;}
    public int getiCaminosCompletos(){return caminosCompletos;}
    public int getTotalColores(){return totalColores;}
    public void setCaminosCompletos(int caminosCompletos){this.caminosCompletos=caminosCompletos;}
}

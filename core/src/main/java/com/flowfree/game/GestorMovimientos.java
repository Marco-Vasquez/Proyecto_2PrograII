/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.game;

import com.flowfree.model.EstadoCelda;
import com.flowfree.model.Tablero;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mjosu
 */
public class GestorMovimientos {
    public enum ResultadoTrazo{
        AVANZO, RETROCEDIO, CERRADO, BLOQUEADO, IGNORADO
    }
    private Tablero tablero;
    private List<int[]> caminoActivo;
    private int colorActivo;
    private boolean trazando;
    private boolean trazoCerrado;
    private ResultadoTrazo ultimoResultado;
    public GestorMovimientos(Tablero tablero){
        this.tablero=tablero;
        this.caminoActivo=new ArrayList<>();
        this.colorActivo=0;
        this.trazando=false;
        this.trazoCerrado=false;
        this.ultimoResultado=ResultadoTrazo.IGNORADO;
    }
    public boolean iniciarTrazo(int fila,int columna){
        if(!tablero.dentroDelTablero(fila,columna)) return false;
        if(!tablero.isPuntoOrigen(fila,columna)) return false;
        limpiarCaminoDeColor(tablero.getColor(fila,columna));
        colorActivo=tablero.getColor(fila,columna);
        caminoActivo.clear();
        caminoActivo.add(new int[]{fila,columna});
        trazando=true;
        trazoCerrado=false;
        ultimoResultado=ResultadoTrazo.IGNORADO;
        return true;
    }
    public boolean continuarTrazo(int fila,int columna){
        if(!trazando){
            ultimoResultado=ResultadoTrazo.IGNORADO;
            return false;
        }
        if(!tablero.dentroDelTablero(fila,columna)){
            ultimoResultado=ResultadoTrazo.IGNORADO;
            return false;
        }
        int[] celdaUltima=caminoActivo.get(caminoActivo.size()-1);
        if(!sonAdyacentes(celdaUltima[0],celdaUltima[1],fila,columna)){
            ultimoResultado=ResultadoTrazo.IGNORADO;
            return false;
        }
        for(int posicion=0;posicion<caminoActivo.size();posicion++){
            int[] celdaEnCamino=caminoActivo.get(posicion);
            if(celdaEnCamino[0]==fila&&celdaEnCamino[1]==columna){
                retrocederHasta(posicion);
                ultimoResultado=ResultadoTrazo.RETROCEDIO;
                return true;
            }
        }
        if(!tablero.isVacia(fila,columna)){
            boolean esPuntoDelMismoColor=tablero.isPuntoOrigen(fila,columna)
                    &&tablero.getColor(fila,columna)==colorActivo;
            if(!esPuntoDelMismoColor){
                ultimoResultado=ResultadoTrazo.BLOQUEADO;
                return false;
            }
            conectarCeldas(celdaUltima[0],celdaUltima[1],fila,columna);
            caminoActivo.add(new int[]{fila,columna});
            trazando=false;
            trazoCerrado=true;
            ultimoResultado=ResultadoTrazo.CERRADO;
            return true;
        }
        tablero.trazarCamino(fila,columna,colorActivo);
        conectarCeldas(celdaUltima[0],celdaUltima[1],fila,columna);
        caminoActivo.add(new int[]{fila,columna});
        ultimoResultado=ResultadoTrazo.AVANZO;
        return true;
    }
    public void terminarTrazo(){
        trazando=false;
        trazoCerrado=false;
        colorActivo=0;
        caminoActivo.clear();
        ultimoResultado=ResultadoTrazo.IGNORADO;
    }
    private void conectarCeldas(int filaA,int colA,int filaB,int colB){
        if(filaB==filaA-1){
            tablero.agregarConexion(filaA,colA,Tablero.CON_ARRIBA);
            tablero.agregarConexion(filaB,colB,Tablero.CON_ABAJO);
        }else if(filaB==filaA+1){
            tablero.agregarConexion(filaA,colA,Tablero.CON_ABAJO);
            tablero.agregarConexion(filaB,colB,Tablero.CON_ARRIBA);
        }else if(colB==colA-1){
            tablero.agregarConexion(filaA,colA,Tablero.CON_IZQ);
            tablero.agregarConexion(filaB,colB,Tablero.CON_DER);
        }else if(colB==colA+1){
            tablero.agregarConexion(filaA,colA,Tablero.CON_DER);
            tablero.agregarConexion(filaB,colB,Tablero.CON_IZQ);
        }
    }
    private void limpiarCaminoDeColor(int color){
        for(int fila=0;fila<tablero.getFilas();fila++){
            for(int columna=0;columna<tablero.getColumnas();columna++){
                if(tablero.getColor(fila,columna)==color
                        &&tablero.getEstado(fila,columna)==EstadoCelda.CAMINO){
                    tablero.borrarCelda(fila,columna);
                }
            }
        }
    }
    private void retrocederHasta(int posicion){
        while(caminoActivo.size()-1>posicion){
            int ultimoIndice=caminoActivo.size()-1;
            int[] celda=caminoActivo.get(ultimoIndice);
            int[] anterior=caminoActivo.get(ultimoIndice-1);
            desconectarCeldas(anterior[0],anterior[1],celda[0],celda[1]);
            tablero.borrarCelda(celda[0],celda[1]);
            caminoActivo.remove(ultimoIndice);
        }
        if(caminoActivo.size()==1){
            int[] origen=caminoActivo.get(0);
            tablero.limpiarConexiones(origen[0],origen[1]);
        }
    }
    private void desconectarCeldas(int filaA,int colA,int filaB,int colB){
        if(filaB==filaA-1){
            tablero.quitarConexion(filaA,colA,Tablero.CON_ARRIBA);
            tablero.quitarConexion(filaB,colB,Tablero.CON_ABAJO);
        }else if(filaB==filaA+1){
            tablero.quitarConexion(filaA,colA,Tablero.CON_ABAJO);
            tablero.quitarConexion(filaB,colB,Tablero.CON_ARRIBA);
        }else if(colB==colA-1){
            tablero.quitarConexion(filaA,colA,Tablero.CON_IZQ);
            tablero.quitarConexion(filaB,colB,Tablero.CON_DER);
        }else if(colB==colA+1){
            tablero.quitarConexion(filaA,colA,Tablero.CON_DER);
            tablero.quitarConexion(filaB,colB,Tablero.CON_IZQ);
        }
    }
    private boolean sonAdyacentes(int fila1,int columna1,int fila2,int columna2){
        return(Math.abs(fila1-fila2)+Math.abs(columna1-columna2))==1;
    }
    public boolean isTrazando(){
        return trazando;
    }
    public boolean isTrazoCerrado(){
        return trazoCerrado;
    }
    public int getColorActivo(){
        return colorActivo;
    }
    public Tablero getTablero(){
        return tablero;
    }
    public ResultadoTrazo getUltimoResultado(){
        return ultimoResultado;
    }
}

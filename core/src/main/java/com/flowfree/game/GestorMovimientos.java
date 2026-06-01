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

    private Tablero tablero;
    private List<int[]> caminoActivo;
    private int colorActivo;
    private boolean trazando;

    public GestorMovimientos(Tablero tablero) {
        this.tablero = tablero;
        this.caminoActivo = new ArrayList<>();
        this.colorActivo = 0;
        this.trazando = false;
    }

    public boolean iniciarTrazo(int fila,int columna){
        if (!tablero.dentroDelTablero(fila,columna)){
            return false;
        }
        if (!tablero.isPuntoOrigen(fila,columna)){
            return false;
        }

        limpiarCaminoDeColor(tablero.getColor(fila, columna));

        colorActivo = tablero.getColor(fila, columna);
        caminoActivo.clear();
        caminoActivo.add(new int[]{fila, columna});
        trazando = true;
        return true;
    }

    public boolean continuarTrazo(int fila, int columna) {
        if (!trazando){
            return false;
        }
        if (!tablero.dentroDelTablero(fila, columna)){
            return false;
        }

        int[] celdaUltima = caminoActivo.get(caminoActivo.size() - 1);

        if (!sonAdyacentes(celdaUltima[0], celdaUltima[1], fila, columna)){
            return false;
        }

        for (int posicion = 0; posicion < caminoActivo.size(); posicion++) {
            int[] celdaEnCamino = caminoActivo.get(posicion);
            if (celdaEnCamino[0] == fila && celdaEnCamino[1] == columna) {
                retrocederHasta(posicion);
                return true;
            }
        }

        if (!tablero.isVacia(fila, columna)) {
            boolean esPuntoDelMismoColor = tablero.isPuntoOrigen(fila, columna)
                    && tablero.getColor(fila, columna) == colorActivo;
            if (!esPuntoDelMismoColor) return false;
        }

        tablero.trazarCamino(fila, columna, colorActivo);
        caminoActivo.add(new int[]{fila, columna});
        return true;
    }

    public void terminarTrazo() {
        trazando = false;
        colorActivo = 0;
        caminoActivo.clear();
    }

    private void limpiarCaminoDeColor(int color) {
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (tablero.getColor(fila, columna) == color
                        && tablero.getEstado(fila, columna) == EstadoCelda.CAMINO) {
                    tablero.borrarCelda(fila, columna);
                }
            }
        }
    }

    private void retrocederHasta(int posicion) {
        while (caminoActivo.size() - 1 > posicion) {
            int ultimoIndice = caminoActivo.size() - 1;
            int[] celda = caminoActivo.get(ultimoIndice);
            tablero.borrarCelda(celda[0], celda[1]);
            caminoActivo.remove(ultimoIndice);
        }
    }

    private boolean sonAdyacentes(int fila1, int columna1, int fila2, int columna2) {
        return (Math.abs(fila1 - fila2) + Math.abs(columna1 - columna2)) == 1;
    }

    public boolean isTrazando(){
        return trazando; 
    }
    public int getColorActivo(){
        return colorActivo; 
    }
    public Tablero getTablero(){
        return tablero; 
    }
}

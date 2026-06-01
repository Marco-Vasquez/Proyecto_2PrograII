/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.game;

import com.flowfree.base.Juego;
import com.flowfree.model.EstadoCelda;
import com.flowfree.model.Nivel;
import com.flowfree.model.Tablero;
/**
 *
 * @author andres
 */
public class FlowFree extends Juego {

    private Nivel nivelActivo;
    private Tablero tablero;
    private GestorMovimientos gestorMovimientos;

    private int movimientos;
    private int fallos;
    private boolean nivelCompleto;

    public FlowFree() {
        super("Flow Free");
    }

    public void cargarNivel(Nivel nivel) {
        this.nivelActivo = nivel;
        this.nivelAct = nivel.getNumNivel();
        this.tablero = nivel.crearTablero();
        this.gestorMovimientos = new GestorMovimientos(tablero);
        iniciar();
    }

    @Override
    public void iniciar() {
        movimientos = 0;
        fallos = 0;
        nivelCompleto = false;
        enPausa = false;
    }

    @Override
    public void pausar() {
        enPausa = !enPausa;
    }

    @Override
    public void reiniciar() {
        if (nivelActivo != null) {
            tablero = nivelActivo.crearTablero();
            gestorMovimientos = new GestorMovimientos(tablero);
        }
        iniciar();
    }

    @Override
    public void terminar() {
        enPausa = true;
    }

    @Override
    public boolean verificarVictoria() {
        return nivelCompleto;
    }

    public boolean iniciarTrazo(int fila, int columna) {
        if (enPausa || nivelCompleto){
            return false;
        }
        else{
            return gestorMovimientos.iniciarTrazo(fila, columna);
        }
    }

    public boolean continuarTrazo(int fila, int columna) {
        if (enPausa || nivelCompleto){
            return false;
        }
        boolean exito = gestorMovimientos.continuarTrazo(fila, columna);
        if (exito){
            movimientos++;
        }
        return exito;
    }

    public void terminarTrazo() {
        gestorMovimientos.terminarTrazo();
        verificarEstadoNivel();
    }

    private void verificarEstadoNivel() {
        int completos = contarCaminosCompletos();
        tablero.setCaminosCompletos(completos);
        if (tablero.nivelResuelto()) {
            nivelCompleto = true;
        }
    }

    private int contarCaminosCompletos() {
        int completos = 0;
        for (int color = 1; color <= tablero.getTotalColores(); color++) {
            if (colorTieneDosOrigenes(color) && colorTieneCamino(color)) {
                completos++;
            }
        }
        return completos;
    }

    private boolean colorTieneDosOrigenes(int color) {
        int conteo = 0;
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (tablero.getColor(fila, columna) == color
                        && tablero.isPuntoOrigen(fila, columna)) {
                    conteo++;
                }
            }
        }
        return conteo == 2;
    }

    private boolean colorTieneCamino(int color) {
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                if (tablero.getColor(fila, columna) == color
                        && tablero.getEstado(fila, columna) == EstadoCelda.CAMINO) {
                    return true;
                }
            }
        }
        return false;
    }

    public void registrarFallo() {
        fallos++;
    }

    public Tablero getTablero() { 
        return tablero; 
    }
    public Nivel getNivelActivo() { 
        return nivelActivo; 
    }
    public int getMovimientos() { 
        return movimientos; 
    }
    public int getFallos() { 
        return fallos; 
    }
    public boolean isNivelCompleto() { 
        return nivelCompleto; 
    }
    public GestorMovimientos getGestorMovimientos() { 
        return gestorMovimientos; 
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mjosu
 */
public class Estadisticas implements Serializable {
    private static final long serialVersionUID=1L;
    private int partidasJugadas;
    private int nivelesCompletados;
    private long tiempoTotalSeg;
    private int totalMovimientos;
    private int totalFallos;
    private int puntuacionGeneral;
    private List<String> historial;
    private Map<Integer,Long> mejorTiempoPorNivel;
    private Map<Integer,Integer> mejorMovimientosPorNivel;
    private Map<Integer,Integer> mejorFallosPorNivel;
    private Map<Integer,Integer> mejorPuntosPorNivel;
    public Estadisticas(){
        this.partidasJugadas=0;
        this.nivelesCompletados=0;
        this.tiempoTotalSeg=0;
        this.totalMovimientos=0;
        this.totalFallos=0;
        this.puntuacionGeneral=0;
        this.historial=new ArrayList<>();
        this.mejorTiempoPorNivel=new HashMap<>();
        this.mejorMovimientosPorNivel=new HashMap<>();
        this.mejorFallosPorNivel=new HashMap<>();
        this.mejorPuntosPorNivel=new HashMap<>();
    }
    private void inicializarCamposFaltantes(){
        if(historial==null) historial=new ArrayList<>();
        if(mejorTiempoPorNivel==null) mejorTiempoPorNivel=new HashMap<>();
        if(mejorMovimientosPorNivel==null) mejorMovimientosPorNivel=new HashMap<>();
        if(mejorFallosPorNivel==null) mejorFallosPorNivel=new HashMap<>();
        if(mejorPuntosPorNivel==null) mejorPuntosPorNivel=new HashMap<>();
    }
    public void registrarPartida(int nivel,long segundos,int movimientos,int fallos,int puntos){
        inicializarCamposFaltantes();
        partidasJugadas++;
        boolean esPrimeraVez=!mejorTiempoPorNivel.containsKey(nivel);
        if(esPrimeraVez){
            nivelesCompletados++;
            tiempoTotalSeg+=segundos;
            totalMovimientos+=movimientos;
            totalFallos+=fallos;
            puntuacionGeneral+=puntos;
            mejorTiempoPorNivel.put(nivel,segundos);
            mejorMovimientosPorNivel.put(nivel,movimientos);
            mejorFallosPorNivel.put(nivel,fallos);
            mejorPuntosPorNivel.put(nivel,puntos);
            historial.add(formatearEntrada(nivel,segundos,movimientos,fallos,puntos));
            return;
        }
        long tiempoAnterior=mejorTiempoPorNivel.get(nivel);
        if(segundos>=tiempoAnterior) return;
        int movAnterior=mejorMovimientosPorNivel.get(nivel);
        int fallosAnterior=mejorFallosPorNivel.get(nivel);
        int puntosAnterior=mejorPuntosPorNivel.get(nivel);
        tiempoTotalSeg=tiempoTotalSeg-tiempoAnterior+segundos;
        totalMovimientos=totalMovimientos-movAnterior+movimientos;
        totalFallos=totalFallos-fallosAnterior+fallos;
        puntuacionGeneral=puntuacionGeneral-puntosAnterior+puntos;
        mejorTiempoPorNivel.put(nivel,segundos);
        mejorMovimientosPorNivel.put(nivel,movimientos);
        mejorFallosPorNivel.put(nivel,fallos);
        mejorPuntosPorNivel.put(nivel,puntos);
        actualizarHistorialNivel(nivel,segundos,movimientos,fallos,puntos);
    }
    private String formatearEntrada(int nivel,long segundos,int movimientos,int fallos,int puntos){
        return String.format("Nivel %d | %02d:%02d | Mov: %d | Fallos: %d | Pts: %d",
            nivel,segundos/60,segundos%60,movimientos,fallos,puntos);
    }
    private void actualizarHistorialNivel(int nivel,long segundos,int movimientos,int fallos,int puntos){
        String prefijo="Nivel "+nivel+" |";
        for(int posicion=0;posicion<historial.size();posicion++){
            if(historial.get(posicion).startsWith(prefijo)){
                historial.set(posicion,formatearEntrada(nivel,segundos,movimientos,fallos,puntos));
                return;
            }
        }
    }
    public double getTiempoPromedioPorNivel(){
        if(nivelesCompletados==0) return 0;
        return(double)tiempoTotalSeg/nivelesCompletados;
    }
    public boolean tieneRegistroParaNivel(int nivel){
        inicializarCamposFaltantes();
        return mejorTiempoPorNivel.containsKey(nivel);
    }
    public long getMejorTiempoNivel(int nivel){
        inicializarCamposFaltantes();
        return mejorTiempoPorNivel.getOrDefault(nivel,0L);
    }
    public int getPartidasJugadas(){
        return partidasJugadas;
    }
    public int getNivelesCompletados(){
        return nivelesCompletados;
    }
    public long getTiempoTotalSeg(){
        return tiempoTotalSeg;
    }
    public int getTotalMovimientos(){
        return totalMovimientos;
    }
    public int getTotalFallos(){
        return totalFallos;
    }
    public int getPuntuacionGeneral(){
        return puntuacionGeneral;
    }
    public List<String> getHistorial(){
        inicializarCamposFaltantes();
        return historial;
    }
}
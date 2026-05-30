/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mjosu
 */
public class Estadisticas implements Serializable {

    private static final long serialVersionUID = 1L;

    private int partidasJugadas;
    private int nivelesCompletados;
    private long tiempoTotalSeg;
    private int totalMovimientos;
    private int totalFallos;
    private int puntuacionGeneral;
    private List<String> historial;

    public Estadisticas() {
        this.partidasJugadas = 0;
        this.nivelesCompletados = 0;
        this.tiempoTotalSeg = 0;
        this.totalMovimientos = 0;
        this.totalFallos = 0;
        this.puntuacionGeneral = 0;
        this.historial = new ArrayList<>();
    }

    public void registrarPartida(int nivel, long segundos, int movimientos, int fallos, int puntos) {
        partidasJugadas++;
        nivelesCompletados++;
        tiempoTotalSeg += segundos;
        totalMovimientos += movimientos;
        totalFallos += fallos;
        puntuacionGeneral += puntos;
        historial.add("Nivel "+ nivel+" | Tiempo: "+segundos+"s | Mov: "+movimientos+
                " | Fallos: "+fallos+" | Pts: "+puntos);
    }

    public double getTiempoPromedioPorNivel() {
        if (nivelesCompletados == 0) {
            return 0;
        }
        else{
            return (double) tiempoTotalSeg / nivelesCompletados;
        }
    }

    public int getPartidasJugadas() { 
        return partidasJugadas; 
    }
    
    public int getNivelesCompletados() { 
        return nivelesCompletados; 
    }
    
    public long getTiempoTotalSeg() { 
        return tiempoTotalSeg; 
    }
    
    public int getTotalMovimientos() { 
        return totalMovimientos; 
    }
    
    public int getTotalFallos() { 
        return totalFallos; 
    }
    
    public int getPuntuacionGeneral() { 
        return puntuacionGeneral; 
    }
    
    public List<String> getHistorial() { 
        return historial; 
    }
    
}
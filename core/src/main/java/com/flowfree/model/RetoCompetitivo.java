/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.model;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 *
 * @author andres
 */
public class RetoCompetitivo implements Serializable{
    private static final long serialVersionUID=1L;
    public enum EstadoReto{
        PENDIENTE,ACEPTADO,RECHAZADO,COMPLETADO
    }
    private String retador;
    private String retado;
    private int numNivel;
    private LocalDateTime fechaEnvio;
    private EstadoReto estado;
    private long tiempoRetador;
    private int movimientosRetador;
    private int fallosRetador;
    private long tiempoRetado;
    private int movimientosRetado;
    private int fallosRetado;
    private String ganador;
    public RetoCompetitivo(String retador,String retado,int numNivel){
        this.retador=retador;
        this.retado=retado;
        this.numNivel=numNivel;
        this.fechaEnvio=LocalDateTime.now();
        this.estado=EstadoReto.PENDIENTE;
        this.tiempoRetador=0;
        this.movimientosRetador=0;
        this.fallosRetador=0;
        this.tiempoRetado=0;
        this.movimientosRetado=0;
        this.fallosRetado=0;
        this.ganador=null;
    }
    public void registrarResultadoRetador(long tiempo,int movimientos,int fallos){
        this.tiempoRetador=tiempo;
        this.movimientosRetador=movimientos;
        this.fallosRetador=fallos;
        this.estado=EstadoReto.ACEPTADO;
    }
    public void registrarResultadoRetado(long tiempo,int movimientos,int fallos){
        this.tiempoRetado=tiempo;
        this.movimientosRetado=movimientos;
        this.fallosRetado=fallos;
        this.estado=EstadoReto.COMPLETADO;
        determinarGanador();
    }
    private void determinarGanador(){
        if(tiempoRetador<tiempoRetado) ganador=retador;
        else if(tiempoRetado<tiempoRetador) ganador=retado;
        else if(movimientosRetador<movimientosRetado) ganador=retador;
        else if(movimientosRetado<movimientosRetador) ganador=retado;
        else if(fallosRetador<fallosRetado) ganador=retador;
        else if(fallosRetado<fallosRetador) ganador=retado;
        else ganador="empate";
    }
    public void rechazar(){this.estado=EstadoReto.RECHAZADO;}
    public String getRetador(){return retador;}
    public String getRetado(){return retado;}
    public int getNumNivel(){return numNivel;}
    public LocalDateTime getFechaEnvio(){return fechaEnvio;}
    public EstadoReto getEstado(){return estado;}
    public long getTiempoRetador(){return tiempoRetador;}
    public int getMovimientosRetador(){return movimientosRetador;}
    public int getFallosRetador(){return fallosRetador;}
    public long getTiempoRetado(){return tiempoRetado;}
    public int getMovimientosRetado(){return movimientosRetado;}
    public int getFallosRetado(){return fallosRetado;}
    public String getGanador(){return ganador;}
}

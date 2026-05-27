/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.game;
import com.flowfree.base.Juego;
/**
 *
 * @author andres
 */
public class FlowFree extends Juego {
    private int movimientos;
    private int fallos;
    private boolean nivelCompleto;
    public FlowFree(){
        super("Flow Free");
        this.movimientos=0;
        this.fallos=0;
        this.nivelCompleto=false;
    }
    public void iniciar(){
        movimientos=0;
        fallos=0;
        nivelCompleto=false;
        enPausa=false;
    }
    public void pausar(){
        enPausa=!enPausa;
    }
    public void reiniciar(){
        iniciar();
    }
    public void terminar(){
        enPausa=true;
    }
    public boolean verificarVictoria(){
        return nivelCompleto;
    }
    public void registrarMovimiento(){
        movimientos++;
    }
    public void registrarFallos(){
        fallos++;
    }
    public void setNivelCompleto(boolean nivelCompleto){
        this.nivelCompleto=nivelCompleto;
    }
    public void avanzarNivel(){
        nivelAct++;
        iniciar();
    }
    public int getMovimientos(){
        return movimientos;
    }
    public int getFallos(){
        return fallos;
    }
    public boolean isNivelCompleto(){
        return nivelCompleto;
    }
}

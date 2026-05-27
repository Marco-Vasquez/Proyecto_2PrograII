/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.base;

/**
 *
 * @author andres
 */
public abstract class Juego {
    protected String nombreJuego;
    protected int nivelAct;
    protected boolean enPausa;
    public Juego(String nombreJuego){
        this.nombreJuego=nombreJuego;
        this.nivelAct=1;
        this.enPausa=false;
    }
    public abstract void iniciar();
    public abstract void pausar();
    public abstract void reiniciar();
    public abstract void terminar();
    public abstract boolean verificarVictoria();
    public int getNivelAct(){
        return nivelAct;
    }
    public String getNombreJuego(){
        return nombreJuego;
    }
    public boolean isEnPausa(){
        return enPausa;
    }
}

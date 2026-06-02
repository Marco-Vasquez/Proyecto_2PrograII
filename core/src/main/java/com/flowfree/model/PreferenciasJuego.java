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
public class PreferenciasJuego implements Serializable{
    private static final long serialVersionUID=1L;
    private float volumen;
    private String idioma;
    private String controlMov;
    public PreferenciasJuego(){
        this.volumen=1.0f;
        this.idioma="es";
        this.controlMov="arrastre";
    }
    public float getVolumen(){
        return volumen;
    }
    public String getIdioma(){
        return idioma;
    }
    public String getControlMov(){
        return controlMov;
    }
    public void setVolumen(float volumen){
        this.volumen=volumen;
    }
    public void setIdioma(String idioma){
        this.idioma=idioma;
    }
    public void setControlMov(String controlMov){
        this.controlMov=controlMov;
    }
}

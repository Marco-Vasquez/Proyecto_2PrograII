/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.model;

import java.io.Serializable;

/**
 *
 * @author mjosu
 */
public class Perfil implements Serializable{
    
    private static final long serialVersionUID=1L;
    private int avatarIndex;
    private String rutaFotoPerfil;
    private float volumen;
    private String idioma;
    private boolean musicaActiva;
    private boolean idiomaEspanol;
    
    public Perfil(){
        this.avatarIndex=0;
        this.rutaFotoPerfil=null;
        this.volumen=0.5f;
        this.idioma="es";
        this.musicaActiva=true;
        this.idiomaEspanol=true;
    }
    public int getAvatarIndex(){
        return avatarIndex;
    }
    public String getRutaFotoPerfil(){
        return rutaFotoPerfil;
    }
    public float getVolumen(){
        return volumen;
    }
    public String getIdioma(){
        return idioma;
    }
    public boolean isMusicaActiva(){
        return musicaActiva;
    }
    public boolean isIdiomaEspanol(){
        return idiomaEspanol;
    }
    public void setAvatarIndex(int avatarIndex){
        this.avatarIndex=avatarIndex;
    }
    public void setRutaFotoPerfil(String rutaFotoPerfil){
        this.rutaFotoPerfil=rutaFotoPerfil;
    }
    public void setVolumen(float volumen){
        this.volumen=volumen;
    }
    public void setIdioma(String idioma){
        this.idioma=idioma;
    }
    public void setMusicaActiva(boolean musicaActiva){
        this.musicaActiva=musicaActiva;
    }
    public void setIdiomaEspanol(boolean idiomaEspanol){
        this.idiomaEspanol=idiomaEspanol;
        this.idioma=idiomaEspanol ? "es" : "en";
    }
}
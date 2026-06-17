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
    private Object readResolve(){
        if(volumen<=0f) volumen=0.5f;
        if(idioma==null) idioma="es";
        if(idioma.equals("es")) idiomaEspanol=true;
        return this;
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
        return "es".equals(idioma);
    }
    public void setAvatarIndex(int idx){
        this.avatarIndex=idx;
    }
    public void setRutaFotoPerfil(String ruta){
        this.rutaFotoPerfil=ruta;
    }
    public void setVolumen(float volumen){
        this.volumen=Math.max(0f,Math.min(1f,volumen));
    }
    public void setIdioma(String idioma){
        this.idioma=idioma;
    }
    public void setMusicaActiva(boolean musicaActiva){
        this.musicaActiva=musicaActiva;
    }
    public void setIdiomaEspanol(boolean espanol){
        this.idioma = espanol ? "es" : "en";
    }
}
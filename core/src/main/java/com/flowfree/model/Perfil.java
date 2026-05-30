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

    private static final long serialVersionUID = 1L;

    private int avatarIndex;
    private String rutaFotoPerfil;
    private float volumen;
    private String idioma;

    public Perfil() {
        this.avatarIndex = 0;
        this.rutaFotoPerfil = null;
        this.volumen = 1.0f;
        this.idioma = "es";
    }

    public int getAvatarIndex() { 
        return avatarIndex; 
    }
    
    public String getRutaFotoPerfil() { 
        return rutaFotoPerfil; 
    }
    
    public float getVolumen() { 
        return volumen; 
    }
    
    public String getIdioma() {
        return idioma;
    }
    
    public void setAvatarIndex(int avatarIndex) {
        this.avatarIndex = avatarIndex;
    }
    
    public void setRutaFotoPerfil(String rutaFotoPerfil) {
        this.rutaFotoPerfil = rutaFotoPerfil;
    }
    
    public void setVolumen(float volumen) {
        this.volumen = volumen;
    }
    
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
    
}
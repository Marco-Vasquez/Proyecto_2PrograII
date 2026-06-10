/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mjosu
 */
public class Usuario implements Serializable{
    
    private static final long serialVersionUID = 1L;

    private String username;
    private String passwordHash;
    private String salt;
    private String nombreCompleto;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimaSesion;
    private int nivelDesbloqueado;
    private List<String> amigos;
    private Perfil perfil;
    private Estadisticas estadisticas;

    public Usuario(String username, String password, String salt, String nombreCompleto){
        this.username = username;
        this.passwordHash = password;
        this.salt = salt;
        this.nombreCompleto = nombreCompleto;
        this.fechaRegistro = LocalDateTime.now();
        this.ultimaSesion = LocalDateTime.now();
        this.nivelDesbloqueado = 1;
        this.amigos = new ArrayList<>();
        this.perfil = new Perfil();
        this.estadisticas = new Estadisticas();
    }

    public void agregarAmigo(String usernameAmigo){
        if (!amigos.contains(usernameAmigo)){
            amigos.add(usernameAmigo);
        }
    }

    public void actualizarUltimaSesion() {
        this.ultimaSesion = LocalDateTime.now();
    }

    public String getUsername() { 
        return username; 
    }
    
    public String getPasswordHash() { 
        return passwordHash; 
    }
    
    public String getSalt() { 
        return salt; 
    }
    
    public String getNombreCompleto() { 
        return nombreCompleto; 
    }
    
    public LocalDateTime getFechaRegistro() { 
        return fechaRegistro; 
    }
    
    public LocalDateTime getUltimaSesion() { 
        return ultimaSesion; 
    }
    
    public int getNivelDesbloqueado() { 
        return nivelDesbloqueado; 
    }
    
    public List<String> getAmigos() { 
        return amigos; 
    }
    
    public Perfil getPerfil() { 
        return perfil; 
    }
    
    public Estadisticas getEstadisticas() { 
        return estadisticas; 
    }

    public void setNivelDesbloqueado(int nivelDesbloqueado) { 
        this.nivelDesbloqueado = nivelDesbloqueado; 
    }
    
    public void setNombreCompleto(String nombreCompleto) { 
        this.nombreCompleto = nombreCompleto; 
    }
    
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil; 
    }
    
    public void setEstadisticas(Estadisticas estadisticas) { 
        this.estadisticas = estadisticas; 
    }
    
    public String toString(){
        return "Usuario{username="+username+", nombre="+nombreCompleto+", registro="+fechaRegistro.toLocalDate()+", nivelDesbloqueado="+nivelDesbloqueado+", partidas="+estadisticas.getPartidasJugadas()+"}";
    }
}
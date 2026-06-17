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
public class SolicitudAmistad implements Serializable{
    private static final long serialVersionUID=1L;
    private String emisor;
    private String receptor;
    private LocalDateTime fecha;
    private boolean pendiente;
    public SolicitudAmistad(String emisor,String receptor){
        this.emisor=emisor;
        this.receptor=receptor;
        this.fecha=LocalDateTime.now();
        this.pendiente=true;
    }
    public String getEmisor(){
        return emisor;
    }
    public String getReceptor(){
        return receptor;
    }
    public LocalDateTime getFecha(){
        return fecha;
    }
    public boolean isPendiente(){
        return pendiente;
    }
    public void aceptar(){
        this.pendiente=false;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.hilos;
import com.flowfree.data.GestorUsuarios;
import com.flowfree.model.Usuario;
/**
 *
 * @author andres
 */
public class AutoSave implements Runnable{
    private volatile boolean corriendo;
    private final Usuario usuario;
    private final GestorUsuarios gestorUsuarios;
    private static final int INTERVALO_SEG=30;
    private Thread hilo;
    public AutoSave(Usuario usuario,GestorUsuarios gestorUsuarios){
        this.usuario=usuario;
        this.gestorUsuarios=gestorUsuarios;
        this.corriendo=false;
    }
    public void iniciar(){
        corriendo=true;
        hilo=new Thread(this,"AutoSave");
        hilo.setDaemon(true);
        hilo.start();
    }
    public void detener(){
        corriendo=false;
        if(hilo!=null) hilo.interrupt();
    }
    public void run(){
        while(corriendo){
            try{
                Thread.sleep(INTERVALO_SEG*1000);
                if(corriendo) gestorUsuarios.guardarUser(usuario);
            }catch(InterruptedException e){
                corriendo=false;
            }
        }
    }
}
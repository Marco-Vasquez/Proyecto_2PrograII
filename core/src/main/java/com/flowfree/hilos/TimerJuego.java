/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.hilos;

/**
 *
 * @author andres
 */
public class TimerJuego implements Runnable {
    private volatile boolean corriendo;
    private volatile boolean pausado;
    private volatile long segundosTranscurridos;
    private Thread hilo;
    public TimerJuego(){
        this.corriendo=false;
        this.pausado=false;
        this.segundosTranscurridos=0;
    }
    public void iniciar(){
        corriendo=true;
        pausado=false;
        hilo=new Thread(this,"TimerJuego");
        hilo.setDaemon(true);
        hilo.start();
    }
    public void pausar(){
        pausado=true;
    }
    public void reanudar(){
        pausado=false;
    }
    public void detener(){
        corriendo=false;
        if(hilo!=null) hilo.interrupt();
    }
    public void reiniciar(){
        segundosTranscurridos=0;
    }
    public void run(){
        while(corriendo){
            try{
                Thread.sleep(1000);
                if(!pausado) segundosTranscurridos++;
            }catch(InterruptedException e){
                corriendo=false;
            }
        }
    }
    public long getSegundos(){return segundosTranscurridos;}
    public void setSegundos(long segundos){this.segundosTranscurridos=segundos;}
}

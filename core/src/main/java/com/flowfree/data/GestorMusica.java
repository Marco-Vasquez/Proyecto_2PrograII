/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
/**
 *
 * @author mjosu
 */
public class GestorMusica{
    private static GestorMusica instancia;
    private Music musicaFondo;
    private float volumen;
    private boolean activa;
    private GestorMusica(){
        volumen=0.5f;
        activa=true;
    }
    public static GestorMusica getInstance(){
        if(instancia==null) instancia=new GestorMusica();
        return instancia;
    }
    public void inicializar(){
        if(musicaFondo!=null) return;
        musicaFondo=Gdx.audio.newMusic(Gdx.files.internal("musica_fondo.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(volumen);
        if(activa) musicaFondo.play();
    }
    public void aplicarConfiguracion(float nuevoVolumen,boolean nuevaActiva){
        volumen=Math.max(0f,Math.min(1f,nuevoVolumen));
        activa=nuevaActiva;
        if(musicaFondo==null) return;
        musicaFondo.setVolume(volumen);
        if(activa&&!musicaFondo.isPlaying()){
            musicaFondo.play();
        }else if(!activa&&musicaFondo.isPlaying()){
            musicaFondo.pause();
        }
    }
    public void pausar(){
        if(musicaFondo!=null&&musicaFondo.isPlaying()) musicaFondo.pause();
    }
    public void reanudar(){
        if(musicaFondo!=null&&activa&&!musicaFondo.isPlaying()) musicaFondo.play();
    }
    public void dispose(){
        if(musicaFondo!=null){
            musicaFondo.stop();
            musicaFondo.dispose();
            musicaFondo=null;
        }
        instancia=null;
    }
    public float getVolumen(){
        return volumen;
    }
    public boolean isActiva(){
        return activa;
    }
}
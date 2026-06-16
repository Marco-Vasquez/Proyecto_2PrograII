package com.flowfree;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flowfree.data.GestorMusica;
import com.flowfree.screens.PantallaInicio;

public class FlowFreeGame extends Game{
    public SpriteBatch batch;
    private GestorMusica gestorMusica;
    @Override
    public void create(){
        batch=new SpriteBatch();
        gestorMusica=GestorMusica.getInstance();
        gestorMusica.inicializar();
        setScreen(new PantallaInicio(this));
    }
    @Override
    public void pause(){
        super.pause();
        gestorMusica.pausar();
    }
    @Override
    public void resume(){
        super.resume();
        gestorMusica.reanudar();
    }
    @Override
    public void dispose(){
        super.dispose();
        batch.dispose();
        gestorMusica.dispose();
    }
}

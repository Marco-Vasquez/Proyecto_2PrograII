/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.flowfree.FlowFreeGame;
/**
 *
 * @author mjosu
 */
public class PantallaInicio implements Screen{
    private final FlowFreeGame juego;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private float panelX,panelY,panelAncho,panelAlto;
    public PantallaInicio(FlowFreeGame juego){
        this.juego=juego;
    }
    
    public void show(){
        skin=new Skin(Gdx.files.internal("ui/uiskin.json"));
        dibujador=new ShapeRenderer();
        construirEscenario();
    }
    
    public void render(float delta){
        Gdx.gl.glClearColor(EstiloUI.FONDO.r,EstiloUI.FONDO.g,EstiloUI.FONDO.b,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        dibujador.setProjectionMatrix(escenario.getViewport().getCamera().combined);
        dibujarFondo();
        escenario.act(delta);
        escenario.draw();
    }
    
    public void resize(int ancho,int alto){
        if(escenario!=null) escenario.dispose();
        construirEscenario();
    }
    
    public void pause(){}
    public void resume(){}
    public void hide(){dispose();}
    public void dispose(){
        if(escenario!=null) escenario.dispose();
        skin.dispose();
        dibujador.dispose();
    }
    
    private void construirEscenario(){
        escenario=new Stage(new ScreenViewport());
        escenario.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        Gdx.input.setInputProcessor(escenario);
        calcularPanel();
        construirUI();
    }
    
    private void calcularPanel(){
        float anchoTotal=Gdx.graphics.getWidth();
        float altoTotal=Gdx.graphics.getHeight();
        panelAncho=anchoTotal*EstiloUI.PANEL_ANCHO_FRAC;
        panelAlto=altoTotal*EstiloUI.PANEL_ALTO_FRAC;
        panelX=(anchoTotal-panelAncho)/2f;
        panelY=(altoTotal-panelAlto)/2f;
    }
    
    private void dibujarFondo(){
        dibujador.begin(ShapeRenderer.ShapeType.Filled);
        dibujador.setColor(EstiloUI.PANEL);
        dibujador.rect(panelX,panelY,panelAncho,panelAlto);
        float encAncho=panelAncho*0.72f;
        float encAlto=54f;
        float encX=panelX+(panelAncho-encAncho)/2f;
        float encY=panelY+panelAlto-encAlto-14f;
        dibujador.setColor(EstiloUI.ENCABEZADO);
        dibujador.rect(encX,encY,encAncho,encAlto);
        float radioCirc=EstiloUI.RADIO_CIRCULO_DEC;
        float paso=panelAlto*0.20f;
        
        for(int posicion=0;posicion<3;posicion++){
            float circY=panelY+panelAlto*0.20f+posicion*paso;
            dibujador.setColor(EstiloUI.CIRCULOS_IZQ[posicion%EstiloUI.CIRCULOS_IZQ.length]);
            dibujador.circle(panelX-radioCirc*0.4f,circY,radioCirc,28);
            dibujador.setColor(EstiloUI.CIRCULOS_DER[posicion%EstiloUI.CIRCULOS_DER.length]);
            dibujador.circle(panelX+panelAncho-radioCirc*1.6f,circY,radioCirc,28);
        }
        dibujador.end();
    }
    
    private void construirUI(){
        float altoTotal=Gdx.graphics.getHeight();
        float encAlto=54f;
        float margenArriba=altoTotal-(panelY+panelAlto)+14f;
        Table tablaEnc=new Table();
        tablaEnc.setFillParent(true);
        tablaEnc.top();
        tablaEnc.add(new Label("Flow Free",skin)).center().padTop(margenArriba).height(encAlto);
        escenario.addActor(tablaEnc);
        Table tablaBotones=new Table();
        tablaBotones.setFillParent(true);
        tablaBotones.center();
        TextButton btnLogin=new TextButton("Iniciar Sesion",skin);
        TextButton btnRegistro=new TextButton("Crear cuenta",skin);
        TextButton btnSalir=new TextButton("Salir",skin);
        float anchoBTN=panelAncho*0.55f;
        tablaBotones.add(btnLogin).width(anchoBTN).height(48).padBottom(16).row();
        tablaBotones.add(btnRegistro).width(anchoBTN).height(48).padBottom(16).row();
        tablaBotones.add(btnSalir).width(anchoBTN).height(48).row();
        Label labelSub=new Label("Conecta todos los puntos y completa cada nivel",skin);
        tablaBotones.row();
        tablaBotones.add(labelSub).center().padTop(24);
        escenario.addActor(tablaBotones);
        btnLogin.addListener(new ChangeListener(){
            
            public void changed(ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaLogin(juego,false));
            }
            
        });
        btnRegistro.addListener(new ChangeListener(){
            
            public void changed(ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaLogin(juego,true));
            }
            
        });
        btnSalir.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                Gdx.app.exit();
            }
        });
    }
}

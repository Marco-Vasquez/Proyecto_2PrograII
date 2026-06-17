/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.flowfree.FlowFreeGame;
import com.flowfree.data.GestorIdiomas;
/**
 *
 * @author mjosu
 */
public class PantallaInicio implements Screen{
    private final FlowFreeGame juego;
    private GestorIdiomas idiomas=GestorIdiomas.getInstance();
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private float panelX,panelY,panelAncho,panelAlto;
    private static final float ENC_ALTO=52f;
    private static final float ENC_MARGEN_TOP=14f;
    private static final float RADIO_CIRC=22f;
    public PantallaInicio(FlowFreeGame juego){
        this.juego=juego;
    }
    public void show(){
        skin=new Skin(Gdx.files.internal("ui/uiskin.json"));
        dibujador=new ShapeRenderer();
        idiomas.setEspanol(true);
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
        panelAncho=anchoTotal*0.78f;
        panelAlto=altoTotal*0.86f;
        panelX=(anchoTotal-panelAncho)/2f;
        panelY=(altoTotal-panelAlto)/2f;
    }
    private void dibujarFondo(){
        dibujador.begin(ShapeRenderer.ShapeType.Filled);
        dibujador.setColor(EstiloUI.PANEL);
        dibujador.rect(panelX,panelY,panelAncho,panelAlto);
        float encAncho=panelAncho*0.68f;
        float encX=panelX+(panelAncho-encAncho)/2f;
        float encY=panelY+panelAlto-ENC_ALTO-ENC_MARGEN_TOP;
        dibujador.setColor(EstiloUI.ENCABEZADO);
        dibujador.rect(encX,encY,encAncho,ENC_ALTO);
        float zonaCircX_izq=panelX+RADIO_CIRC+8f;
        float zonaCircX_der=panelX+panelAncho-RADIO_CIRC-8f;
        float zonaCircBase=panelY+panelAlto*0.20f;
        float paso=panelAlto*0.18f;
        for(int posicion=0;posicion<3;posicion++){
            float circY=zonaCircBase+posicion*paso;
            dibujador.setColor(EstiloUI.CIRCULOS_IZQ[posicion%EstiloUI.CIRCULOS_IZQ.length]);
            dibujador.circle(zonaCircX_izq,circY,RADIO_CIRC,28);
            dibujador.setColor(EstiloUI.CIRCULOS_DER[posicion%EstiloUI.CIRCULOS_DER.length]);
            dibujador.circle(zonaCircX_der,circY,RADIO_CIRC,28);
        }
        dibujador.end();
    }
    private void construirUI(){
        float encAncho=panelAncho*0.68f;
        float encX=panelX+(panelAncho-encAncho)/2f;
        float encY=panelY+panelAlto-ENC_ALTO-ENC_MARGEN_TOP;
        Table tablaEnc=new Table();
        tablaEnc.setPosition(encX,encY);
        tablaEnc.setSize(encAncho,ENC_ALTO);
        tablaEnc.add(new Label(idiomas.get("app.titulo"),skin)).center().expand();
        escenario.addActor(tablaEnc);
        float anchoBTN=panelAncho*0.50f;
        TextButton btnLogin=new TextButton(idiomas.get("inicio.btn.login"),skin);
        TextButton btnRegistro=new TextButton(idiomas.get("inicio.btn.registro"),skin);
        TextButton btnSalir=new TextButton(idiomas.get("inicio.btn.salir"),skin);
        btnLogin.setColor(EstiloUI.BTN_AZUL);
        btnRegistro.setColor(EstiloUI.BTN_VERDE);
        btnSalir.setColor(EstiloUI.BTN_ROJO);
        Table tablaRaiz=new Table();
        tablaRaiz.setFillParent(true);
        tablaRaiz.center();
        tablaRaiz.add().height(ENC_ALTO+ENC_MARGEN_TOP+20f).row();
        tablaRaiz.add(btnLogin).width(anchoBTN).height(46f).padBottom(14f).row();
        tablaRaiz.add(btnRegistro).width(anchoBTN).height(46f).padBottom(14f).row();
        tablaRaiz.add(btnSalir).width(anchoBTN).height(46f).padBottom(20f).row();
        tablaRaiz.add(new Label(idiomas.get("inicio.subtitulo"),skin)).center();
        escenario.addActor(tablaRaiz);
        btnLogin.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaLogin(juego,false));
            }
        });
        btnRegistro.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaLogin(juego,true));
            }
        });
        btnSalir.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                Gdx.app.exit();
            }
        });
    }
}
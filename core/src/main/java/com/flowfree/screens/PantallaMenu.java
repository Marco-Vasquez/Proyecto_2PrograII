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
import com.flowfree.model.Usuario;
import com.flowfree.data.GestorIdiomas;
/**
 *
 * @author mjosu
 */
public class PantallaMenu implements Screen{
    private final FlowFreeGame juego;
    private final Usuario usuarioAct;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private float panelX,panelY,panelAncho,panelAlto;
    private static final float ENC_ALTO=52f;
    private static final float ENC_MARGEN_TOP=14f;
    private static final float RADIO_CIRC=20f;
    private GestorIdiomas idiomas;
    public PantallaMenu(FlowFreeGame juego,Usuario usuarioAct){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
    }
    public void show(){
        skin=new Skin(Gdx.files.internal("ui/uiskin.json"));
        dibujador=new ShapeRenderer();
        idiomas=GestorIdiomas.getInstance();
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
        float zonaCircX_izq=panelX+RADIO_CIRC+10f;
        float zonaCircX_der=panelX+panelAncho-RADIO_CIRC-10f;
        float zonaCircBase=panelY+panelAlto*0.16f;
        float paso=panelAlto*0.13f;
        for(int posicion=0;posicion<5;posicion++){
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
        float anchoBTN=panelAncho*0.52f;
        float altoBTN=38f;
        float sepBTN=8f;
        TextButton btnJugar=new TextButton(idiomas.get("menu.btn.jugar"),skin);
        TextButton btnNiveles=new TextButton(idiomas.get("menu.btn.niveles"),skin);
        TextButton btnPerfil=new TextButton(idiomas.get("menu.btn.perfil"),skin);
        TextButton btnEstadisticas=new TextButton(idiomas.get("menu.btn.estadisticas"),skin);
        TextButton btnAmigos=new TextButton(idiomas.get("menu.btn.amigos"),skin);
        TextButton btnRetos=new TextButton(idiomas.get("menu.btn.retos"),skin);
        TextButton btnConfiguraciones=new TextButton(idiomas.get("menu.btn.configuraciones"),skin);
        TextButton btnCerrarSesion=new TextButton(idiomas.get("menu.btn.cerrarsesion"),skin);
        btnJugar.setColor(EstiloUI.BTN_AZUL);
        btnNiveles.setColor(EstiloUI.BTN_VERDE);
        btnPerfil.setColor(EstiloUI.BTN_CYAN);
        btnEstadisticas.setColor(EstiloUI.BTN_AMARILLO);
        btnAmigos.setColor(EstiloUI.BTN_NARANJA);
        btnRetos.setColor(EstiloUI.BTN_MORADOCLARO);
        btnConfiguraciones.setColor(EstiloUI.BTN_MORADOCLARO);
        btnCerrarSesion.setColor(EstiloUI.BTN_ROJO);
        Table tablaRaiz=new Table();
        tablaRaiz.setFillParent(true);
        tablaRaiz.center();
        tablaRaiz.add().height(ENC_ALTO+ENC_MARGEN_TOP+16f).row();
        tablaRaiz.add(btnJugar).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaRaiz.add(btnNiveles).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaRaiz.add(btnPerfil).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaRaiz.add(btnEstadisticas).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaRaiz.add(btnAmigos).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaRaiz.add(btnRetos).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaRaiz.add(btnConfiguraciones).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaRaiz.add(btnCerrarSesion).width(anchoBTN).height(altoBTN).row();
        escenario.addActor(tablaRaiz);
        btnJugar.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaNiveles(juego,usuarioAct));
            }
        });
        btnNiveles.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaNiveles(juego,usuarioAct));
            }
        });
        btnPerfil.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaPerfil(juego,usuarioAct));
            }
        });
        btnEstadisticas.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaEstadisticas(juego,usuarioAct));
            }
        });
        btnAmigos.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaAmigos(juego,usuarioAct));
            }
        });
        btnRetos.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaRetos(juego,usuarioAct));
            }
        });
        btnConfiguraciones.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaConfiguraciones(juego,usuarioAct));
            }
        });
        btnCerrarSesion.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaInicio(juego));
            }
        });
    }
}
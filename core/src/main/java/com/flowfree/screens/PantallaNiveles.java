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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.flowfree.FlowFreeGame;
import com.flowfree.game.GestorNiveles;
import com.flowfree.model.Nivel;
import com.flowfree.model.Usuario;
/**
 *
 * @author andres
 */
public class PantallaNiveles implements Screen{
    private final FlowFreeGame juego;
    private final Usuario usuarioAct;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private GestorNiveles gestorNiveles;
    private float panelX,panelY,panelAncho,panelAlto;
    private static final float ENC_ALTO=52f;
    private static final float ENC_MARGEN_TOP=14f;
    private static final float RADIO_CIRC=20f;
    private static final Color COLOR_BLOQUEADO=new Color(0.40f,0.40f,0.40f,1f);
    public PantallaNiveles(FlowFreeGame juego,Usuario usuarioAct){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
    }
    public void show(){
        skin=new Skin(Gdx.files.internal("ui/uiskin.json"));
        dibujador=new ShapeRenderer();
        gestorNiveles=new GestorNiveles();
        gestorNiveles.aplicarProgresoUsuario(usuarioAct.getNivelDesbloqueado());
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
        float paso=panelAlto*0.20f;
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
        tablaEnc.add(new Label("Flow Free",skin)).center().expand();
        escenario.addActor(tablaEnc);
        float anchoBTN=panelAncho*0.52f;
        float altoBTN=42f;
        float sepBTN=10f;
        Nivel[] todosLosNiveles=gestorNiveles.getTodosLosNiveles();
        Table tablaRaiz=new Table();
        tablaRaiz.setFillParent(true);
        tablaRaiz.center();
        tablaRaiz.add().height(ENC_ALTO+ENC_MARGEN_TOP+10f).row();
        for(int posicion=0;posicion<todosLosNiveles.length;posicion++){
            Nivel nivel=todosLosNiveles[posicion];
            String textoBoton="Nivel "+nivel.getNumNivel()+" - "+nivel.getDificultad();
            TextButton botonNivel=new TextButton(textoBoton,skin);
            final Color colorFijo=nivel.isDesbloqueado()
                ? new Color(EstiloUI.BTN_VERDE)
                : new Color(COLOR_BLOQUEADO);
            botonNivel.setColor(colorFijo);
            botonNivel.addListener(new InputListener(){
                public void enter(InputEvent e,float x,float y,int p,Actor from){
                    botonNivel.setColor(colorFijo);
                }
                public void exit(InputEvent e,float x,float y,int p,Actor to){
                    botonNivel.setColor(colorFijo);
                }
            });
            boolean esUltimo=(posicion==todosLosNiveles.length-1);
            tablaRaiz.add(botonNivel).width(anchoBTN).height(altoBTN)
                .padBottom(esUltimo ? 20f : sepBTN).row();
            final int numNivel=nivel.getNumNivel();
            final boolean desbloqueado=nivel.isDesbloqueado();
            botonNivel.addListener(new ChangeListener(){
                public void changed(ChangeEvent evento,Actor actor){
                    if(desbloqueado){
                        juego.setScreen(new PantallaJuego(juego,usuarioAct,numNivel));
                    }
                }
            });
        }
        Label labelPista=new Label("Completa el nivel anterior para desbloquear el siguiente",skin);
        labelPista.setWrap(true);
        tablaRaiz.add(labelPista).width(anchoBTN).center().row();
        tablaRaiz.add().height(14f).row();
        TextButton btnVolver=new TextButton("Volver al menu",skin);
        btnVolver.setColor(EstiloUI.BTN_AZUL);
        tablaRaiz.add(btnVolver).width(anchoBTN*0.7f).height(36f).row();
        escenario.addActor(tablaRaiz);
        btnVolver.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaMenu(juego,usuarioAct));
            }
        });
    }
}
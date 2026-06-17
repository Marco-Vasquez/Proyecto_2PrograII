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
import com.flowfree.data.GestorMusica;
import com.flowfree.data.GestorUsuarios;
import com.flowfree.model.Perfil;
import com.flowfree.model.Usuario;
/**
 *
 * @author mjosu
 */
public class PantallaConfiguraciones implements Screen{
    private final FlowFreeGame juego;
    private final Usuario usuarioAct;
    private final GestorUsuarios gestorUsuarios;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private float panelX,panelY,panelAncho,panelAlto;
    private static final float ENC_ALTO=52f;
    private static final float ENC_MARGEN_TOP=14f;
    private static final float RADIO_CIRC=20f;
    private Slider sliderVolumen;
    private TextButton btnMusica;
    private TextButton btnIdioma;
    private Label labelVolumenValor;
    private GestorIdiomas idiomas;
    public PantallaConfiguraciones(FlowFreeGame juego,Usuario usuarioAct){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
        this.gestorUsuarios=new GestorUsuarios();
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
        Perfil perfil=usuarioAct.getPerfil();
        GestorMusica gestor=GestorMusica.getInstance();
        Table tablaContenido=new Table();
        tablaContenido.pad(16f);
        tablaContenido.add(new Label("Configuraciones",skin)).colspan(2).center().padBottom(20f).row();
        tablaContenido.add(new Label("Volumen:",skin)).left().padRight(20f).padBottom(12f);
        float volumenInicial=perfil.getVolumen();
        sliderVolumen=new Slider(0f,1f,0.05f,false,skin);
        sliderVolumen.setValue(volumenInicial);
        labelVolumenValor=new Label(String.format("%d%%",(int)(volumenInicial*100)),skin);
        Table filaVolumen=new Table();
        filaVolumen.add(sliderVolumen).width(panelAncho*0.35f).padRight(10f);
        filaVolumen.add(labelVolumenValor).width(40f);
        tablaContenido.add(filaVolumen).left().padBottom(12f).row();
        tablaContenido.add(new Label("Musica:",skin)).left().padRight(20f).padBottom(12f);
        String textoMusica=perfil.isMusicaActiva() ? "Activada" : "Desactivada";
        btnMusica=new TextButton(textoMusica,skin);
        btnMusica.setColor(perfil.isMusicaActiva() ? EstiloUI.BTN_VERDE : EstiloUI.BTN_ROJO);
        tablaContenido.add(btnMusica).width(panelAncho*0.35f).height(36f).left().padBottom(12f).row();
        tablaContenido.add(new Label("Idioma:",skin)).left().padRight(20f).padBottom(12f);
        String textoIdioma=perfil.isIdiomaEspanol() ? "Espanol" : "English";
        btnIdioma=new TextButton(textoIdioma,skin);
        btnIdioma.setColor(EstiloUI.BTN_CYAN);
        tablaContenido.add(btnIdioma).width(panelAncho*0.35f).height(36f).left().padBottom(20f).row();
        TextButton btnGuardar=new TextButton("Guardar",skin);
        TextButton btnVolver=new TextButton("Volver al menu",skin);
        btnGuardar.setColor(EstiloUI.BTN_VERDE);
        btnVolver.setColor(EstiloUI.BTN_AZUL);
        tablaContenido.add(btnGuardar).colspan(2).width(panelAncho*0.45f).height(38f).center().padBottom(10f).row();
        tablaContenido.add(btnVolver).colspan(2).width(panelAncho*0.45f).height(38f).center().row();
        ScrollPane scroll=new ScrollPane(tablaContenido,skin);
        scroll.setFadeScrollBars(true);
        scroll.setScrollingDisabled(true,false);
        Table tablaRaiz=new Table();
        tablaRaiz.setFillParent(true);
        tablaRaiz.center();
        tablaRaiz.add().height(ENC_ALTO+ENC_MARGEN_TOP+8f).row();
        tablaRaiz.add(scroll).width(panelAncho*0.80f).maxHeight(Gdx.graphics.getHeight()*0.65f).row();
        escenario.addActor(tablaRaiz);
        sliderVolumen.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                float val=sliderVolumen.getValue();
                labelVolumenValor.setText(String.format("%d%%",(int)(val*100)));
                perfil.setVolumen(val);
                gestor.aplicarConfiguracion(val,perfil.isMusicaActiva());
            }
        });
        btnMusica.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                boolean nuevo=!perfil.isMusicaActiva();
                perfil.setMusicaActiva(nuevo);
                btnMusica.setText(nuevo ? "Activada" : "Desactivada");
                btnMusica.setColor(nuevo ? EstiloUI.BTN_VERDE : EstiloUI.BTN_ROJO);
                gestor.aplicarConfiguracion(sliderVolumen.getValue(),nuevo);
            }
        });
        btnIdioma.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                boolean nuevo=!perfil.isIdiomaEspanol();
                perfil.setIdiomaEspanol(nuevo);
                btnIdioma.setText(nuevo ? "Espanol" : "English");

                GestorIdiomas.getInstance().setEspanol(nuevo);

                juego.setScreen(new PantallaMenu(juego,usuarioAct));
            }
        });
        btnGuardar.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                perfil.setVolumen(sliderVolumen.getValue());
                gestorUsuarios.guardarUser(usuarioAct);
                gestor.aplicarConfiguracion(perfil.getVolumen(),perfil.isMusicaActiva());
                juego.setScreen(new PantallaMenu(juego,usuarioAct));
            }
        });
        btnVolver.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaMenu(juego,usuarioAct));
            }
        });
    }
}

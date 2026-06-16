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
import com.flowfree.data.GestorUsuarios;
import com.flowfree.model.Usuario;
/**
 *
 * @author mjosu
 */
public class PantallaPerfil implements Screen{
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
    private static final float RADIO_AVATAR_GRANDE=48f;
    private static final float RADIO_AVATAR_BTN=22f;
    private static final Color[] COLORES_AVATAR={
        new Color(0.95f,0.15f,0.15f,1f),
        new Color(0.20f,0.50f,0.95f,1f),
        new Color(0.20f,0.82f,0.25f,1f),
        new Color(0.95f,0.88f,0.10f,1f),
        new Color(0.95f,0.52f,0.08f,1f)
    };
    private int avatarSeleccionado;
    private float avatarGrandeX;
    private float avatarGrandeY;
    public PantallaPerfil(FlowFreeGame juego,Usuario usuarioAct){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
        this.gestorUsuarios=new GestorUsuarios();
        this.avatarSeleccionado=usuarioAct.getPerfil().getAvatarIndex();
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
        dibujarAvatarGrande();
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
        avatarGrandeX=panelX+panelAncho/2f;
        avatarGrandeY=panelY+panelAlto-ENC_ALTO-ENC_MARGEN_TOP-RADIO_AVATAR_GRANDE-16f;
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
        float zonaCircBase=panelY+panelAlto*0.08f;
        float paso=panelAlto*0.14f;
        for(int posicion=0;posicion<3;posicion++){
            float circY=zonaCircBase+posicion*paso;
            dibujador.setColor(EstiloUI.CIRCULOS_IZQ[posicion%EstiloUI.CIRCULOS_IZQ.length]);
            dibujador.circle(zonaCircX_izq,circY,RADIO_CIRC,28);
            dibujador.setColor(EstiloUI.CIRCULOS_DER[posicion%EstiloUI.CIRCULOS_DER.length]);
            dibujador.circle(zonaCircX_der,circY,RADIO_CIRC,28);
        }
        dibujador.end();
    }
    private void dibujarAvatarGrande(){
        int indice=avatarSeleccionado<COLORES_AVATAR.length ? avatarSeleccionado : 0;
        dibujador.begin(ShapeRenderer.ShapeType.Filled);
        dibujador.setColor(Color.WHITE);
        dibujador.circle(avatarGrandeX,avatarGrandeY,RADIO_AVATAR_GRANDE+4f,40);
        dibujador.setColor(COLORES_AVATAR[indice]);
        dibujador.circle(avatarGrandeX,avatarGrandeY,RADIO_AVATAR_GRANDE,40);
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
        float espacioAvatar=RADIO_AVATAR_GRANDE*2+24f;
        Table tablaContenido=new Table();
        tablaContenido.pad(12f);
        tablaContenido.add().height(espacioAvatar).colspan(2).row();
        tablaContenido.add(new Label("Perfil de usuario",skin)).colspan(2).center().padBottom(14f).row();
        agregarFila(tablaContenido,"Username:",usuarioAct.getUsername());
        agregarFila(tablaContenido,"Nombre:",usuarioAct.getNombreCompleto());
        agregarFila(tablaContenido,"Nivel desbloqueado:",""+usuarioAct.getNivelDesbloqueado());
        String fechaReg=usuarioAct.getFechaRegistro().toLocalDate().toString();
        agregarFila(tablaContenido,"Registrado:",fechaReg);
        String ultimaSesion=usuarioAct.getUltimaSesion().toLocalDate().toString();
        agregarFila(tablaContenido,"Ultima sesion:",ultimaSesion);
        tablaContenido.add(new Label("Cambiar avatar:",skin)).left().padTop(14f).padBottom(8f).colspan(2).row();
        Table tablaAvatares=new Table();
        for(int posicion=0;posicion<COLORES_AVATAR.length;posicion++){
            final int indice=posicion;
            TextButton btnAvatar=new TextButton(""+(posicion+1),skin);
            btnAvatar.setColor(COLORES_AVATAR[posicion]);
            tablaAvatares.add(btnAvatar).width(44f).height(44f).padRight(8f);
            btnAvatar.addListener(new ChangeListener(){
                public void changed(ChangeEvent evento,Actor actor){
                    avatarSeleccionado=indice;
                    usuarioAct.getPerfil().setAvatarIndex(indice);
                    gestorUsuarios.guardarUser(usuarioAct);
                }
            });
        }
        tablaContenido.add(tablaAvatares).colspan(2).left().padBottom(14f).row();
        ScrollPane scroll=new ScrollPane(tablaContenido,skin);
        scroll.setFadeScrollBars(true);
        scroll.setScrollingDisabled(true,false);
        float altoTotal=Gdx.graphics.getHeight();
        Table tablaRaiz=new Table();
        tablaRaiz.setFillParent(true);
        tablaRaiz.center();
        tablaRaiz.add().height(ENC_ALTO+ENC_MARGEN_TOP+8f).row();
        tablaRaiz.add(scroll).width(panelAncho*0.80f).maxHeight(altoTotal*0.65f).row();
        tablaRaiz.add().height(10f).row();
        TextButton btnVolver=new TextButton("Volver al menu",skin);
        btnVolver.setColor(EstiloUI.BTN_AZUL);
        tablaRaiz.add(btnVolver).width(panelAncho*0.45f).height(36f).row();
        escenario.addActor(tablaRaiz);
        btnVolver.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaMenu(juego,usuarioAct));
            }
        });
    }
    private void agregarFila(Table tabla,String etiqueta,String valor){
        tabla.add(new Label(etiqueta,skin)).left().padRight(20f).padBottom(6f);
        tabla.add(new Label(valor,skin)).left().padBottom(6f).row();
    }
}
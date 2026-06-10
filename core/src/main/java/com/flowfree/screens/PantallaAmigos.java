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
import com.flowfree.data.GestorUsuarios;
import com.flowfree.model.Usuario;
import java.util.List;
/**
 *
 * @author mjosu
 */
public class PantallaAmigos implements Screen{
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
    private Label labelMensaje;
    private TextField campoBusqueda;
    
    public PantallaAmigos(FlowFreeGame juego,Usuario usuarioAct){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
        this.gestorUsuarios=new GestorUsuarios();
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
        if(escenario!=null){
            escenario.dispose();
        }
        construirEscenario();
    }
    
    public void pause(){}
    public void resume(){}
    public void hide(){dispose();}
    public void dispose(){
        if(escenario!=null){
            escenario.dispose();
        }
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
        java.util.List<String> todosLosUsuarios=gestorUsuarios.listarUsuariosRegistrados();
        java.util.List<String> usuariosDisponibles=new java.util.ArrayList<>();
        for(String nombre:todosLosUsuarios){
            if(!nombre.equals(usuarioAct.getUsername())
                    &&!usuarioAct.getAmigos().contains(nombre)){
                usuariosDisponibles.add(nombre);
            }
        }
        Table tablaContenido=new Table();
        tablaContenido.pad(12f);
        tablaContenido.add(new Label("Amigos",skin)).colspan(2).center().padBottom(14f).row();
        labelMensaje=new Label("",skin);
        TextButton btnAgregar=new TextButton("Agregar",skin);
        btnAgregar.setColor(EstiloUI.BTN_VERDE);
        if(usuariosDisponibles.isEmpty()){
            tablaContenido.add(new Label("No hay usuarios disponibles para agregar",skin)).colspan(2).left().padBottom(10f).row();
        }else{
            tablaContenido.add(new Label("Seleccionar usuario:",skin)).left().padBottom(4f);
            tablaContenido.add().row();
            com.badlogic.gdx.scenes.scene2d.ui.SelectBox<String> selectorUsuarios=
                new com.badlogic.gdx.scenes.scene2d.ui.SelectBox<>(skin);
            com.badlogic.gdx.utils.Array<String> itemsArray=new com.badlogic.gdx.utils.Array<>();
            for(String nombre:usuariosDisponibles) itemsArray.add(nombre);
            selectorUsuarios.setItems(itemsArray);
            tablaContenido.add(selectorUsuarios).width(panelAncho*0.55f).height(36f).padBottom(6f).colspan(2).row();
            tablaContenido.add(btnAgregar).width(panelAncho*0.45f).height(36f).colspan(2).padBottom(10f).row();
            tablaContenido.add(labelMensaje).colspan(2).left().padBottom(10f).row();
            btnAgregar.addListener(new ChangeListener(){
                public void changed(ChangeEvent evento,Actor actor){
                    String seleccionado=selectorUsuarios.getSelected();
                    if(seleccionado==null){
                        labelMensaje.setText("Selecciona un usuario");
                        return;
                    }
                    usuarioAct.agregarAmigo(seleccionado);
                    gestorUsuarios.guardarUser(usuarioAct);
                    juego.setScreen(new PantallaAmigos(juego,usuarioAct));
                }
            });
        }
        java.util.List<String> amigos=usuarioAct.getAmigos();
        tablaContenido.add(new Label("Mis amigos:",skin)).colspan(2).left().padBottom(6f).row();
        if(amigos.isEmpty()){
            tablaContenido.add(new Label("Todavia no tienes amigos agregados",skin)).colspan(2).left().padBottom(4f).row();
        }else{
            tablaContenido.add(new Label("Usuario",skin)).left().padRight(30f).padBottom(4f);
            tablaContenido.add(new Label("Puntuacion",skin)).left().padBottom(4f).row();
            for(String usernameAmigo:amigos){
                int puntos=gestorUsuarios.getPuntuacionDeUsuario(usernameAmigo);
                tablaContenido.add(new Label(usernameAmigo,skin)).left().padRight(30f).padBottom(4f);
                tablaContenido.add(new Label(""+puntos,skin)).left().padBottom(4f).row();
            }
        }
        ScrollPane scroll=new ScrollPane(tablaContenido,skin);
        scroll.setFadeScrollBars(true);
        scroll.setScrollingDisabled(true,false);
        float altoTotal=Gdx.graphics.getHeight();
        Table tablaRaiz=new Table();
        tablaRaiz.setFillParent(true);
        tablaRaiz.center();
        tablaRaiz.add().height(ENC_ALTO+ENC_MARGEN_TOP+8f).row();
        tablaRaiz.add(scroll).width(panelAncho*0.80f).maxHeight(altoTotal*0.62f).row();
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
    
    private void intentarAgregarAmigo(){
        String usernameObjetivo=campoBusqueda.getText().trim();
        
        if(usernameObjetivo.isEmpty()){
            labelMensaje.setText("Escribe un username para buscar");
            return;
        }
        
        if(usernameObjetivo.equals(usuarioAct.getUsername())){
            labelMensaje.setText("No puedes agregarte a ti mismo");
            return;
        }
        
        if(!gestorUsuarios.userExists(usernameObjetivo)){
            labelMensaje.setText("El usuario no existe");
            return;
        }
        
        if(usuarioAct.getAmigos().contains(usernameObjetivo)){
            labelMensaje.setText("Ya es tu amigo");
            return;
        }
        
        usuarioAct.agregarAmigo(usernameObjetivo);
        gestorUsuarios.guardarUser(usuarioAct);
        juego.setScreen(new PantallaAmigos(juego,usuarioAct));
    }
    
}
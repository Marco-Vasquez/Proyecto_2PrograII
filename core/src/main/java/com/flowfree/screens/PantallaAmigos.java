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
import com.flowfree.data.GestorUsuarios;
import com.flowfree.model.SolicitudAmistad;
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
    private GestorIdiomas idiomas=GestorIdiomas.getInstance();
    private Label labelMensaje;
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
        tablaEnc.add(new Label(idiomas.get("app.titulo"),skin)).center().expand();
        escenario.addActor(tablaEnc);
        List<String> todosLosUsuarios=gestorUsuarios.listarUsuariosRegistrados();
        List<String> usuariosDisponibles=new java.util.ArrayList<>();
        for(String nombre:todosLosUsuarios){
            if(!nombre.equals(usuarioAct.getUsername())
                    &&!usuarioAct.getAmigos().contains(nombre)
                    &&!usuarioAct.yaEnvioSolicitudA(nombre)){
                usuariosDisponibles.add(nombre);
            }
        }
        Table tablaContenido=new Table();
        tablaContenido.pad(12f);
        tablaContenido.add(new Label(idiomas.get("amigos.titulo"),skin)).colspan(3).center().padBottom(14f).row();
        labelMensaje=new Label("",skin);
        List<SolicitudAmistad> pendientes=usuarioAct.getSolicitudesRecibidas();
        boolean hayPendientes=false;
        for(SolicitudAmistad s:pendientes){
            if(s.isPendiente()) hayPendientes=true;
        }
        if(hayPendientes){
            tablaContenido.add(new Label(idiomas.get("amigos.solicitudes"),skin)).colspan(3).left().padBottom(6f).row();
            for(SolicitudAmistad solicitud:pendientes){
                if(!solicitud.isPendiente()) continue;
                String emisor=solicitud.getEmisor();
                tablaContenido.add(new Label(emisor,skin)).left().expandX().padRight(8f);
                TextButton btnAceptar=new TextButton(idiomas.get("amigos.btn.aceptar"),skin);
                TextButton btnRechazar=new TextButton(idiomas.get("amigos.btn.rechazar"),skin);
                btnAceptar.setColor(EstiloUI.BTN_VERDE);
                btnRechazar.setColor(EstiloUI.BTN_ROJO);
                tablaContenido.add(btnAceptar).width(80f).height(30f).padRight(4f);
                tablaContenido.add(btnRechazar).width(80f).height(30f).row();
                btnAceptar.addListener(new ChangeListener(){
                    public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                        gestorUsuarios.aceptarSolicitud(usuarioAct,emisor);
                        juego.setScreen(new PantallaAmigos(juego,usuarioAct));
                    }
                });
                btnRechazar.addListener(new ChangeListener(){
                    public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                        gestorUsuarios.rechazarSolicitud(usuarioAct,emisor);
                        juego.setScreen(new PantallaAmigos(juego,usuarioAct));
                    }
                });
            }
            tablaContenido.add().height(10f).colspan(3).row();
        }
        tablaContenido.add(new Label(idiomas.get("amigos.enviar"),skin)).colspan(3).left().padBottom(4f).row();
        if(usuariosDisponibles.isEmpty()){
            tablaContenido.add(new Label(idiomas.get("amigos.nodisponibles"),skin)).colspan(3).left().padBottom(8f).row();
        }else{
            com.badlogic.gdx.scenes.scene2d.ui.SelectBox<String> selector=
                new com.badlogic.gdx.scenes.scene2d.ui.SelectBox<>(skin);
            com.badlogic.gdx.utils.Array<String> items=new com.badlogic.gdx.utils.Array<>();
            for(String nombre:usuariosDisponibles) items.add(nombre);
            selector.setItems(items);
            TextButton btnEnviar=new TextButton(idiomas.get("amigos.btn.enviar"),skin);
            btnEnviar.setColor(EstiloUI.BTN_AZUL);
            tablaContenido.add(selector).width(panelAncho*0.42f).height(34f).padRight(8f).colspan(2);
            tablaContenido.add(btnEnviar).width(80f).height(34f).row();
            tablaContenido.add(labelMensaje).colspan(3).left().padBottom(10f).row();
            btnEnviar.addListener(new ChangeListener(){
                public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                    String seleccionado=selector.getSelected();
                    if(seleccionado==null) return;
                    boolean exito=gestorUsuarios.enviarSolicitud(usuarioAct.getUsername(),seleccionado);
                    if(exito){
                        Usuario actualizado=gestorUsuarios.cargarUser(usuarioAct.getUsername());
                        if(actualizado!=null) juego.setScreen(new PantallaAmigos(juego,actualizado));
                    }else{
                        labelMensaje.setText(idiomas.get("amigos.error.enviar"));
                    }
                }
            });
        }
        List<String> amigos=usuarioAct.getAmigos();
        tablaContenido.add(new Label(idiomas.get("amigos.mislista"),skin)).colspan(3).left().padBottom(6f).row();
        if(amigos.isEmpty()){
            tablaContenido.add(new Label(idiomas.get("amigos.sinAmigos"),skin)).colspan(3).left().row();
        }else{
            tablaContenido.add(new Label(idiomas.get("amigos.col.usuario"),skin)).left().expandX().padBottom(4f);
            tablaContenido.add(new Label(idiomas.get("amigos.col.puntos"),skin)).left().padBottom(4f);
            tablaContenido.add().row();
            List<String> amigosOrdenados=new java.util.ArrayList<>(amigos);
            amigosOrdenados.sort((a,b)->
                gestorUsuarios.getPuntuacionDeUsuario(b)-gestorUsuarios.getPuntuacionDeUsuario(a));
            int posicion=1;
            for(String usernameAmigo:amigosOrdenados){
                int puntos=gestorUsuarios.getPuntuacionDeUsuario(usernameAmigo);
                tablaContenido.add(new Label(posicion+". "+usernameAmigo,skin)).left().padBottom(4f);
                tablaContenido.add(new Label(""+puntos,skin)).left().padBottom(4f);
                tablaContenido.add().row();
                posicion++;
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
        tablaRaiz.add(scroll).width(panelAncho*0.82f).maxHeight(altoTotal*0.62f).row();
        tablaRaiz.add().height(10f).row();
        TextButton btnVolver=new TextButton(idiomas.get("amigos.btn.volver"),skin);
        btnVolver.setColor(EstiloUI.BTN_AZUL);
        tablaRaiz.add(btnVolver).width(panelAncho*0.45f).height(36f).row();
        escenario.addActor(tablaRaiz);
        btnVolver.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaMenu(juego,usuarioAct));
            }
        });
    }
}
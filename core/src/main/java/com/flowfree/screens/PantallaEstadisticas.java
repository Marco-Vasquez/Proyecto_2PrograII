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
import com.flowfree.model.Estadisticas;
import com.flowfree.model.Usuario;
import java.util.List;
/**
 *
 * @author mjosu
 */
public class PantallaEstadisticas implements Screen{
    private final FlowFreeGame juego;
    private final Usuario usuarioAct;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private float panelX,panelY,panelAncho,panelAlto;
    private static final float ENC_ALTO=52f;
    private static final float ENC_MARGEN_TOP=14f;
    private static final float RADIO_CIRC=20f;
    public PantallaEstadisticas(FlowFreeGame juego,Usuario usuarioAct){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
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
        tablaEnc.add(new Label("Flow Free",skin)).center().expand();
        escenario.addActor(tablaEnc);
        Estadisticas stats=usuarioAct.getEstadisticas();
        int segundosTotales=(int)stats.getTiempoTotalSeg();
        int minutos=segundosTotales/60;
        int segundos=segundosTotales%60;
        double promedio=stats.getTiempoPromedioPorNivel();
        int promedioMin=(int)(promedio/60);
        int promedioSeg=(int)(promedio%60);
        Table tablaContenido=new Table();
        tablaContenido.pad(12f);
        agregarFila(tablaContenido,"Usuario:",usuarioAct.getUsername());
        agregarFila(tablaContenido,"Partidas jugadas:",""+stats.getPartidasJugadas());
        agregarFila(tablaContenido,"Niveles completados:",""+stats.getNivelesCompletados());
        agregarFila(tablaContenido,"Tiempo total:",String.format("%02d:%02d",minutos,segundos));
        agregarFila(tablaContenido,"Tiempo promedio:",String.format("%02d:%02d",promedioMin,promedioSeg));
        agregarFila(tablaContenido,"Movimientos totales:",""+stats.getTotalMovimientos());
        agregarFila(tablaContenido,"Fallos totales:",""+stats.getTotalFallos());
        agregarFila(tablaContenido,"Puntuacion general:",""+stats.getPuntuacionGeneral());
        tablaContenido.add(new Label("Historial de partidas:",skin)).left().padTop(14f).padBottom(4f).colspan(2).row();
        List<String> historial=stats.getHistorial();
        
        if(historial.isEmpty()){
            tablaContenido.add(new Label("Sin partidas registradas",skin)).colspan(2).center().row();
        }else{
            int inicio=Math.max(0,historial.size()-5);
            for(int posicion=inicio;posicion<historial.size();posicion++){
                tablaContenido.add(new Label(historial.get(posicion),skin)).colspan(2).left().padBottom(2f).row();
            }
        }
        
        construirRankingAmigos(tablaContenido);
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
    
    private void construirRankingAmigos(Table tabla){
        GestorUsuarios gestorUsuarios=new GestorUsuarios();
        List<String> amigos=usuarioAct.getAmigos();
        if(amigos.isEmpty()) return;
        tabla.add(new Label("Ranking con amigos:",skin)).left().padTop(14f).padBottom(4f).colspan(2).row();
        agregarFila(tabla,usuarioAct.getUsername()+" (tu)",""+usuarioAct.getEstadisticas().getPuntuacionGeneral());
        for(String usernameAmigo:amigos){
            int puntos=gestorUsuarios.getPuntuacionDeUsuario(usernameAmigo);
            agregarFila(tabla,usernameAmigo,""+puntos);
        }
    }
    
}
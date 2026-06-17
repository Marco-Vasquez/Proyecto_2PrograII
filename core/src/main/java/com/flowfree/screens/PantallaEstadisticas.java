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
    private GestorIdiomas idiomas=GestorIdiomas.getInstance();
    private static final float ANCHO_ETIQUETA=180f;
    private static final float ANCHO_VALOR=120f;
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
        tablaEnc.add(new Label(idiomas.get("app.titulo"),skin)).center().expand();
        escenario.addActor(tablaEnc);
        Estadisticas stats=usuarioAct.getEstadisticas();
        long segTotales=stats.getTiempoTotalSeg();
        double promedio=stats.getTiempoPromedioPorNivel();
        Table tablaContenido=new Table();
        tablaContenido.pad(12f).left();
        agregarFila(tablaContenido,idiomas.get("stats.usuario"),usuarioAct.getUsername());
        agregarFila(tablaContenido,idiomas.get("stats.partidas"),""+stats.getPartidasJugadas());
        agregarFila(tablaContenido,idiomas.get("stats.niveles"),""+stats.getNivelesCompletados());
        agregarFila(tablaContenido,idiomas.get("stats.tiempo.total"),formatTiempo(segTotales));
        agregarFila(tablaContenido,idiomas.get("stats.tiempo.promedio"),formatTiempo((long)promedio));
        agregarFila(tablaContenido,idiomas.get("stats.movimientos"),""+stats.getTotalMovimientos());
        agregarFila(tablaContenido,idiomas.get("stats.fallos"),""+stats.getTotalFallos());
        agregarFila(tablaContenido,idiomas.get("stats.puntuacion"),""+stats.getPuntuacionGeneral());
        tablaContenido.add(new Label(idiomas.get("stats.historial"),skin))
            .left().padTop(14f).padBottom(6f).colspan(4).row();
        List<String> historial=stats.getHistorial();
        if(historial.isEmpty()){
            tablaContenido.add(new Label(idiomas.get("stats.sinpartidas"),skin)).colspan(4).center().row();
        }else{
            tablaContenido.add(new Label(idiomas.get("stats.col.nivel"),skin)).width(50f).left().padBottom(4f);
            tablaContenido.add(new Label(idiomas.get("stats.col.tiempo"),skin)).width(70f).left().padBottom(4f);
            tablaContenido.add(new Label(idiomas.get("stats.col.mov"),skin)).width(50f).left().padBottom(4f);
            tablaContenido.add(new Label(idiomas.get("stats.col.fallos"),skin)).width(55f).left().padBottom(4f);
            tablaContenido.add(new Label(idiomas.get("stats.col.pts"),skin)).width(55f).left().padBottom(4f).row();
            int inicio=Math.max(0,historial.size()-5);
            for(int posicion=inicio;posicion<historial.size();posicion++){
                String[] partes=parsearHistorial(historial.get(posicion));
                tablaContenido.add(new Label(partes[0],skin)).width(50f).left().padBottom(3f);
                tablaContenido.add(new Label(partes[1],skin)).width(70f).left().padBottom(3f);
                tablaContenido.add(new Label(partes[2],skin)).width(50f).left().padBottom(3f);
                tablaContenido.add(new Label(partes[3],skin)).width(55f).left().padBottom(3f);
                tablaContenido.add(new Label(partes[4],skin)).width(55f).left().padBottom(3f).row();
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
        tablaRaiz.add(scroll).width(panelAncho*0.82f).maxHeight(altoTotal*0.62f).row();
        tablaRaiz.add().height(10f).row();
        TextButton btnVolver=new TextButton(idiomas.get("stats.btn.volver"),skin);
        btnVolver.setColor(EstiloUI.BTN_AZUL);
        tablaRaiz.add(btnVolver).width(panelAncho*0.45f).height(36f).row();
        escenario.addActor(tablaRaiz);
        btnVolver.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaMenu(juego,usuarioAct));
            }
        });
    }
    private void agregarFila(Table tabla,String etiqueta,String valor){
        tabla.add(new Label(etiqueta,skin)).width(ANCHO_ETIQUETA).left().padBottom(6f);
        tabla.add(new Label(valor,skin)).width(ANCHO_VALOR).left().padBottom(6f).colspan(3).row();
    }
    private String formatTiempo(long segundos){
        return String.format("%02d:%02d",segundos/60,segundos%60);
    }
    private String[] parsearHistorial(String linea){
        String[] resultado=new String[]{"?","?","?","?","?"};
        try{
            String[] partes=linea.split("\\|");
            if(partes.length>=5){
                resultado[0]=partes[0].replace("Nivel","").trim();
                resultado[1]=partes[1].trim();
                resultado[2]=partes[2].replace("Mov:","").trim();
                resultado[3]=partes[3].replace("Fallos:","").trim();
                resultado[4]=partes[4].replace("Pts:","").trim();
            }
        }catch(Exception ignored){}
        return resultado;
    }
    private void construirRankingAmigos(Table tabla){
        GestorUsuarios gestorUsuarios=new GestorUsuarios();
        List<String> amigos=usuarioAct.getAmigos();
        if(amigos.isEmpty()) return;
        tabla.add(new Label(idiomas.get("stats.ranking"),skin)).left().padTop(14f).padBottom(6f).colspan(4).row();
        tabla.add(new Label(idiomas.get("stats.col.usuario"),skin)).width(ANCHO_ETIQUETA).left().padBottom(4f);
        tabla.add(new Label(idiomas.get("stats.col.puntuacion"),skin)).width(ANCHO_VALOR).left().padBottom(4f).colspan(3).row();
        agregarFila(tabla,usuarioAct.getUsername()+" "+idiomas.get("stats.tu"),""+usuarioAct.getEstadisticas().getPuntuacionGeneral());
        for(String usernameAmigo:amigos){
            int puntos=gestorUsuarios.getPuntuacionDeUsuario(usernameAmigo);
            agregarFila(tabla,usernameAmigo,""+puntos);
        }
    }
}
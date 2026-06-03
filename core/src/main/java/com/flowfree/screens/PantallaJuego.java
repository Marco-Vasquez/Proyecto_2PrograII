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
import com.flowfree.game.FlowFree;
import com.flowfree.game.GestorNiveles;
import com.flowfree.model.EstadoCelda;
import com.flowfree.model.Tablero;
import com.flowfree.model.Usuario;
/**
 *
 * @author andres
 */
public class PantallaJuego implements Screen{
    private static final float RADIO_PUNTO=0.38f;
    private static final float GROSOR_VIA=0.36f;
    private static final float ALTO_ENC=48f;
    private static final float ALTO_HUD=58f;
    private static final float MARGEN=10f;
    private final FlowFreeGame juego;
    private final Usuario usuarioAct;
    private final int numNivelInicial;
    private FlowFree flowfree;
    private GestorNiveles gestorNiveles;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private Label labelNivel;
    private Label labelTimer;
    private Label labelMov;
    private Label labelFails;
    private Label labelMsj;
    private float tiempoAcum;
    private float tamCelda;
    private float origenX;
    private float origenY;
    private float panelX;
    private float panelY;
    private float panelAncho;
    private float panelAlto;
    private int ultimaFilaArrastre=-1;
    private int ultimaColArrastre=-1;
    public PantallaJuego(FlowFreeGame juego,Usuario usuarioAct,int numNivel){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
        this.numNivelInicial=numNivel;
    }
    public void show(){
        escenario=new Stage(new ScreenViewport());
        skin=new Skin(Gdx.files.internal("ui/uiskin.json"));
        dibujador=new ShapeRenderer();
        gestorNiveles=new GestorNiveles();
        gestorNiveles.aplicarProgresoUsuario(usuarioAct.getNivelDesbloqueado());
        flowfree=new FlowFree();
        flowfree.cargarNivel(gestorNiveles.getNivel(numNivelInicial));
        calcularPanel();
        calcularGeometriaTablero();
        construirHUD();
        Gdx.input.setInputProcessor(escenario);
    }
    public void render(float delta){
        actualizarTimer(delta);
        procesarInput();
        Gdx.gl.glClearColor(EstiloUI.FONDO.r,EstiloUI.FONDO.g,EstiloUI.FONDO.b,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        dibujarFondo();
        dibujarTablero();
        escenario.act(delta);
        escenario.draw();
        actualizarHUD();
    }
    public void resize(int ancho,int alto){
        escenario.getViewport().update(ancho,alto,true);
        calcularPanel();
        calcularGeometriaTablero();
    }
    public void pause(){}
    public void resume(){}
    public void hide(){dispose();}
    public void dispose(){
        escenario.dispose();
        skin.dispose();
        dibujador.dispose();
    }
    private void calcularPanel(){
        float anchoTotal=Gdx.graphics.getWidth();
        float altoTotal=Gdx.graphics.getHeight();
        panelAncho=anchoTotal*0.82f;
        panelAlto=altoTotal*0.88f;
        panelX=(anchoTotal-panelAncho)/2f;
        panelY=(altoTotal-panelAlto)/2f;
    }
    private void calcularGeometriaTablero(){
        if(flowfree==null||flowfree.getTablero()==null) return;
        Tablero tablero=flowfree.getTablero();
        float espacioV=panelAlto-ALTO_ENC-ALTO_HUD-MARGEN*4;
        float espacioH=panelAncho-MARGEN*2;
        tamCelda=Math.min(espacioH/tablero.getColumnas(),espacioV/tablero.getFilas());
        float anchoTablero=tamCelda*tablero.getColumnas();
        origenX=panelX+(panelAncho-anchoTablero)/2f;
        origenY=panelY+ALTO_HUD+MARGEN*2;
    }
    private void dibujarFondo(){
        dibujador.begin(ShapeRenderer.ShapeType.Filled);
        dibujador.setColor(EstiloUI.PANEL);
        dibujador.rect(panelX,panelY,panelAncho,panelAlto);
        float encAncho=panelAncho*0.65f;
        float encX=panelX+(panelAncho-encAncho)/2f;
        float encY=panelY+panelAlto-ALTO_ENC-8f;
        dibujador.setColor(EstiloUI.ENCABEZADO);
        dibujador.rect(encX,encY,encAncho,ALTO_ENC);
        dibujador.end();
    }
    private void construirHUD(){
        float encY=panelY+panelAlto-ALTO_ENC-8f;
        Table tablaEnc=new Table();
        tablaEnc.setPosition(panelX,encY);
        tablaEnc.setSize(panelAncho,ALTO_ENC);
        tablaEnc.center();
        tablaEnc.add(new Label("Flow Free",skin)).center();
        escenario.addActor(tablaEnc);
        Table tablaHUD=new Table();
        tablaHUD.setPosition(panelX,panelY);
        tablaHUD.setSize(panelAncho,ALTO_HUD);
        tablaHUD.pad(6);
        labelNivel=new Label("Nivel "+numNivelInicial,skin);
        labelTimer=new Label("00:00",skin);
        labelMov=new Label("Mov: 0",skin);
        labelFails=new Label("Fallos: 0",skin);
        labelMsj=new Label("",skin);
        TextButton btnSalir=new TextButton("Menu",skin);
        tablaHUD.add(new Label(usuarioAct.getUsername(),skin)).left().expandX().padRight(14);
        tablaHUD.add(labelNivel).padRight(14);
        tablaHUD.add(labelTimer).padRight(14);
        tablaHUD.add(labelMov).padRight(14);
        tablaHUD.add(labelFails).padRight(14);
        tablaHUD.add(btnSalir).width(66).height(28).row();
        tablaHUD.add(labelMsj).colspan(6).center().padTop(4);
        escenario.addActor(tablaHUD);
        btnSalir.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                Gdx.app.log("PantallaJuego","Volver al menu");
            }
        });
    }
    private void actualizarHUD(){
        int segundos=(int)tiempoAcum;
        labelTimer.setText(String.format("%02d:%02d",segundos/60,segundos%60));
        labelMov.setText("Mov: "+flowfree.getMovimientos());
        labelFails.setText("Fallos: "+flowfree.getFallos());
        labelNivel.setText("Nivel "+flowfree.getNivelAct());
        if(flowfree.isNivelCompleto()){
            if(flowfree.getNivelAct()==gestorNiveles.getTotalNiveles()){
                labelMsj.setText("Felicidades, completaste todos los niveles!");
            }else{
                labelMsj.setText("Nivel completado! Pasa al siguiente.");
            }
        }
    }
    private void dibujarTablero(){
        Tablero tablero=flowfree.getTablero();
        if(tablero==null) return;
        dibujador.begin(ShapeRenderer.ShapeType.Filled);
        dibujarFondoCeldas(tablero);
        dibujarCaminos(tablero);
        dibujador.end();
        dibujarLineasCuadricula(tablero);
        dibujador.begin(ShapeRenderer.ShapeType.Filled);
        dibujarPuntosOrigen(tablero);
        dibujador.end();
    }
    private void dibujarFondoCeldas(Tablero tablero){
        Color fondoCelda=new Color(0.20f,0.08f,0.38f,1f);
        for(int fila=0;fila<tablero.getFilas();fila++){
            for(int columna=0;columna<tablero.getColumnas();columna++){
                dibujador.setColor(fondoCelda);
                dibujador.rect(celdaX(columna)+1,celdaY(tablero,fila)+1,tamCelda-2,tamCelda-2);
            }
        }
    }
    private void dibujarCaminos(Tablero tablero){
        for(int fila=0;fila<tablero.getFilas();fila++){
            for(int columna=0;columna<tablero.getColumnas();columna++){
                EstadoCelda estado=tablero.getEstado(fila,columna);
                if(estado!=EstadoCelda.CAMINO&&estado!=EstadoCelda.PUNTO_ORIGEN) continue;
                if(tablero.getColor(fila,columna)==0) continue;
                int colorId=tablero.getColor(fila,columna);
                Color colorReal=EstiloUI.COLORES_GAME[colorId<EstiloUI.COLORES_GAME.length?colorId:0];
                dibujador.setColor(colorReal);
                float x=celdaX(columna);
                float y=celdaY(tablero,fila);
                float grosor=tamCelda*GROSOR_VIA;
                float offset=(tamCelda-grosor)/2f;
                dibujador.rect(x+offset,y+offset,grosor,grosor);
                if(tablero.tieneConexion(fila,columna,Tablero.CON_ARRIBA))
                    dibujador.rect(x+offset,y+tamCelda/2f,grosor,tamCelda/2f);
                if(tablero.tieneConexion(fila,columna,Tablero.CON_ABAJO))
                    dibujador.rect(x+offset,y,grosor,tamCelda/2f);
                if(tablero.tieneConexion(fila,columna,Tablero.CON_IZQ))
                    dibujador.rect(x,y+offset,tamCelda/2f,grosor);
                if(tablero.tieneConexion(fila,columna,Tablero.CON_DER))
                    dibujador.rect(x+tamCelda/2f,y+offset,tamCelda/2f,grosor);
            }
        }
    }
    private void dibujarPuntosOrigen(Tablero tablero){
        for(int fila=0;fila<tablero.getFilas();fila++){
            for(int columna=0;columna<tablero.getColumnas();columna++){
                if(tablero.getEstado(fila,columna)!=EstadoCelda.PUNTO_ORIGEN) continue;
                int colorId=tablero.getColor(fila,columna);
                float cx=celdaX(columna)+tamCelda/2f;
                float cy=celdaY(tablero,fila)+tamCelda/2f;
                float radio=tamCelda*RADIO_PUNTO;
                dibujador.setColor(Color.WHITE);
                dibujador.circle(cx,cy,radio+2.5f,30);
                Color colorReal=EstiloUI.COLORES_GAME[colorId<EstiloUI.COLORES_GAME.length?colorId:0];
                dibujador.setColor(colorReal);
                dibujador.circle(cx,cy,radio,30);
            }
        }
    }
    private void dibujarLineasCuadricula(Tablero tablero){
        dibujador.begin(ShapeRenderer.ShapeType.Line);
        dibujador.setColor(new Color(0.45f,0.25f,0.68f,1f));
        for(int fila=0;fila<=tablero.getFilas();fila++){
            float y=origenY+fila*tamCelda;
            dibujador.line(origenX,y,origenX+tablero.getColumnas()*tamCelda,y);
        }
        for(int columna=0;columna<=tablero.getColumnas();columna++){
            float x=origenX+columna*tamCelda;
            dibujador.line(x,origenY,x,origenY+tablero.getFilas()*tamCelda);
        }
        dibujador.end();
    }
    private void procesarInput(){
        if(flowfree.isNivelCompleto()) return;
        if(Gdx.input.isTouched()){
            float touchX=Gdx.input.getX();
            float touchY=Gdx.graphics.getHeight()-Gdx.input.getY();
            int fila=pantallaAFila(touchY);
            int columna=pantallaAColumna(touchX);
            if(!flowfree.getTablero().dentroDelTablero(fila,columna)) return;
            if(Gdx.input.justTouched()){
                ultimaFilaArrastre=fila;
                ultimaColArrastre=columna;
                flowfree.iniciarTrazo(fila,columna);
            }else{
                if(fila!=ultimaFilaArrastre||columna!=ultimaColArrastre){
                    flowfree.continuarTrazo(fila,columna);
                    ultimaFilaArrastre=fila;
                    ultimaColArrastre=columna;
                }
            }
        }else{
            if(ultimaFilaArrastre>=0){
                flowfree.terminarTrazo();
                ultimaFilaArrastre=-1;
                ultimaColArrastre=-1;
            }
        }
    }
    private float celdaX(int columna){return origenX+columna*tamCelda;}
    private float celdaY(Tablero tablero,int fila){return origenY+(tablero.getFilas()-1-fila)*tamCelda;}
    private int pantallaAFila(float pixelY){return flowfree.getTablero().getFilas()-1-(int)((pixelY-origenY)/tamCelda);}
    private int pantallaAColumna(float pixelX){return (int)((pixelX-origenX)/tamCelda);}
    private void actualizarTimer(float delta){
        if(!flowfree.isEnPausa()&&!flowfree.isNivelCompleto()) tiempoAcum+=delta;
    }
}

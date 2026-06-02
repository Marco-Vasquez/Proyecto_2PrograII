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
    private static final Color[] COLORES_JUEGO={
        Color.WHITE,Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW,new Color(1f,0.5f,0f,1f),Color.CYAN,Color.MAGENTA,new Color(0.5f,0f,0.5f,1f)
    };
    private static final float ALTO_HUD=80f;
    private static final float MARGEN=16f;
    private static final float RADIO_PUNTO=0.35f;
    private final FlowFreeGame juego;
    private final Usuario usuarioAct;
    private final int numNivelInicial;
    private FlowFree flowfree;
    private GestorNiveles gestorNiveles;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private Label labelNombre;
    private Label labelNivel;
    private Label labelTimer;
    private Label labelMov;
    private Label labelFails;
    private Label labelMsj;
    private float tiempoAcum;
    private float tamCelda;
    private float origenX;
    private float origenY;
    private int ultimaFilaArrastre;
    private int ultimaColArrastre;
    public PantallaJuego(FlowFreeGame juego,Usuario usuarioAct,int numNivel){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
        this.numNivelInicial=numNivel;
    }
    public void render(float delta){
        actualizarTimer(delta);
        procesarInput();
        Gdx.gl.glClearColor(0.13f,0.13f,0.20f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        dibujarTablero();
        escenario.act(delta);
        escenario.draw();
        actualizarHUD();
    }
    public void show(){
        escenario=new Stage(new ScreenViewport());
        skin=new Skin(Gdx.files.internal("ui/uiskin.json"));
        dibujador=new ShapeRenderer();
        gestorNiveles=new GestorNiveles();
        gestorNiveles.aplicarProgresoUsuario(usuarioAct.getNivelDesbloqueado());
        flowfree=new FlowFree();
        flowfree.cargarNivel(gestorNiveles.getNivel(numNivelInicial));
        calcularGeometriaTablero();
        construirHUD();
        Gdx.input.setInputProcessor(escenario);
    }
    public void resize(int ancho,int alto){
        escenario.getViewport().update(ancho,alto,true);
        calcularGeometriaTablero();
    }
    public void pause(){
    }
    public void resume(){
    }
    public void hide(){
        dispose();
    }
    public void dispose(){
        escenario.dispose();
        skin.dispose();
        dibujador.dispose();
    }
    private void construirHUD(){
        Table tablaHUD=new Table();
        tablaHUD.setFillParent(true);
        tablaHUD.top().pad(8);
        labelNombre=new Label("Usuario: "+usuarioAct.getNombreCompleto(),skin);
        labelNivel=new Label("Nivel: "+numNivelInicial,skin);
        labelTimer=new Label("Tiempo: 0s",skin);
        labelMov=new Label("Mov: 0",skin);
        labelFails=new Label("Fallos: 0",skin);
        labelMsj=new Label("",skin);
        tablaHUD.add(labelNombre).left().expandX().padRight(10);
        tablaHUD.add(labelNivel).padRight(10);
        tablaHUD.add(labelTimer).padRight(10);
        tablaHUD.add(labelMov).padRight(10);
        tablaHUD.add(labelFails).padRight(10);
        TextButton btnSalir;
        btnSalir=new TextButton("Menu",skin);
        tablaHUD.add(btnSalir).width(80).height(32);
        tablaHUD.row();
        tablaHUD.add(labelMsj).colspan(6).center().padTop(4);
        escenario.addActor(tablaHUD);
        btnSalir.addListener(new ChangeListener(){
           public void changed(ChangeEvent evento,Actor actor){
               Gdx.app.log("PantallaJuego","Volver al menu");
           } 
        });
    }
    private void calcularGeometriaTablero(){
        if(flowfree==null || flowfree.getTablero()==null){
            return;
        }
        Tablero tablero;
        tablero=flowfree.getTablero();
        float anchoDispo,altoDispo,tamPorAncho,tamPorAlto,anchoTablero,altoTablero;
        anchoDispo=Gdx.graphics.getWidth()-MARGEN*2;
        altoDispo=Gdx.graphics.getHeight()-ALTO_HUD-MARGEN*2;
        tamPorAncho=anchoDispo/tablero.getColumnas();
        tamPorAlto=altoDispo/tablero.getFilas();
        tamCelda=Math.min(tamPorAncho,tamPorAlto);
        anchoTablero=tamCelda*tablero.getColumnas();
        altoTablero=tamCelda*tablero.getFilas();
        origenX=(Gdx.graphics.getWidth()-anchoTablero)/2f;
        origenY=(Gdx.graphics.getHeight()-altoTablero)/2f-ALTO_HUD/2f;
    }
    private void dibujarTablero(){
        Tablero tablero;
        tablero=flowfree.getTablero();
        if(tablero==null){
            return;
        }
        dibujador.begin(ShapeRenderer.ShapeType.Filled);
        for(int fila=0;fila<tablero.getFilas();fila++){
            for(int columna=0;columna<tablero.getColumnas();columna++){
                dibujarCelda(tablero,fila,columna);
            }
        }
        dibujador.end();
        dibujarLineasCuadricula(tablero);
    }
    private void dibujarCelda(Tablero tablero,int fila,int columna){
        int colorId;
        float x,y;
        EstadoCelda estado;
        colorId=tablero.getColor(fila,columna);
        estado=tablero.getEstado(fila,columna);
        x=origenX+columna*tamCelda;
        y=origenY+(tablero.getFilas()-1-fila)*tamCelda;
        dibujador.setColor(new Color(0.f,0.2f,0.25f,1f));
        dibujador.rect(x+1,y+1,tamCelda-2,tamCelda-2);
        if(colorId==0){
            return;
        }
        Color colorReal;
        colorReal=obtenerColor(colorId);
        if(estado==EstadoCelda.PUNTO_ORIGEN){
            dibujador.setColor(colorReal);
            float radio=tamCelda*RADIO_PUNTO;
            dibujador.circle(x+tamCelda/2f,y+tamCelda/2f,radio,24);
        }
        else if(estado==EstadoCelda.CAMINO){
            dibujador.setColor(colorReal);
            float grosor,offset;
            grosor=tamCelda*0.35f;
            offset=(tamCelda-grosor)/2f;
            dibujador.rect(x+offset,y+offset,grosor,grosor);
            Tablero t=tablero;
            int filas,columnas;
            filas=t.getFilas();
            columnas=t.getColumnas();
            if(fila>0 && t.getColor(fila-1,columna)==colorId){
                dibujador.rect(x+offset,y+tamCelda/2f,grosor,tamCelda/2f);
            }
            if(fila<filas-1 && t.getColor(fila+1,columna)==colorId){
                dibujador.rect(x+offset,y,grosor,tamCelda/2f);
            }
            if(columna>0 && t.getColor(fila,columna-1)==colorId){
                dibujador.rect(x,y+offset,tamCelda/2f,grosor);
            }
            if(columna<columnas-1 && t.getColor(fila,columna+1)==colorId){
                dibujador.rect(x+tamCelda/2f,y+offset,tamCelda/2f,grosor);
            }
        }
    }
    private void dibujarLineasCuadricula(Tablero tablero){
        dibujador.begin(ShapeRenderer.ShapeType.Line);
        dibujador.setColor(new Color(0.35f,0.35f,0.40f,1f));
        for(int fila=0;fila<tablero.getFilas();fila++){
            float y;
            y=origenY+fila*tamCelda;
            dibujador.line(origenX,y,origenX+tablero.getColumnas()*tamCelda,y);
        }
        for(int columna=0;columna<tablero.getColumnas();columna++){
            float x;
            x=origenX+columna*tamCelda;
            dibujador.line(x,origenY,x,origenY+tablero.getFilas()*tamCelda);
        }
        dibujador.end();
    }
    private void procesarInput(){
        if(flowfree.isNivelCompleto()){
            return;
        }
        if(Gdx.input.isTouched()){
            float touchX,touchY;
            int fila,columna;
            touchX=Gdx.input.getX();
            touchY=Gdx.graphics.getHeight()-Gdx.input.getY();
            fila=pantallaAFila(touchY);
            columna=pantallaAColumna(touchX);
            if(!flowfree.getTablero().dentroDelTablero(fila,columna)){
                return;
            }
            if(Gdx.input.justTouched()){
                ultimaFilaArrastre=fila;
                ultimaColArrastre=columna;
                flowfree.iniciarTrazo(fila,columna);
            }
            else{
                if(fila!=ultimaFilaArrastre || columna!=ultimaColArrastre){
                    flowfree.continuarTrazo(fila,columna);
                    ultimaFilaArrastre=fila;
                    ultimaColArrastre=columna;
                }
            }
        }
        else{
            if(!Gdx.input.isTouched() && ultimaFilaArrastre>=0){
                flowfree.terminarTrazo();
                ultimaFilaArrastre=-1;
                ultimaColArrastre=-1;
            }
        }
    }
    private int pantallaAFila(float pixelY){
        Tablero tablero;
        float relativo;
        int fila;
        tablero=flowfree.getTablero();
        relativo=pixelY-origenY;
        fila=tablero.getFilas()-1-(int)(relativo/tamCelda);
        return fila;
    }
    private int pantallaAColumna(float PixelX){
        float relativo;
        relativo=PixelX-origenX;
        return (int)(relativo/tamCelda);
    }
    private void actualizarTimer(float delta){
        if(!flowfree.isEnPausa() && !flowfree.isNivelCompleto()){
            tiempoAcum+=delta;
        }
    }
    private void actualizarHUD(){
        int segundos,minutos,segsResto;
        segundos=(int) tiempoAcum;
        minutos=segundos/60;
        segsResto=segundos%60;
        labelTimer.setText(String.format("Tiempo: %02d:%02d",minutos,segsResto));
        labelMov.setText("Mov: "+flowfree.getMovimientos());
        labelFails.setText("Fallos: "+flowfree.getFallos());
        labelNivel.setText("Nivel: "+flowfree.getNivelAct());
        if(flowfree.isNivelCompleto()){
            if(flowfree.getNivelAct()==gestorNiveles.getTotalNiveles()){
                labelMsj.setText("¡Felicidades has completado todos los niveles!");
            }
            else{
                labelMsj.setText("¡Nivel completado! Pasa al siguiente nivel");
            }
        }
    }
    private Color obtenerColor(int colorId){
        if(colorId<0 || colorId>=COLORES_JUEGO.length){
            return Color.WHITE;
        }
        return COLORES_JUEGO[colorId];
    }
}

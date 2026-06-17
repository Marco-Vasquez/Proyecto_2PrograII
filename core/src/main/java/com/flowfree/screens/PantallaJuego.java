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
import com.flowfree.data.GestorIdiomas;
import com.flowfree.data.GestorMusica;
import com.flowfree.data.GestorUsuarios;
import java.text.MessageFormat;
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
    private static final float ALTO_HUD=44f;
    private static final float MARGEN=10f;
    private final FlowFreeGame juego;
    private final Usuario usuarioAct;
    private final int numNivelInicial;
    private FlowFree flowfree;
    private GestorNiveles gestorNiveles;
    private GestorUsuarios gestorUsuarios;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private Label labelNivel;
    private Label labelTimer;
    private Label labelMov;
    private Label labelFails;
    private Label labelMsj;
    private TextButton btnLimpiar;
    private TextButton btnDeshacer;
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
    private boolean victoriaRegistrada=false;
    private boolean modalMostrado=false;
    private com.flowfree.hilos.TimerJuego timerHilo;
    private com.flowfree.hilos.AutoSave autoSave;
    protected boolean modoReto=false;
    private GestorIdiomas idiomas=GestorIdiomas.getInstance();
    public PantallaJuego(FlowFreeGame juego,Usuario usuarioAct,int numNivel){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
        this.numNivelInicial=numNivel;
    }
    public void show(){
        skin=new Skin(Gdx.files.internal("ui/uiskin.json"));
        dibujador=new ShapeRenderer();
        gestorUsuarios=new GestorUsuarios();
        gestorNiveles=new GestorNiveles();
        gestorNiveles.aplicarProgresoUsuario(usuarioAct.getNivelDesbloqueado());
        GestorMusica.getInstance().aplicarConfiguracion(
            usuarioAct.getPerfil().getVolumen(),
            usuarioAct.getPerfil().isMusicaActiva()
        );
        GestorIdiomas.getInstance().setEspanol(usuarioAct.getPerfil().isIdiomaEspanol());
        flowfree=new FlowFree();
        if(!flowfree.cargarNivel(gestorNiveles.getNivel(numNivelInicial))){
            juego.setScreen(new PantallaNiveles(juego,usuarioAct));
            return;
        }
        timerHilo=new com.flowfree.hilos.TimerJuego();
        timerHilo.iniciar();
        autoSave=new com.flowfree.hilos.AutoSave(usuarioAct,gestorUsuarios);
        autoSave.iniciar();
        construirEscenario();
    }
    public void render(float delta){
        actualizarTimer();
        if(autoSave!=null) autoSave.ejecutarGuardado();
        procesarInput();
        verificarYRegistrarVictoria();
        Gdx.gl.glClearColor(EstiloUI.FONDO.r,EstiloUI.FONDO.g,EstiloUI.FONDO.b,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        dibujador.setProjectionMatrix(escenario.getViewport().getCamera().combined);
        dibujarFondo();
        dibujarTablero();
        escenario.act(delta);
        escenario.draw();
        actualizarHUD();
    }
    public void resize(int ancho,int alto){
        if(escenario!=null) escenario.dispose();
        construirEscenario();
    }
    public void pause(){}
    public void resume(){}
    public void hide(){dispose();}
    public void dispose(){
        if(timerHilo!=null) timerHilo.detener();
        if(autoSave!=null) autoSave.detener();
        if(escenario!=null) escenario.dispose();
        skin.dispose();
        dibujador.dispose();
    }
    private void construirEscenario(){
        escenario=new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenario);
        escenario.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        calcularPanel();
        calcularGeometriaTablero();
        construirHUD();
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
        float altoTablero=tamCelda*tablero.getFilas();
        origenX=panelX+(panelAncho-anchoTablero)/2f;
        float centroVertical=panelY+ALTO_HUD+MARGEN+(espacioV-altoTablero)/2f;
        origenY=centroVertical;
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
        Table tablaRaiz=new Table();
        tablaRaiz.setFillParent(true);
        tablaRaiz.top();
        float margenArriba=Gdx.graphics.getHeight()-(panelY+panelAlto)+8f;
        tablaRaiz.add(new Label(idiomas.get("app.titulo"),skin))
            .colspan(3).center().padTop(margenArriba).height(ALTO_ENC).row();
        tablaRaiz.add().expandY().colspan(3).row();
        Table tablaHUD=new Table();
        tablaHUD.bottom().padBottom(panelY+6f);
        labelNivel=new Label(MessageFormat.format(idiomas.get("juego.label.nivel"), numNivelInicial),skin);
        labelTimer=new Label("00:00",skin);
        labelMov=new Label(MessageFormat.format(idiomas.get("juego.mov"), 0),skin);
        labelFails=new Label(MessageFormat.format(idiomas.get("juego.fallos"), 0),skin);
        labelMsj=new Label("",skin);
        TextButton btnMenu=new TextButton(idiomas.get("juego.btn.menu"),skin);
        btnLimpiar=new TextButton(idiomas.get("juego.btn.limpiar"),skin);
        btnDeshacer=new TextButton(idiomas.get("juego.btn.deshacer"),skin);
        btnLimpiar.setColor(EstiloUI.BTN_NARANJA);
        btnDeshacer.setColor(EstiloUI.BTN_AMARILLO);
        Table tablaCentro=new Table();
        tablaCentro.add(labelNivel).padRight(30);
        tablaCentro.add(labelTimer).padRight(30);
        tablaCentro.add(labelMov).padRight(30);
        tablaCentro.add(labelFails);
        Table tablaBotones=new Table();
        tablaBotones.add(btnDeshacer).width(76).height(28).padRight(4);
        tablaBotones.add(btnLimpiar).width(70).height(28).padRight(4);
        tablaBotones.add(btnMenu).width(58).height(28);
        tablaHUD.add(new Label(usuarioAct.getUsername(),skin)).left().padLeft(10);
        tablaHUD.add(tablaCentro).expandX().center();
        tablaHUD.add(tablaBotones).right().padRight(10);
        tablaHUD.row();
        tablaHUD.add(labelMsj).colspan(3).center().padTop(2);
        tablaRaiz.add(tablaHUD).bottom().colspan(3).row();
        escenario.addActor(tablaRaiz);
        btnMenu.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaMenu(juego,usuarioAct));
            }
        });
        btnLimpiar.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                if(flowfree.isNivelCompleto()) return;
                flowfree.reiniciar();
                tiempoAcum=0;
                timerHilo.reiniciar();
                victoriaRegistrada=false;
                modalMostrado=false;
                labelMsj.setText("");
                btnLimpiar.setDisabled(false);
                btnDeshacer.setDisabled(false);
                btnLimpiar.setColor(EstiloUI.BTN_NARANJA);
                btnDeshacer.setColor(EstiloUI.BTN_AMARILLO);
            }
        });
        btnDeshacer.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                if(flowfree.isNivelCompleto()) return;
                flowfree.deshacerPaso();
            }
        });
    }
    private void actualizarHUD(){
        tiempoAcum=timerHilo.getSegundos();
        labelTimer.setText(String.format("%02d:%02d",(int)tiempoAcum/60,(int)tiempoAcum%60));
        labelMov.setText(MessageFormat.format(idiomas.get("juego.mov"), flowfree.getMovimientos()));
        labelFails.setText(MessageFormat.format(idiomas.get("juego.fallos"), flowfree.getFallos()));
        labelNivel.setText(MessageFormat.format(idiomas.get("juego.label.nivel"), flowfree.getNivelAct()));
        if(flowfree.isNivelCompleto()){
            timerHilo.pausar();
            btnLimpiar.setDisabled(true);
            btnDeshacer.setDisabled(true);
            btnLimpiar.setColor(0.35f,0.35f,0.35f,0.55f);
            btnDeshacer.setColor(0.35f,0.35f,0.35f,0.55f);
        }
    }
    private void verificarYRegistrarVictoria(){
        if(!flowfree.isNivelCompleto()||victoriaRegistrada) return;
        victoriaRegistrada=true;
        tiempoAcum=timerHilo.getSegundos();
        int nivelActual=flowfree.getNivelAct();
        int puntos=calcularPuntos();
        usuarioAct.getEstadisticas().registrarPartida(
            nivelActual,(long)tiempoAcum,
            flowfree.getMovimientos(),flowfree.getFallos(),puntos);
        if(nivelActual>=usuarioAct.getNivelDesbloqueado()
                &&nivelActual<gestorNiveles.getTotalNiveles()){
            usuarioAct.setNivelDesbloqueado(nivelActual+1);
        }
        gestorUsuarios.guardarUser(usuarioAct);
        alCompletarNivel((long)tiempoAcum,flowfree.getMovimientos(),flowfree.getFallos());
        mostrarModalVictoria();
    }
    private int calcularPuntos(){
        int base=1000;
        int penalizacionMov=flowfree.getMovimientos()*2;
        int penalizacionFallos=flowfree.getFallos()*10;
        int penalizacionTiempo=(int)(tiempoAcum);
        return Math.max(base-penalizacionMov-penalizacionFallos-penalizacionTiempo,10);
    }
    private void mostrarModalVictoria(){
        if(modalMostrado) return;
        modalMostrado=true;
        int nivelActual=flowfree.getNivelAct();
        long segundos=(long)tiempoAcum;
        int puntos=calcularPuntos();
        boolean esUltimo=nivelActual==gestorNiveles.getTotalNiveles();
        float anchoModal=340f;
        float altoModal=modoReto ? 280f : (esUltimo ? 260f : 300f);
        float modalX=(escenario.getWidth()-anchoModal)/2f;
        float modalY=(escenario.getHeight()-altoModal)/2f;
        Table fondo=new Table();
        fondo.setFillParent(true);
        fondo.setBackground(skin.newDrawable("white",new Color(0f,0f,0f,0.55f)));
        escenario.addActor(fondo);
        Table modal=new Table(skin);
        modal.setBackground(skin.newDrawable("white",EstiloUI.PANEL));
        modal.setBounds(modalX,modalY,anchoModal,altoModal);
        String tituloTexto=modoReto
            ? idiomas.get("retos.titulo")
            : (esUltimo
                ? idiomas.get("juego.victoria.todos")
                : MessageFormat.format(idiomas.get("juego.label.nivel"), nivelActual) + " " + idiomas.get("juego.victoria.nivel"));
        modal.add(new Label(tituloTexto,skin)).colspan(2).center().padTop(14f).padBottom(14f).row();
        modal.add(new Label(idiomas.get("juego.victoria.tiempo"),skin)).left().padRight(16f).padBottom(6f);
        modal.add(new Label(String.format("%02d:%02d",segundos/60,segundos%60),skin)).left().padBottom(6f).row();
        modal.add(new Label(idiomas.get("juego.victoria.movimientos"),skin)).left().padRight(16f).padBottom(6f);
        modal.add(new Label(""+flowfree.getMovimientos(),skin)).left().padBottom(6f).row();
        modal.add(new Label(idiomas.get("juego.victoria.fallos"),skin)).left().padRight(16f).padBottom(6f);
        modal.add(new Label(""+flowfree.getFallos(),skin)).left().padBottom(6f).row();
        modal.add(new Label(idiomas.get("juego.victoria.puntos"),skin)).left().padRight(16f).padBottom(14f);
        modal.add(new Label(""+puntos,skin)).left().padBottom(14f).row();
        TextButton btnSiguienteModal=new TextButton(idiomas.get("juego.victoria.btn.siguiente"),skin);
        TextButton btnReintentar=new TextButton(idiomas.get("juego.victoria.btn.reintentar"),skin);
        TextButton btnMapa=new TextButton(idiomas.get("juego.victoria.btn.mapa"),skin);
        btnSiguienteModal.setColor(EstiloUI.BTN_VERDE);
        btnReintentar.setColor(EstiloUI.BTN_AMARILLO);
        btnMapa.setColor(EstiloUI.BTN_AZUL);
        float anchoBTN=anchoModal*0.72f;
        if(!modoReto&&!esUltimo){
            modal.add(btnSiguienteModal).colspan(2).width(anchoBTN).height(34f).padBottom(6f).row();
        }
        modal.add(btnReintentar).colspan(2).width(anchoBTN).height(34f).padBottom(6f).row();
        if(!modoReto){
            modal.add(btnMapa).colspan(2).width(anchoBTN).height(34f).padBottom(14f).row();
        }else{
            TextButton btnMenuReto=new TextButton(idiomas.get("juego.btn.menu"),skin);
            btnMenuReto.setColor(EstiloUI.BTN_AZUL);
            modal.add(btnMenuReto).colspan(2).width(anchoBTN).height(34f).padBottom(14f).row();
            btnMenuReto.addListener(new ChangeListener(){
                public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                    juego.setScreen(new PantallaMenu(juego,usuarioAct));
                }
            });
        }
        escenario.addActor(modal);
        final int numSiguiente=nivelActual+1;
        btnSiguienteModal.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaJuego(juego,usuarioAct,numSiguiente));
            }
        });
        btnReintentar.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaJuego(juego,usuarioAct,numNivelInicial));
            }
        });
        btnMapa.addListener(new ChangeListener(){
            public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                juego.setScreen(new PantallaNiveles(juego,usuarioAct));
            }
        });
    }
    private void dibujarTablero(){
        Tablero tablero=flowfree.getTablero();
        if(tablero==null) return;
        dibujador.begin(ShapeRenderer.ShapeType.Filled);
        dibujarFondoCeldas(tablero);
        dibujarCaminos(tablero);
        dibujarExtensionesDeOrigenes(tablero);
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
                if(tablero.getEstado(fila,columna)!=EstadoCelda.CAMINO) continue;
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
    private void dibujarExtensionesDeOrigenes(Tablero tablero){
        for(int fila=0;fila<tablero.getFilas();fila++){
            for(int columna=0;columna<tablero.getColumnas();columna++){
                if(tablero.getEstado(fila,columna)!=EstadoCelda.PUNTO_ORIGEN) continue;
                int colorId=tablero.getColor(fila,columna);
                if(colorId==0) continue;
                boolean tieneConexion=
                    tablero.tieneConexion(fila,columna,Tablero.CON_ARRIBA)||
                    tablero.tieneConexion(fila,columna,Tablero.CON_ABAJO)||
                    tablero.tieneConexion(fila,columna,Tablero.CON_IZQ)||
                    tablero.tieneConexion(fila,columna,Tablero.CON_DER);
                if(!tieneConexion) continue;
                Color colorReal=EstiloUI.COLORES_GAME[colorId<EstiloUI.COLORES_GAME.length?colorId:0];
                dibujador.setColor(colorReal);
                float x=celdaX(columna);
                float y=celdaY(tablero,fila);
                float grosor=tamCelda*GROSOR_VIA;
                float offset=(tamCelda-grosor)/2f;
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
                flowfree.resetUltimoTrazoCerrado();
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
                flowfree.pausarTrazo();
                ultimaFilaArrastre=-1;
                ultimaColArrastre=-1;
            }
        }
    }
    private float celdaX(int columna){return origenX+columna*tamCelda;}
    private float celdaY(Tablero tablero,int fila){return origenY+(tablero.getFilas()-1-fila)*tamCelda;}
    private int pantallaAFila(float pixelY){return flowfree.getTablero().getFilas()-1-(int)((pixelY-origenY)/tamCelda);}
    private int pantallaAColumna(float pixelX){return(int)((pixelX-origenX)/tamCelda);}
    private void actualizarTimer(){
        if(flowfree.isNivelCompleto()) timerHilo.pausar();
    }
    protected void alCompletarNivel(long tiempo,int movimientos,int fallos){}
    protected Usuario getUsuarioAct(){return usuarioAct;}
    protected Label getLabelMsj(){return labelMsj;}
    
}
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
import com.flowfree.data.GestorRetos;
import com.flowfree.data.GestorUsuarios;
import com.flowfree.game.GestorNiveles;
import com.flowfree.model.Nivel;
import com.flowfree.model.RetoCompetitivo;
import com.flowfree.model.Usuario;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
/**
 *
 * @author andres
 */
public class PantallaRetos implements Screen{
    private final FlowFreeGame juego;
    private final Usuario usuarioAct;
    private final GestorRetos gestorRetos;
    private final GestorUsuarios gestorUsuarios;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private float panelX,panelY,panelAncho,panelAlto;
    private static final float ENC_ALTO=52f;
    private static final float ENC_MARGEN_TOP=14f;
    private static final float RADIO_CIRC=20f;
    private Label labelMensaje;
    private GestorIdiomas idiomas=GestorIdiomas.getInstance();
    public PantallaRetos(FlowFreeGame juego,Usuario usuarioAct){
        this.juego=juego;
        this.usuarioAct=usuarioAct;
        this.gestorRetos=new GestorRetos();
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
        Table tablaContenido=new Table();
        tablaContenido.pad(12f);
        tablaContenido.add(new Label(idiomas.get("retos.titulo"),skin)).colspan(3).center().padBottom(14f).row();
        labelMensaje=new Label("",skin);
        List<RetoCompetitivo> todosLosRetos=usuarioAct.getRetos();
        todosLosRetos.addAll(usuarioAct.getRetosRecibidos());
        boolean hayCompletados=false;
        for(RetoCompetitivo r:todosLosRetos){
            if(r.getEstado()==RetoCompetitivo.EstadoReto.COMPLETADO){
                hayCompletados=true;
                break;
            }
        }
        if(hayCompletados){
            tablaContenido.add(new Label("Resultados de retos:",skin)).colspan(3).left().padTop(10f).padBottom(6f).row();
            tablaContenido.add(new Label("Rival",skin)).left().expandX().padBottom(4f);
            tablaContenido.add(new Label("Nivel",skin)).left().padRight(10f).padBottom(4f);
            tablaContenido.add(new Label("Resultado",skin)).left().padBottom(4f).row();
            java.util.Set<String> retosVistos=new java.util.HashSet<>();
            for(RetoCompetitivo r:todosLosRetos){
                if(r.getEstado()!=RetoCompetitivo.EstadoReto.COMPLETADO) continue;
                String claveUnica=r.getRetador()+"-"+r.getRetado()+"-"+r.getNumNivel();
                if(retosVistos.contains(claveUnica)) continue;
                retosVistos.add(claveUnica);
                String rival=r.getRetador().equals(usuarioAct.getUsername())
                    ? r.getRetado() : r.getRetador();
                String ganador=r.getGanador();
                String resultado;
                if("empate".equals(ganador)) resultado=idiomas.get("retos.empate");
                else if(usuarioAct.getUsername().equals(ganador)) resultado=idiomas.get("retos.ganaste");
                else resultado=idiomas.get("retos.perdiste");
                String tiempoRetador=String.format("%02d:%02d",r.getTiempoRetador()/60,r.getTiempoRetador()%60);
                String tiempoRetado=String.format("%02d:%02d",r.getTiempoRetado()/60,r.getTiempoRetado()%60);
                String detalle=rival+" | "+tiempoRetador+" vs "+tiempoRetado
                    +" | Fallos: "+r.getFallosRetador()+" vs "+r.getFallosRetado();
                tablaContenido.add(new Label(detalle,skin)).left().expandX().padBottom(2f);
                tablaContenido.add(new Label(""+r.getNumNivel(),skin)).left().padRight(10f).padBottom(2f);
                Label labelResultado=new Label(resultado,skin);
                if(idiomas.get("retos.ganaste").equals(resultado))
                    labelResultado.setColor(EstiloUI.BTN_VERDE);
                else if(idiomas.get("retos.perdiste").equals(resultado))
                    labelResultado.setColor(EstiloUI.BTN_ROJO);
                tablaContenido.add(labelResultado).left().padBottom(2f).row();
            }
        }else{
            tablaContenido.add(new Label(idiomas.get("retos.sinRetos"),skin)).colspan(3).left().padBottom(10f).row();
        }
        tablaContenido.add(new Label(idiomas.get("retos.nuevo"),skin)).colspan(3).left().padBottom(6f).row();
        List<String> amigos=usuarioAct.getAmigos();
        if(!amigos.isEmpty()){
            SelectBox<String> selectorAmigo=new SelectBox<>(skin);
            com.badlogic.gdx.utils.Array<String> itemsAmigos=new com.badlogic.gdx.utils.Array<>();
            for(String a:amigos) itemsAmigos.add(a);
            selectorAmigo.setItems(itemsAmigos);
            GestorNiveles gestorNiveles=new GestorNiveles();
            gestorNiveles.aplicarProgresoUsuario(usuarioAct.getNivelDesbloqueado());
            Nivel[] niveles=gestorNiveles.getTodosLosNiveles();
            SelectBox<String> selectorNivel=new SelectBox<>(skin);
            com.badlogic.gdx.utils.Array<String> itemsNiveles=new com.badlogic.gdx.utils.Array<>();
            for(Nivel n:niveles){
                if(n.isDesbloqueado()) itemsNiveles.add(MessageFormat.format(idiomas.get("retos.nivel.item"), n.getNumNivel()));
            }
            if(!itemsNiveles.isEmpty()) selectorNivel.setItems(itemsNiveles);
            TextButton btnEnviarReto=new TextButton(idiomas.get("retos.btn.enviar"),skin);
            btnEnviarReto.setColor(EstiloUI.BTN_NARANJA);
            tablaContenido.add(selectorAmigo).width(panelAncho*0.35f).height(34f).padBottom(4f).colspan(2).row();
            tablaContenido.add(new Label(idiomas.get("retos.selNivel"),skin)).left().padBottom(4f);
            tablaContenido.add(selectorNivel).width(panelAncho*0.35f).height(34f).padBottom(4f).colspan(2).row();
            tablaContenido.add(btnEnviarReto).colspan(3).width(panelAncho*0.45f).height(34f).center().padBottom(6f).row();
            tablaContenido.add(labelMensaje).colspan(3).left().row();
            btnEnviarReto.addListener(new ChangeListener(){
                public void changed(ChangeListener.ChangeEvent evento,Actor actor){
                    String amigoSel=selectorAmigo.getSelected();
                    String nivelSel=selectorNivel.getSelected();
                    if(amigoSel==null||nivelSel==null) return;
                    String[] partesNivel=nivelSel.split(" ");
                    int numNivel=Integer.parseInt(partesNivel[partesNivel.length-1].trim());
                    Usuario amigo=gestorUsuarios.cargarUser(amigoSel);
                    if(amigo==null||amigo.getNivelDesbloqueado()<numNivel){
                        labelMensaje.setText(idiomas.get("retos.nivelBloqueado"));
                        return;
                    }
                    boolean exito=gestorRetos.enviarReto(usuarioAct.getUsername(),amigoSel,numNivel);
                    if(exito){
                        Usuario retadorActualizado=gestorUsuarios.cargarUser(usuarioAct.getUsername());
                        List<RetoCompetitivo> retosEnviados=retadorActualizado!=null
                            ? retadorActualizado.getRetos() : usuarioAct.getRetos();
                        if(!retosEnviados.isEmpty()){
                            RetoCompetitivo retoNuevo=retosEnviados.get(retosEnviados.size()-1);
                            juego.setScreen(new PantallaJuegoReto(juego,usuarioAct,retoNuevo,gestorRetos));
                        }
                    }else{
                        labelMensaje.setText(idiomas.get("retos.nivelBloqueado"));
                    }
                }
            });
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
        TextButton btnVolver=new TextButton(idiomas.get("retos.btn.volver"),skin);
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
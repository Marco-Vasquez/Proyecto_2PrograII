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
import com.flowfree.model.Usuario;
/**
 *
 * @author mjosu
 */
public class PantallaMenu implements Screen{
    private final FlowFreeGame juego;
    private final Usuario usuarioAct;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private float panelX,panelY,panelAncho,panelAlto;
    public PantallaMenu(FlowFreeGame juego,Usuario usuarioAct){
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
        panelAncho=anchoTotal*EstiloUI.PANEL_ANCHO_FRAC;
        panelAlto=altoTotal*EstiloUI.PANEL_ALTO_FRAC;
        panelX=(anchoTotal-panelAncho)/2f;
        panelY=(altoTotal-panelAlto)/2f;
    }
    private void dibujarFondo(){
        dibujador.begin(ShapeRenderer.ShapeType.Filled);
        dibujador.setColor(EstiloUI.PANEL);
        dibujador.rect(panelX,panelY,panelAncho,panelAlto);
        float encAncho=panelAncho*0.72f;
        float encAlto=54f;
        float encX=panelX+(panelAncho-encAncho)/2f;
        float encY=panelY+panelAlto-encAlto-14f;
        dibujador.setColor(EstiloUI.ENCABEZADO);
        dibujador.rect(encX,encY,encAncho,encAlto);
        float radioCirc=EstiloUI.RADIO_CIRCULO_DEC;
        float paso=panelAlto*0.14f;
        for(int posicion=0;posicion<5;posicion++){
            float circY=panelY+panelAlto*0.15f+posicion*paso;
            dibujador.setColor(EstiloUI.CIRCULOS_IZQ[posicion%EstiloUI.CIRCULOS_IZQ.length]);
            dibujador.circle(panelX-radioCirc*0.4f,circY,radioCirc,28);
            dibujador.setColor(EstiloUI.CIRCULOS_DER[posicion%EstiloUI.CIRCULOS_DER.length]);
            dibujador.circle(panelX+panelAncho-radioCirc*1.6f,circY,radioCirc,28);
        }
        dibujador.end();
    }
    private void construirUI(){
        float altoTotal=Gdx.graphics.getHeight();
        float encAlto=54f;
        float margenArriba=altoTotal-(panelY+panelAlto)+14f;
        Table tablaEnc=new Table();
        tablaEnc.setFillParent(true);
        tablaEnc.top();
        tablaEnc.add(new Label("Flow Free",skin)).center().padTop(margenArriba).height(encAlto);
        escenario.addActor(tablaEnc);
        Table tablaBotones=new Table();
        tablaBotones.setFillParent(true);
        tablaBotones.center();
        float anchoBTN=panelAncho*0.55f;
        float altoBTN=42f;
        float sepBTN=12f;
        TextButton btnJugar=new TextButton("Jugar",skin);
        TextButton btnNiveles=new TextButton("Niveles",skin);
        TextButton btnPerfil=new TextButton("Perfil",skin);
        TextButton btnEstadisticas=new TextButton("Estadisticas",skin);
        TextButton btnAmigos=new TextButton("Amigos",skin);
        TextButton btnConfiguraciones=new TextButton("Configuraciones",skin);
        TextButton btnSalir=new TextButton("Cerrar Sesion",skin);
        tablaBotones.add(btnJugar).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaBotones.add(btnNiveles).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaBotones.add(btnPerfil).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaBotones.add(btnEstadisticas).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaBotones.add(btnAmigos).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaBotones.add(btnConfiguraciones).width(anchoBTN).height(altoBTN).padBottom(sepBTN).row();
        tablaBotones.add(btnSalir).width(anchoBTN).height(altoBTN).row();
        escenario.addActor(tablaBotones);
        btnJugar.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                //juego.setScreen(new PantallaNiveles(juego,usuarioAct));
            }
        });
        btnNiveles.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                //juego.setScreen(new PantallaNiveles(juego,usuarioAct));
            }
        });
        btnPerfil.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                Gdx.app.log("PantallaMenu","Perfil - proximamente");
            }
        });
        btnEstadisticas.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                Gdx.app.log("PantallaMenu","Estadisticas - proximamente");
            }
        });
        btnAmigos.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                Gdx.app.log("PantallaMenu","Amigos - proximamente");
            }
        });
        btnConfiguraciones.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                Gdx.app.log("PantallaMenu","Configuraciones - proximamente");
            }
        });
        btnSalir.addListener(new ChangeListener(){
    		public void changed(ChangeEvent evento,Actor actor){
        		juego.setScreen(new PantallaInicio(juego));
    		}
	});
    }
}

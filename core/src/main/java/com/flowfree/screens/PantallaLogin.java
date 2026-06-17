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
import com.flowfree.data.HashUtil;
import com.flowfree.exceptions.InvalidPasswordException;
import com.flowfree.exceptions.UserExistenteException;
import com.flowfree.model.Perfil;
import com.flowfree.model.Usuario;
/**
 *
 * @author andres
 */

public class PantallaLogin implements Screen {
    private final FlowFreeGame juego;
    private Stage escenario;
    private Skin skin;
    private ShapeRenderer dibujador;
    private final GestorUsuarios gestorUsuarios;
    private boolean modoRegistro;
    private boolean passwordVisible;
    private TextField campoUsername;
    private TextField campoPassword;
    private TextField campoNombreCompleto;
    private TextField campoConfirmarPassword;
    private Label labelOchoCaracteres;
    private Label labelLetras;
    private Label labelNumeros;
    private Label labelSimbolos;
    private Label labelPasswordValida;
    private Label mensajeEstado;
    private Table tablaContenido;
    private float panelX,panelY,panelAncho,panelAlto;
    private static final float ANCHO_CAMPO=290f;
    private static final float ALTO_CAMPO=38f;
    private static final float ENC_ALTO=52f;
    private static final float ENC_MARGEN_TOP=14f;
    public PantallaLogin(FlowFreeGame juego,boolean iniciarEnRegistro){
        this.juego=juego;
        this.gestorUsuarios=new GestorUsuarios();
        this.modoRegistro=iniciarEnRegistro;
        this.passwordVisible=false;
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
        modoRegistro=modoRegistro;
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
        construirInterfaz();
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
        dibujador.end();
    }
    private void construirInterfaz(){
        escenario.clear();
        float encAncho=panelAncho*0.68f;
        float encX=panelX+(panelAncho-encAncho)/2f;
        float encY=panelY+panelAlto-ENC_ALTO-ENC_MARGEN_TOP;
        Table tablaEnc=new Table();
        tablaEnc.setPosition(encX,encY);
        tablaEnc.setSize(encAncho,ENC_ALTO);
        tablaEnc.add(new Label("Flow Free",skin)).center().expand();
        escenario.addActor(tablaEnc);
        float altoTotal=Gdx.graphics.getHeight();
        Table tablaRaiz=new Table();
        tablaRaiz.setFillParent(true);
        tablaRaiz.center();
        tablaContenido=new Table();
        tablaContenido.pad(16f);
        ScrollPane scroll=new ScrollPane(tablaContenido,skin);
        scroll.setFadeScrollBars(true);
        scroll.setScrollingDisabled(true,false);
        tablaRaiz.add(scroll).width(ANCHO_CAMPO+80f).maxHeight(altoTotal*0.68f);
        escenario.addActor(tablaRaiz);
        construirFormulario();
    }
    private void construirFormulario(){
        tablaContenido.clearChildren();
        String titulo=modoRegistro ? "Crear cuenta" : "Iniciar sesion";
        tablaContenido.add(new Label(titulo,skin)).colspan(2).center().padBottom(20f).row();
        tablaContenido.add(new Label("Usuario:",skin)).left().padBottom(3f);
        tablaContenido.add().row();
        campoUsername=new TextField("",skin);
        tablaContenido.add(campoUsername).colspan(2).width(ANCHO_CAMPO).height(ALTO_CAMPO).padBottom(10f).row();
        if(modoRegistro){
            tablaContenido.add(new Label("Nombre completo:",skin)).left().padBottom(3f);
            tablaContenido.add().row();
            campoNombreCompleto=new TextField("",skin);
            tablaContenido.add(campoNombreCompleto).colspan(2).width(ANCHO_CAMPO).height(ALTO_CAMPO).padBottom(10f).row();
        }
        tablaContenido.add(new Label("Contrasena:",skin)).left().padBottom(3f);
        tablaContenido.add().row();
        tablaContenido.add(crearFilaPassword()).colspan(2).padBottom(10f).row();
        if(modoRegistro){
            tablaContenido.add(new Label("Confirmar contrasena:",skin)).left().padBottom(3f);
            tablaContenido.add().row();
            campoConfirmarPassword=new TextField("",skin);
            campoConfirmarPassword.setPasswordMode(true);
            campoConfirmarPassword.setPasswordCharacter('*');
            tablaContenido.add(campoConfirmarPassword).colspan(2).width(ANCHO_CAMPO).height(ALTO_CAMPO).padBottom(10f).row();
            tablaContenido.add(crearTablaRequisitos()).colspan(2).left().padBottom(10f).row();
        }
        mensajeEstado=new Label("",skin);
        mensajeEstado.setWrap(true);
        tablaContenido.add(mensajeEstado).colspan(2).width(ANCHO_CAMPO).padBottom(8f).row();
        String textoAccion=modoRegistro ? "Registrarse" : "Entrar";
        TextButton botonAccion=new TextButton(textoAccion,skin);
        botonAccion.setColor(modoRegistro ? EstiloUI.BTN_VERDE : EstiloUI.BTN_AZUL);
        tablaContenido.add(botonAccion).colspan(2).width(ANCHO_CAMPO).height(42f).padBottom(8f).row();
        String textoCambio=modoRegistro ? "Ya tienes cuenta? Inicia sesion" : "No tienes cuenta? Registrate";
        TextButton botonCambiarModo=new TextButton(textoCambio,skin);
        botonCambiarModo.setColor(EstiloUI.BTN_MORADOCLARO);
        tablaContenido.add(botonCambiarModo).colspan(2).width(ANCHO_CAMPO).height(36f).row();
        if(modoRegistro){
            campoPassword.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
                public boolean keyTyped(com.badlogic.gdx.scenes.scene2d.InputEvent evento,char caracter){
                    actualizarRequisitosPassword(campoPassword.getText());
                    return false;
                }
            });
        }
        botonAccion.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                if(modoRegistro) intentarRegistro();
                else intentarLogin();
            }
        });
        botonCambiarModo.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                modoRegistro=!modoRegistro;
                passwordVisible=false;
                campoConfirmarPassword=null;
                construirFormulario();
            }
        });
    }
    private Table crearFilaPassword(){
        Table fila=new Table();
        campoPassword=new TextField("",skin);
        campoPassword.setPasswordMode(true);
        campoPassword.setPasswordCharacter('*');
        TextButton botonVer=new TextButton("Ver",skin);
        fila.add(campoPassword).width(ANCHO_CAMPO-70f).height(ALTO_CAMPO);
        fila.add(botonVer).width(62f).height(ALTO_CAMPO).padLeft(6f);
        botonVer.addListener(new ChangeListener(){
            public void changed(ChangeEvent evento,Actor actor){
                passwordVisible=!passwordVisible;
                campoPassword.setPasswordMode(!passwordVisible);
                botonVer.setText(passwordVisible ? "Ocultar" : "Ver");
                if(modoRegistro&&campoConfirmarPassword!=null)
                    campoConfirmarPassword.setPasswordMode(!passwordVisible);
            }
        });
        return fila;
    }
    private Table crearTablaRequisitos(){
        Table tablaRequisitos=new Table();
        tablaRequisitos.left();
        labelOchoCaracteres=new Label("XX Minimo 8 caracteres",skin);
        labelLetras=new Label("XX Contiene letras",skin);
        labelNumeros=new Label("XX Contiene numeros",skin);
        labelSimbolos=new Label("XX Contiene simbolo especial",skin);
        labelPasswordValida=new Label("XX Contrasena valida",skin);
        tablaRequisitos.add(labelOchoCaracteres).left().padBottom(2f).row();
        tablaRequisitos.add(labelLetras).left().padBottom(2f).row();
        tablaRequisitos.add(labelNumeros).left().padBottom(2f).row();
        tablaRequisitos.add(labelSimbolos).left().padBottom(2f).row();
        tablaRequisitos.add(labelPasswordValida).left().row();
        return tablaRequisitos;
    }
    private void actualizarRequisitosPassword(String password){
        marcarRequisito(labelOchoCaracteres,HashUtil.tieneMinOchoCarac(password),"Minimo 8 caracteres");
        marcarRequisito(labelLetras,HashUtil.tieneLetras(password),"Contiene letras");
        marcarRequisito(labelNumeros,HashUtil.tieneNumeros(password),"Contiene numeros");
        marcarRequisito(labelSimbolos,HashUtil.tieneSimbolos(password),"Contiene simbolo especial");
        marcarRequisito(labelPasswordValida,HashUtil.isValidPassword(password),"Contrasena valida");
    }
    private void marcarRequisito(Label etiqueta,boolean cumplido,String texto){
        etiqueta.setText((cumplido ? "OK " : "XX ")+texto);
    }
    private void intentarLogin(){
        String username=campoUsername.getText().trim();
        String password=campoPassword.getText();
        if(username.isEmpty()||password.isEmpty()){
            mensajeEstado.setText("Completa todos los campos");
            return;
        }
        Usuario[] resultado=new Usuario[1];
        GestorUsuarios.ResultadoLogin estado=gestorUsuarios.iniciarSesion(username,password,resultado);
        switch(estado){
            case EXITO:
                Usuario usuario=resultado[0];
                Perfil perfil=usuario.getPerfil();
                GestorIdiomas.getInstance().setEspanol(perfil.isIdiomaEspanol());
                com.flowfree.data.GestorMusica.getInstance().aplicarConfiguracion(perfil.getVolumen(), perfil.isMusicaActiva());
                juego.setScreen(new PantallaMenu(juego, usuario));
                break;
            case USUARIO_NO_EXISTE:
                mensajeEstado.setText("El usuario no existe");
                break;
            case PASSWORD_INCORRECTA:
                mensajeEstado.setText("Contrasena incorrecta");
                break;
            case ERROR_ARCHIVO:
                mensajeEstado.setText("Error al leer datos del usuario");
                break;
        }
    }
    private void intentarRegistro(){
        String username=campoUsername.getText().trim();
        String nombreCompleto=campoNombreCompleto.getText().trim();
        String password=campoPassword.getText();
        String confirmacion=campoConfirmarPassword.getText();
        if(username.isEmpty()||nombreCompleto.isEmpty()||password.isEmpty()||confirmacion.isEmpty()){
            mensajeEstado.setText("Completa todos los campos");
            return;
        }
        if(!password.equals(confirmacion)){
            mensajeEstado.setText("Las contrasenas no coinciden");
            return;
        }
        try{
            gestorUsuarios.registrarUsuarioSeguro(username,password,nombreCompleto);
            mensajeEstado.setText("Usuario registrado. Ahora puedes iniciar sesion.");
            modoRegistro=false;
            passwordVisible=false;
            campoConfirmarPassword=null;
            construirFormulario();
        }catch(UserExistenteException error){
            mensajeEstado.setText(error.getMessage());
        }catch(InvalidPasswordException error){
            mensajeEstado.setText(error.getMessage());
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.flowfree.FlowFreeGame;
import com.flowfree.data.GestorUsuarios;
import com.flowfree.data.HashUtil;
import com.flowfree.exceptions.InvalidPasswordException;
import com.flowfree.exceptions.UserExistenteException;
import com.flowfree.model.Usuario;
/**
 *
 * @author andres
 */

public class PantallaLogin implements Screen {

    private final FlowFreeGame juego;
    private Stage escenario;
    private Skin skin;

    private final GestorUsuarios gestorUsuarios;
    private boolean modoRegistro;
    private TextField campoUsername;
    private TextField campoPassword;
    private Label mensajeEstado;
    private TextField campoNombreCompleto;
    private TextField campoConfirmarPassword;
    
    private Label labelOchoCaracteres;
    private Label labelLetras;
    private Label labelNumeros;
    private Label labelSimbolos;
    private Label labelPasswordValida;

    private boolean passwordVisible;
    private Table tablaContenido;

    public PantallaLogin(FlowFreeGame juego) {
        this.juego = juego;
        this.gestorUsuarios = new GestorUsuarios();
        this.modoRegistro = false;
        this.passwordVisible = false;
    }

    @Override
    public void show() {
        escenario = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenario);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center();
        escenario.addActor(tablaPrincipal);

        tablaContenido = new Table();
        tablaPrincipal.add(tablaContenido).pad(20);

        construirFormulario();
    }

    private void construirFormulario() {
        tablaContenido.clearChildren();

        float anchocamp = 300;
        float altoCampo = 40;

        String titulo = modoRegistro ? "Registro de Usuario" : "Iniciar Sesion";
        tablaContenido.add(new Label(titulo, skin, "default")).colspan(2).padBottom(20).row();

        tablaContenido.add(new Label("Username:", skin)).left().padRight(10);
        campoUsername = new TextField("", skin);
        tablaContenido.add(campoUsername).width(anchocamp).height(altoCampo).row();

        if (modoRegistro) {
            tablaContenido.add(new Label("Nombre completo:", skin)).left().padRight(10);
            campoNombreCompleto = new TextField("", skin);
            tablaContenido.add(campoNombreCompleto).width(anchocamp).height(altoCampo).row();
        }

        tablaContenido.add(new Label("Contrasena:", skin)).left().padRight(10);
        Table filaPassword = new Table();
        campoPassword = new TextField("", skin);
        campoPassword.setPasswordMode(true);
        campoPassword.setPasswordCharacter('*');
        filaPassword.add(campoPassword).width(230).height(altoCampo);

        TextButton botonVerPassword = new TextButton("Ver", skin);
        filaPassword.add(botonVerPassword).width(60).height(altoCampo).padLeft(5);
        tablaContenido.add(filaPassword).row();

        if (modoRegistro) {
            tablaContenido.add(new Label("Confirmar contrasena:", skin)).left().padRight(10);
            campoConfirmarPassword = new TextField("", skin);
            campoConfirmarPassword.setPasswordMode(true);
            campoConfirmarPassword.setPasswordCharacter('*');
            tablaContenido.add(campoConfirmarPassword).width(anchocamp).height(altoCampo).row();
        }

        if (modoRegistro){
            tablaContenido.add(new Label("Requisitos:", skin)).left().padTop(10);
            Table tablaRequisitos = new Table();
            labelOchoCaracteres = new Label("Minimo 8 caracteres", skin);
            labelLetras = new Label("Contiene letras", skin);
            labelNumeros = new Label("Contiene numeros", skin);
            labelSimbolos = new Label("Contiene simbolo especial", skin);
            labelPasswordValida = new Label("Contrasena válida", skin);
            tablaRequisitos.add(labelOchoCaracteres).left().row();
            tablaRequisitos.add(labelLetras).left().row();
            tablaRequisitos.add(labelNumeros).left().row();
            tablaRequisitos.add(labelSimbolos).left().row();
            tablaRequisitos.add(labelPasswordValida).left().row();
            tablaContenido.add(tablaRequisitos).left().padBottom(10).row();
        }

        mensajeEstado = new Label("",skin);
        tablaContenido.add(mensajeEstado).colspan(2).padTop(5).row();

        String textoBotonAccion = modoRegistro ? "Registrarse" : "Entrar";
        TextButton botonAccion = new TextButton(textoBotonAccion,skin);
        tablaContenido.add(botonAccion).colspan(2).width(200).height(45).padTop(10).row();

        String textoBotonCambio = modoRegistro ? "¿Ya tienes cuenta? Inicia sesion" : "¿No tienes cuenta? Regístrate";
        TextButton botonCambiarModo = new TextButton(textoBotonCambio,skin);
        tablaContenido.add(botonCambiarModo).colspan(2).padTop(5).row();

        botonVerPassword.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent evento, Actor actor) {
                alternarVisibilidadPassword(botonVerPassword);
            }
        });

        if (modoRegistro){
            campoPassword.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener(){
                @Override
                public boolean keyTyped(com.badlogic.gdx.scenes.scene2d.InputEvent evento,char caracter){
                    actualizarRequisitosPassword(campoPassword.getText());
                    return false;
                }
            });
        }

        botonAccion.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent evento, Actor actor){
                if (modoRegistro){
                    intentarRegistro();
                } 
                else{
                    intentarLogin();
                }
            }
        });

        botonCambiarModo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent evento, Actor actor){
                modoRegistro = !modoRegistro;
                passwordVisible = false;
                construirFormulario();
            }
        });
    }

    private void alternarVisibilidadPassword(TextButton boton){
        passwordVisible = !passwordVisible;
        campoPassword.setPasswordMode(!passwordVisible);
        boton.setText(passwordVisible ? "Ocultar" : "Ver");
        if (modoRegistro && campoConfirmarPassword != null) {
            campoConfirmarPassword.setPasswordMode(!passwordVisible);
        }
    }

    private void actualizarRequisitosPassword(String password) {
        marcarRequisito(labelOchoCaracteres, HashUtil.tieneMinOchoCarac(password),"Minimo 8 caracteres");
        marcarRequisito(labelLetras,HashUtil.tieneLetras(password),"Contiene letras");
        marcarRequisito(labelNumeros,HashUtil.tieneNumeros(password),"Contiene números");
        marcarRequisito(labelSimbolos,HashUtil.tieneSimbolos(password),"Contiene símbolo especial");
        marcarRequisito(labelPasswordValida, HashUtil.isValidPassword(password),"Contrasena valida");
    }

    private void marcarRequisito(Label etiqueta, boolean cumplido, String texto) {
        etiqueta.setText((cumplido ? "? " : "? ") + texto);
    }

    private void intentarLogin() {
        String username  = campoUsername.getText().trim();
        String password  = campoPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mensajeEstado.setText("Completá todos los campos.");
            return;
        }

        Usuario[] resultado = new Usuario[1];
        GestorUsuarios.ResultadoLogin estado = gestorUsuarios.iniciarSesion(username,password,resultado);

        switch (estado) {
            case EXITO:
                mensajeEstado.setText("¡Bienvenido, " + resultado[0].getNombreCompleto() + "!");
                break;
            case USUARIO_NO_EXISTE:
                mensajeEstado.setText("El usuario no existe.");
                break;
            case PASSWORD_INCORRECTA:
                mensajeEstado.setText("Contrasena incorrecta.");
                break;
            case ERROR_ARCHIVO:
                mensajeEstado.setText("Error al leer datos del usuario.");
                break;
        }
    }

    private void intentarRegistro() {
        String username = campoUsername.getText().trim();
        String nombreCompleto = campoNombreCompleto.getText().trim();
        String password = campoPassword.getText();
        String confirmacion = campoConfirmarPassword.getText();

        if (username.isEmpty() || nombreCompleto.isEmpty() || password.isEmpty() || confirmacion.isEmpty()){
            mensajeEstado.setText("Completá todos los campos.");
            return;
        }

        if (!password.equals(confirmacion)){
            mensajeEstado.setText("Las contraseñas no coinciden.");
            return;
        }

        try {
            gestorUsuarios.registrarUsuarioSeguro(username, password, nombreCompleto);
            mensajeEstado.setText("Usuario registrado. ¡Ahora podés iniciar sesión!");
            modoRegistro = false;
            construirFormulario();
        } catch (UserExistenteException error) {
            mensajeEstado.setText(error.getMessage());
        } catch (InvalidPasswordException error) {
            mensajeEstado.setText(error.getMessage());
        }
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.12f,0.12f,0.18f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        escenario.act(delta);
        escenario.draw();
    }

    @Override
    public void resize(int ancho,int alto){
        escenario.getViewport().update(ancho, alto, true);
    }

    @Override public void pause(){}
    @Override public void resume(){}

    @Override
    public void hide(){
        dispose();
    }

    @Override
    public void dispose(){
        escenario.dispose();
        skin.dispose();
    }
}

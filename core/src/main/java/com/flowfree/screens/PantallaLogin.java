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

    private static final float ANCHO_CAMPO = 280f;
    private static final float ALTO_CAMPO  = 36f;
    private static final float ANCHO_PANEL = 420f;

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
        construirInterfaz();
    }

    private void construirInterfaz() {
        escenario.clear();

        Table tablaPrincipal = new Table();
        tablaPrincipal.setFillParent(true);
        tablaPrincipal.center();
        escenario.addActor(tablaPrincipal);

        tablaContenido = new Table();
        tablaContenido.pad(30);

        ScrollPane scroll = new ScrollPane(tablaContenido, skin);
        scroll.setFadeScrollBars(true);
        scroll.setScrollingDisabled(true, false);

        tablaPrincipal.add(scroll).width(ANCHO_PANEL).maxHeight(540);

        construirFormulario();
    }

    private void construirFormulario() {
        tablaContenido.clearChildren();

        String titulo = modoRegistro ? "Crear cuenta" : "Iniciar sesión";
        tablaContenido.add(new Label(titulo, skin, "default"))
                .colspan(2).center().padBottom(24).row();

        tablaContenido.add(new Label("Usuario:", skin)).left().padBottom(4);
        tablaContenido.add().row();
        campoUsername = new TextField("", skin);
        tablaContenido.add(campoUsername)
                .colspan(2).width(ANCHO_CAMPO).height(ALTO_CAMPO).padBottom(12).row();

        if (modoRegistro) {
            tablaContenido.add(new Label("Nombre completo:", skin)).left().padBottom(4);
            tablaContenido.add().row();
            campoNombreCompleto = new TextField("", skin);
            tablaContenido.add(campoNombreCompleto)
                    .colspan(2).width(ANCHO_CAMPO).height(ALTO_CAMPO).padBottom(12).row();
        }

        tablaContenido.add(new Label("Contraseña:", skin)).left().padBottom(4);
        tablaContenido.add().row();
        tablaContenido.add(crearFilaPassword()).colspan(2).padBottom(12).row();

        if (modoRegistro) {
            tablaContenido.add(new Label("Confirmar contraseña:", skin)).left().padBottom(4);
            tablaContenido.add().row();
            campoConfirmarPassword = new TextField("", skin);
            campoConfirmarPassword.setPasswordMode(true);
            campoConfirmarPassword.setPasswordCharacter('*');
            tablaContenido.add(campoConfirmarPassword)
                    .colspan(2).width(ANCHO_CAMPO).height(ALTO_CAMPO).padBottom(12).row();
        }

        if (modoRegistro) {
            tablaContenido.add(crearTablaRequisitos()).colspan(2).left().padBottom(14).row();
        }

        mensajeEstado = new Label("", skin);
        mensajeEstado.setWrap(true);
        tablaContenido.add(mensajeEstado)
                .colspan(2).width(ANCHO_CAMPO).padBottom(10).row();

        String textoAccion = modoRegistro ? "Registrarse" : "Entrar";
        TextButton botonAccion = new TextButton(textoAccion, skin);
        tablaContenido.add(botonAccion)
                .colspan(2).width(ANCHO_CAMPO).height(42).padBottom(8).row();

        String textoCambio = modoRegistro ? "Ya tienes cuenta? Inicia sesion" : "No tienes cuenta? Registrate";
        TextButton botonCambiarModo = new TextButton(textoCambio, skin);
        tablaContenido.add(botonCambiarModo)
                .colspan(2).width(ANCHO_CAMPO).height(36).row();

        if (modoRegistro) {
            campoPassword.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
                @Override
                public boolean keyTyped(com.badlogic.gdx.scenes.scene2d.InputEvent evento, char caracter) {
                    actualizarRequisitosPassword(campoPassword.getText());
                    return false;
                }
            });
        }

        botonAccion.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent evento, Actor actor) {
                if (modoRegistro) {
                    intentarRegistro();
                } else {
                    intentarLogin();
                }
            }
        });

        botonCambiarModo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent evento, Actor actor) {
                modoRegistro     = !modoRegistro;
                passwordVisible  = false;
                campoConfirmarPassword = null;
                construirInterfaz();
            }
        });
    }

    private Table crearFilaPassword() {
        Table fila = new Table();

        campoPassword = new TextField("", skin);
        campoPassword.setPasswordMode(true);
        campoPassword.setPasswordCharacter('*');

        TextButton botonVerPassword = new TextButton("Ver", skin);

        fila.add(campoPassword).width(ANCHO_CAMPO - 70).height(ALTO_CAMPO);
        fila.add(botonVerPassword).width(62).height(ALTO_CAMPO).padLeft(6);

        botonVerPassword.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent evento, Actor actor) {
                alternarVisibilidadPassword(botonVerPassword);
            }
        });

        return fila;
    }

    private Table crearTablaRequisitos() {
        Table tablaRequisitos = new Table();
        tablaRequisitos.left();

        labelOchoCaracteres = new Label("XX Mínimo 8 caracteres", skin);
        labelLetras = new Label("XX Contiene letras", skin);
        labelNumeros = new Label("XX Contiene números", skin);
        labelSimbolos = new Label("XX Contiene símbolo especial", skin);
        labelPasswordValida = new Label("XX Contraseña valida", skin);

        tablaRequisitos.add(labelOchoCaracteres).left().padBottom(2).row();
        tablaRequisitos.add(labelLetras).left().padBottom(2).row();
        tablaRequisitos.add(labelNumeros).left().padBottom(2).row();
        tablaRequisitos.add(labelSimbolos).left().padBottom(2).row();
        tablaRequisitos.add(labelPasswordValida).left().row();

        return tablaRequisitos;
    }
    
    private void alternarVisibilidadPassword(TextButton boton) {
        passwordVisible = !passwordVisible;
        campoPassword.setPasswordMode(!passwordVisible);
        boton.setText(passwordVisible ? "Ocultar" : "Ver");
        if (modoRegistro && campoConfirmarPassword != null) {
            campoConfirmarPassword.setPasswordMode(!passwordVisible);
        }
    }

    private void actualizarRequisitosPassword(String password) {
        marcarRequisito(labelOchoCaracteres,HashUtil.tieneMinOchoCarac(password), "Mínimo 8 caracteres");
        marcarRequisito(labelLetras,HashUtil.tieneLetras(password), "Contiene letras");
        marcarRequisito(labelNumeros,HashUtil.tieneNumeros(password), "Contiene números");
        marcarRequisito(labelSimbolos, HashUtil.tieneSimbolos(password), "Contiene simbolo especial");
        marcarRequisito(labelPasswordValida,HashUtil.isValidPassword(password), "Contraseña válida");
    }

    private void marcarRequisito(Label etiqueta, boolean cumplido, String texto) {
        etiqueta.setText((cumplido ? "OK " : "XX ") + texto);
    }

    private void intentarLogin() {
        String username = campoUsername.getText().trim();
        String password = campoPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mensajeEstado.setText("Completa todos los campos");
            return;
        }

        Usuario[] resultado = new Usuario[1];
        GestorUsuarios.ResultadoLogin estado = gestorUsuarios.iniciarSesion(username, password, resultado);

        switch (estado) {
            case EXITO:
                mensajeEstado.setText("Bienvenido, " + resultado[0].getNombreCompleto() + "!");
                break;
            case USUARIO_NO_EXISTE:
                mensajeEstado.setText("El usuario no existe");
                break;
            case PASSWORD_INCORRECTA:
                mensajeEstado.setText("Contraseña incorrecta");
                break;
            case ERROR_ARCHIVO:
                mensajeEstado.setText("Error al leer datos del usuario");
                break;
        }
    }

    private void intentarRegistro() {
        String username = campoUsername.getText().trim();
        String nombreCompleto = campoNombreCompleto.getText().trim();
        String password = campoPassword.getText();
        String confirmacion = campoConfirmarPassword.getText();

        if (username.isEmpty() || nombreCompleto.isEmpty() || password.isEmpty() || confirmacion.isEmpty()) {
            mensajeEstado.setText("Completa todos los campos");
            return;
        }

        if (!password.equals(confirmacion)) {
            mensajeEstado.setText("Las contrasenas no coinciden");
            return;
        }

        try {
            gestorUsuarios.registrarUsuarioSeguro(username, password, nombreCompleto);
            mensajeEstado.setText("Usuario registrado. Ahora puedes iniciar sesion.");
            modoRegistro    = false;
            passwordVisible = false;
            campoConfirmarPassword = null;
            construirInterfaz();
        } catch (UserExistenteException error) {
            mensajeEstado.setText(error.getMessage());
        } catch (InvalidPasswordException error) {
            mensajeEstado.setText(error.getMessage());
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.13f, 0.13f, 0.20f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        escenario.act(delta);
        escenario.draw();
    }

    @Override
    public void resize(int ancho, int alto) {
        escenario.getViewport().update(ancho, alto, true);
    }

    @Override public void pause()  {}
    @Override public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        escenario.dispose();
        skin.dispose();
    }
}

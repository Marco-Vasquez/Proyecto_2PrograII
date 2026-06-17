/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.data;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author andres
 */
public class GestorIdiomas { 
    private static GestorIdiomas instancia;
    private boolean espanol;
    private Map<String,String> textos;
    private static final Map<String,String> TEXTOS_ES=new HashMap<>();
    private static final Map<String,String> TEXTOS_EN=new HashMap<>();
    static{
        TEXTOS_ES.put("app.titulo","Flow Free");
        TEXTOS_ES.put("inicio.btn.login","Iniciar Sesion");
        TEXTOS_ES.put("inicio.btn.registro","Crear cuenta");
        TEXTOS_ES.put("inicio.btn.salir","Salir");
        TEXTOS_ES.put("inicio.subtitulo","Conecta todos los puntos y completa cada nivel");
        TEXTOS_ES.put("login.titulo","Iniciar sesion");
        TEXTOS_ES.put("login.btn.entrar","Entrar");
        TEXTOS_ES.put("login.btn.registro","No tienes cuenta? Registrate");
        TEXTOS_ES.put("login.campo.usuario","Usuario:");
        TEXTOS_ES.put("login.campo.password","Contrasena:");
        TEXTOS_ES.put("login.campo.confirmar","Confirmar contrasena:");
        TEXTOS_ES.put("login.campo.nombre","Nombre completo:");
        TEXTOS_ES.put("login.error.campos","Completa todos los campos");
        TEXTOS_ES.put("login.error.noexiste","El usuario no existe");
        TEXTOS_ES.put("login.error.password","Contrasena incorrecta");
        TEXTOS_ES.put("login.error.archivo","Error al leer datos del usuario");
        TEXTOS_ES.put("login.error.nocoinciden","Las contrasenas no coinciden");
        TEXTOS_ES.put("login.exito.registro","Usuario registrado. Ahora puedes iniciar sesion.");
        TEXTOS_ES.put("registro.titulo","Crear cuenta");
        TEXTOS_ES.put("registro.btn.registrarse","Registrarse");
        TEXTOS_ES.put("registro.btn.login","Ya tienes cuenta? Inicia sesion");
        TEXTOS_ES.put("menu.btn.jugar","Jugar");
        TEXTOS_ES.put("menu.btn.niveles","Niveles");
        TEXTOS_ES.put("menu.btn.perfil","Perfil");
        TEXTOS_ES.put("menu.btn.estadisticas","Estadisticas");
        TEXTOS_ES.put("menu.btn.amigos","Amigos");
        TEXTOS_ES.put("menu.btn.configuraciones","Configuraciones");
        TEXTOS_ES.put("menu.btn.retos","Retos");
        TEXTOS_ES.put("menu.btn.cerrarsesion","Cerrar sesion");
        TEXTOS_ES.put("niveles.pista","Completa el nivel anterior para desbloquear el siguiente");
        TEXTOS_ES.put("niveles.btn.volver","Volver al menu");
        TEXTOS_ES.put("juego.btn.menu","Menu");
        TEXTOS_ES.put("juego.btn.limpiar","Limpiar");
        TEXTOS_ES.put("juego.btn.deshacer","Deshacer");
        TEXTOS_ES.put("juego.btn.siguiente","Siguiente");
        TEXTOS_ES.put("juego.victoria.nivel","completado!");
        TEXTOS_ES.put("juego.victoria.todos","Felicidades! Completaste todos los niveles!");
        TEXTOS_ES.put("juego.victoria.tiempo","Tiempo:");
        TEXTOS_ES.put("juego.victoria.movimientos","Movimientos:");
        TEXTOS_ES.put("juego.victoria.fallos","Fallos:");
        TEXTOS_ES.put("juego.victoria.puntos","Puntos:");
        TEXTOS_ES.put("juego.victoria.btn.siguiente","Siguiente nivel");
        TEXTOS_ES.put("juego.victoria.btn.reintentar","Reintentar");
        TEXTOS_ES.put("juego.victoria.btn.mapa","Ver niveles");
        TEXTOS_ES.put("stats.titulo","Estadisticas");
        TEXTOS_ES.put("stats.usuario","Usuario:");
        TEXTOS_ES.put("stats.partidas","Partidas jugadas:");
        TEXTOS_ES.put("stats.niveles","Niveles completados:");
        TEXTOS_ES.put("stats.tiempo.total","Tiempo total:");
        TEXTOS_ES.put("stats.tiempo.promedio","Tiempo promedio:");
        TEXTOS_ES.put("stats.movimientos","Movimientos totales:");
        TEXTOS_ES.put("stats.fallos","Fallos totales:");
        TEXTOS_ES.put("stats.puntuacion","Puntuacion general:");
        TEXTOS_ES.put("stats.historial","Historial de partidas:");
        TEXTOS_ES.put("stats.sinpartidas","Sin partidas registradas");
        TEXTOS_ES.put("stats.ranking","Ranking con amigos:");
        TEXTOS_ES.put("stats.btn.volver","Volver al menu");
        TEXTOS_ES.put("perfil.titulo","Perfil de usuario");
        TEXTOS_ES.put("perfil.username","Username:");
        TEXTOS_ES.put("perfil.nombre","Nombre:");
        TEXTOS_ES.put("perfil.nivel","Nivel desbloqueado:");
        TEXTOS_ES.put("perfil.registro","Registrado:");
        TEXTOS_ES.put("perfil.sesion","Ultima sesion:");
        TEXTOS_ES.put("perfil.avatar","Cambiar avatar:");
        TEXTOS_ES.put("perfil.btn.volver","Volver al menu");
        TEXTOS_ES.put("amigos.titulo","Amigos y Solicitudes");
        TEXTOS_ES.put("amigos.solicitudes","Solicitudes recibidas:");
        TEXTOS_ES.put("amigos.btn.aceptar","Aceptar");
        TEXTOS_ES.put("amigos.btn.rechazar","Rechazar");
        TEXTOS_ES.put("amigos.enviar","Enviar solicitud:");
        TEXTOS_ES.put("amigos.nodisponibles","No hay usuarios disponibles");
        TEXTOS_ES.put("amigos.btn.enviar","Enviar");
        TEXTOS_ES.put("amigos.mislista","Mis amigos:");
        TEXTOS_ES.put("amigos.sinAmigos","Sin amigos todavia");
        TEXTOS_ES.put("amigos.btn.volver","Volver al menu");
        TEXTOS_ES.put("amigos.btn.retar","Retar");
        TEXTOS_ES.put("config.titulo","Configuraciones");
        TEXTOS_ES.put("config.volumen","Volumen:");
        TEXTOS_ES.put("config.musica","Musica:");
        TEXTOS_ES.put("config.musica.on","Activada");
        TEXTOS_ES.put("config.musica.off","Desactivada");
        TEXTOS_ES.put("config.idioma","Idioma:");
        TEXTOS_ES.put("config.idioma.es","Espanol");
        TEXTOS_ES.put("config.idioma.en","English");
        TEXTOS_ES.put("config.btn.guardar","Guardar");
        TEXTOS_ES.put("config.btn.volver","Volver al menu");
        TEXTOS_ES.put("retos.titulo","Retos");
        TEXTOS_ES.put("retos.pendientes","Retos pendientes:");
        TEXTOS_ES.put("retos.btn.aceptar","Aceptar reto");
        TEXTOS_ES.put("retos.btn.rechazar","Rechazar");
        TEXTOS_ES.put("retos.nuevo","Nuevo reto:");
        TEXTOS_ES.put("retos.selNivel","Seleccionar nivel:");
        TEXTOS_ES.put("retos.btn.enviar","Enviar reto");
        TEXTOS_ES.put("retos.nivelBloqueado","Ese nivel no esta desbloqueado para ese amigo. Selecciona otro nivel.");
        TEXTOS_ES.put("retos.sinRetos","Sin retos pendientes");
        TEXTOS_ES.put("retos.btn.volver","Volver al menu");
        TEXTOS_ES.put("retos.ganaste","Ganaste el reto!");
        TEXTOS_ES.put("retos.perdiste","Perdiste el reto.");
        TEXTOS_ES.put("retos.empate","Empate!");
        TEXTOS_EN.put("app.titulo","Flow Free");
        TEXTOS_EN.put("inicio.btn.login","Log In");
        TEXTOS_EN.put("inicio.btn.registro","Create Account");
        TEXTOS_EN.put("inicio.btn.salir","Exit");
        TEXTOS_EN.put("inicio.subtitulo","Connect all dots and complete each level");
        TEXTOS_EN.put("login.titulo","Log In");
        TEXTOS_EN.put("login.btn.entrar","Enter");
        TEXTOS_EN.put("login.btn.registro","No account? Register");
        TEXTOS_EN.put("login.campo.usuario","Username:");
        TEXTOS_EN.put("login.campo.password","Password:");
        TEXTOS_EN.put("login.campo.confirmar","Confirm password:");
        TEXTOS_EN.put("login.campo.nombre","Full name:");
        TEXTOS_EN.put("login.error.campos","Fill in all fields");
        TEXTOS_EN.put("login.error.noexiste","User does not exist");
        TEXTOS_EN.put("login.error.password","Incorrect password");
        TEXTOS_EN.put("login.error.archivo","Error reading user data");
        TEXTOS_EN.put("login.error.nocoinciden","Passwords do not match");
        TEXTOS_EN.put("login.exito.registro","User registered. You can now log in.");
        TEXTOS_EN.put("registro.titulo","Create Account");
        TEXTOS_EN.put("registro.btn.registrarse","Register");
        TEXTOS_EN.put("registro.btn.login","Already have an account? Log in");
        TEXTOS_EN.put("menu.btn.jugar","Play");
        TEXTOS_EN.put("menu.btn.niveles","Levels");
        TEXTOS_EN.put("menu.btn.perfil","Profile");
        TEXTOS_EN.put("menu.btn.estadisticas","Statistics");
        TEXTOS_EN.put("menu.btn.amigos","Friends");
        TEXTOS_EN.put("menu.btn.configuraciones","Settings");
        TEXTOS_EN.put("menu.btn.retos","Challenges");
        TEXTOS_EN.put("menu.btn.cerrarsesion","Log Out");
        TEXTOS_EN.put("niveles.pista","Complete the previous level to unlock the next");
        TEXTOS_EN.put("niveles.btn.volver","Back to menu");
        TEXTOS_EN.put("juego.btn.menu","Menu");
        TEXTOS_EN.put("juego.btn.limpiar","Clear");
        TEXTOS_EN.put("juego.btn.deshacer","Undo");
        TEXTOS_EN.put("juego.btn.siguiente","Next");
        TEXTOS_EN.put("juego.victoria.nivel","completed!");
        TEXTOS_EN.put("juego.victoria.todos","Congrats! You completed all levels!");
        TEXTOS_EN.put("juego.victoria.tiempo","Time:");
        TEXTOS_EN.put("juego.victoria.movimientos","Moves:");
        TEXTOS_EN.put("juego.victoria.fallos","Fails:");
        TEXTOS_EN.put("juego.victoria.puntos","Points:");
        TEXTOS_EN.put("juego.victoria.btn.siguiente","Next level");
        TEXTOS_EN.put("juego.victoria.btn.reintentar","Retry");
        TEXTOS_EN.put("juego.victoria.btn.mapa","See levels");
        TEXTOS_EN.put("stats.titulo","Statistics");
        TEXTOS_EN.put("stats.usuario","User:");
        TEXTOS_EN.put("stats.partidas","Games played:");
        TEXTOS_EN.put("stats.niveles","Levels completed:");
        TEXTOS_EN.put("stats.tiempo.total","Total time:");
        TEXTOS_EN.put("stats.tiempo.promedio","Avg time:");
        TEXTOS_EN.put("stats.movimientos","Total moves:");
        TEXTOS_EN.put("stats.fallos","Total fails:");
        TEXTOS_EN.put("stats.puntuacion","Overall score:");
        TEXTOS_EN.put("stats.historial","Game history:");
        TEXTOS_EN.put("stats.sinpartidas","No games recorded");
        TEXTOS_EN.put("stats.ranking","Friends ranking:");
        TEXTOS_EN.put("stats.btn.volver","Back to menu");
        TEXTOS_EN.put("perfil.titulo","User Profile");
        TEXTOS_EN.put("perfil.username","Username:");
        TEXTOS_EN.put("perfil.nombre","Name:");
        TEXTOS_EN.put("perfil.nivel","Unlocked level:");
        TEXTOS_EN.put("perfil.registro","Registered:");
        TEXTOS_EN.put("perfil.sesion","Last session:");
        TEXTOS_EN.put("perfil.avatar","Change avatar:");
        TEXTOS_EN.put("perfil.btn.volver","Back to menu");
        TEXTOS_EN.put("amigos.titulo","Friends & Requests");
        TEXTOS_EN.put("amigos.solicitudes","Received requests:");
        TEXTOS_EN.put("amigos.btn.aceptar","Accept");
        TEXTOS_EN.put("amigos.btn.rechazar","Reject");
        TEXTOS_EN.put("amigos.enviar","Send request:");
        TEXTOS_EN.put("amigos.nodisponibles","No users available");
        TEXTOS_EN.put("amigos.btn.enviar","Send");
        TEXTOS_EN.put("amigos.mislista","My friends:");
        TEXTOS_EN.put("amigos.sinAmigos","No friends yet");
        TEXTOS_EN.put("amigos.btn.volver","Back to menu");
        TEXTOS_EN.put("amigos.btn.retar","Challenge");
        TEXTOS_EN.put("config.titulo","Settings");
        TEXTOS_EN.put("config.volumen","Volume:");
        TEXTOS_EN.put("config.musica","Music:");
        TEXTOS_EN.put("config.musica.on","Enabled");
        TEXTOS_EN.put("config.musica.off","Disabled");
        TEXTOS_EN.put("config.idioma","Language:");
        TEXTOS_EN.put("config.idioma.es","Spanish");
        TEXTOS_EN.put("config.idioma.en","English");
        TEXTOS_EN.put("config.btn.guardar","Save");
        TEXTOS_EN.put("config.btn.volver","Back to menu");
        TEXTOS_EN.put("retos.titulo","Challenges");
        TEXTOS_EN.put("retos.pendientes","Pending challenges:");
        TEXTOS_EN.put("retos.btn.aceptar","Accept challenge");
        TEXTOS_EN.put("retos.btn.rechazar","Reject");
        TEXTOS_EN.put("retos.nuevo","New challenge:");
        TEXTOS_EN.put("retos.selNivel","Select level:");
        TEXTOS_EN.put("retos.btn.enviar","Send challenge");
        TEXTOS_EN.put("retos.nivelBloqueado","That level is locked for that friend. Select another.");
        TEXTOS_EN.put("retos.sinRetos","No pending challenges");
        TEXTOS_EN.put("retos.btn.volver","Back to menu");
        TEXTOS_EN.put("retos.ganaste","You won the challenge!");
        TEXTOS_EN.put("retos.perdiste","You lost the challenge.");
        TEXTOS_EN.put("retos.empate","It's a tie!");
    }
    private GestorIdiomas(){
        espanol=true;
        textos=TEXTOS_ES;
    }
    public static GestorIdiomas getInstance(){
        if(instancia==null) instancia=new GestorIdiomas();
        return instancia;
    }
    public void setEspanol(boolean espanol){
        this.espanol=espanol;
        textos=espanol ? TEXTOS_ES : TEXTOS_EN;
    }
    public boolean isEspanol(){return espanol;}
    public String get(String clave){
        String valor=textos.get(clave);
        return valor!=null ? valor : clave;
    }
    public void aplicarPerfil(boolean espanol){
        setEspanol(espanol);
    }
}
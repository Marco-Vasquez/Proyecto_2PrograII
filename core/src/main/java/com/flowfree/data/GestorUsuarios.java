/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.data;
import com.flowfree.model.Usuario;
import java.io.IOException;
import com.flowfree.model.SolicitudAmistad;
/**
 *
 * @author andres
 */
public class GestorUsuarios {
    public static final String CARPETA_DATOS="data/usuarios/";
    public enum ResultadoLogin{
        EXITO,USUARIO_NO_EXISTE,PASSWORD_INCORRECTA,ERROR_ARCHIVO;
    }
    private String getRutaArchivo(String username){
        return CARPETA_DATOS+username+"/usuario.dat";
    }
    public boolean userExists(String username){
        return Serializador.existe(getRutaArchivo(username));
    }
    public boolean registrarUser(String username,String passwordPlana,String nombreCompleto){
        if(userExists(username)){
            return false;
        }
        if(!HashUtil.isValidPassword(passwordPlana)){
            return false;
        }
        String salt,hash;
        Usuario nuevoUser;
        salt=HashUtil.generarSalt();
        hash=HashUtil.hashearPassword(passwordPlana,salt);
        nuevoUser=new Usuario(username,hash,salt,nombreCompleto);
        try{
            Serializador.guardar(nuevoUser,getRutaArchivo(username));
            return true;
        }
        catch (IOException error){
            System.err.println("Error al guardar usuario: "+error.getMessage());
            return false;
        }
    }
    public ResultadoLogin iniciarSesion(String username,String passwordPlana,Usuario[] usuarioSalida){
        if(!userExists(username)){
            return ResultadoLogin.USUARIO_NO_EXISTE;
        }
        try{
            Usuario usuario;
            usuario=(Usuario) Serializador.cargar(getRutaArchivo(username));
            if(!usuario.getUsername().equals(username)){
                return ResultadoLogin.USUARIO_NO_EXISTE;
            }
            if(!HashUtil.verificarPassword(passwordPlana,usuario.getSalt(),usuario.getPasswordHash())){
                return ResultadoLogin.PASSWORD_INCORRECTA;
            }   
            usuario.actualizarUltimaSesion();
            guardarUser(usuario);
            if(usuarioSalida!=null && usuarioSalida.length>=0){
                usuarioSalida[0]=usuario;
            }
            return ResultadoLogin.EXITO;
        }
        catch(IOException | ClassNotFoundException error){
            System.err.println("Error al cargar usuario: "+error.getMessage());
            return ResultadoLogin.ERROR_ARCHIVO;
        }
    }
    public void guardarUser(Usuario usuario){
        try{
            Serializador.guardar(usuario,getRutaArchivo(usuario.getUsername()));
        }
        catch(IOException error){
            System.err.println("Error al guardar usuario: "+error.getMessage());
        }
    }
    public Usuario cargarUser(String username){
        if(!userExists(username)){
            return null;
        }
        try{
            return (Usuario) Serializador.cargar(getRutaArchivo(username));
        }
        catch(IOException | ClassNotFoundException error){
            System.err.println("Error al cargar usuario: "+error.getMessage());
            return null;
        }
    }
    public Usuario registrarUsuarioSeguro(String username,String passwordPlana,String nombreCompleto)throws com.flowfree.exceptions.UserExistenteException,com.flowfree.exceptions.InvalidPasswordException{
        if(userExists(username)){
            throw new com.flowfree.exceptions.UserExistenteException(username);
        }
        if(!HashUtil.isValidPassword(passwordPlana)){
            throw new com.flowfree.exceptions.InvalidPasswordException("Debe tener mí­nimo 8 caracteres, letras, números y mínimo un caracter especial");
        }
        String salt,hash;
        Usuario nuevoUser;
        salt=HashUtil.generarSalt();
        hash=HashUtil.hashearPassword(passwordPlana, salt);
        nuevoUser=new Usuario(username,hash,salt,nombreCompleto);
        try{
            Serializador.guardar(nuevoUser, getRutaArchivo(username));
            return nuevoUser;
        }
        catch(java.io.IOException error){
            throw new RuntimeException("No se pudo guardar el usuario",error);
        }
    }
    
    public java.util.List<String> listarUsuariosRegistrados(){
        java.util.List<String> lista=new java.util.ArrayList<>();
        java.io.File carpeta=new java.io.File(CARPETA_DATOS);
        
        if(!carpeta.exists()||!carpeta.isDirectory()){
            return lista;
        }
        for(java.io.File subcarpeta:carpeta.listFiles()){
            if(subcarpeta.isDirectory()){
                lista.add(subcarpeta.getName());
            }
        }
        return lista;
    }

    public int getPuntuacionDeUsuario(String username){
        Usuario usuario=cargarUser(username);
        if(usuario==null){
            return 0;
        }
        return usuario.getEstadisticas().getPuntuacionGeneral();
    }
    public boolean enviarSolicitud(String usernameEmisor,String usernameReceptor){
        Usuario emisor=cargarUser(usernameEmisor);
        Usuario receptor=cargarUser(usernameReceptor);
        if(emisor==null||receptor==null) return false;
        if(emisor.getAmigos().contains(usernameReceptor)) return false;
        if(emisor.yaEnvioSolicitudA(usernameReceptor)) return false;
        if(receptor.tieneSolicitudPendienteDe(usernameEmisor)) return false;
        SolicitudAmistad solicitud=new SolicitudAmistad(usernameEmisor,usernameReceptor);
        emisor.getSolicitudesEnviadas().add(solicitud);
        receptor.getSolicitudesRecibidas().add(new SolicitudAmistad(usernameEmisor,usernameReceptor));
        guardarUser(emisor);
        guardarUser(receptor);
        return true;
    }
    public boolean aceptarSolicitud(Usuario receptor,String usernameEmisor){
        for(SolicitudAmistad s:receptor.getSolicitudesRecibidas()){
            if(s.getEmisor().equals(usernameEmisor)&&s.isPendiente()){
                s.aceptar();
                receptor.agregarAmigo(usernameEmisor);
                guardarUser(receptor);
                Usuario emisor=cargarUser(usernameEmisor);
                if(emisor!=null){
                    emisor.agregarAmigo(receptor.getUsername());
                    for(SolicitudAmistad se:emisor.getSolicitudesEnviadas()){
                        if(se.getReceptor().equals(receptor.getUsername())) se.aceptar();
                    }
                    guardarUser(emisor);
                }
                return true;
            }
        }
        return false;
    }
    public boolean rechazarSolicitud(Usuario receptor,String usernameEmisor){
        receptor.getSolicitudesRecibidas().removeIf(
            s->s.getEmisor().equals(usernameEmisor)&&s.isPendiente()
        );
        guardarUser(receptor);
        return true;
    }
}
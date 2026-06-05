/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.data;
import com.flowfree.model.Usuario;
import java.io.IOException;
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
}

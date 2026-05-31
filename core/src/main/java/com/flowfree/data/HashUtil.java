/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.data;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
/**
 *
 * @author andres
 */
public final class HashUtil {
    private static final String ALGORITMO="PBKDF2WithHmacSHA256";
    private static final int ITERACIONES=65536;
    private static final int LONGITUD_CONTRA=256;
    private static final int LONGITUD_SALT=16;
    private HashUtil(){
        
    }
    public static String generarSalt(){
        SecureRandom random;
        random=new SecureRandom();
        byte[] salt=new byte[LONGITUD_SALT];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    public static String hashearPassword(String password,String salt){
        try{
            byte[] saltBytes=Base64.getDecoder().decode(salt);
            PBEKeySpec spec;
            spec=new PBEKeySpec(password.toCharArray(),saltBytes,ITERACIONES,LONGITUD_CONTRA);
            SecretKeyFactory fabrica;
            fabrica=SecretKeyFactory.getInstance(ALGORITMO);
            byte[] hash;
            hash=fabrica.generateSecret(spec).getEncoded();
            spec.clearPassword();
            return Base64.getEncoder().encodeToString(hash);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException error){
            throw new RuntimeException("Error al hashear la contraseña",error);
        }
    }
    public static boolean verificarPassword(String passwordIngresada,String salt,String hashGuardado){
        String hashGenerado;
        hashGenerado=hashearPassword(passwordIngresada,salt);
        return hashGenerado.equals(hashGuardado);
    }
    public static boolean tieneMinOchoCarac(String password){
        return password!=null && password.length()>=8;
    }
    public static boolean tieneLetras(String password){
        if(password==null){
            return false;
        }
        for(char caracter:password.toCharArray()){
            if(Character.isLetter(caracter)){
                return true;
            }
        }
        return false;
    }
    public static boolean tieneNumeros(String password){
        if(password==null){
            return false;
        }
        for(char caracter:password.toCharArray()){
            if(Character.isDigit(caracter)){
                return true;
            }
        }
        return false;
    }
    public static boolean tieneSimbolos(String password){
        if(password==null){
            return false;
        }
        String simbolos="!@#$%^&*()_+-=[]{}|;':\",./<>?";
        for(char caracter:password.toCharArray()){
            if(simbolos.indexOf(caracter)>=0){
                return true;
            }
        }
        return false;
    }
    public static boolean isValidPassword(String password){
        return tieneMinOchoCarac(password) && tieneLetras(password) && tieneNumeros(password) && tieneSimbolos(password);
    }
}

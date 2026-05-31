/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package com.flowfree.exceptions;

/**
 *
 * @author andres
 */
public class UserExistenteException extends Exception{

    /**
     * Creates a new instance of <code>UserExistenteException</code> without
     * detail message.
     */
    public UserExistenteException() {
    }

    /**
     * Constructs an instance of <code>UserExistenteException</code> with the
     * specified detail message.
     *
     * @param username the detail message.
     */
    public UserExistenteException(String username) {
        super("El usuario '"+username+"' ya existe");
    }
}

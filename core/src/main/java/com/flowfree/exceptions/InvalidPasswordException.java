/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package com.flowfree.exceptions;

/**
 *
 * @author andres
 */
public class InvalidPasswordException extends Exception {

    /**
     * Creates a new instance of <code>InvalidPasswordException</code> without
     * detail message.
     */
    public InvalidPasswordException() {
    }

    /**
     * Constructs an instance of <code>InvalidPasswordException</code> with the
     * specified detail message.
     *
     * @param motivo the detail message.
     */
    public InvalidPasswordException(String motivo) {
        super("Contraseña inválida: "+motivo);
    }
}

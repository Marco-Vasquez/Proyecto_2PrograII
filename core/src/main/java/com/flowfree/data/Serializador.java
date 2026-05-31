/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.data;
import java.io.*;
/**
 *
 * @author andres
 */
public class Serializador {
    public static void guardar(Object objeto,String ruta) throws IOException{
        File archivo,carpetaPadre;
        archivo=new File(ruta);
        carpetaPadre=archivo.getParentFile();
        if(carpetaPadre!=null && !carpetaPadre.exists()){
            carpetaPadre.mkdirs();
        }
        try(ObjectOutputStream salida=new ObjectOutputStream(new FileOutputStream(archivo))){
            salida.writeObject(objeto);
        }
    }
    public static Object cargar(String ruta) throws IOException,ClassNotFoundException{
        try(ObjectInputStream entrada=new ObjectInputStream(new FileInputStream(ruta))){
            return entrada.readObject();
        }
    }
    public static boolean existe(String ruta){
        return new File(ruta).exists();
    }
}

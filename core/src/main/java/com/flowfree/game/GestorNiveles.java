/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.game;

import com.flowfree.model.Nivel;

/**
 *
 * @author mjosu
 */
public class GestorNiveles {

    private Nivel[] niveles;

    public GestorNiveles() {
        niveles = new Nivel[5];
        inicializarNiveles();
    }

    private void inicializarNiveles() {

        niveles[0]=new Nivel(1,5,5,new int[][]{
            {0,0, 0,4, 1},
            {2,1, 4,4, 2},
            {2,2, 3,4, 3},
            {2,4, 4,1, 4}
        },"Facil");

        niveles[1]=new Nivel(2,6,6,new int[][]{
            {0,0, 1,2, 4},
            {0,2, 4,3, 1},
            {0,5, 5,5, 5},
            {1,0, 1,3, 6},
            {3,1, 4,4, 3},
            {3,2, 4,1, 2}
        },"Facil");

        niveles[2]=new Nivel(3,7,7,new int[][]{
            {0,6, 6,5, 2},
            {1,5, 2,1, 6},
            {1,6, 5,4, 1},
            {3,3, 4,2, 4},
            {3,4, 6,6, 5},
            {4,4, 5,5, 3}
        },"Medio");

        niveles[3]=new Nivel(4,8,8,new int[][]{
            {0,0, 1,1, 4},
            {0,1, 0,6, 1},
            {0,3, 3,3, 5},
            {0,7, 2,0, 2},
            {1,6, 5,2, 3},
            {2,1, 2,6, 6}
        },"MedioDificil");

         niveles[4]=new Nivel(5,9,9,new int[][]{
            {0,0, 3,7, 5},
            {1,0, 1,4, 6},
            {1,7, 8,1, 7},
            {2,7, 6,2, 3},
            {4,2, 4,4, 8},
            {4,1, 4,5, 2},
            {4,7, 5,5, 1},
            {5,7, 7,3, 4}
        },"Dificil");
    }

    public Nivel getNivel(int numNivel){
        if(numNivel<1||numNivel>niveles.length) return null;
        return niveles[numNivel-1];
    }

    public int getTotalNiveles() {
        return niveles.length;
    }

    public void desbloquearSiguiente(int nivelCompletado){
        int indiceSiguiente=nivelCompletado;
        if(indiceSiguiente<niveles.length) niveles[indiceSiguiente].desbloquear();
    }

    public void aplicarProgresoUsuario(int nivelDesbloqueado){
        for(int posicion=0;posicion<niveles.length;posicion++){
            if((posicion+1)<=nivelDesbloqueado) niveles[posicion].desbloquear();
        }
    }

    public Nivel[] getTodosLosNiveles() {
        return niveles;
    }
}
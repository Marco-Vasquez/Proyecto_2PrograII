/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.game;

import com.flowfree.base.Juego;
import com.flowfree.model.EstadoCelda;
import com.flowfree.model.Nivel;
import com.flowfree.model.Tablero;
/**
 *
 * @author andres
 */
public class FlowFree extends Juego {
    private Nivel nivelActivo;
    private Tablero tablero;
    private GestorMovimientos gestorMovimientos;
    private int movimientos;
    private int fallos;
    private boolean nivelCompleto;
    private boolean ultimoTrazoCerrado=false;
    public FlowFree(){
        super("Flow Free");
    }
    public boolean cargarNivel(Nivel nivel){
        if(nivel==null) return false;
        this.nivelActivo=nivel;
        this.nivelAct=nivel.getNumNivel();
        this.tablero=nivel.crearTablero();
        this.gestorMovimientos=new GestorMovimientos(tablero);
        iniciar();
        return true;
    }
    public void iniciar(){
        movimientos=0;
        fallos=0;
        nivelCompleto=false;
        enPausa=false;
    }
    public void pausar(){
        enPausa=!enPausa;
    }
    public void reiniciar(){
        if(nivelActivo!=null){
            tablero=nivelActivo.crearTablero();
            gestorMovimientos=new GestorMovimientos(tablero);
        }
        iniciar();
        gestorMovimientos.limpiarHistorial();
    }
    public void terminar(){
        enPausa=true;
    }
    public boolean verificarVictoria(){
        return nivelCompleto;
    }
    public boolean iniciarTrazo(int fila,int columna){
        if(enPausa||nivelCompleto) return false;
        return gestorMovimientos.iniciarTrazo(fila,columna);
    }
    public boolean continuarTrazo(int fila,int columna){
        if(enPausa||nivelCompleto) return false;
        boolean exito=gestorMovimientos.continuarTrazo(fila,columna);
        if(exito&&gestorMovimientos.getUltimoResultado()!=GestorMovimientos.ResultadoTrazo.RETROCEDIO){
            movimientos++;
        }
        if(gestorMovimientos.getUltimoResultado()==GestorMovimientos.ResultadoTrazo.BLOQUEADO){
            fallos++;
        }
        if(gestorMovimientos.isTrazoCerrado()){
            ultimoTrazoCerrado=true;
            gestorMovimientos.terminarTrazo();
            verificarEstadoNivel();
        }
        return exito;
    }
    public void pausarTrazo(){
        gestorMovimientos.pausarTrazo();
    }
    public void terminarTrazo(){
        gestorMovimientos.terminarTrazo();
        verificarEstadoNivel();
    }
    public boolean deshacerPaso(){
        if(enPausa||nivelCompleto) return false;
        if(gestorMovimientos.isTrazando()&&gestorMovimientos.getColorActivo()!=0){
            boolean resultado=gestorMovimientos.deshacerUltimoPaso();
            if(resultado) verificarEstadoNivel();
            return resultado;
        }
        if(gestorMovimientos.hayTrazoPausado()){
            boolean resultado=gestorMovimientos.deshacerTrazoPausado();
            if(resultado) verificarEstadoNivel();
            return resultado;
        }
        boolean resultado=gestorMovimientos.deshacerUltimoTrazoCompleto();
        if(resultado) verificarEstadoNivel();
        return resultado;
    }
    private void verificarEstadoNivel(){
        int completos=contarCaminosCompletos();
        tablero.setCaminosCompletos(completos);
        if(tablero.nivelResuelto()){
            nivelCompleto=true;
        }
    }
    private int contarCaminosCompletos(){
        int completos=0;
        for(int color=1;color<=tablero.getTotalColores();color++){
            if(colorTieneDosOrigenes(color)&&coloresConectados(color)){
                completos++;
            }
        }
        return completos;
    }
    private boolean colorTieneDosOrigenes(int color){
        int conteo=0;
        for(int fila=0;fila<tablero.getFilas();fila++){
            for(int columna=0;columna<tablero.getColumnas();columna++){
                if(tablero.getColor(fila,columna)==color&&tablero.isPuntoOrigen(fila,columna)){
                    conteo++;
                }
            }
        }
        return conteo==2;
    }
    private boolean coloresConectados(int color){
        int[][] origenes=new int[2][2];
        int encontrados=0;
        for(int fila=0;fila<tablero.getFilas();fila++){
            for(int columna=0;columna<tablero.getColumnas();columna++){
                if(tablero.getColor(fila,columna)==color&&tablero.isPuntoOrigen(fila,columna)){
                    if(encontrados<2){
                        origenes[encontrados][0]=fila;
                        origenes[encontrados][1]=columna;
                        encontrados++;
                    }
                }
            }
        }
        if(encontrados!=2) return false;
        boolean[][] visitado=new boolean[tablero.getFilas()][tablero.getColumnas()];
        return dfsConexion(origenes[0][0],origenes[0][1],origenes[1][0],origenes[1][1],color,visitado);
    }
    private boolean dfsConexion(int fila,int col,int objFila,int objCol,int color,boolean[][] visitado){
        if(fila==objFila&&col==objCol) return true;
        if(!tablero.dentroDelTablero(fila,col)) return false;
        if(visitado[fila][col]) return false;
        if(tablero.getColor(fila,col)!=color) return false;
        visitado[fila][col]=true;
        if(tablero.tieneConexion(fila,col,Tablero.CON_ARRIBA)){
            if(dfsConexion(fila-1,col,objFila,objCol,color,visitado)) return true;
        }
        if(tablero.tieneConexion(fila,col,Tablero.CON_ABAJO)){
            if(dfsConexion(fila+1,col,objFila,objCol,color,visitado)) return true;
        }
        if(tablero.tieneConexion(fila,col,Tablero.CON_IZQ)){
            if(dfsConexion(fila,col-1,objFila,objCol,color,visitado)) return true;
        }
        if(tablero.tieneConexion(fila,col,Tablero.CON_DER)){
            if(dfsConexion(fila,col+1,objFila,objCol,color,visitado)) return true;
        }
        return false;
    }
    public void registrarFallo(){
        fallos++;
    }
    public Tablero getTablero(){
        return tablero;
    }
    public Nivel getNivelActivo(){
        return nivelActivo;
    }
    public int getMovimientos(){
        return movimientos;
    }
    public int getFallos(){
        return fallos;
    }
    public boolean isNivelCompleto(){
        return nivelCompleto;
    }
    public GestorMovimientos getGestorMovimientos(){
        return gestorMovimientos;
    }
    public boolean isUltimoTrazoCerrado(){
        return ultimoTrazoCerrado;
    }
    public void resetUltimoTrazoCerrado(){
        ultimoTrazoCerrado=false;
    }
}
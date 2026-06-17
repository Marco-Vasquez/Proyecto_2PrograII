/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.screens;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.flowfree.FlowFreeGame;
import com.flowfree.data.GestorIdiomas;
import com.flowfree.data.GestorRetos;
import com.flowfree.data.GestorUsuarios;
import com.flowfree.model.RetoCompetitivo;
import com.flowfree.model.Usuario;
/**
 *
 * @author andres
 */
public class PantallaJuegoReto extends PantallaJuego{
    private final RetoCompetitivo reto;
    private final GestorRetos gestorRetos;
    private final boolean esRetador;
    private boolean retoRegistrado=false;
    public PantallaJuegoReto(FlowFreeGame juego,Usuario usuarioAct,
            RetoCompetitivo reto,GestorRetos gestorRetos){
        super(juego,usuarioAct,reto.getNumNivel());
        this.reto=reto;
        this.gestorRetos=gestorRetos;
        this.esRetador=reto.getRetador().equals(usuarioAct.getUsername());
        this.modoReto=true;
    }
    protected void alCompletarNivel(long tiempo,int movimientos,int fallos){
        if(retoRegistrado) return;
        retoRegistrado=true;
        if(esRetador){
            gestorRetos.registrarResultadoRetador(
                getUsuarioAct(),
                indiceRetador(),
                tiempo,movimientos,fallos);
        }else{
            gestorRetos.aceptarYCompletarReto(getUsuarioAct(),reto,tiempo,movimientos,fallos);
            mostrarResultadoReto(reto);
        }
    }
    private int indiceRetador(){
        java.util.List<RetoCompetitivo> lista=getUsuarioAct().getRetos();
        for(int posicion=0;posicion<lista.size();posicion++){
            RetoCompetitivo r=lista.get(posicion);
            if(r.getRetado().equals(reto.getRetado())&&r.getNumNivel()==reto.getNumNivel()){
                return posicion;
            }
        }
        return lista.size()-1;
    }
    private void mostrarResultadoReto(RetoCompetitivo retoFinal){
        String ganador=retoFinal.getGanador();
        String username=getUsuarioAct().getUsername();
        GestorIdiomas idiomas=GestorIdiomas.getInstance();
        String mensaje;
        if("empate".equals(ganador)) mensaje=idiomas.get("retos.empate");
        else if(username.equals(ganador)) mensaje=idiomas.get("retos.ganaste");
        else mensaje=idiomas.get("retos.perdiste");
        getLabelMsj().setText(mensaje);
    }
}

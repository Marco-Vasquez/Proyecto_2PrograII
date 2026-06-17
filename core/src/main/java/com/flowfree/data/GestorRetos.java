/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.data;
import com.flowfree.model.RetoCompetitivo;
import com.flowfree.model.Usuario;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author andres
 */
public class GestorRetos {
    private final GestorUsuarios gestorUsuarios;
    public GestorRetos(){
        this.gestorUsuarios=new GestorUsuarios();
    }
    public boolean enviarReto(String usernameRetador,String usernameRetado,int numNivel){
        Usuario retador=gestorUsuarios.cargarUser(usernameRetador);
        Usuario retado=gestorUsuarios.cargarUser(usernameRetado);
        if(retador==null||retado==null) return false;
        if(retado.getNivelDesbloqueado()<numNivel) return false;
        if(retador.getNivelDesbloqueado()<numNivel) return false;
        RetoCompetitivo reto=new RetoCompetitivo(usernameRetador,usernameRetado,numNivel);
        retador.getRetos().add(reto);
        retado.getRetosRecibidos().add(reto);
        gestorUsuarios.guardarUser(retador);
        gestorUsuarios.guardarUser(retado);
        return true;
    }
    public void registrarResultadoRetador(Usuario retador,int indiceReto,long tiempo,int mov,int fallos){
        List<RetoCompetitivo> retos=retador.getRetos();
        if(indiceReto<0||indiceReto>=retos.size()) return;
        retos.get(indiceReto).registrarResultadoRetador(tiempo,mov,fallos);
        gestorUsuarios.guardarUser(retador);
    }
    public void aceptarYCompletarReto(Usuario retado,RetoCompetitivo reto,long tiempo,int mov,int fallos){
        reto.registrarResultadoRetado(tiempo,mov,fallos);
        gestorUsuarios.guardarUser(retado);
        Usuario retador=gestorUsuarios.cargarUser(reto.getRetador());
        if(retador!=null){
            for(RetoCompetitivo r:retador.getRetos()){
                if(r.getRetado().equals(retado.getUsername())
                        &&r.getNumNivel()==reto.getNumNivel()
                        &&r.getEstado()==RetoCompetitivo.EstadoReto.ACEPTADO){
                    r.registrarResultadoRetado(tiempo,mov,fallos);
                    break;
                }
            }
            gestorUsuarios.guardarUser(retador);
        }
    }
    public void rechazarReto(Usuario retado,RetoCompetitivo reto){
        reto.rechazar();
        gestorUsuarios.guardarUser(retado);
    }
    public List<RetoCompetitivo> getRetosPendientes(Usuario usuario){
        List<RetoCompetitivo> pendientes=new ArrayList<>();
        for(RetoCompetitivo r:usuario.getRetosRecibidos()){
            if(r.getEstado()==RetoCompetitivo.EstadoReto.PENDIENTE) pendientes.add(r);
        }
        return pendientes;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.flowfree.screens;
import com.badlogic.gdx.graphics.Color;
/**
 *
 * @author andres
 */
public final class EstiloUI {
    public static final Color FONDO=new Color(0.05f,0.05f,0.05f,1f);
    public static final Color PANEL=new Color(0.38f,0.18f,0.65f,1f);
    public static final Color ENCABEZADO=new Color(0.82f,0.12f,0.12f,1f);
    public static final Color BTN_AZUL=new Color(0.22f,0.52f,0.90f,1f);
    public static final Color BTN_VERDE=new Color(0.20f,0.75f,0.25f,1f);
    public static final Color BTN_AMARILLO=new Color(0.95f,0.85f,0.15f,1f);
    public static final Color BTN_CYAN=new Color(0.10f,0.85f,0.90f,1f);
    public static final Color BTN_NARANJA=new Color(0.95f,0.55f,0.10f,1f);
    public static final Color BTN_ROJO=new Color(0.85f,0.15f,0.15f,1f);
    public static final Color BTN_MORADOCLARO=new Color(0.65f,0.40f,0.85f,1f);
    public static final Color TEXTO_BLANCO=Color.WHITE;
    public static final Color[] CIRCULOS_IZQ={
      BTN_AZUL,BTN_VERDE,BTN_AMARILLO,BTN_MORADOCLARO,BTN_NARANJA,BTN_ROJO,BTN_MORADOCLARO  
    };
    public static final Color[] CIRCULOS_DER={
      BTN_CYAN,BTN_VERDE,BTN_AMARILLO,BTN_MORADOCLARO,BTN_NARANJA,BTN_ROJO,BTN_MORADOCLARO  
    };
    public static final Color[] COLORES_GAME={
      Color.WHITE,
      new Color(0.95f,0.15f,0.15f,1f),  
      new Color(0.20f,0.50f,0.95f,1f),  
      new Color(0.20f,0.82f,0.25f,1f),  
      new Color(0.95f,0.88f,0.10f,1f),  
      new Color(0.95f,0.52f,0.08f,1f),
      new Color(0.10f,0.88f,0.90f,1f),  
      new Color(0.90f,0.25f,0.88f,1f),  
      new Color(0.58f,0.18f,0.80f,1f)
    };
    public static final float PANEL_ANCHO_FRAC=0.70f;
    public static final float PANEL_ALTO_FRAC=0.78f;
    public static final float RADIO_CIRCULO_DEC=28f;
    public static final float RADIO_ENCABEZADO=26f;
    private EstiloUI(){
    }
}

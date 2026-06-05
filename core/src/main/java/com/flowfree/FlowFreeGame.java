package com.flowfree;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flowfree.screens.PantallaInicio;

public class FlowFreeGame extends Game {

    public SpriteBatch batch;

    @Override
    public void create(){
    batch=new SpriteBatch();
    setScreen(new PantallaInicio(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

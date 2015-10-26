package com.projetoes.roundfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by Gervasio on 6/6/2015.
 */
public class GameStart extends ScreenAdapter {

    private final MyGdxGame game;
    private final GameStage stage;
    protected boolean vibrate;

    public GameStart(MyGdxGame game, boolean vibrate, int estagio) {
        this.game = game;
        this.vibrate = vibrate;
        this.stage = new GameStage(game, vibrate, estagio);
    }

    @Override
    public void pause() {
        super.pause();
        if (!stage.gamePaused) stage.pausar();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        stage.act(delta);
    }
}


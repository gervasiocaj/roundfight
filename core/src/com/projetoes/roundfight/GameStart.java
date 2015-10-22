package com.projetoes.roundfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.projetoes.roundfight.GameStage;

/**
 * Created by Gervasio on 6/6/2015.
 */
public class GameStart extends ScreenAdapter {

    private final MyGdxGame game;
    private final GameStage stage;
    protected boolean vibrate;
    protected  boolean sound;

    public GameStart(MyGdxGame game, boolean vibrate, Vector2 initialposition) {
        this.game = game;
        this.vibrate = vibrate;
        this.stage = new GameStage(game, vibrate, initialposition);
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


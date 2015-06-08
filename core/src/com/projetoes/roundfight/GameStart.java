package com.projetoes.roundfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Gervasio on 6/6/2015.
 */
public class GameStart extends ScreenAdapter {

    private final MyGdxGame game;
    private final GameStage stage;

    public GameStart(MyGdxGame game) {
        this.game = game;
        this.stage = new GameStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        stage.act(delta);
    }
}

class GameStage extends Stage {

    private final Body ball;
    private World world;
    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;

    public GameStage() {
        world = new World(new Vector2(0, 0), true);
        renderer = new Box2DDebugRenderer();
        //camera = new OrthographicCamera(Gdx.graphics.getWidth()/1100f, Gdx.graphics.getHeight()/1100f);
        camera = new OrthographicCamera(1, Float.valueOf(Gdx.graphics.getHeight()) / Float.valueOf(Gdx.graphics.getWidth()));

        ball = Assets.createBall(world);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        world.step(1 / 45f, 6, 2);
        // ball.applyForceToCenter(Gdx.input.getAccelerometerX(),Gdx.input.getAccelerometerY(), true);
        // aplica a forca, porem ainda em teste
    }

    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        renderer.dispose();
    }
}

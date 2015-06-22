package com.projetoes.roundfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.TextureData;
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

/**
 * Created by Gervasio on 6/6/2015.
 */
public class GameStart extends ScreenAdapter {

    private final MyGdxGame game;
    private final GameStage stage;

    public GameStart(MyGdxGame game) {
        this.game = game;
        this.stage = new GameStage(game);
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

    private Table table;
    private final Stage stage;
    private Body ball, ballpc;
    private TextureData arena;
    private MyGdxGame game;
    private World world;
    private TextButton buttonPause;
    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;
    private boolean gamePaused = false;
    private Vector2 positionball, forceballpc, velocidadepc;

    public GameStage(final MyGdxGame game) {
        this.game = game;

        Gdx.input.setInputProcessor(null); // para sobrescrever os ClickListeners da classe MainMenuScreen
        world = new World(new Vector2(0, 0), true);
        renderer = new Box2DDebugRenderer();
        //camera = new OrthographicCamera(Gdx.graphics.getWidth()/1100f, Gdx.graphics.getHeight()/1100f);

        // o mundo é infinito para todos os lados, e a câmera está a uma altura x do ponto inicial
        // não lembro exatamente como cheguei a esta fórmula, mas deve funcionar para todas as resoluções de tela
        camera = new OrthographicCamera(1.3f, 1.3f * Float.valueOf(Gdx.graphics.getHeight()) / Float.valueOf(Gdx.graphics.getWidth()));
        ball = Assets.createBall(world,0,0); // cria uma nova bola neste mundo
        ballpc = Assets.createBall(world,0.1f,0.1f);
        arena = Assets.background.getTextureData();

        TextButton.TextButtonStyle pauseButtonStyle = new TextButton.TextButtonStyle();
        pauseButtonStyle.font = Assets.font_small;

        buttonPause = new TextButton("||", pauseButtonStyle);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gamePaused = !gamePaused;
                buttonPause.setText(gamePaused ? ">" : "||");
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);
        table.align(Align.topRight).row();
        table.add(buttonPause);
        stage = new Stage();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
     }

    @Override
    public void act(float delta) {
        if (gamePaused) return;
        super.act(delta);
        world.step(1 / 45f, 6, 2); // com que frequência a tela é atualizada, 45 frames por segundo. Esses valores 6 e 2 são "padroes" para android.
        ball.applyForceToCenter(Gdx.input.getAccelerometerY() / 1200f, -Gdx.input.getAccelerometerX() / 1200f, true); // aplica a força à bola
        positionball = ball.getPosition();
        forceballpc = ballpc.getPosition();
        forceballpc.set(positionball.x - forceballpc.x, positionball.y - forceballpc.y);
        velocidadepc = ballpc.getLinearVelocity();
        velocidadepc.rotate(2 * velocidadepc.angle(forceballpc));
        forceballpc.add(velocidadepc);
        ballpc.applyForceToCenter(forceballpc.x/150f, forceballpc.y/150f, true);

    }

    @Override
    public void draw() {
        super.draw();

        float lowerX = Gdx.graphics.getWidth()/2f - Assets.background.getWidth()/2f;
        float lowerY = Gdx.graphics.getHeight()/2f - Assets.background.getHeight()/2f;

        game.batch.begin();
        game.batch.draw(Assets.background, lowerX, lowerY);
        game.batch.end();

        //System.out.println(camera.position.toString());
        //System.out.println(camera.viewportWidth + " " + camera.viewportHeight);
        //System.out.println("bola " + ball.getPosition().toString());

        if ((ball.getPosition().x > lowerX/camera.viewportWidth )) //&& ball.getPosition().x < lowerX + Assets.background.getWidth() && ball.getPosition().y > lowerY && ball.getPosition().y < lowerY + Assets.background.getHeight()))
            game.setScreen(new MainMenuScreen(game)); // TODO game over

        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
            game.setScreen(new MainMenuScreen(game)); // TODO mostrar tela de confirmação

        stage.draw();
        renderer.render(world, camera.combined);
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        renderer.dispose();
    }
}

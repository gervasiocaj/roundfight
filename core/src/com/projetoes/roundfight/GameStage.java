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
/**
 * Created by pc1 on 14/10/2015.
 */
public class GameStage extends Stage {

    private Table table;
    private final Stage stage;
    private Body ball, ballpc;
    private TextureData arena;
    private MyGdxGame game;
    private World world;
    private TextButton buttonPause, buttonDash;
    private ShapeRenderer renderer;
    private OrthographicCamera camera;
    boolean gamePaused = false;
    private Vector2 positionball, forceballpc, velocidadepc;

    public GameStage(final MyGdxGame game) {
        this.game = game;

        Gdx.input.setInputProcessor(null); // para sobrescrever os ClickListeners da classe MainMenuScreen
        world = new World(new Vector2(0, 0), true);
        renderer = new ShapeRenderer();
        //renderer = new Box2DDebugRenderer();
        //camera = new OrthographicCamera(Gdx.graphics.getWidth()/1100f, Gdx.graphics.getHeight()/1100f);

        // o mundo é infinito para todos os lados, e a câmera está a uma altura x do ponto inicial
        // não lembro exatamente como cheguei a esta fórmula, mas deve funcionar para todas as resoluções de tela
        camera = new OrthographicCamera(1.3f, 1.3f * Float.valueOf(Gdx.graphics.getHeight()) / Float.valueOf(Gdx.graphics.getWidth()));
        ball = Assets.createBall(world,0,0); // cria uma nova bola neste mundo
        ballpc = Assets.createBall(world, 0.1f, 0.1f);
        arena = Assets.background.getTextureData();

        TextButton.TextButtonStyle pauseButtonStyle = new TextButton.TextButtonStyle();
        pauseButtonStyle.font = Assets.font_small;

        buttonPause = new TextButton("||", pauseButtonStyle);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pausar();
            }
        });

        buttonDash = new TextButton(">>>", pauseButtonStyle);
        buttonDash.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dash(ball);
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);
        table.align(Align.topRight).row();
        table.add(buttonPause);
        for (int i = 0; i < 5; i++){
            table.align(Align.topRight).add().row();
        }
        table.add(buttonDash);
        stage = new Stage();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    void pausar() {
        gamePaused = !gamePaused;
        buttonPause.setText(gamePaused ? ">" : "||");
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
        velocidadepc.rotate(2f * velocidadepc.angle(forceballpc));
        forceballpc.add(velocidadepc);
        ballpc.applyForceToCenter(forceballpc.x/150f, forceballpc.y/150f, true);

    }

    @Override
    public void draw() {
        super.draw();

        camera.update();
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.GRAY);
        renderer.rect(-0.5f, -0.3f, 1f, 0.6f);
        renderer.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);
        renderer.circle(ball.getPosition().x, ball.getPosition().y, 0.025f, 100);
        renderer.setColor(Color.BLUE);
        renderer.circle(ballpc.getPosition().x, ballpc.getPosition().y, 0.025f, 100);
        renderer.end();

        stage.draw();

        float lowerX = -0.5f, lowerY = -0.3f;
        if ((ball.getPosition().x > -lowerX  || ball.getPosition().x < lowerX || ball.getPosition().y > -lowerY || ball.getPosition().y < lowerY))
            game.setScreen(new MainMenuScreen(game)); // TODO game over

        if ((ballpc.getPosition().x > -lowerX  || ballpc.getPosition().x < lowerX || ballpc.getPosition().y > -lowerY || ballpc.getPosition().y < lowerY))
            game.setScreen(new MainMenuScreen(game)); // TODO game over


        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
            game.setScreen(new MainMenuScreen(game)); // TODO mostrar tela de confirmação
    }

    public void dash(Body obj){
        Dash d = new Dash(ball);
        d.start();
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        renderer.dispose();
    }
}

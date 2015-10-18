package com.projetoes.roundfight;

/**
 * Created by asus on 17/10/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class GameStage extends Stage {

    private Table table;
    private Stage stage;
    private Body ball, ballpc;
    private TextureData arena;
    private MyGdxGame game;
    private World world;
    private TextButton buttonPause, buttonDash, buttonBack, buttonNewGame;
    private ShapeRenderer renderer;
    private OrthographicCamera camera;
    boolean gamePaused = false;
    boolean vibrate = false;
    private Vector2 positionball, forceballpc, velocidadepc;
    private TextButton.TextButtonStyle textButtonStyle;

    public GameStage(final MyGdxGame game, final boolean vibrate) {
        this.game = game;
        this.vibrate = vibrate;

        Gdx.input.setInputProcessor(null); // para sobrescrever os ClickListeners da classe MainMenuScreen
        world = new World(new Vector2(0, 0), true);
        renderer = new ShapeRenderer();
        //renderer = new Box2DDebugRenderer();
        //camera = new OrthographicCamera(Gdx.graphics.getWidth()/1100f, Gdx.graphics.getHeight()/1100f);

        // o mundo é infinito para todos os lados, e a câmera está a uma altura x do ponto inicial
        // não lembro exatamente como cheguei a esta fórmula, mas deve funcionar para todas as resoluções de tela
        camera = new OrthographicCamera(1.3f, 1.3f * Float.valueOf(Gdx.graphics.getHeight()) / Float.valueOf(Gdx.graphics.getWidth()));
        ball = Assets.createBall(world, 0, 0); // cria uma nova bola neste mundo
        ballpc = Assets.createBall(world, 0.1f, 0.1f);
        arena = Assets.background.getTextureData();

        TextButton.TextButtonStyle pauseButtonStyle = new TextButton.TextButtonStyle();
        pauseButtonStyle.font = Assets.font_small;

        buttonPause = new TextButton("||", pauseButtonStyle);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(vibrate){ Gdx.input.vibrate(100);}
                pausar();
            }
        });

        buttonDash = new TextButton(">>>", pauseButtonStyle);
        buttonDash.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(vibrate){ Gdx.input.vibrate(100);}
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
        ballpc.applyForceToCenter(forceballpc.x / 150f, forceballpc.y / 150f, true);

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

        // configuracao da fonte da mensagem
        Label.LabelStyle labelStyle = new Label.LabelStyle(); // estilo da mensagem
        labelStyle.font = Assets.font_small; // fonte pequena que foi gerada

        float lowerX = -0.5f, lowerY = -0.3f;
        if ((ball.getPosition().x > -lowerX  || ball.getPosition().x < lowerX || ball.getPosition().y > -lowerY || ball.getPosition().y < lowerY)) {
            // neste caso, você perdeu.
            mensagem("Opa! Voce perdeu!" + "\n Tente novamente.");
        }

        if ((ballpc.getPosition().x > -lowerX  || ballpc.getPosition().x < lowerX || ballpc.getPosition().y > -lowerY || ballpc.getPosition().y < lowerY)) {
            // neste caso, você ganhou.
            mensagem("Parabéns!" + "\n Voce ganhou!");
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
            game.setScreen(new MainMenuScreen(game, vibrate)); // TODO mostrar tela de confirmação
    }

    public void mensagem(String msg) {
        gamePaused = true;
        // configuracao da fonte da mensagem
        Label.LabelStyle labelStyle = new Label.LabelStyle(); // estilo da mensagem
        labelStyle.font = Assets.font_small; // fonte pequena que foi gerada
        textButtonStyle = new TextButton.TextButtonStyle(); // estilo dos botoes
        textButtonStyle.font = Assets.font_small; // fonte media que foi gerada

        Label labelTitle = new Label(msg, labelStyle);
        labelTitle.setAlignment(Align.center);

        buttonBack = new TextButton("<", textButtonStyle);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vibrate) {
                    Gdx.input.vibrate(100);
                }
                game.setScreen(new MainMenuScreen(game, vibrate)); // TODO game over // acao do botao (volta ao Menu principal)
            }
        });

        buttonNewGame = new TextButton("New Game", textButtonStyle);
        buttonNewGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vibrate) {
                    Gdx.input.vibrate(100);
                }
                game.setScreen(new GameStart(game, vibrate)); // acao do botao (ir para uma nova tela de GameStart)
            }
        });

        // disposicao dos elementos na tela
        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);

        // adicionando os botões na tela
        table.add(labelTitle).row();
        table.row();
        table.add(buttonBack).row();
        table.row();
        table.add(buttonNewGame).row();

        // tem que ver aqui como dar pause no movimento das bolinhas
        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo

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

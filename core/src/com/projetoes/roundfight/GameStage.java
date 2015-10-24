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

import java.util.LinkedList;
import java.util.Random;

public class GameStage extends Stage {

    private Table table;
    private Stage stage;
    private Body ball;
    private TextureData arena;
    private MyGdxGame game;
    private World world;
    private ShapeRenderer renderer;
    private OrthographicCamera camera;
    private LinkedList<Body> bolasInimigas;
    private TextButton.TextButtonStyle textButtonStyle;
    private Vector2 positionball, forceballpc, velocidadepc;
    private TextButton buttonPause, buttonDash, buttonBackMenu, buttonNewGame, buttonNextStage;

    boolean gamePaused = false;
    boolean vibrate = false;

    private int estagioPontuacao;
    private int corBolasEstagio;

    private Random random = new Random();

    public GameStage(final MyGdxGame game, final boolean vibrate, int estagioPontuacao) {
        this.game = game;
        this.vibrate = vibrate;
        this.estagioPontuacao = estagioPontuacao;
        this.corBolasEstagio = random.nextInt(12);

        // OBS: QUANDO FOR REFATORAR, LEMBRAR DE CRIAR UM METODO INICIALIZA() E COLOCAR ESSAS INSTANCIAS NELE

        Gdx.input.setInputProcessor(null); // para sobrescrever os ClickListeners da classe MainMenuScreen
        world = new World(new Vector2(0, 0), true);
        renderer = new ShapeRenderer();
        //renderer = new Box2DDebugRenderer();
        //camera = new OrthographicCamera(Gdx.graphics.getWidth()/1100f, Gdx.graphics.getHeight()/1100f);

        // o mundo é infinito para todos os lados, e a câmera está a uma altura x do ponto inicial
        // não lembro exatamente como cheguei a esta fórmula, mas deve funcionar para todas as resoluções de tela
        camera = new OrthographicCamera(1.3f, 1.3f * Float.valueOf(Gdx.graphics.getHeight()) / Float.valueOf(Gdx.graphics.getWidth()));

        bolasInimigas = new LinkedList<Body>();
        criarBolas(world, estagioPontuacao);
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

        // configuracao da fonte
        Label.LabelStyle labelStyle = new Label.LabelStyle(); // estilo do titulo
        labelStyle.font = Assets.font_medium; // fonte media que foi gerada

        // estagio
        Label labelEstagio = new Label("Estagio: " + String.valueOf(estagioPontuacao), labelStyle);
        labelEstagio.setAlignment(Align.center);

        table = new Table();
        table.setFillParent(true);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);
        table.align(Align.topRight).row();

        // table.add(labelEstagio); Adicao do estagio na tela
        table.add(buttonPause);
        for (int i = 0; i < 5; i++){
            table.align(Align.topRight).add().row();
        }
        table.add(buttonDash);
        stage = new Stage();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    void criarBolas(World world, int quantidade) {
        Random r = new Random();
        final float minX = -0.3f, maxX = 0.3f;
        ball = Assets.createBall(world, 0, 0); // cria uma nova bola neste mundo
        for (int i=0; i<quantidade; i++) {
            float posX = (maxX - minX) * r.nextFloat() + minX;
            float posY = (maxX - minX) * r.nextFloat() + minX;
            bolasInimigas.add(Assets.createBall(world, posX, posY));
        }
    }

    void aplicarForcasBolas() {
        float inclinacaoX = Gdx.input.getAccelerometerY() / 1200f;
        float inclinacaoY = -Gdx.input.getAccelerometerX() / 1200f;
        ball.applyForceToCenter(inclinacaoX, inclinacaoY, true); // aplica a força à bola

        for (Body bola : bolasInimigas)
            setNovoAlvoIA(bola); // dá um novo alvo à bola inimiga

    }

    void setNovoAlvoIA(Body bola) {
        positionball = ball.getPosition();
        forceballpc = bola.getPosition();
        forceballpc.set(positionball.x - forceballpc.x, positionball.y - forceballpc.y);
        velocidadepc = bola.getLinearVelocity();
        velocidadepc.rotate(2f * velocidadepc.angle(forceballpc));
        forceballpc.add(velocidadepc);

        bola.applyForceToCenter(forceballpc.x / 150f, forceballpc.y / 150f, true);
    }

    void verificarFimDoJogo() {
        float lowerX = -0.5f, lowerY = -0.3f;
        if ((ball.getPosition().x > -lowerX || ball.getPosition().x < lowerX || ball.getPosition().y > -lowerY || ball.getPosition().y < lowerY))
            //ganhou = false;
            mensagem("Try again." + "\n You lost! \n", false); // neste caso, você perdeu.

        boolean todosInimigosDerrotados = true;
        for (Body bola : bolasInimigas)
            if (!(bola.getPosition().x > -lowerX || bola.getPosition().x < lowerX || bola.getPosition().y > -lowerY || bola.getPosition().y < lowerY)) {
                todosInimigosDerrotados = false;
            }
        if (todosInimigosDerrotados) {
            //ganhou = true;
            mensagem("Very good!" + "\n You won! \n", true); // neste caso, você ganhou.
        }
    }

    void drawBolas(int cor) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(255, 255, 255, 0));
        renderer.circle(ball.getPosition().x, ball.getPosition().y, 0.025f, 100);

        corBolasInimigas(cor);

        renderer.end();
    }

    public void corBolasInimigas(int cor) {
        for (Body bola : bolasInimigas) {

            switch (cor) {
                case 1:
                    renderer.setColor(Color.GREEN);
                    break;
                case 2:
                    renderer.setColor(Color.NAVY);
                    break;
                case 3:
                    renderer.setColor(Color.BLUE);
                    break;
                case 4:
                    renderer.setColor(Color.CYAN);
                    break;
                case 5:
                    renderer.setColor(Color.MAROON);
                    break;
                case 6:
                    renderer.setColor(Color.MAGENTA);
                    break;
                case 7:
                    renderer.setColor(Color.OLIVE);
                    break;
                case 8:
                    renderer.setColor(Color.ORANGE);
                    break;
                case 9:
                    renderer.setColor(Color.PINK);
                    break;
                case 10:
                    renderer.setColor(Color.RED);
                    break;
                case 11:
                    renderer.setColor(Color.YELLOW);
                    break;
                default:
                    renderer.setColor(Color.GREEN);
                    break;
            }
            renderer.circle(bola.getPosition().x, bola.getPosition().y, 0.025f, 100);
        }
    }

    void pausar() {
        gamePaused = !gamePaused;
        buttonPause.setText(gamePaused ? ">" : "||");
    }

    @Override
    public void act(float delta) {
        if (gamePaused) return;
        super.act(delta);
        world.step(1 / 45f, 6, 2);
        // com que frequência a tela é atualizada, 45 frames por segundo. Esses valores 6 e 2 são "padroes" para android.

        aplicarForcasBolas();
        verificarFimDoJogo();

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

        drawBolas(corBolasEstagio);

        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
            game.setScreen(new MainMenuScreen(game, vibrate)); // TODO mostrar tela de confirmação
    }

    public void mensagem(String msg, boolean venceu) {
        gamePaused = true;

        // configuracao da fonte da mensagem
        Label.LabelStyle labelStyle = new Label.LabelStyle(); // estilo da mensagem
        labelStyle.font = Assets.font_small; // fonte pequena
        textButtonStyle = new TextButton.TextButtonStyle(); // estilo dos botoes
        textButtonStyle.font = Assets.font_small; // fonte pequena

        Label labelTitle = new Label(msg, labelStyle);
        labelTitle.setAlignment(Align.center);

        buttonBackMenu = new TextButton("Back Menu", textButtonStyle);
        buttonBackMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vibrate) {
                    Gdx.input.vibrate(100);
                }
                game.setScreen(new MainMenuScreen(game, vibrate)); // acao do botao (ir para o Menu principal)
            }
        });

        buttonNewGame = new TextButton("New Game", textButtonStyle);
        buttonNewGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vibrate) {
                    Gdx.input.vibrate(100);
                }
                game.setScreen(new GameStart(game, vibrate, 1)); // acao do botao (iniciar um novo GameStart, com pontuação 1)
            }
        });

        buttonNextStage = new TextButton("Next Stage", textButtonStyle);
        buttonNextStage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vibrate) {
                    Gdx.input.vibrate(100);
                }
                game.setScreen(new GameStart(game, vibrate, estagioPontuacao + 1)); // acao do botao (ir para uma nova tela de GameStart, incrementando em 1 a pontuação)
            }
        });

        // disposicao dos elementos na tela
        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);

        table.add(labelTitle).row();
        table.row();

        if(venceu) {
            // adicionando os botões na tela de vitoria
            table.add(buttonBackMenu).row();
            table.row();
            table.add(buttonNextStage);
        } else {
            // adicionando os botões na tela de derrota
            table.add(buttonBackMenu).row();
            table.row();
            table.add(buttonNewGame);
        }

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

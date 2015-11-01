package com.projetoes.roundfight.android;

/**
 * Created by asus on 17/10/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Random;

public class GameStage extends Stage {

    public static final float CONSTANTE_DE_ATRITO = 1800f;
    private Table table;
    private Stage stage;
    private Body bolaJogador;
    private MyGdxGame game;
    private World world;
    private ShapeRenderer renderer;
    private OrthographicCamera camera;
    private LinkedList<Body> bolasInimigas, bolasRemover;
    private Label labelTitle, labelHighscore, labelLocation;
    private Label.LabelStyle labelStyle;
    private TextButton.TextButtonStyle textButtonStyle;

    private TextButton buttonPause, buttonDash, buttonBack, buttonBackMenu, buttonNewGame, buttonNextStage;

    boolean gamePaused = false;
    private int estagioPontuacao;

    private int corBolasEstagio;
    private static final float ARENA_X = -0.5f;
    private static final float ARENA_Y = -0.3f;
    private static final Random random = new Random();
    private static final Color[] cores = {Color.GREEN, Color.NAVY, Color.BLUE, Color.CYAN, Color.MAROON, Color.MAGENTA, Color.OLIVE, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};

    public GameStage(final MyGdxGame game, int estagioPontuacao) {
        this.game = game;
        this.estagioPontuacao = estagioPontuacao;
        this.corBolasEstagio = random.nextInt(cores.length);

        configuracaoFonteTextos();

        // OBS: QUANDO FOR REFATORAR, LEMBRAR DE CRIAR UM METODO INICIALIZA() E COLOCAR ESSAS INSTANCIAS NELE

        Gdx.input.setInputProcessor(null); // para sobrescrever os ClickListeners da classe MainMenuScreen
        world = new World(new Vector2(0, 0), true);
        renderer = new ShapeRenderer();
        //renderer = new Box2DDebugRenderer();
        //camera = new OrthographicCamera(Gdx.graphics.getWidth()/1100f, Gdx.graphics.getHeight()/1100f);

        // o mundo é infinito para todos os lados, e a câmera está a uma altura x do ponto inicial
        // não lembro exatamente como cheguei a esta fórmula, mas deve funcionar para todas as resoluções de tela
        camera = new OrthographicCamera(1.3f, 1.3f * Gdx.graphics.getHeight() / (Gdx.graphics.getWidth() * 1f));

        bolasInimigas = new LinkedList<Body>();
        bolasRemover = new LinkedList<Body>();
        criarBolas(world, estagioPontuacao);

        buttonPause = new TextButton("||", textButtonStyle);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                gamePaused = !gamePaused;
                opcoesPause();
            }
        });

        buttonDash = new TextButton(">>>", textButtonStyle);
        buttonDash.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.wooshSound();
                dash(bolaJogador);
            }
        });


        // estagio
        Label labelEstagio = new Label("Estagio: " + String.valueOf(estagioPontuacao), labelStyle);
        labelEstagio.setAlignment(Align.center);

        criaTabela();

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

    public void configuracaoFonteTextos() {
        // Por enquanto, tamanho das fontes pequeno
        labelStyle = new Label.LabelStyle(); // estilo do titulo
        labelStyle.font = Assets.font_medium; // fonte media que foi gerada
        textButtonStyle = new TextButton.TextButtonStyle(); // estilo dos botoes
        textButtonStyle.font = Assets.font_medium; // fonte media que foi gerada
    }

    public void criaTabela() {
        // disposicao dos elementos na tela
        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);
    }

    void criarBolas(World world, int quantidade) {
        final float minX = -0.3f, maxX = 0.3f;
        bolaJogador = Assets.createBall(world, 0, 0); // cria uma nova bola neste mundo
        for (int i=0; i<quantidade; i++) {
            float posX = (maxX - minX) * random.nextFloat() + minX;
            float posY = (maxX - minX) * random.nextFloat() + minX;
            bolasInimigas.add(Assets.createBall(world, posX, posY));
        }
    }

    void aplicarForcasBolas() {
        float inclinacaoX = Gdx.input.getAccelerometerY() / (CONSTANTE_DE_ATRITO - estagioPontuacao * 20);
        float inclinacaoY = -Gdx.input.getAccelerometerX() / (CONSTANTE_DE_ATRITO - estagioPontuacao * 20);
        bolaJogador.applyForceToCenter(inclinacaoX, inclinacaoY, true); // aplica a força à bola

        for (Body bola : bolasInimigas)
            setNovoAlvoIA(bola); // dá um novo alvo à bola inimiga

        for (Body bola : bolasRemover) {
            bola.setLinearVelocity(0f,0f);
            bola.setAngularVelocity(0f);
        }

    }

    void setNovoAlvoIA(Body bola) {
        Vector2 posicaoBola, forcaBolaInimiga, velocidadeBolaInimiga;

        posicaoBola = bolaJogador.getPosition();
        forcaBolaInimiga = bola.getPosition();
        forcaBolaInimiga.set(posicaoBola.x - forcaBolaInimiga.x, posicaoBola.y - forcaBolaInimiga.y);
        velocidadeBolaInimiga = bola.getLinearVelocity();
        velocidadeBolaInimiga.rotate(2f * velocidadeBolaInimiga.angle(forcaBolaInimiga));
        forcaBolaInimiga.add(velocidadeBolaInimiga);

        bola.applyForceToCenter(forcaBolaInimiga.x / 150f, forcaBolaInimiga.y / 150f, true);
    }

    void verificarFimDoJogo() {
        if (saiuDaArena(bolaJogador))
            mostrarMensagemFim("Try again." + "\n You lost! \n", false); // neste caso, você perdeu.

        boolean todosInimigosDerrotados = true;
        for (Body bola : bolasInimigas) {
            if (saiuDaArena(bola))
                bolasRemover.add(bola);
            else
                todosInimigosDerrotados = false;
        }

        for(Body bola: bolasRemover)
            bolasInimigas.remove(bola);

        if (todosInimigosDerrotados)
            mostrarMensagemFim("Very good!" + "\n You won! \n", true); // neste caso, você ganhou.
    }

    boolean saiuDaArena(Body bola) {
        return bola.getPosition().x > -ARENA_X ||
                bola.getPosition().x < ARENA_X ||
                bola.getPosition().y > -ARENA_Y ||
                bola.getPosition().y < ARENA_Y;
    }

    void drawBolas(int cor) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(new Color(255, 255, 255, 0));
        renderer.circle(bolaJogador.getPosition().x, bolaJogador.getPosition().y, 0.025f, 100);

        corBolasInimigas(cor);

        renderer.end();
    }

    public void corBolasInimigas(int cor) {
        for (Body bola : bolasInimigas) {
            renderer.setColor(cores[cor]);
            renderer.circle(bola.getPosition().x, bola.getPosition().y, 0.025f, 100);
        }
        for (Body bola: bolasRemover) {
            renderer.setColor(Color.DARK_GRAY);
            renderer.circle(bola.getPosition().x, bola.getPosition().y, 0.025f, 100);
        }
    }

    public void opcoesPause() {
        stage = new Stage();

        labelTitle = new Label("Options", labelStyle);
        labelTitle.setAlignment(Align.center);

        buttonBackMenu = new TextButton("Quit the game", textButtonStyle);
        buttonBackMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                game.setScreen(new MainMenuScreen(game)); // acao do botao (ir para o Menu principal)
                // TODO conservar pontuacao
            }
        });

        buttonNewGame = new TextButton("Restart game", textButtonStyle);
        buttonNewGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                game.setScreen(new GameStart(game, estagioPontuacao)); // acao do botao (iniciar um novo GameStart, com pontuação 1)
            }
        });

        buttonBack = new TextButton("Return to game", textButtonStyle);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                // aqui tem que fazer ele voltar ao jogo (sair da tela q foi criada)
                pausar();
                //game.setScreen(new GameStart(game, 1)); // acao do botao (iniciar um novo GameStart, com pontuação 1)
            }
        });

        criaTabela();

        table.add(labelTitle).row();
        table.row();
        table.add(buttonBackMenu).row();
        table.row();
        table.add(buttonNewGame).row();
        table.row();
        table.add(buttonBack);

        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo
    }

    void pausar() {
        gamePaused = !gamePaused;

        criaTabela();
        table.add(buttonPause);
        for (int i = 0; i < 5; i++){
            table.align(Align.topRight).add().row();
        }
        table.add(buttonDash);

        stage = new Stage();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    float calculaPontuacao(float mult) {
        int result = 0;
        for (int i = 1; i <= estagioPontuacao; i++)
            result += 10*i;
        return result * mult;
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
        renderer.rect(ARENA_X, ARENA_Y, 1f, 0.6f);
        renderer.end();

        drawBolas(corBolasEstagio);

        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK))
            game.setScreen(new MainMenuScreen(game)); // TODO mostrar tela de confirmação
    }

    public void mostrarMensagemFim(String msg, boolean venceu) {
        stage = new Stage();
        gamePaused = true;

        labelTitle = new Label(msg, labelStyle);
        labelTitle.setAlignment(Align.center);

        buttonBackMenu = new TextButton("Back Menu", textButtonStyle);
        buttonBackMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                game.setScreen(new MainMenuScreen(game)); // acao do botao (ir para o Menu principal)
            }
        });

        buttonNewGame = new TextButton("New Game", textButtonStyle);
        buttonNewGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                game.setScreen(new GameStart(game, 1)); // acao do botao (iniciar um novo GameStart, com pontuação 1)
            }
        });

        buttonNextStage = new TextButton("Next Stage", textButtonStyle);
        buttonNextStage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                game.setScreen(new GameStart(game, estagioPontuacao + 1)); // acao do botao (ir para uma nova tela de GameStart, incrementando em 1 a pontuação)
            }
        });

        criaTabela();

        table.align(Align.center);
        table.add(labelTitle).row();

        if (!venceu)
            calculaMult();

        table.row();

        table.add(buttonBackMenu).row();
        table.row();
        table.add(venceu ? buttonNextStage : buttonNewGame);

        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo

    }

    private void calculaMult() {
        float result, mult = 1f, distance = 0f;
        String location = null;
        //WebClient.checkConnection2();
        if (WebClient.checkConnection()) {
            try {
                JSONObject jsonResponse = WebClient.getMultiplier();
                mult = (float) jsonResponse.getDouble("mult");
                location = jsonResponse.has("local") ? jsonResponse.getString("local") : null;
                distance = (float) (jsonResponse.has("distance") ? jsonResponse.getDouble("distance") : 0d);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        result = calculaPontuacao(mult);

        if (Settings.prefs.getFloat(Settings.RF_PREFERENCES_HIGHSCORE) < result) { // Usuario superou seu highscore
            // TODO
            Settings.prefs.putFloat(Settings.RF_PREFERENCES_HIGHSCORE, result).flush();
            labelHighscore = new Label("Your new highscore is " + result, labelStyle);
            labelHighscore.setAlignment(Align.center);
            table.add(labelHighscore).row();
            if (location != null) {
                labelLocation = new Label("Location: " + location + ", distance: " + distance, labelStyle);
                labelLocation.setAlignment(Align.center);
                table.add(labelLocation).row();
            }
        }
    }

    public void dash(Body obj){
        Dash d = new Dash(obj);
        d.start();
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        renderer.dispose();
    }
}

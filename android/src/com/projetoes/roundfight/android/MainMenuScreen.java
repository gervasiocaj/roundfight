package com.projetoes.roundfight.android;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.Input.TextInputListener;

import org.json.JSONArray;

/**
 * Created by Gervasio on 4/6/2015.
 */
public class MainMenuScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private Table table;
    private Stage stage;
    private Label.LabelStyle labelStyle;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextButton buttonStart, buttonExit, buttonOptions, buttonUsername, buttonLeaderboards, buttonSubmit, buttonSound, buttonVibrate, buttonHelp, buttonLBall, buttonLBuser, buttonBack;

    public MainMenuScreen(MyGdxGame game) {
        this.game = game;
        Gdx.input.setCatchBackKey(true);
        configuracaoFonteTextos();
    }

    public void configuracaoFonteTextos() {
        // configuracao da fonte
        labelStyle = new Label.LabelStyle(); // estilo do titulo
        labelStyle.font = Assets.font_large; // fonte grande que foi gerada
        textButtonStyle = new TextButton.TextButtonStyle(); // estilo dos botoes
        textButtonStyle.font = Assets.font_medium; // fonte media que foi gerada
    }

    public void criaConfiguraTabela() {
        // disposicao dos elementos na tela
        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);
    }

    public void verificaNomeUsuario() {
        TextInputListener inputListener = new TextInputListener() {
            @Override
            public void input(String text) {
                if (text == null || text.equals("")) text = "Anonymous";
                Settings.prefs.putString(Settings.RF_PREFERENCES_USER, text).flush();
                setTextButtonUsername();
            }
            @Override
            public void canceled() {
                Settings.prefs.putString(Settings.RF_PREFERENCES_USER, "Anonymous").flush();
                setTextButtonUsername();
            }
        };

        String title = "Please, enter your name";
        String text = Settings.prefs.getString(Settings.RF_PREFERENCES_USER);
        Gdx.input.getTextInput(inputListener, title, text, "");
    }

    void setTextButtonUsername() {
        buttonUsername.setText("Change username: " + Settings.prefs.getString(Settings.RF_PREFERENCES_USER));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw(); // TODO
    }

    @Override
    public void show() {
        super.show();
        stage = new Stage();

        //if(!prefs.contains(RF_PREFERENCES_USER))
        //    verificaNomeUsuario();

        // titulo
        Label labelTitle = new Label("RoundFight", labelStyle);
        labelTitle.setAlignment(Align.center);

        // botoes
        // --------------------------
        buttonStart = new TextButton("Start", textButtonStyle);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                game.setScreen(new com.projetoes.roundfight.android.GameStart(game, 1)); // acao do botao (ir para uma nova tela de GameStart)
            }
        });

        buttonLeaderboards = new TextButton("Leaderboards", textButtonStyle);
        buttonLeaderboards.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                leaderboards();
            }
        });

        buttonOptions = new TextButton("Options", textButtonStyle);
        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                show2();// acao do botao (ir para uma nova tela de GameStart)
            }
        });

        buttonExit = new TextButton("Exit", textButtonStyle);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                Gdx.app.exit(); // acao do botao
            }
        });
        // ---------------------------

       criaConfiguraTabela();

        // row significa nova linha, mesma coisa de table.add(labelTitle); table.row(); 
        table.add(labelTitle).row();
        table.add(buttonStart).row();
        table.add(buttonLeaderboards).row();
        table.add(buttonOptions).row();
        table.add(buttonExit).row();


        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo
    }

    public void show2() {
        super.show();
        stage = new Stage();

        // titulo
        Label labelTitle = new Label("Options", labelStyle);
        labelTitle.setAlignment(Align.center);

        // botoes
        // --------------------------

        buttonSound = new TextButton(Settings.prefs.getBoolean(Settings.RF_PREFERENCES_SOUND) ? "Sound On" : "Sound Off", textButtonStyle);
        buttonSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                Settings.prefs.putBoolean(Settings.RF_PREFERENCES_SOUND, !Settings.prefs.getBoolean(Settings.RF_PREFERENCES_SOUND)).flush();
                show2();// acao do botao
            }
        });

        buttonVibrate = new TextButton(Settings.prefs.getBoolean(Settings.RF_PREFERENCES_VIBRATE) ? "Vibrate On" : "Vibrate Off", textButtonStyle);
        buttonVibrate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.prefs.putBoolean(Settings.RF_PREFERENCES_VIBRATE, !Settings.prefs.getBoolean(Settings.RF_PREFERENCES_VIBRATE)).flush();
                Settings.vibrateAndBeepIfAvailable();
                show2();
            }
        });

        buttonUsername = new TextButton("", textButtonStyle);
        setTextButtonUsername();
        buttonUsername.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                verificaNomeUsuario();
            }
        });

        buttonHelp = new TextButton("Help", textButtonStyle);
        buttonHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                help();// acao do botao (ir para uma nova tela de GameStart)
            }
        });

        buttonBack = new TextButton("<", textButtonStyle);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                show(); // acao do botao (ir para uma nova tela de GameStart)
            }
        });
        // ---------------------------

        criaConfiguraTabela();

        // row significa nova linha, mesma coisa de table.add(labelTitle); table.row();
        table.add(labelTitle).row();
        table.add(buttonSound).row();
        table.add(buttonVibrate).row();
        table.add(buttonUsername).row();
        table.add(buttonHelp).row();
        table.add(buttonBack).row();

        //table.add(new Label(MainMenuScreen.prefs.getString(MainMenuScreen.RF_PREFERENCES_USER), labelStyle)).align(Align.bottomLeft);
        //table.add(new Label("Highscore: " + MainMenuScreen.prefs.getInteger(MainMenuScreen.RF_PREFERENCES_HIGHSCORE), labelStyle)).align(Align.bottomRight);


        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo
    }

    public void help(){
        super.show();
        stage = new Stage();

        // titulo
        Label labelTitle = new Label("Help", labelStyle);
        labelTitle.setAlignment(Align.center);

        Label labelTitle2 = new Label("\nYou need to defeat your enemies," +
                "\n throwing them out of the arena." +
                "\n Tilt your device to move the ball." + 
                "\n Press the >>> button to dash.", labelStyle);
        labelTitle2.setAlignment(Align.center);
        // botoes
        // --------------------------

        buttonBack = new TextButton("<", textButtonStyle);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                show2(); // acao do botao (ir para uma nova tela de GameStart)
            }
        });
        // ---------------------------

        criaConfiguraTabela();

        // row significa nova linha, mesma coisa de table.add(labelTitle); table.row();
        table.add(labelTitle).row();
        table.add(labelTitle2).row();
        table.add(buttonBack).row();

        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo
    }

    public void leaderboards() {
        super.show();
        stage = new Stage();

        // titulo
        Label labelTitle = new Label("Leaderboards", labelStyle);
        labelTitle.setAlignment(Align.center);

        // botoes
        // --------------------------
        buttonLBall = new TextButton("Top leaderboards", textButtonStyle);
        buttonLBall.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                // TODO abrir lista todos
                JSONArray b = WebClient.getLeaderboard();
                Log.i("rf2", b.toString());
            }
        });
        buttonLBuser = new TextButton("User leaderboards", textButtonStyle);
        buttonLBuser.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                // TODO abrir lista user
                JSONArray a = WebClient.getLeaderboard(Settings.prefs.getString(Settings.RF_PREFERENCES_USER));
                Log.i("rf2", a.toString());
            }
        });
        buttonSubmit = new TextButton("Submit my highscore", textButtonStyle);
        buttonSubmit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                WebClient.postScore(Settings.prefs.getString(Settings.RF_PREFERENCES_USER), Settings.prefs.getFloat(Settings.RF_PREFERENCES_HIGHSCORE));

            }
        });
        buttonBack = new TextButton("<", textButtonStyle);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                show(); // acao do botao (ir para uma nova tela de GameStart)
            }
        });
        // ---------------------------

        criaConfiguraTabela();

        // row significa nova linha, mesma coisa de table.add(labelTitle); table.row();
        table.add(labelTitle).row();
        table.add(buttonLBall).row();
        table.add(buttonLBuser).row();
        table.add(buttonSubmit).row();
        table.add(buttonBack).row();

        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo
    }
}

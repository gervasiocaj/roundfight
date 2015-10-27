package com.projetoes.roundfight.android;

import android.content.SharedPreferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.Input.TextInputListener;

/**
 * Created by Gervasio on 4/6/2015.
 */
public class MainMenuScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private Table table;
    private Stage stage;
    private Label.LabelStyle labelStyle;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextButton buttonStart, buttonExit, buttonOptions;
    private TextButton buttonSound, buttonVibrate, buttonHelp, buttonBack;
    public boolean vibrate = false;
    protected boolean sound = false;

    public MainMenuScreen(MyGdxGame game, boolean vibrate) {
        this.game = game;
        this.vibrate = vibrate;
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
        final SharedPreferences.Editor editor = AndroidLauncher.prefs.edit();

        TextInputListener inputListener = new TextInputListener() {
            @Override
            public void input(String text) {
                editor.putString("username", text);
                editor.commit();
            }
            @Override
            public void canceled() {
                if (!AndroidLauncher.prefs.contains("username"))
                    editor.putString("username", null);
                editor.commit();
            }
        };

        Gdx.input.getTextInput(inputListener, "Por favor, digite o seu nome", "", "");
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

        //TODO Falta salvar o nome do jogador. Atualmente ele pede toda vez pq sempre q o MainMenu é iniciado, o nomeJogador recebe ""
        if(!AndroidLauncher.prefs.contains("username"))
            verificaNomeUsuario();

        // titulo
        Label labelTitle = new Label("RoundFight", labelStyle);
        labelTitle.setAlignment(Align.center);

        // botoes
        // --------------------------
        buttonExit = new TextButton("Exit", textButtonStyle);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(vibrate){ Gdx.input.vibrate(100);}
                Gdx.app.exit(); // acao do botao
            }
        });

        buttonStart = new TextButton("Start", textButtonStyle);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vibrate) {
                    Gdx.input.vibrate(100);
                }
                game.setScreen(new com.projetoes.roundfight.android.GameStart(game, vibrate, 1)); // acao do botao (ir para uma nova tela de GameStart)
            }
        });

        buttonOptions = new TextButton("Options", textButtonStyle);
        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vibrate) {
                    Gdx.input.vibrate(100);
                }
                show2();// acao do botao (ir para uma nova tela de GameStart)
            }
        });
        // ---------------------------

       criaConfiguraTabela();

        // row significa nova linha, mesma coisa de table.add(labelTitle); table.row(); 
        table.add(labelTitle).row();
        table.add(buttonStart).row();
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
        if(sound){
            buttonSound = new TextButton("Sound On", textButtonStyle);
        } else{
            buttonSound = new TextButton("Sound Off", textButtonStyle);
        }
        buttonSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(vibrate){ Gdx.input.vibrate(100);}
                sound = !sound;
                if(sound){
                }
                show2();// acao do botao
            }
        });

        if(vibrate){
            buttonVibrate = new TextButton("Vibrate On", textButtonStyle);
        }else{
            buttonVibrate = new TextButton("Vibrate Off", textButtonStyle);
        }
        buttonVibrate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                vibrate = !vibrate;
                if(vibrate){ Gdx.input.vibrate(100);}
                show2();
            }
        });

        buttonHelp = new TextButton("Help", textButtonStyle);
        buttonHelp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(vibrate){ Gdx.input.vibrate(100);}
                help();// acao do botao (ir para uma nova tela de GameStart)
            }
        });

        buttonBack = new TextButton("<", textButtonStyle);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (vibrate) {
                    Gdx.input.vibrate(100);
                }
                if (sound) {

                }
                show(); // acao do botao (ir para uma nova tela de GameStart)
            }
        });
        // ---------------------------

        criaConfiguraTabela();

        // row significa nova linha, mesma coisa de table.add(labelTitle); table.row();
        table.add(labelTitle).row();
        table.add(buttonSound).row();
        table.add(buttonVibrate).row();
        table.add(buttonHelp).row();
        table.add(buttonBack).row();


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
                if (vibrate) {
                    Gdx.input.vibrate(100);
                }
                if (sound) {

                }
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
}
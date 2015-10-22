package com.projetoes.roundfight;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Gervasio on 4/6/2015.
 */
public class MainMenuScreen extends ScreenAdapter {

    private final MyGdxGame game;
    private Table table;
    private Stage stage;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextButton buttonStart, buttonExit, buttonOptions;
    private TextButton buttonSound, buttonVibrate, buttonHelp, buttonBack;
    public boolean vibrate = false;
    protected boolean sound = false;


    public MainMenuScreen(MyGdxGame game, boolean vibrate) {
        this.game = game;
        this.vibrate = vibrate;
        Gdx.input.setCatchBackKey(true);
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

        // configuracao da fonte
        Label.LabelStyle labelStyle = new Label.LabelStyle(); // estilo do titulo
        labelStyle.font = Assets.font_large; // fonte grande que foi gerada
        textButtonStyle = new TextButton.TextButtonStyle(); // estilo dos botoes
        textButtonStyle.font = Assets.font_medium; // fonte media que foi gerada

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
                if(vibrate){ Gdx.input.vibrate(100);}
                float positionX = Gdx.input.getAccelerometerX();
                float positionY = Gdx.input.getAccelerometerY();
                Vector2 position = new Vector2(positionX,positionY);
                game.setScreen(new GameStart(game, vibrate,position)); // acao do botao (ir para uma nova tela de GameStart)
            }
        });

        buttonOptions = new TextButton("Options", textButtonStyle);
        buttonOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(vibrate){ Gdx.input.vibrate(100);}
                show2();// acao do botao (ir para uma nova tela de GameStart)
            }
        });
        // ---------------------------

        // disposicao dos elementos na tela 
        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);

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

        // configuracao da fonte
        Label.LabelStyle labelStyle = new Label.LabelStyle(); // estilo do titulo
        labelStyle.font = Assets.font_large; // fonte grande que foi gerada
        textButtonStyle = new TextButton.TextButtonStyle(); // estilo dos botoes
        textButtonStyle.font = Assets.font_medium; // fonte media que foi gerada

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
                if(vibrate){ Gdx.input.vibrate(100);}
                if(sound){

                }
                show(); // acao do botao (ir para uma nova tela de GameStart)
            }
        });
        // ---------------------------

        // disposicao dos elementos na tela
        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);

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

        // configuracao da fonte
        Label.LabelStyle labelStyle = new Label.LabelStyle(); // estilo do titulo
        labelStyle.font = Assets.font_large; // fonte grande que foi gerada
        Label.LabelStyle labelStyle2 = new Label.LabelStyle(); // estilo do titulo
        labelStyle2.font = Assets.font_medium; // fonte grande que foi gerada
        textButtonStyle = new TextButton.TextButtonStyle(); // estilo dos botoes
        textButtonStyle.font = Assets.font_medium; // fonte media que foi gerada

        // titulo
        Label labelTitle = new Label("Help", labelStyle);
        labelTitle.setAlignment(Align.center);

        Label labelTitle2 = new Label("\nVocê precisa derrotar seu oponente," +
                "\n empurrando-o para fora da arena." +
                "\n Para isso mova o celular. \n", labelStyle2);
        labelTitle2.setAlignment(Align.center);
        // botoes
        // --------------------------

        buttonBack = new TextButton("<", textButtonStyle);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(vibrate){ Gdx.input.vibrate(100);}
                if(sound){

                }
                show2(); // acao do botao (ir para uma nova tela de GameStart)
            }
        });
        // ---------------------------

        // disposicao dos elementos na tela
        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);

        // row significa nova linha, mesma coisa de table.add(labelTitle); table.row();
        table.add(labelTitle).row();
        table.add(labelTitle2).row();
        table.add(buttonBack).row();


        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo
    }

}

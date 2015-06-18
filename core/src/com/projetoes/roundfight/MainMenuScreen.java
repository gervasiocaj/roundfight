package com.projetoes.roundfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
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
    private TextButton buttonStart, buttonExit;

    public MainMenuScreen(MyGdxGame game) {
        this.game = game;
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
                Gdx.app.exit(); // acao do botao
            }
        });

        buttonStart = new TextButton("Start", textButtonStyle);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameStart(game)); // acao do botao (ir para uma nova tela de GameStart)
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
        table.add(buttonExit).row();

        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo
    }
}

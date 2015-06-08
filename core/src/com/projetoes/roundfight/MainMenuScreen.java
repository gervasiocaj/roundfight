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

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = Assets.font_large;
        Label labelTitle = new Label("RoundFight", labelStyle);
        labelTitle.setAlignment(Align.center);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Assets.font_medium;

        buttonExit = new TextButton("Exit", textButtonStyle);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        buttonStart = new TextButton("Start", textButtonStyle);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameStart(game));
            }
        });

        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);

        table.add(labelTitle).row();
        table.add(buttonStart).row();
        table.add(buttonExit).row();
    }
}

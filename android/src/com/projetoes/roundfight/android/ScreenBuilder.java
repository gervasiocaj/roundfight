package com.projetoes.roundfight.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by gerva on 05/11/2015.
 */
public class ScreenBuilder {

    private Table table;
    private Label.LabelStyle labelStyle;
    private TextButton.TextButtonStyle textButtonStyle;
    private static Stage stage;

    public static void actAndDrawStage(float delta) {
        stage.act(delta);
        stage.draw(); // TODO
    }

    public ScreenBuilder (ScreenBuilder.Size size) {
        labelStyle = new Label.LabelStyle(); // estilo do titulo
        textButtonStyle = new TextButton.TextButtonStyle(); // estilo dos botoes
        switch (size) {
            case SMALL:
                labelStyle.font = Assets.font_small; // fonte grande que foi gerada
                textButtonStyle.font = Assets.font_small;
            case LARGE:
                labelStyle.font = Assets.font_large; // fonte grande que foi gerada
                textButtonStyle.font = Assets.font_large;
                break;
            case MEDIUM:
            default:
                labelStyle.font = Assets.font_medium; // fonte grande que foi gerada
                textButtonStyle.font = Assets.font_medium;
        }

        stage = new Stage();
        table = new Table();
        table.setFillParent(true);
        table.align(Align.center);
        table.defaults().size(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 6);
        table.debug();
    }

    public Cell<TextButton> addButton(String text, ClickListener listener) {
        TextButton button = new TextButton(text, textButtonStyle);
        button.addListener(listener);
        return table.add(button);
    }

    public Cell<Label> addLabel(String text) {
        Label label = new Label(text, labelStyle);
        label.setAlignment(Align.center);
        return table.add(label);
    }

    public Cell<Label> addLabel(String text, Size size, int align) {
        Label.LabelStyle style = new Label.LabelStyle();
        switch (size) {
            case SMALL:
                style.font = Assets.font_small;
                break;
            case LARGE:
                style.font = Assets.font_medium;
                break;
            case MEDIUM:
            default:
                style.font = Assets.font_medium;
        }

        Label label = new Label(text, style);
        label.setAlignment(align);

        return table.add(label);
    }

    public Table table() {
        return table;
    }

    public void finish() {
        stage.addActor(table); // adiciona no stage
        Gdx.input.setInputProcessor(stage); // adiciona esse stage ao processamento padrao do jogo
    }

    public enum Size {SMALL, MEDIUM, LARGE}
}

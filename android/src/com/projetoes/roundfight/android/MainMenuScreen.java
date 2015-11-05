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
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gervasio on 4/6/2015.
 */
public class MainMenuScreen extends ScreenAdapter {

    private final MyGdxGame game;

    public MainMenuScreen(MyGdxGame game) {
        this.game = game;
        Gdx.input.setCatchBackKey(true);
    }

    public void verificaNomeUsuario() {
        TextInputListener inputListener = new TextInputListener() {
            @Override
            public void input(String text) {
                if (text == null || text.equals("")) text = "Anonymous";
                Settings.prefs.putString(Settings.RF_PREFERENCES_USER, text).flush();
                show2();
            }
            @Override
            public void canceled() {
                Settings.prefs.putString(Settings.RF_PREFERENCES_USER, "Anonymous").flush();
                show2();
            }
        };

        String title = "Please, enter your name";
        String text = Settings.prefs.getString(Settings.RF_PREFERENCES_USER);
        Gdx.input.getTextInput(inputListener, title, text, "");
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ScreenBuilder.actAndDrawStage(delta);
    }

    @Override
    public void show() {
        super.show();
        ScreenBuilder screenBuilder = new ScreenBuilder(ScreenBuilder.Size.MEDIUM);

        screenBuilder.addLabel(String.format("Highscore: %.0f", Settings.prefs.getFloat(Settings.RF_PREFERENCES_HIGHSCORE, 0f)), ScreenBuilder.Size.SMALL, Align.top).expandX();
        screenBuilder.addLabel("RoundFight", ScreenBuilder.Size.LARGE, Align.center).expandX();
        screenBuilder.addLabel(String.format("User: %s", Settings.prefs.getString(Settings.RF_PREFERENCES_USER, "Anonymous")), ScreenBuilder.Size.SMALL, Align.top).expandX().row();

        screenBuilder.table().add();
        screenBuilder.addButton("Start", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                game.setScreen(new com.projetoes.roundfight.android.GameStart(game, 1)); // acao do botao (ir para uma nova tela de GameStart)
            }
        }).row();

        screenBuilder.table().add();
        screenBuilder.addButton("Leaderboards", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                leaderboards();
            }
        }).row();

        screenBuilder.table().add();
        screenBuilder.addButton("Options", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                show2();// acao do botao (ir para uma nova tela de GameStart)
            }
        }).row();

        screenBuilder.table().add();
        screenBuilder.addButton("Exit", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                Gdx.app.exit(); // acao do botao
            }
        }).row();
        // ---------------------------

        screenBuilder.finish();
    }

    public void show2() {
        super.show();
        ScreenBuilder screenBuilder = new ScreenBuilder(ScreenBuilder.Size.MEDIUM);

        screenBuilder.addLabel("Options").row();
        screenBuilder.addButton(Settings.prefs.getBoolean(Settings.RF_PREFERENCES_SOUND) ? "Sound On" : "Sound Off", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                Settings.prefs.putBoolean(Settings.RF_PREFERENCES_SOUND, !Settings.prefs.getBoolean(Settings.RF_PREFERENCES_SOUND)).flush();
                show2();// acao do botao
            }
        }).row();

        screenBuilder.addButton(Settings.prefs.getBoolean(Settings.RF_PREFERENCES_VIBRATE) ? "Vibrate On" : "Vibrate Off", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.prefs.putBoolean(Settings.RF_PREFERENCES_VIBRATE, !Settings.prefs.getBoolean(Settings.RF_PREFERENCES_VIBRATE)).flush();
                Settings.vibrateAndBeepIfAvailable();
                show2();
            }
        }).row();

        screenBuilder.addButton("Change username: " + Settings.prefs.getString(Settings.RF_PREFERENCES_USER), new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                verificaNomeUsuario();
                show2();
            }
        }).row();

        screenBuilder.addButton("Help", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                help();// acao do botao (ir para uma nova tela de GameStart)
            }
        }).row();

        screenBuilder.addButton("<", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                show(); // acao do botao (ir para uma nova tela de GameStart)
            }
        }).row();

        screenBuilder.finish();
    }

    public void help(){
        super.show();
        ScreenBuilder screenBuilder = new ScreenBuilder(ScreenBuilder.Size.MEDIUM);

        screenBuilder.addLabel("Help").row();

        screenBuilder.addLabel("\n" +
                "You need to defeat your enemies,\n" +
                "throwing them out of the arena.\n" +
                "Tilt your device to move the ball.\n" +
                "Press the >>> button to dash.\n\n" +
                "Everytime you finish a game, \n" +
                "your highscore will be calculated.\n" +
                "Go to locations with multipliers to\n" +
                "gather even more points!\n\n", ScreenBuilder.Size.SMALL, Align.center).row();

        screenBuilder.addButton("<", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                show2(); // acao do botao (ir para uma nova tela de GameStart)
            }
        });

        screenBuilder.finish();
    }

    public void leaderboards() {
        super.show();
        ScreenBuilder screenBuilder = new ScreenBuilder(ScreenBuilder.Size.MEDIUM);

        screenBuilder.addLabel("Leaderboards").row();

        screenBuilder.addButton("Top leaderboards", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                showList(WebClient.getLeaderboard());
            }
        }).row();
        screenBuilder.addButton("User leaderboards", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                showList(WebClient.getLeaderboard(Settings.prefs.getString(Settings.RF_PREFERENCES_USER)));
            }
        }).row();
        screenBuilder.addButton("Submit my highscore", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                WebClient.postScore(Settings.prefs.getString(Settings.RF_PREFERENCES_USER), Settings.prefs.getFloat(Settings.RF_PREFERENCES_HIGHSCORE));
            }
        }).row();
        screenBuilder.addButton("<", new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                show(); // acao do botao (ir para uma nova tela de GameStart)
            }
        }).row();

        screenBuilder.finish();
    }

    void showList(JSONArray jsonArray) {
        super.show();
        ScreenBuilder screenBuilder = new ScreenBuilder(ScreenBuilder.Size.SMALL);

        screenBuilder.addButton("<", (new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.vibrateAndBeepIfAvailable();
                leaderboards();
            }
        })).row();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                screenBuilder.addLabel(jsonArray.getJSONObject(i).getString("user")).expandX();
                screenBuilder.addLabel(String.format("%.0f", jsonArray.getJSONObject(i).getDouble("points"))).expandX().row();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        screenBuilder.finish();
    }
}

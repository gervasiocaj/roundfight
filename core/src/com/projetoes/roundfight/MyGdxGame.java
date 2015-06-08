package com.projetoes.roundfight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	private static MainMenuScreen main;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Settings.load();
		Assets.load();
		main = new MainMenuScreen(this);
		setScreen(main);
	}

	@Override
	public void render () {
        super.render();
	}

	public static MainMenuScreen getMainMenuScreen() {
		return main;
	}
}

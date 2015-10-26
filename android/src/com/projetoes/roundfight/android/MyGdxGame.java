package com.projetoes.roundfight.android;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGdxGame extends Game {
	public SpriteBatch batch;
	private static com.projetoes.roundfight.android.MainMenuScreen main;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Settings.load();
		Assets.load();
		main = new com.projetoes.roundfight.android.MainMenuScreen(this, false);
		setScreen(main);
	}

	@Override
	public void render () {
        super.render();
	}

	public static com.projetoes.roundfight.android.MainMenuScreen getMainMenuScreen() {
		return main;
	}


}

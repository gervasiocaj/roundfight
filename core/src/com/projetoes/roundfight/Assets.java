package com.projetoes.roundfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Gervasio on 6/6/2015.
 */
public class Assets {
    public static Texture items;
    public static TextureRegion mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static TextureRegion gameOver;
    public static BitmapFont font_small, font_medium, font_large;

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load() {
        font_small = new BitmapFont(Gdx.files.internal("data/rounded_elegance_small.fnt"));
        font_medium = new BitmapFont(Gdx.files.internal("data/rounded_elegance.fnt"));
        font_large = new BitmapFont(Gdx.files.internal("data/rounded_elegance_large.fnt"));
        /*items = loadTexture("data/items.png"); // TODO xxx

        mainMenu = new TextureRegion(items, 0, 224, 300, 110);
        pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = new TextureRegion(items, 352, 256, 160, 96);
        */
    }
}

package com.projetoes.roundfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

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

    public static Body createBall(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; 
        // tipos de corpos:
            // dinâmicos: se movem e são afetados por forças (mario)
            // estáticos: não se movem e não são afetados por forças (canos do mario)
            // cinemáticos: se movem, mas não são afetados por forças (plataformas do mario) 
        bodyDef.position.set(0, 0);

        CircleShape shape = new CircleShape();
        shape.setRadius(.025f); // tamanho padrao (25mm) da bola de gude/bila/chimbra

        FixtureDef fixtureDef = new FixtureDef(); // propriedades físicas desse corpo (atrito, forma, densidade...)
        fixtureDef.shape = shape;
        fixtureDef.density = 2.7f; // http://www.engineeringtoolbox.com/density-solids-d_1265.html
        fixtureDef.friction = 0.25f;
        fixtureDef.restitution = .75f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }
}

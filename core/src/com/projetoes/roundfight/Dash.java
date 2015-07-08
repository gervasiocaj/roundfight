package com.projetoes.roundfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by pc1 on 08/07/2015.
 */
public class Dash extends Thread {

    private Body ball;

    public Dash(Body b){
        super();
        ball = b;
    }

    @Override
    public void run() {
        try {
            Vector2 v = new Vector2(Gdx.input.getAccelerometerY(), -Gdx.input.getAccelerometerX());
            v.setLength(0.3f);
            ball.applyForceToCenter(v, true);
            this.sleep(100);
            ball.setLinearVelocity(v.setLength(0.2f));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

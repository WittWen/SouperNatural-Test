package com.badlogic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Currency extends BaseActor {
    public int randomX;
    public int randomY;

    public Currency(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("silver.png");

    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);

        randomX = MathUtils.random(-15,15);
        randomY = MathUtils.random(-15,15);
        addAction((Actions.after(Actions.sequence(
            Actions.moveBy(randomX, randomY, 0.5f),
            Actions.moveBy(randomX, randomY, 0.5f)))));

        addAction(Actions.sequence(
            Actions.delay(9f),
            Actions.fadeOut(1f),
            Actions.removeActor()));
    }
}

package com.badlogic;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class EnemyCoriander extends Enemy {
    public EnemyCoriander(float x, float y, Stage s) {
        super(x, y, s);
        loadAnimations();
        setDefaultValues();
    }

    @Override
    protected void loadAnimations() {
        // Load Move Animations
        leftMoveAnimation = loadAnimationFromSheetByRow(
            "enemy-coriander.png", 1, 0, 4, 0.1f, true, false);
        rightMoveAnimation = loadAnimationFromSheetByRow(
            "enemy-coriander.png", 1, 0, 4, 0.1f, true, true);

        // Set Default Animation
        setAnimation(rightMoveAnimation);
    }

    @Override
    protected void setDefaultValues() {
        moveSpeed = 50;
        health = 1;
        setBoundaryPolygon(8);
    }
}

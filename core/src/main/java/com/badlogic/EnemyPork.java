package com.badlogic;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class EnemyPork extends Enemy {
    private Animation<TextureRegion> pork1LeftMoveAnimation;
    private Animation<TextureRegion> pork1RightMoveAnimation;

    private Animation<TextureRegion> pork2LeftMoveAnimation;
    private Animation<TextureRegion> pork2RightMoveAnimation;

    private boolean isPork1;

    public EnemyPork(float x, float y, Stage s) {
        super(x, y, s);
        Random random = new Random();
        isPork1 = random.nextBoolean();
        loadAnimations();
        setDefaultValues();
    }

    @Override
    protected void loadAnimations() {
        // Prok Type 1 Move Animations
        pork1LeftMoveAnimation = loadAnimationFromSheetByRow(
            "pork-movement-1.png", 1, 0, 4, 0.2f, true, false);
        pork1RightMoveAnimation = loadAnimationFromSheetByRow(
            "pork-movement-1.png", 1, 0, 4, 0.2f, true, true);

        // Pork Type 2 Move Animations
        pork2LeftMoveAnimation = loadAnimationFromSheetByRow(
            "pork-movement-2.png", 1, 0, 4, 0.2f, true, false);
        pork2RightMoveAnimation = loadAnimationFromSheetByRow(
            "pork-movement-2.png", 1, 0, 4, 0.2f, true, true);

        // Set Animation Randomly
        if (isPork1) {
            leftMoveAnimation = pork1LeftMoveAnimation;
            rightMoveAnimation = pork1RightMoveAnimation;
        } else {
            leftMoveAnimation = pork2LeftMoveAnimation;
            rightMoveAnimation = pork2RightMoveAnimation;
        }

        // Set Default Animation
        setAnimation(rightMoveAnimation);
    }

    @Override
    protected void setDefaultValues() {
        moveSpeed = 50;
        health = 1;
        setBoundaryPolygon(8);
    }

    public boolean isPork1() {
        return isPork1;
    }
}

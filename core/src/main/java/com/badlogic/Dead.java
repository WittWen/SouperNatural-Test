package com.badlogic;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Dead extends BaseActor {
    // Coriander Dead Animations
    private Animation<TextureRegion> corianderLeftDeadAnimation;
    private Animation<TextureRegion> corianderRightDeadAnimation;

    // Pork Dead Animations
    private Animation<TextureRegion> pork1LeftDeadAnimation;
    private Animation<TextureRegion> pork1RightDeadAnimation;
    private Animation<TextureRegion> pork2LeftDeadAnimation;
    private Animation<TextureRegion> pork2RightDeadAnimation;

    private float deathTimer = 0;
    private float deathDuration = 1.0f;
    private float fadeOutDuration = 0.5f;

    public Dead(float x, float y, Stage s) {
        super(x, y, s);

        // Load Coriander Dead Animation
        corianderLeftDeadAnimation = loadAnimationFromSheetByRow(
            "enemy-coriander-dead.png", 1, 0, 4, 0.1f, false, false);
        corianderRightDeadAnimation = loadAnimationFromSheetByRow(
            "enemy-coriander-dead.png", 1, 0, 4, 0.1f, false, true);

        // Load Pork Dead Animation (2 Types)
        pork1LeftDeadAnimation = loadAnimationFromSheetByRow(
            "pork-dead-1.png", 1, 0, 4, 0.2f, false, false);
        pork1RightDeadAnimation = loadAnimationFromSheetByRow(
            "pork-dead-1.png", 1, 0, 4, 0.2f, false, true);
        pork2LeftDeadAnimation = loadAnimationFromSheetByRow(
            "pork-dead-2.png", 1, 0, 4, 0.2f, false, false);
        pork2RightDeadAnimation = loadAnimationFromSheetByRow(
            "pork-dead-2.png", 1, 0, 4, 0.2f, false, true);
    }

    public void setDeadAnimation(Enemy enemy, boolean isFacingLeft) {
        if (enemy instanceof EnemyCoriander) {
            setAnimation(isFacingLeft ? corianderLeftDeadAnimation : corianderRightDeadAnimation);
        } else if (enemy instanceof EnemyPork) {
            EnemyPork pork = (EnemyPork) enemy;
            if (pork.isPork1()) {
                setAnimation(isFacingLeft ? pork1LeftDeadAnimation : pork1RightDeadAnimation);
            } else {
                setAnimation(isFacingLeft ? pork2LeftDeadAnimation : pork2RightDeadAnimation);
            }
        }
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        deathTimer += dt;

        // Fade out
        if (deathTimer > deathDuration) {
            float fadeOutProgress = (deathTimer - deathDuration) / fadeOutDuration;
            setOpacity(1 - fadeOutProgress);

            // Remove After Fade Out
            if (fadeOutProgress >= 1) {
                remove();
            }
        }
    }
}

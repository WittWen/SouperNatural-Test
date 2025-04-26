package com.badlogic;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class EnemyMeatball extends Enemy {
    public EnemyMeatball(float x, float y, Stage s) {
        super(x, y, s);
        loadAnimations();
        setDefaultValues();
    }

    @Override
    protected void loadAnimations() {
        // TODO: 加载肉丸敌人的动画
        // leftMoveAnimation = loadAnimationFromSheetByRow(...);
        // rightMoveAnimation = loadAnimationFromSheetByRow(...);
        // leftDeadAnimation = loadAnimationFromSheetByRow(...);
        // rightDeadAnimation = loadAnimationFromSheetByRow(...);
        
        // 设置默认动画
        setAnimation(rightMoveAnimation);
    }

    @Override
    protected void setDefaultValues() {
        moveSpeed = 80; // 肉丸敌人移动速度较快
        health = 1; // 肉丸敌人血量较低
        setBoundaryPolygon(8);
    }
}

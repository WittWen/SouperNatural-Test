package com.badlogic;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class EnemyBeef extends Enemy {
    public EnemyBeef(float x, float y, Stage s) {
        super(x, y, s);
        loadAnimations();
        setDefaultValues();
    }

    @Override
    protected void loadAnimations() {
        // TODO: 加载牛肉敌人的动画
        // leftMoveAnimation = loadAnimationFromSheetByRow(...);
        // rightMoveAnimation = loadAnimationFromSheetByRow(...);
        // leftDeadAnimation = loadAnimationFromSheetByRow(...);
        // rightDeadAnimation = loadAnimationFromSheetByRow(...);
        
        // 设置默认动画
        setAnimation(rightMoveAnimation);
    }

    @Override
    protected void setDefaultValues() {
        moveSpeed = 50; // 牛肉敌人移动较慢
        health = 3; // 牛肉敌人血量较高
        setBoundaryPolygon(8);
    }
}

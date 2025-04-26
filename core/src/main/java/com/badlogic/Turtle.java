package com.badlogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Turtle extends BaseActor {
    private Vector2 velocity;
    public float moveSpeed = GameStatsManager.getInstance().getCurrentMoveSpeed();
    public boolean isAlive;

    // Movement Animations
    private Animation<TextureRegion> moveUpAnimation;
    private Animation<TextureRegion> moveDownAnimation;
    private Animation<TextureRegion> moveLeftAnimation;
    private Animation<TextureRegion> moveRightAnimation;

    // Attack Animations
    private Animation<TextureRegion> attackUpAnimation;
    private Animation<TextureRegion> attackDownAnimation;
    private Animation<TextureRegion> attackLeftAnimation;
    private Animation<TextureRegion> attackRightAnimation;

    private Direction currentDirection;

    public boolean isAttacking = false;
    private float attackTimer = 0f;

    private Rectangle bodyRectangle;

    private Rectangle horizontalAttackRectangle;
    private Rectangle verticalAttackRectangle;
    private boolean isHorizontalAttackActive;
    private boolean isVerticalAttackActive;

    private boolean isInvincible;
    private float invincibleTimer;
    private static final float INVINCIBLE_DURATION = 0.75f;

    private float currentHungryPoint = GameStatsManager.getInstance().getCurrentMaxHungryPoint();

    public Turtle(float x, float y, Stage s) {
        super(x, y, s);

        // Load Movement Animations
        moveUpAnimation = loadAnimationFromSheetByRow(
                "player-movements.png", 6, 0, 6, 0.1f, true, false);
        moveDownAnimation = loadAnimationFromSheetByRow(
                "player-movements.png", 6, 2, 6, 0.1f, true, false);
        moveLeftAnimation = loadAnimationFromSheetByRow(
                "player-movements.png", 6, 4, 6, 0.1f, true, false);
        moveRightAnimation = loadAnimationFromSheetByRow(
                "player-movements.png", 6, 4, 6, 0.1f, true, true);

        // Load Attack Animations
        attackUpAnimation = loadAnimationFromSheetByRow(
                "player-movements.png", 6, 1, 6, 0.1f, true, false);
        attackDownAnimation = loadAnimationFromSheetByRow(
                "player-movements.png", 6, 3, 6, 0.1f, true, false);
        attackLeftAnimation = loadAnimationFromSheetByRow(
                "player-movements.png", 6, 5, 6, 0.1f, true, false);
        attackRightAnimation = loadAnimationFromSheetByRow(
                "player-movements.png", 6, 5, 6, 0.1f, true, true);


        setBoundaryPolygon(8);
        velocity = new Vector2(0, 0);
        isAlive = true;
        currentDirection = Direction.DOWN;

        bodyRectangle = new Rectangle(getX() + 20, getY() + 20, 30,45);

        horizontalAttackRectangle = new Rectangle(getX(), getY(), 17, 28);
        verticalAttackRectangle = new Rectangle(getX(), getY(), 32, 16);
        isHorizontalAttackActive = false;
        isVerticalAttackActive = false;
        isInvincible = false;
        invincibleTimer = 0;

        currentHungryPoint = GameStatsManager.getInstance().getCurrentMaxHungryPoint();
    }

    public void act(float dt) {
        super.act(dt);

        bodyRectangle.setPosition(getX() + 20, getY() + 20);

        if (isInvincible) {
            invincibleTimer += dt;
            if (invincibleTimer >= INVINCIBLE_DURATION) {
                isInvincible = false;
                invincibleTimer = 0;
                setOpacity(1.0f); // 恢复正常显示
            }
        }

        // Update Horizontal Attack Hitbox
        if (currentDirection == Direction.LEFT) {
            horizontalAttackRectangle.setPosition(getX(), getY() + getHeight()/2 - 14);
        } else if (currentDirection == Direction.RIGHT) {
            horizontalAttackRectangle.setPosition(getX() + getWidth(), getY() + getHeight()/2 - 14);
        }

        // Update Vertical Attack Hitbox
        if (currentDirection == Direction.UP) {
            verticalAttackRectangle.setPosition(getX() + getWidth()/2 - 16, getY() + getHeight());
        } else if (currentDirection == Direction.DOWN) {
            verticalAttackRectangle.setPosition(getX() + getWidth()/2 - 16, getY() - 16);
        }

        isAlive = currentHungryPoint > 0;

        if (!isAlive) {
            return;
        }

        if (isAttacking) {
            attackTimer += dt;
            if (attackTimer >= 0.6f) {
                isAttacking = false;
                isHorizontalAttackActive = false;
                isVerticalAttackActive = false;
                attackTimer = 0f;
                setMoveAnimation();
            }
            return;
        }

        // 重置速度向量
        velocity.set(0, 0);
        if (Gdx.input.isKeyPressed(Keys.A)) {
            velocity.x = -moveSpeed;
            currentDirection = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            velocity.x = moveSpeed;
            currentDirection = Direction.RIGHT;
        }

        if (Gdx.input.isKeyPressed(Keys.W)) {
            velocity.y = moveSpeed;
            currentDirection = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            velocity.y = -moveSpeed;
            currentDirection = Direction.DOWN;
        }

        if (Gdx.input.isKeyJustPressed(Keys.J)) {
            playAttackAnimation();
            MusicManager.getInstance().play("attack");
            return;
        }

        if (velocity.len() > 0) {
            velocity.nor().scl(moveSpeed);
            moveBy(velocity.x * dt, velocity.y * dt);
            setMoveAnimation();
        } else {
            stopAnimation();
        }

        moveBy(velocity.x * dt, velocity.y * dt);

        boundToWorld();
        alignCamera();
    }

    private void setMoveAnimation() {
        switch (currentDirection) {
            case UP:
                setAnimation(moveUpAnimation);
                break;
            case DOWN:
                setAnimation(moveDownAnimation);
                break;
            case LEFT:
                setAnimation(moveLeftAnimation);
                break;
            case RIGHT:
                setAnimation(moveRightAnimation);
                break;
        }
    }

    private void playAttackAnimation() {
        if (!isAttacking) {
            isAttacking = true;
            attackTimer = 0f;
            switch (currentDirection) {
                case UP:
                    setAnimation(attackUpAnimation);
                    isVerticalAttackActive = true;
                    break;
                case DOWN:
                    setAnimation(attackDownAnimation);
                    isVerticalAttackActive = true;
                    break;
                case LEFT:
                    setAnimation(attackLeftAnimation);
                    isHorizontalAttackActive = true;
                    break;
                case RIGHT:
                    setAnimation(attackRightAnimation);
                    isHorizontalAttackActive = true;
                    break;
            }
        }
    }

    private void stopAnimation() {
        switch (currentDirection) {
            case UP:
                setAnimation(new Animation<>(0.1f, moveUpAnimation.getKeyFrame(0)));
                break;
            case DOWN:
                setAnimation(new Animation<>(0.1f, moveDownAnimation.getKeyFrame(0)));
                break;
            case LEFT:
                setAnimation(new Animation<>(0.1f, moveLeftAnimation.getKeyFrame(0)));
                break;
            case RIGHT:
                setAnimation(new Animation<>(0.1f, moveRightAnimation.getKeyFrame(0)));
                break;
        }
    }

    public Rectangle getBodyRectangle(){
        return bodyRectangle;
    }

    public Rectangle getHorizontalAttackRectangle() {
        return isHorizontalAttackActive ? horizontalAttackRectangle : null;
    }

    public Rectangle getVerticalAttackRectangle() {
        return isVerticalAttackActive ? verticalAttackRectangle : null;
    }

    public void makeInvincible() {
        isInvincible = true;
        invincibleTimer = 0;
        setOpacity(0.5f); // Set Invincible Vision Effect
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public float getCurrentHungerPoint() {
        return currentHungryPoint;
    }

    public void setCurrentHungerPoint(float value) {
        currentHungryPoint = MathUtils.clamp(value, 0, GameStatsManager.getInstance().getCurrentMaxHungryPoint());
        // Detect Dead Status
        if (currentHungryPoint <= 0) {
            currentHungryPoint = 0;
            isAlive = false;
            moveSpeed = 0;
        }
    }

    public void damage() {
        if (!isInvincible) {
            currentHungryPoint--;

            if (currentHungryPoint <= 0) {
                currentHungryPoint = 0;
                isAlive = false;
                moveSpeed = 0;
            }
            makeInvincible();
        }
    }
}

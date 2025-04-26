package com.badlogic;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Enemy extends BaseActor {
    protected float moveSpeed; // 移动速度
    protected Vector2 velocity;
    protected float delayTimer = 0; // 生成延迟计时器
    protected float delayDuration = 1f; // 生成延迟时间（1s)
    protected boolean isDelayed = true; // 是否处于生成延迟状态
    protected float stopTimer = 0; // 重叠停滞计时器
    protected float stopDuration = 0.75f; // 重叠停滞时间（0.75秒）
    protected boolean isStopped = false; // 是否处于重叠停滞状态
    protected Turtle target; // 目标对象
    protected Stage gameStage; // 添加gameStage引用
    protected float health;
    protected Animation<TextureRegion> leftMoveAnimation;
    protected Animation<TextureRegion> rightMoveAnimation;
    protected boolean isFacingLeft = false; // 是否面向左

    public Enemy(float x, float y, Stage s) {
        super(x, y, s);
        this.gameStage = s;
        velocity = new Vector2(0, 0);
    }

    protected abstract void loadAnimations();
    protected abstract void setDefaultValues();

    public void setTarget(Turtle target) {
        this.target = target;
    }

    public void updateAI(Turtle target, float dt) {
        this.target = target;
        updateAI(dt);
    }

    public void updateAI(float dt) {
        if (target == null) {
            return;
        }

        // 检查是否与目标重叠
        if (this.getBoundaryPolygon().getBoundingRectangle().overlaps(target.getBodyRectangle())) {
            isStopped = true;
            stopTimer = 0;
        }

        // 如果生成延迟和重叠停滞都结束，开始移动
        if (!isDelayed && !isStopped) {
            moveTowardTarget(target, dt);
        }
    }

    public void act(float dt) {
        super.act(dt);

        // 处理生成延迟
        if (isDelayed) {
            delayTimer += dt;
            if (delayTimer >= delayDuration) {
                isDelayed = false;
                delayTimer = 0;
            }
            setAnimationPaused(true); // 延迟时暂停动画
        }

        // 处理重叠停滞
        if (isStopped) {
            stopTimer += dt;
            if (stopTimer >= stopDuration) {
                isStopped = false;
                stopTimer = 0;
            }
            setAnimationPaused(true); // 停滞时暂停动画
        }

        // 如果既不在延迟也不在停滞状态，则根据移动状态控制动画
        if (!isDelayed && !isStopped) {
            setAnimationPaused(velocity.len() == 0);
        }

        // 防止与其他敌人重叠
        ArrayList<BaseActor> enemies = BaseActor.getList(gameStage, "Enemy");
        for (BaseActor other : enemies) {
            if (other != this) { // 确保不是自己
                Vector2 overlapNormal = preventOverlap(other);
                if (overlapNormal != null) {
                    // 如果发生重叠，稍微调整速度方向以避免卡住
                    velocity.add(overlapNormal.scl(moveSpeed * 0.1f));
                }
            }
        }

        // 如果不在延迟状态且有目标，则更新AI
        if (!isDelayed && target != null) {
            updateAI(dt);
        }
    }

    public void moveTowardTarget(BaseActor target, float dt) {
        if (isDelayed || isStopped) {
            setAnimationPaused(true); // 确保在延迟或停滞时动画暂停
            return;
        }

        // 重置速度向量
        velocity.set(0, 0);

        // 计算到目标的方向
        float targetX = target.getX() + target.getWidth()/2;
        float targetY = target.getY() + target.getHeight()/2;

        float currentX = getX() + getWidth()/2;
        float currentY = getY() + getHeight()/2;

        // 根据目标位置切换动画
        if (targetX < currentX) {
            setAnimation(leftMoveAnimation); // 目标在左边，使用朝左的动画
            isFacingLeft = true;
        } else {
            setAnimation(rightMoveAnimation); // 目标在右边，使用朝右的动画
            isFacingLeft = false;
        }

        // 计算方向向量
        velocity.x = targetX - currentX;
        velocity.y = targetY - currentY;

        // 如果在移动（与目标的距离不为0）
        if (velocity.len() > 0) {
            // 标准化速度向量并设置速度
            velocity.nor().scl(moveSpeed);
        }

        // 直接移动
        moveBy(velocity.x * dt, velocity.y * dt);

        // 根据速度决定是否暂停动画
        setAnimationPaused(velocity.len() == 0);

        boundToWorld();
    }

    public void damage() {
        health--;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public boolean isFacingLeft() {
        return isFacingLeft;
    }

    public static int countEnemiesOfType(Stage stage, Class<? extends Enemy> enemyType) {
        int count = 0;
        for (BaseActor actor : BaseActor.getList(stage, enemyType.getName())) {
            if (enemyType.isInstance(actor)) {
                count++;
            }
        }
        return count;
    }
}

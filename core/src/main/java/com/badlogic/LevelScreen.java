package com.badlogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class LevelScreen extends BaseScreen {
    private Turtle turtle;
    private HUD hud;

    private boolean isPaused;
    private BaseActor pauseMenu;
    private boolean isGameOver;
    private float loseTimer;
    private static final float GAME_OVER_DELAY = 8.0f;

    private int corianderSpawnLimit; // EnemyCoriander summon limit
    private int porkSpawnLimit;      // EnemyProk summon limit
    private int beefSpawnLimit;     //EnemyPork summon limit
    private int meatballSpawnLimit; //EnemyMeatball summon limit

    private int corianderSpawnedCount; // EnemyCoriander summoned count
    private int porkSpawnedCount;      // EnemyPork summoned count
    private int beefSpawnedCount;   //EnemyBeef summoned count
    private int meatballSpawnedCount;   //EnemyMeatball summoned count

    public void initialize() {
        BaseActor map = new BaseActor(0, 0, gameStage);
        map.loadTexture("map.png");
        map.setSize(1000, 1000);
        BaseActor.setWorldBounds(map);

        turtle = new Turtle(500, 500, gameStage);

        hud = new HUD(uiStage);
        hud.updateHungerDisplay(turtle.getCurrentHungerPoint(), GameStatsManager.getInstance().getCurrentMaxHungryPoint());

        MusicManager.getInstance().play("normal");

        // Initialize each enemy's maximum summon amount and counter
        corianderSpawnLimit = MathUtils.random(72, 104);
        porkSpawnLimit = MathUtils.random(72, 104);
        beefSpawnLimit = MathUtils.random(24,32);
        meatballSpawnLimit = MathUtils.random(16,24);
        corianderSpawnedCount = 0;
        porkSpawnedCount = 0;
        beefSpawnedCount = 0;
        meatballSpawnedCount = 0;

        // Initialize Pause Status
        isPaused = false;
        isGameOver = false;
        loseTimer = 0;

        // Create Pause Menu
        pauseMenu = new BaseActor(0, 0, uiStage);
        pauseMenu.loadTexture("pauseMenu.png");
        pauseMenu.centerAtPosition(400, 320);
        pauseMenu.setVisible(false);
    }

    private void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);

        // Freeze the gameStage
        gameStage.getRoot().setVisible(!isPaused);

        if (isPaused) {
            MusicManager.getInstance().pause();
        } else {
            MusicManager.getInstance().resume();
        }
    }

    public void update(float dt) {
        if (isGameOver) {
            loseTimer += dt;
            if (loseTimer >= GAME_OVER_DELAY) {
                SouperNaturalGame.setActiveScreen(new StartScreen());
                return;
            }
            return;
        }

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            togglePause();
        }

        if (isPaused && Gdx.input.isKeyJustPressed(Keys.Q)) {
            togglePause();
            SouperNaturalGame.setActiveScreen(new StartScreen());
            return;
        }

        if (isPaused) {
            return;
        }

        if (!turtle.isAlive) {
            hud.updateHungerDisplay(turtle.getCurrentHungerPoint(), GameStatsManager.getInstance().getCurrentMaxHungryPoint());
            return;
        }

        hud.updateHungerDisplay(turtle.getCurrentHungerPoint(), GameStatsManager.getInstance().getCurrentMaxHungryPoint());

        float healthPercentage = (float)turtle.getCurrentHungerPoint() / GameStatsManager.getInstance().getCurrentMaxHungryPoint();
        if (healthPercentage <= 0.33f) {
            MusicManager.getInstance().play("rapid");
        } else {
            MusicManager.getInstance().play("normal");
        }

        for (BaseActor enemyActor : BaseActor.getList(gameStage, "Enemy")) {
            if (enemyActor instanceof Enemy) {
                Enemy enemy = (Enemy) enemyActor;
                enemy.updateAI(turtle, dt);
            }
        }

        for (BaseActor currencyActor : BaseActor.getList(gameStage, "Currency")) {
            if (turtle.overlaps(currencyActor)) {
                CurrencyManager.getInstance().collectCurrency(1);
                System.out.println("Current Balance: " + CurrencyManager.getInstance().getCurrentBalance());
                MusicManager.getInstance().play("coin");
                currencyActor.addAction(Actions.removeActor());
            }
        }

        if (Gdx.input.isKeyJustPressed(Keys.K) && turtle.getCurrentHungerPoint() > 0) {
            // Decide which enemy to summon randomly
            boolean spawnCoriander = MathUtils.randomBoolean();
            boolean spawnPork = MathUtils.randomBoolean();
            boolean spawnBeef = MathUtils.randomBoolean();
            boolean spawnMeatball = MathUtils.randomBoolean();

            // Check if the summoned amount reaches the limit
            boolean canSpawn = false;
            if (spawnCoriander && corianderSpawnedCount < corianderSpawnLimit) {
                canSpawn = true;
            } else if (!spawnCoriander && porkSpawnedCount < porkSpawnLimit) {
                canSpawn = true;
            }

            if (canSpawn) {
                int numToSpawn = MathUtils.random(2, 4);
                // 确保不超过剩余上限
                int remainingLimit = spawnCoriander ?
                    (corianderSpawnLimit - corianderSpawnedCount) :
                    (porkSpawnLimit - porkSpawnedCount);
                int actualSpawnCount = Math.min(numToSpawn, remainingLimit);

                for (int i = 0; i < actualSpawnCount; i++) {
                    Enemy enemy;
                    if (spawnCoriander) {
                        enemy = new EnemyCoriander(MathUtils.random(900), MathUtils.random(900), gameStage);
                        corianderSpawnedCount++;
                    } else {
                        enemy = new EnemyPork(MathUtils.random(900), MathUtils.random(900), gameStage);
                        porkSpawnedCount++;
                    }
                    enemy.setTarget(turtle);
                }

                MusicManager.getInstance().play("summon");
            }
        }

        boolean hasCollided = false;

        for (BaseActor enemyActor : BaseActor.getList(gameStage, "com.badlogic.Enemy")) {
            if (!(enemyActor instanceof Enemy)) {
                continue;
            }
            Enemy enemy = (Enemy) enemyActor;

            Rectangle horizontalAttack = turtle.getHorizontalAttackRectangle();
            Rectangle verticalAttack = turtle.getVerticalAttackRectangle();

            if ((horizontalAttack != null && enemy.getBoundaryPolygon().getBoundingRectangle().overlaps(horizontalAttack)) ||
                (verticalAttack != null && enemy.getBoundaryPolygon().getBoundingRectangle().overlaps(verticalAttack))) {
                MusicManager.getInstance().play("hit");


                enemy.damage();
                if (enemy.isDead()) {
                    enemyActor.addAction(Actions.after(Actions.removeActor()));

                    Dead dead = new Dead(0, 0, gameStage);
                    dead.centerAtActor(enemyActor);
                    dead.setDeadAnimation(enemy, enemy.isFacingLeft());

                    dead.addAction(Actions.sequence(
                        Actions.delay(0.5f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                int silverCount = MathUtils.random(0, 2);
                                for (int i = 0; i < silverCount; i++) {
                                    Currency silver = new Currency(0, 0, gameStage);
                                    silver.centerAtActor(enemyActor);
                                }
                            }
                        })
                    ));
                }
                continue;
            }

            if (!hasCollided && !turtle.isInvincible() &&
                enemy.getBoundaryPolygon().getBoundingRectangle().overlaps(turtle.getBodyRectangle())) {
                hasCollided = true;
                turtle.damage();
                MusicManager.getInstance().play("hurt");

                if (!turtle.isAlive) {
                    isGameOver = true;
                    MusicManager.getInstance().play("lose");
                    MusicManager.getInstance().play("slow");
                    BaseActor messageLose = new BaseActor(0, 0, uiStage);
                    messageLose.loadTexture("message-lose.png");
                    messageLose.setSize(408,172);
                    messageLose.centerAtPosition(400, 320);
                    messageLose.setColor(1, 1, 1, 0);

                    messageLose.addAction(Actions.sequence(
                        Actions.fadeIn(1),
                        Actions.delay(GAME_OVER_DELAY),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                SouperNaturalGame.setActiveScreen(new StartScreen());
                            }
                        })
                    ));

                    turtle.addAction(Actions.fadeOut(1));
                }
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

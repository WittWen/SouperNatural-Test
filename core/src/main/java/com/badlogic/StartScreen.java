package com.badlogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class StartScreen extends BaseScreen {
    private BaseActor startButton;
    private BaseActor powerButton;

    public void initialize() {
        BaseActor titleScreen = new BaseActor(0, 0, titleStage);
        titleScreen.loadAnimationFromSheet("title-screen.png", 4,4,0.1f,true);

        startButton = new BaseActor(0, 0, titleStage);
        startButton.loadTexture("start-button.png");
        startButton.centerAtPosition(400,320);
        startButton.moveBy(250,-275);

        powerButton = new BaseActor(0, 0, titleStage);
        powerButton.loadTexture("power-button.png");
        powerButton.centerAtPosition(400,320);
        powerButton.moveBy(-250,-275);

        MusicManager.getInstance().play("slow");
    }

    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Keys.S)){
            SouperNaturalGame.setActiveScreen(new LevelScreen());
            MusicManager.getInstance().play("click");
        }

        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            SouperNaturalGame.setActiveScreen(new UpgradeScreen());
            MusicManager.getInstance().play("click");
        }
    }
}

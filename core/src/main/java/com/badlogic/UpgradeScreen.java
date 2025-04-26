package com.badlogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class UpgradeScreen extends BaseScreen{
    @Override
    public void initialize() {
        BaseActor upgradeScreen = new BaseActor(0,0, upgradeStage);
        upgradeScreen.loadTexture("upgrade.png");
        upgradeScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
            MusicManager.getInstance().play("click");
            SouperNaturalGame.setActiveScreen(new StartScreen());
        }




    }
}

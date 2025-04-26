package com.badlogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class BaseScreen implements Screen {

    protected Stage titleStage;
    protected Stage upgradeStage;
    protected Stage uiStage;
    protected Stage gameStage;

    public BaseScreen() {
        titleStage = new Stage();
        gameStage = new Stage();
        uiStage = new Stage();
        upgradeStage = new Stage();
        initialize();
    }

    public abstract void initialize();

    public abstract void update(float dt);

    public void render(float dt) {
        titleStage.act(dt);
        gameStage.act(dt);
        uiStage.act(dt);
        upgradeStage.act(dt);
        update(dt);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        titleStage.draw();
        gameStage.draw();
        uiStage.draw();
        upgradeStage.draw();
    }
    //methods required by Screen interface
    public void resize(int width, int height) {}
    public void pause() {}
    public void resume() {}
    public void dispose() {}
    public void show() {}
    public void hide() {}
}

package com.badlogic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class SouperNaturalGame extends Game {
    private static SouperNaturalGame game;
    private MusicManager musicManager;

    public SouperNaturalGame() {
        game = this;
    }

    public static void setActiveScreen(BaseScreen s) {
        if (game.getScreen() != null) {
            game.getScreen().dispose();
        }
        game.setScreen(s);
    }

    @Override
    public void create() {
        musicManager = MusicManager.getInstance();
        setActiveScreen(new StartScreen());
    }

    @Override
    public void render() {
        super.render();
        if (musicManager != null) {
            musicManager.update(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (musicManager != null) {
            musicManager.dispose();
        }
    }
}

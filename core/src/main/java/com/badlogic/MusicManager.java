package com.badlogic;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private static MusicManager instance;
    private Map<String, Music> musicMap;
    private Music currentMusic;
    private float volume = 0.0f;
    private boolean isFadingIn = false;
    private static final float SLOW_FADE_SPEED = 0.33f; // 3秒淡入
    private static final float NORMAL_FADE_SPEED = 0.33f; // 3秒淡入
    private static final float RAPID_FADE_SPEED = 1.0f; // 1秒淡入
    private static final float TARGET_VOLUME = 1.0f;
    private String currentMusicKey;

    private MusicManager() {
        musicMap = new HashMap<>();
        loadMusic("slow", "backgroundMusic-slow.wav", true);
        loadMusic("normal", "backgroundMusic-normal.wav", true);
        loadMusic("rapid", "backgroundMusic-rapid.wav", true);
        loadMusic("coin", "getCoin.mp3", false);
        loadMusic("lose", "lose.mp3", false);
        loadMusic("win", "win.mp3", false);
        loadMusic("attack", "attack.mp3", false);
        loadMusic("hit", "enemy-hit.mp3", false);
        loadMusic("heal","heal.mp3", false);
        loadMusic("summon", "summon.mp3", false);
        loadMusic("hurt", "hurt.mp3", false);
        loadMusic("click", "click.mp3", false);
    }

    private void loadMusic(String key, String filePath, boolean isLooping) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
        music.setLooping(isLooping);
        musicMap.put(key, music);
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void play(String musicKey) {
        // 如果是音效，直接播放并返回
        if (musicKey.equals("coin") || musicKey.equals("lose") ||
            musicKey.equals("attack") || musicKey.equals("hit") ||
            musicKey.equals("heal") || musicKey.equals("summon") ||
            musicKey.equals("hurt") || musicKey.equals("click")) {
            Music sound = musicMap.get(musicKey);
            if (sound != null) {
                sound.stop();
                sound.play();
            }
            return;
        }

        // 如果当前正在播放相同的音乐，不进行切换
        if (currentMusicKey != null && currentMusicKey.equals(musicKey)) {
            return;
        }

        // 对于背景音乐，确保完全停止当前音乐
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }

        // 开始播放新音乐
        currentMusic = musicMap.get(musicKey);
        currentMusicKey = musicKey;
        if (currentMusic != null) {
            volume = 0.0f;
            currentMusic.setVolume(0.0f);
            currentMusic.play();
            isFadingIn = true;
        }
    }

    public void update(float delta) {
        if (isFadingIn && currentMusic != null) {
            float fadeSpeed = getFadeSpeed(currentMusicKey);
            volume = Math.min(volume + fadeSpeed * delta, TARGET_VOLUME);
            currentMusic.setVolume(volume);

            if (volume >= TARGET_VOLUME) {
                isFadingIn = false;
            }
        }
    }

    private float getFadeSpeed(String musicKey) {
        switch (musicKey) {
            case "slow":
                return SLOW_FADE_SPEED;
            case "normal":
                return NORMAL_FADE_SPEED;
            case "rapid":
                return RAPID_FADE_SPEED;
            default:
                return NORMAL_FADE_SPEED;
        }
    }

    public void dispose() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
        for (Music music : musicMap.values()) {
            if (music != null) {
                music.dispose();
            }
        }
        musicMap.clear();
    }

    public void pause() {
        if (currentMusic != null) {
            currentMusic.pause();
        }
    }

    public void resume() {
        if (currentMusic != null) {
            currentMusic.play();
        }
    }

    public boolean isPlaying(String musicKey) {
        if (currentMusicKey == null) return false;
        return currentMusicKey.equals(musicKey);
    }
}

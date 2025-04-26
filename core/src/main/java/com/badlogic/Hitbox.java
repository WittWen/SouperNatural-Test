package com.badlogic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Hitbox {
    private Rectangle bounds;
    private Vector2 offset;  // 相对于角色中心点的偏移
    
    public Hitbox() {
        bounds = new Rectangle();
        offset = new Vector2();
    }
    
    public void update(float centerX, float centerY, float width, float height) {
        bounds.setPosition(centerX + offset.x, centerY + offset.y);
        bounds.setSize(width, height);
    }
    
    public void setOffset(float x, float y) {
        offset.set(x, y);
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public boolean overlaps(Hitbox other) {
        return bounds.overlaps(other.getBounds());
    }
}
package com.github.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private boolean active;
    private Rectangle bounds;

    public Bullet(float x, float y, Vector2 direction) {
        this.texture = new Texture("bullet.png");
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(direction).nor().scl(300); // tốc độ đạn
        this.active = true;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void update(float deltaTime) {
        if (!active) return;
        position.mulAdd(velocity, deltaTime);
        bounds.setPosition(position);
    }

    public void render(SpriteBatch batch) {
        if (active)
            batch.draw(texture, position.x, position.y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public void dispose() {
        texture.dispose();
    }
}

package com.github.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.game.entity.Wall;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private List<Wall> walls;
    private Texture wallTexture;

    public GameMap() {
        walls = new ArrayList<>();
        wallTexture = new Texture("wall.jpg");

        // Tạo tường bao quanh màn hình (640x480)
        for (int i = 0; i < 10; i++) {
            walls.add(new Wall(i * 64, 0, wallTexture)); // hàng dưới
            walls.add(new Wall(i * 64, 416, wallTexture)); // hàng trên
        }
        for (int j = 1; j < 7; j++) {
            walls.add(new Wall(0, j * 64, wallTexture)); // trái
            walls.add(new Wall(576, j * 64, wallTexture)); // phải
        }
    }

    public void render(SpriteBatch batch) {
        for (Wall wall : walls) wall.render(batch);
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public void dispose() {
        wallTexture.dispose();
    }
}

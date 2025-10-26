package com.github.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.game.entity.Player;
/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private FitViewport viewport;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;
    private Player player;
    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        viewport = new FitViewport(1280, 720);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        player = new Player(20, 100, 100);
    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
    }
    @Override
    public void render() {
        player.update(viewport);
        draw();
    }
    private void logic(){

    }
    private void draw(){
        ScreenUtils.clear(Color.WHITE);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        
        batch.end();
        player.draw(shapeRenderer);
    }
    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}

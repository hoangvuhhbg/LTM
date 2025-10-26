package com.github.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
public class Player{
    private float radius;
    Sprite sprite = new Sprite();
    private Circle hitbox;

    private float speed;
    

    public Player(float radius, float x, float y) {
        this.radius = radius;
        sprite.setCenter(x, y);
        this.hitbox = new Circle(x, y, radius);
        this.speed = 100f;
    }

    public void update(Viewport viewport){
        float dt = Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Keys.A)){
            sprite.translateX(-dt * speed);
        }
        if(Gdx.input.isKeyPressed(Keys.D)){
            sprite.translateX(dt * speed);
        }
        if(Gdx.input.isKeyPressed(Keys.W)){
            sprite.translateY(dt * speed);
        }
        if(Gdx.input.isKeyPressed(Keys.S)){
            sprite.translateY(-dt * speed);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            System.out.println("a");
            
        }        
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, viewport.getWorldWidth() - sprite.getWidth()));
        sprite.setY(MathUtils.clamp(sprite.getY(), 0, viewport.getWorldHeight() - sprite.getHeight()));
    }

    public void draw(ShapeRenderer shapeRenderer){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(sprite.getX(), sprite.getY(), radius);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.circle(sprite.getX(), sprite.getY(), radius - 2);
        shapeRenderer.end();
    }
    
}

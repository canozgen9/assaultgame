package com.canozgen.assault.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.canozgen.assault.screens.PlayScreen;

/**
 * Created by Can Özgen on 8.09.2016.
 */
public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    public Enemy(PlayScreen playScreen,float x,float y){
        this.world = playScreen.getWorld();
        this.screen = playScreen;
        setPosition(x,y);
        defineEnemy();
    }

    protected abstract void defineEnemy();
    public abstract void onHit(float damage);

}

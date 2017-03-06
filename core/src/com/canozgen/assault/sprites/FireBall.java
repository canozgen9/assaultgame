package com.canozgen.assault.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.canozgen.assault.screens.PlayScreen;
import com.canozgen.assault.tools.GameVariables;

import java.io.IOException;

/**
 * Created by Can Özgen on 9.09.2016.
 */
public class FireBall {

    World world;
    public Body b2body;

    ParticleEmitter particleEmitter ;
    Fixture fix;

    Vector2 velocity;
    private PlayScreen screen;
    public FireBall(PlayScreen screen,Vector2 v){

        this.world = screen.getWorld();
        this.velocity = v;
        this.screen = screen;
        Player player = screen.getPlayer();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale=(0.1f);
        if(player.runningRight)
        bodyDef.position.set(player.b2body.getPosition().x+30/GameVariables.PPM,player.b2body.getPosition().y);
        if(!player.runningRight)
            bodyDef.position.set(player.b2body.getPosition().x-30/GameVariables.PPM,player.b2body.getPosition().y);
        CircleShape c = new CircleShape();
        c.setRadius(20/ GameVariables.PPM);

        FixtureDef f  = new FixtureDef();
        f.filter.categoryBits = GameVariables.BIT_FIREBALL;
        f.filter.maskBits = GameVariables.BIT_GROUND | GameVariables.BIT_BOX | GameVariables.BIT_ENEMY ;
        f.restitution = 0.3f;
        f.shape = c;

        b2body = world.createBody(bodyDef);
        fix = b2body.createFixture(f);
        fix.setUserData(this);



        particleEmitter = new ParticleEmitter();
        try {
            particleEmitter.load(Gdx.files.internal("fireball.txt").reader(2024));
        } catch (IOException e) {
            e.printStackTrace();
        }

        particleEmitter.getScale().setHigh(0.5f);
        particleEmitter.getVelocity().setHigh(0.5f);


        Texture eye_fire = new Texture("eyes.png");
        Sprite eye_sprite = new Sprite(eye_fire);

        particleEmitter.setSprite(eye_sprite);


        particleEmitter.start();


        b2body.applyLinearImpulse(velocity,b2body.getWorldCenter(),true);

        destroyed = false;
        setToDestroy = false;
        isHitted = false;

        deleteTime = 0;



    }
    private float deleteTime;
private boolean right ;
    public boolean isHitted;
    public boolean destroyed;
    private boolean setToDestroy;
    public float damage = 300;
    public void update(float dt){

        particleEmitter.setPosition(b2body.getWorldCenter().x,b2body.getWorldCenter().y);

    }

    public void draw(SpriteBatch spriteBatch,float dt){
        if(!destroyed) {
            update(dt);
            particleEmitter.draw(spriteBatch, dt);
        } if(setToDestroy && !destroyed){
            deleteTime += dt;
            Gdx.app.log("VALUS","DELETETIME : "+deleteTime+" DESTROYED : "+destroyed);
            if(deleteTime>=0.2f) {
                world.destroyBody(b2body);
                destroyed = true;
            }
        }
    }

    public void onHit(){

        setCategoryFilter(GameVariables.BIT_DESTROYED);
        setToDestroy = true;
        isHitted = true;


    }
    protected void setCategoryFilter(short bit) {
        Filter filter = new Filter();
        filter.categoryBits = bit;
        filter.maskBits = 0;



        fix.setFilterData(filter);
    }
}

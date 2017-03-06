package com.canozgen.assault.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.canozgen.assault.screens.PlayScreen;
import com.canozgen.assault.tools.GameVariables;

/**
 * Created by Can Özgen on 8.09.2016.
 */
public class Zombie extends Enemy {

    private Animation animation;
    private float stateTime;
    int index= 0;
    TextureRegion[] textureRegions;
    public Boolean facingRight;

    private float health = 1000;
    private float maxHealth = 1000;

    public Boolean setToDestroy;
    public Boolean destroyed;

    private boolean firstsstate;

    public ShapeRenderer renderer;

    public Zombie(PlayScreen playScreen, float x, float y, TextureRegion[] textureRegions,boolean rotation) {
        super(playScreen, x, y);


        firstsstate = rotation;
        this.textureRegions = textureRegions;

        animation = new Animation(0.2f,textureRegions);

        setToDestroy = false;
        destroyed = false;


        setBounds(getX(),getY(),50f/GameVariables.PPM,100f/GameVariables.PPM);

        stateTime = 0;
        facingRight = true;
        setRegion(textureRegions[3]);
        this.facingRight = rotation;
        if(!facingRight){
            v = -v;
            flip(true, false);
        }

        renderer = new ShapeRenderer();
    }

    TextureRegion region;

    public void update(float dt){
        stateTime+=dt;

        if(setToDestroy&&!destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }

        if(!destroyed) {
             region = animation.getKeyFrame(stateTime, true);
            if(b2body.getLinearVelocity().x>0){
                facingRight = true;
                }
            else if (b2body.getLinearVelocity().x<0){
                facingRight = false;
            }

            setRegion(region);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            move();


        }

    }


    public void drawHealth(){
        renderer.setProjectionMatrix(screen.gameCamera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.5f,0,0,1);
        renderer.rect(b2body.getPosition().x-30/GameVariables.PPM,b2body.getPosition().y+60/GameVariables.PPM,
                (60f)/GameVariables.PPM,10/GameVariables.PPM
        );
        renderer.setColor(1,0,0,1);
        renderer.rect(b2body.getPosition().x-30/GameVariables.PPM,b2body.getPosition().y+60/GameVariables.PPM,
                ((health/maxHealth)*60f)/GameVariables.PPM,10/GameVariables.PPM
        );

    }


    private float v= 0.7f;

    private void move() {
        b2body.setAwake(true);
        b2body.setLinearVelocity(v,0);
    }

    public void reverse(){
        v = -v;
        if(facingRight && !region.isFlipX()) {
            region.flip(true, false);
        }else if(!facingRight && region.isFlipX()){
            region.flip(true,false);
        }
    }

    @Override
    protected void defineEnemy() {

        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;

        b2body = world.createBody(bdef);

        FixtureDef fixtureDef = new FixtureDef();
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(20f/ GameVariables.PPM,40f/GameVariables.PPM);

        CircleShape shape = new CircleShape();
        shape.setRadius(40f/GameVariables.PPM);

        fixtureDef.shape = shape;
        fixtureDef.density = 100;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.1f;
        fixtureDef.filter.categoryBits = GameVariables.BIT_ENEMY;
        fixtureDef.filter.maskBits = GameVariables.BIT_PLAYER | GameVariables.BIT_BOX | GameVariables.BIT_GROUND | GameVariables.BIT_BULLET | GameVariables.BIT_ENEMY|GameVariables.BIT_FIREBALL;
        Fixture f = b2body.createFixture(fixtureDef);
        f.setUserData(this);


          PolygonShape radar = new PolygonShape();
        radar.setAsBox(160/GameVariables.PPM,40/GameVariables.PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = radar;
        fdef.filter.categoryBits = GameVariables.BIT_ENEMY;
        fdef.filter.maskBits = GameVariables.BIT_PLAYER;
        fdef.isSensor = true;

        b2body.createFixture(fdef);

        shape.dispose();

    }

    @Override
    public void onHit(float damage) {
        health -= damage;

        if(health<=0){
            setToDestroy = true;
        }


    }
}

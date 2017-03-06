package com.canozgen.assault.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.canozgen.assault.screens.PlayScreen;
import com.canozgen.assault.tools.GameVariables;

import static com.canozgen.assault.tools.GameVariables.*;

/**
 * Created by Can Özgen on 7.09.2016.
 */
public class Player extends Sprite{

    public enum State{FALLING,FIRING,RUNNING,JUMPING,STANDING}

    public State currentState;
    public State previousState;


    private Animation playerStand;
    private Animation playerRunning;


    private World world;
    public Body b2body;

    private float Health = 1000;
    private float MaxHealth = 1000;

    public boolean jumping;


    public float angle;

    public boolean runningRight;


    private Array<TextureRegion> frames;
    private Array<FireBall> fireballs;
    private TextureRegion jumpFrame;
    private TextureRegion fallingFrame;
    private TextureRegion firingFrame;

    private float stateTimer;



    public Player(PlayScreen screen){
        this.world = screen.getWorld();
        definePlayer();
        stateTimer = 0;
        angle = 0;
        frames = new Array<TextureRegion>();
        for(int i=0;i<4;i++){
            frames.add(new TextureRegion(screen.getTextureAtlas().findRegion("d"+i),0,0,128,256));
        }
        playerStand = new Animation(0.4f,frames);

        frames.clear();

        for(int i=0;i<5;i++){
            frames.add(new TextureRegion(screen.getTextureAtlas().findRegion("w"+i),0,0,128,256));
        }

        playerRunning = new Animation(0.1f,frames);

        jumpFrame = new TextureRegion(screen.getTextureAtlas().findRegion("jump"),0,0,128,256);
        fallingFrame = new TextureRegion(screen.getTextureAtlas().findRegion("falling"),0,0,128,256);
        firingFrame = new TextureRegion(screen.getTextureAtlas().findRegion("fire"),0,0,128,256);



        setBounds(0,0,40/PPM,80/PPM);
        setRegion(playerStand.getKeyFrame(stateTimer,true));

        jumping = false;

    }

    public boolean firing;
    float fireTimer = 0;
    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        Gdx.app.log("POSITIOn","X "+b2body.getPosition().x+" Y "+b2body.getPosition().y);
        if (firing && fireTimer < 0.1f){
            fireTimer += dt;
            TextureRegion region = firingFrame;
            if((b2body.getLinearVelocity().x<0||!runningRight)&&!region.isFlipX()){
                region.flip(true,false);
                runningRight = false;
            } else if((b2body.getLinearVelocity().x>0|| runningRight)&&region.isFlipX())
            {
                region.flip(true,false);
                runningRight = true;
            }
            setRegion(region);
            if (fireTimer>=0.1f){
                fireTimer = 0;
                firing = false;
            }
    } else
        setRegion(getFrame(dt));
    }

    private TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;

        switch (currentState){

            case JUMPING:
               region = jumpFrame;
                previousState = State.JUMPING;
                break;
            case RUNNING:
                region = playerRunning.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
                if(previousState ==State.JUMPING) {
                    region = fallingFrame;
                    previousState = null;
                }else{
                    region = playerRunning.getKeyFrame(stateTimer,true);
                }
                break;
            case FIRING:
                region = firingFrame;
                break;
            case STANDING:
                default:
                    region = playerStand.getKeyFrame(stateTimer,true);
                    break;


        }

        if((b2body.getLinearVelocity().x<0||!runningRight)&&!region.isFlipX()){
            region.flip(true,false);
            runningRight = false;
        } else if((b2body.getLinearVelocity().x>0|| runningRight)&&region.isFlipX())
        {
            region.flip(true,false);
            runningRight = true;
        }

        stateTimer = currentState == previousState? stateTimer+dt : 0;
        previousState = currentState;
        return region;
    }

    private State getState(){
        if(b2body.getLinearVelocity().y>0 || b2body.getLinearVelocity().y <0&&jumping) return State.JUMPING;
        else if(b2body.getLinearVelocity().y<0f) return State.FALLING;
        else if(b2body.getLinearVelocity().x!=0) return State.RUNNING;
        else return State.STANDING;
    }

    private void definePlayer(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(50/PPM,300/PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        b2body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(10/PPM);
        circleShape.setPosition(new Vector2(0,-20/GameVariables.PPM));

        PolygonShape polygonShape = new PolygonShape();
        Vector2[] vertices = new Vector2[6];
        vertices[0] = new Vector2(-10/GameVariables.PPM,30/GameVariables.PPM);
        vertices[1] = new Vector2(10/GameVariables.PPM,30/GameVariables.PPM);
        vertices[2] = new Vector2(4/GameVariables.PPM,-20/GameVariables.PPM);
        vertices[3] = new Vector2(-4/GameVariables.PPM,-20/GameVariables.PPM);
        vertices[4] = new Vector2(6/GameVariables.PPM,-15/GameVariables.PPM);
        vertices[5] = new Vector2(-6/GameVariables.PPM,-15/GameVariables.PPM);
        polygonShape.set(vertices);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = polygonShape;
        fdef.friction=10f;
        fdef.filter.categoryBits = GameVariables.BIT_PLAYER;
        fdef.filter.maskBits = GameVariables.BIT_BOX|GameVariables.BIT_GROUND|GameVariables.BIT_ENEMY|GameVariables.BIT_WATER;

        FixtureDef fdef2 = new FixtureDef();
        fdef2.shape = circleShape;
        fdef2.filter.categoryBits = GameVariables.BIT_PLAYER;
        fdef2.filter.maskBits = GameVariables.BIT_BOX|GameVariables.BIT_GROUND|GameVariables.BIT_ENEMY|GameVariables.BIT_WATER;

        b2body.createFixture(fdef).setUserData(this);
        b2body.createFixture(fdef2).setUserData(this);


        runningRight = true;
    }
}

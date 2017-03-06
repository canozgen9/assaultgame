package com.canozgen.assault.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.canozgen.assault.Game;
import com.canozgen.assault.screens.PlayScreen;
import com.canozgen.assault.tools.GameVariables;

import java.io.IOException;

/**
 * Created by Can Özgen on 7.09.2016.
 */
public class Bullet extends Sprite{
    private boolean setToDestroy;
    protected World world;
    protected Body b2body;
    protected PlayScreen screen;

    public float damage = 50;

    private TextureRegion bullet;


    Fixture fix;

    public boolean isHitted = false;
    public boolean destroyed;

    public Bullet(PlayScreen screen,Vector2 v){
        this.world = screen.getWorld();
        this.screen = screen;
        defineBullet(v);

        bullet = new TextureRegion(screen.getTextureAtlas().findRegion("bullet"),0,0,20,5);
        setBounds(getX(),getY(),4/GameVariables.PPM,2/GameVariables.PPM);
        setRegion(bullet);
        setOrigin(getWidth()/2,getHeight()/2);
        setOriginCenter();
        rotate(screen.getPlayer().angle);
        isHitted = false;
        setToDestroy = false;
        destroyed = false;

        sound = Game.assetManager.get("Sounds/bullet.mp3",Sound.class);
        sound.play();


    }

    Sound sound;


    public void update(float dt){

        if(!destroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            particleEmitter.setPosition(b2body.getWorldCenter().x,b2body.getWorldCenter().y);
        }
        if(setToDestroy&&!destroyed){
            world.destroyBody(b2body);
            destroyed = true;
        }

    }

    public ParticleEmitter particleEmitter;

    private void defineBullet(Vector2 v){
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.position.set(new Vector2(screen.getPlayer().b2body.getPosition().x, screen.getPlayer().b2body.getPosition().y + 1.5f / GameVariables.PPM));
            b2body = world.createBody(bdef);

            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(2 / GameVariables.PPM, 1 / GameVariables.PPM);

            FixtureDef fdef = new FixtureDef();
            fdef.shape = polygonShape;
            fdef.filter.categoryBits = GameVariables.BIT_BULLET;
            fdef.filter.maskBits = GameVariables.BIT_GROUND | GameVariables.BIT_BOX | GameVariables.BIT_ENEMY;

            fix = b2body.createFixture(fdef);

            fix.setUserData(this);

            b2body.applyLinearImpulse(v, b2body.getWorldCenter(), true);


        //EMITTER


        particleEmitter = new ParticleEmitter();
        try {
            particleEmitter.load(Gdx.files.internal("fireball.txt").reader(2024));
        } catch (IOException e) {
            e.printStackTrace();
        }

        particleEmitter.getScale().setHigh(0.1f);
        particleEmitter.getVelocity().setHigh(0.1f);


        Texture eye_fire = new Texture("eyes.png");
        Sprite eye_sprite = new Sprite(eye_fire);

        particleEmitter.setSprite(eye_sprite);


        particleEmitter.start();





    }

    public void onHit(){
        isHitted = true;
        setCategoryFilter(GameVariables.BIT_DESTROYED);
        setToDestroy = true;

    }
    protected void setCategoryFilter(short bit) {
        Filter filter = new Filter();
        filter.categoryBits = bit;
        filter.maskBits = 0;
        fix.setFilterData(filter);
    }


}

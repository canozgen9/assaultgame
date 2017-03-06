package com.canozgen.assault.tools;

import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.canozgen.assault.sprites.Box;
import com.canozgen.assault.sprites.Bullet;
import com.canozgen.assault.sprites.Enemy;
import com.canozgen.assault.sprites.FireBall;
import com.canozgen.assault.sprites.Player;
import com.canozgen.assault.sprites.Zombie;

/**
 * Created by Can Özgen on 7.09.2016.
 */
public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        int bit = fA.getFilterData().categoryBits | fB.getFilterData().categoryBits;

        switch (bit){
            case GameVariables.BIT_BULLET | GameVariables.BIT_BOX:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_BOX){
                    if(!((Bullet)fB.getUserData()).isHitted) {
                        ((Box) fA.getUserData()).onHit(((Bullet)fB.getUserData()).damage);
                        ((Bullet) fB.getUserData()).isHitted = true;
                    }
                }else{
                    if(!((Bullet)fA.getUserData()).isHitted) {
                        ((Box) fB.getUserData()).onHit(((Bullet)fA.getUserData()).damage);
                        ((Bullet) fA.getUserData()).isHitted = true;
                    }
                }
                break;
            case GameVariables.BIT_FIREBALL | GameVariables.BIT_BOX:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_BOX){
                        ((Box) fA.getUserData()).onHit(((FireBall)fB.getUserData()).damage);
                        ((FireBall) fB.getUserData()).isHitted = true;

                }else{
                        ((Box) fB.getUserData()).onHit(((FireBall)fA.getUserData()).damage);
                        ((FireBall) fA.getUserData()).isHitted = true;

                }
                break;
            case GameVariables.BIT_BULLET | GameVariables.BIT_ENEMY:

                if(fA.getFilterData().categoryBits==GameVariables.BIT_ENEMY){
                    if(!((Bullet)fB.getUserData()).isHitted) {
                        ((Enemy) fA.getUserData()).onHit(((Bullet)fB.getUserData()).damage);
                        ((Bullet) fB.getUserData()).onHit();
                    }
                }else{
                    if(!((Bullet)fA.getUserData()).isHitted) {
                        ((Enemy) fB.getUserData()).onHit(((Bullet)fA.getUserData()).damage);
                        ((Bullet) fA.getUserData()).onHit();
                    }
                }
                break;
            case GameVariables.BIT_FIREBALL | GameVariables.BIT_ENEMY:

                if(fA.getFilterData().categoryBits==GameVariables.BIT_ENEMY){
                    if(!((FireBall)fB.getUserData()).isHitted) {
                        ((Enemy) fA.getUserData()).onHit(((FireBall)fB.getUserData()).damage);
                        ((FireBall) fB.getUserData()).onHit();
                    }
                }else{
                    if(!((FireBall)fA.getUserData()).isHitted) {
                        ((Enemy) fB.getUserData()).onHit(((FireBall)fA.getUserData()).damage);
                        ((FireBall) fA.getUserData()).onHit();
                    }
                }
                break;
            case GameVariables.BIT_BOX | GameVariables.BIT_ENEMY:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_ENEMY){
                    ((Zombie)fA.getUserData()).reverse();
                }
                if(fB.getFilterData().categoryBits==GameVariables.BIT_ENEMY){
                    ((Zombie)fB.getUserData()).reverse();
                }
                break;
            case GameVariables.BIT_ENEMY | GameVariables.BIT_ENEMY:
                    ((Zombie)fA.getUserData()).reverse();
                    ((Zombie)fB.getUserData()).reverse();

                break;

            case GameVariables.BIT_WATER | GameVariables.BIT_PLAYER:

                if(fA.getFilterData().categoryBits==GameVariables.BIT_PLAYER){
                    ((Player)fA.getUserData()).b2body.setGravityScale(-.25f);
                }else{
                    ((Player)fB.getUserData()).b2body.setGravityScale(-.25f);
                }

                break;


        }

        if(fA.getFilterData().categoryBits==GameVariables.BIT_BULLET){
                ((Bullet) fA.getUserData()).onHit();
        }else if(fB.getFilterData().categoryBits == GameVariables.BIT_BULLET){
                ((Bullet) fB.getUserData()).onHit();
        }
        if(fA.getFilterData().categoryBits==GameVariables.BIT_FIREBALL){
                ((FireBall) fA.getUserData()).onHit();

        }else if(fB.getFilterData().categoryBits == GameVariables.BIT_FIREBALL){
                ((FireBall) fB.getUserData()).onHit();
        }

    }


    @Override
    public void endContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        int bit = fA.getFilterData().categoryBits | fB.getFilterData().categoryBits;

        if(bit ==  (GameVariables.BIT_WATER | GameVariables.BIT_PLAYER)) {

            if (fA.getFilterData().categoryBits == GameVariables.BIT_PLAYER) {
                ((Player) fA.getUserData()).b2body.setGravityScale(1);
            } else {
                ((Player) fB.getUserData()).b2body.setGravityScale(1);
            }
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}

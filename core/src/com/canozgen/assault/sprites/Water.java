package com.canozgen.assault.sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.canozgen.assault.screens.PlayScreen;
import com.canozgen.assault.tools.GameVariables;

/**
 * Created by Can Özgen on 9.09.2016.
 */
public class Water {

    World world;
    Body b2body;

    public Water(PlayScreen screen, Rectangle rectangle){
        this.world = screen.getWorld();

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/ GameVariables.PPM,(rectangle.getY()+rectangle.getHeight()/2)/ GameVariables.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
         b2body = world.createBody(bodyDef);
b2body.setGravityScale(0);
        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rectangle.getWidth()/2/GameVariables.PPM,rectangle.getHeight()/2/GameVariables.PPM);

        fdef.shape = shape;
        fdef.density = 1f;
        fdef.filter.categoryBits = GameVariables.BIT_WATER;
        fdef.filter.maskBits = GameVariables.BIT_GROUND | GameVariables.BIT_BOX | GameVariables.BIT_PLAYER;
        fdef.isSensor = true;
        b2body.createFixture(fdef);
    }
}

package com.canozgen.assault.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.canozgen.assault.screens.PlayScreen;
import com.canozgen.assault.sprites.Box;
import com.canozgen.assault.sprites.Water;

import  static com.canozgen.assault.tools.GameVariables.*;

/**
 * Created by Can Özgen on 7.09.2016.
 */
public class WorldInitializer {

    private World world;
    private TiledMap tiledMap;

    public WorldInitializer(PlayScreen screen){
        this.world = screen.getWorld();
        this.tiledMap = screen.getTiledMap();

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //Ground
        for(MapObject mapObject : tiledMap.getLayers().get(2).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/PPM,(rectangle.getY()+rectangle.getHeight()/2)/PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rectangle.getWidth()/2/PPM,rectangle.getHeight()/2/PPM);

            fixtureDef.shape = shape;

            fixtureDef.filter.categoryBits = GameVariables.BIT_GROUND;
            fixtureDef.filter.maskBits = GameVariables.BIT_PLAYER | GameVariables.BIT_WATER | GameVariables.BIT_BOX | GameVariables.BIT_ENEMY | GameVariables.BIT_BULLET|GameVariables.BIT_FIREBALL;
            body.createFixture(fixtureDef);


        }

        //Boxes
        for(MapObject mapObject : tiledMap.getLayers().get(3).getObjects()) {

            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();

            new Box(screen,rectangle);

        }
        for(MapObject mapObject : tiledMap.getLayers().get(4).getObjects()) {

            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();

            new Water(screen,rectangle);

        }

    }

}

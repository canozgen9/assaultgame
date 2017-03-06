package com.canozgen.assault.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.canozgen.assault.screens.PlayScreen;
import com.canozgen.assault.tools.GameVariables;

/**
 * Created by Can Özgen on 7.09.2016.
 */
public class Box {

    private World world;
    private TiledMap tiledMap;
    private Body b2body;
    private Rectangle objectRectangle;

    private float Health=150;

    protected Fixture fixture;

    public Box(PlayScreen screen, Rectangle objectRectangle) {

        this.world = screen.getWorld();
        this.objectRectangle = objectRectangle;
        this.tiledMap = screen.getTiledMap();

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((objectRectangle.getX()+objectRectangle.getWidth()/2)/GameVariables.PPM,(objectRectangle.getHeight()/2+objectRectangle.getY())/GameVariables.PPM);
        b2body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(objectRectangle.getWidth()/2/GameVariables.PPM,objectRectangle.getHeight()/2/GameVariables.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameVariables.BIT_BOX;
        fixtureDef.filter.maskBits = GameVariables.BIT_BULLET|GameVariables.BIT_PLAYER|GameVariables.BIT_ENEMY|GameVariables.BIT_WATER|GameVariables.BIT_FIREBALL;

        fixture = b2body.createFixture(fixtureDef);

        fixture.setUserData(this);



    }


    protected void setCategoryFilter(short bit) {
        Filter filter = new Filter();
        filter.categoryBits = bit;
        fixture.setFilterData(filter);
    }


    public void onHit(float damage){
        Health-=damage;
        if(Health<=0){
            setCategoryFilter(GameVariables.BIT_DESTROYED);
            getCell().setTile(null);
        }

    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(1);
        return layer.getCell((int)(b2body.getPosition().x*GameVariables.PPM/32),(int)(b2body.getPosition().y*GameVariables.PPM/32));
    }


}

package com.canozgen.assault.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.canozgen.assault.Game;
import com.canozgen.assault.sprites.Bullet;
import com.canozgen.assault.sprites.FireBall;
import com.canozgen.assault.sprites.Player;
import com.canozgen.assault.sprites.Zombie;
import com.canozgen.assault.tools.GameVariables;
import com.canozgen.assault.tools.WorldContactListener;
import com.canozgen.assault.tools.WorldInitializer;

import java.io.IOException;
import java.util.ArrayList;

import static com.canozgen.assault.Game.*;
import static com.canozgen.assault.tools.GameVariables.*;

/**
 * Created by Can Özgen on 7.09.2016.
 */
public class PlayScreen implements Screen {


    //Constructer
    private Game game;
    public OrthographicCamera gameCamera;
    private Viewport viewport;

    //TiledMap
    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    //World
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;


    //PLAYER
    protected Player player;

    Sprite gun;

    ShapeRenderer sRenderer;
   Array<Rectangle> buttons;


    //TEXTUREATLAS
    private TextureAtlas textureAtlas;
    private TextureRegion[] zombie1;
    private TextureRegion[] zombie2;
    private TextureRegion[] zombie3;
    private TextureRegion[] zombie4;
    private Texture zombies;
    TextureRegion[][] tmpFrames;

    Array<Zombie> zombieArray;


    //BULELT LIST
    Array<Bullet> bulletArray;
    Array<FireBall> fireBalls;

    ParticleEmitter exhaust;
    ParticleEmitter exhaust_right_eye;
    ParticleEmitter exhaust_left_eye;



    public PlayScreen(Game game){
        this.game = game;

        bulletArray = new Array<Bullet>();
        fireBalls = new Array<FireBall>();
        //TEXTURE ATLAS
        textureAtlas = new TextureAtlas("Sprites/player3.pack");

        zombies = new Texture(Gdx.files.internal("Sprites/zombies.png"));

       tmpFrames = TextureRegion.split(zombies,128,128);

        zombie1 = new TextureRegion[4];

        for(int i=0;i<4;i++){
            zombie1[i] = tmpFrames[0][i];
        }

        zombie2 = new TextureRegion[4];

        for(int i=0;i<4;i++){
            zombie2[i] = tmpFrames[1][i];
        }

        zombie3 = new TextureRegion[4];

        for(int i=0;i<4;i++){
            zombie3[i] = tmpFrames[2][i];
        }

        zombie4 = new TextureRegion[4];

        for(int i=0;i<4;i++){
            zombie4[i] = tmpFrames[3][i];
        }

        sRenderer = new ShapeRenderer();

        buttons = new Array<Rectangle>();

        buttons.add(new Rectangle(700,10,80,80));
        buttons.add(new Rectangle(10,10,80,80));
        buttons.add(new Rectangle(120,10,80,80));
        buttons.add(new Rectangle(50,120,80,80));
        buttons.add(new Rectangle(600,10,80,80));


        gun = new Sprite(new Texture("gun.png"));


        //initializes
        gameCamera = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH/PPM,V_HEIGHT/PPM,gameCamera);
        gameCamera.position.set(viewport.getWorldWidth()/2,viewport.getWorldHeight()/2,0);

        //tiled map
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("Levels/level2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/PPM);

        //World

        world = new World(new Vector2(0,-9.81f),true);

        box2DDebugRenderer = new Box2DDebugRenderer();


        new WorldInitializer(this);

        //Player
        player = new Player(this);

        //WORLD CONTACT
        world.setContactListener(new WorldContactListener());

        zombieArray = new Array<Zombie>();
        zombieArray.add( new Zombie(this,2262/ GameVariables.PPM,70/GameVariables.PPM,zombie1,false));
        zombieArray.add( new Zombie(this,2320/ GameVariables.PPM,230/GameVariables.PPM,zombie2,true));
        zombieArray.add( new Zombie(this,3106/ GameVariables.PPM,70/GameVariables.PPM,zombie3,false));
        zombieArray.add( new Zombie(this,4000/ GameVariables.PPM,70/GameVariables.PPM,zombie4,true));
        zombieArray.add( new Zombie(this,5000/ GameVariables.PPM,70/GameVariables.PPM,zombie1,false));
        zombieArray.add( new Zombie(this,6500/ GameVariables.PPM,70/GameVariables.PPM,zombie2,true));
        zombieArray.add( new Zombie(this,6847/ GameVariables.PPM,720/GameVariables.PPM,zombie3,false));
        zombieArray.add( new Zombie(this,5000/ GameVariables.PPM,720/GameVariables.PPM,zombie4,true));
        zombieArray.add( new Zombie(this,4000/ GameVariables.PPM,720/GameVariables.PPM,zombie1,false));
        zombieArray.add( new Zombie(this,3000/ GameVariables.PPM,720/GameVariables.PPM,zombie2,true));


        exhaust = new ParticleEmitter();

        try {
            exhaust.load(Gdx.files.internal("deneme.txt").reader(2024));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Texture ballTexture = new Texture("ball.png");
        Sprite balls = new Sprite(ballTexture);
        exhaust.setSprite(balls);
        exhaust.getScale().setHigh(0.1f);
        exhaust.getAngle().setLow(270);
        exhaust.getAngle().setHighMin(270-45);
        exhaust.getAngle().setHighMax(270+45);
        exhaust.start();

        Texture eye_fire = new Texture("eyes.png");
        Sprite eye_sprite = new Sprite(eye_fire);

        exhaust_right_eye = new ParticleEmitter();

        try {
            exhaust_right_eye.load(Gdx.files.internal("eyes.txt").reader(2024));
        } catch (IOException e) {
            e.printStackTrace();
        }
        exhaust_right_eye.setSprite(eye_sprite);


        exhaust_left_eye = new ParticleEmitter();

        try {
            exhaust_left_eye.load(Gdx.files.internal("eyes.txt").reader(2024));
        } catch (IOException e) {
            e.printStackTrace();
        }
        exhaust_left_eye.setSprite(eye_sprite);

        exhaust_left_eye.getScale().setHigh(0.03f);
        exhaust_left_eye.getVelocity().setHigh(0,0.2f);
        exhaust_right_eye.getVelocity().setHigh(0,0.2f);
        exhaust_right_eye.getScale().setHigh(0.03f);

        exhaust_left_eye.start();
        exhaust_right_eye.start();

        try {
            music = Game.assetManager.get("Sounds/music.ogg",Music.class);
            music.setLooping(true);
            music.play();

        }catch (Exception e){
            e.printStackTrace();
        }

        if(music.isPlaying()) Gdx.app.log("PLAYIN","**************************"); else Gdx.app.log("NOT PLAYIN","****************************");



    }
    private Music music;
Sound sound;
float timer = 0;
    public void handleInput(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            player.b2body.setAwake(true);
            if(player.b2body.getLinearVelocity().y<2f){
            player.b2body.applyLinearImpulse(new Vector2(0,0.5f),player.b2body.getWorldCenter(),true);
                player.jumping = true;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)&&player.b2body.getLinearVelocity().x<=3){
            player.b2body.setAwake(true);
            player.b2body.applyLinearImpulse(new Vector2(0.15f,0),player.b2body.getWorldCenter(),true);

        }else
        if(Gdx.input.isKeyPressed(Input.Keys.A)&&player.b2body.getLinearVelocity().x>=-3){
            player.b2body.setAwake(true);
            player.b2body.applyLinearImpulse(new Vector2(-0.15f,0),player.b2body.getWorldCenter(),true);

        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){

            sound = Game.assetManager.get("Sounds/jump.ogg",Sound.class);
            sound.play();
            timer+=delta;
            if(timer>=0.15f){
          bulletArray.add(new Bullet(this,player.runningRight ?new Vector2(15f,player.angle):new Vector2(-15f,player.angle)));
                timer = 0;
            }
            player.firing = true;


        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            if(player.angle<10)
            player.angle+=0.5f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            if(player.angle>-3)
            player.angle-=0.5f;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)){
           DEBUGGING = DEBUGGING ? false : true;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
           Gdx.app.exit();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.G)){
            fireBalls.add(new FireBall(this,player.runningRight ?new Vector2(5f,player.angle/2):new Vector2(-5f,player.angle/2)));
        }


        if(Gdx.input.isTouched(0)&&!Gdx.input.isTouched(1)&&!Gdx.input.isTouched(2)&&!Gdx.input.isTouched(3)){
            if(player.b2body.getLinearVelocity().x>=-3)
                player.b2body.setAwake(true);
            player.b2body.applyLinearImpulse(new Vector2(-0.15f,0),player.b2body.getWorldCenter(),true);



        }
        if(Gdx.input.isTouched(0)&&Gdx.input.isTouched(1)&&!Gdx.input.isTouched(2)&&!Gdx.input.isTouched(3)){
            if(player.b2body.getLinearVelocity().x<=3)
                player.b2body.setAwake(true);
            player.b2body.applyLinearImpulse(new Vector2(0.15f,0),player.b2body.getWorldCenter(),true);

        }
        if(Gdx.input.isTouched(0)&&Gdx.input.isTouched(1)&&Gdx.input.isTouched(2)&&!Gdx.input.isTouched(3)&&player.b2body.getLinearVelocity().x<=3){
            if(player.b2body.getLinearVelocity().y<1f)
                player.b2body.applyLinearImpulse(new Vector2(0,5f),player.b2body.getWorldCenter(),true);

        }
        if(Gdx.input.isTouched(0)&&Gdx.input.isTouched(1)&&Gdx.input.isTouched(2)&&Gdx.input.isTouched(3)&&player.b2body.getLinearVelocity().x>=-3){

            timer+=delta;
            if(timer>=0.15f){
                bulletArray.add(new Bullet(this,player.runningRight ?new Vector2(15f,player.angle):new Vector2(-15f,player.angle)));
                timer = 0;
            }
            player.firing = true;

        }


    }


    public boolean DEBUGGING = true;

    float gunOffset = 50;
    public void update(float delta){
        handleInput(delta);
        world.step(1/60f,8,6);
        player.update(delta);
        for(Zombie z : zombieArray){
            z.update(delta);
        }
        for(Bullet b : bulletArray){
            if(!b.destroyed)
            b.update(delta);
        }


        //ü

        gameCamera.position.x=Math.max(gameCamera.position.x+(player.b2body.getPosition().x-gameCamera.position.x)*.05f,gameCamera.viewportWidth/2);
        gameCamera.position.y=Math.max(gameCamera.position.y+(player.b2body.getPosition().y-gameCamera.position.y)*.05f,gameCamera.viewportHeight/2);

        gameCamera.update();
        mapRenderer.setView(gameCamera);


        gun.setPosition(player.b2body.getPosition().x-gunOffset/PPM,player.b2body.getPosition().y-10/PPM);
        if(player.runningRight&&gun.isFlipX()){
           gun.flip(true,false);
            gunOffset = 60;
        }else if(!player.runningRight&&!gun.isFlipX()){
            gun.flip(true,false);
            gunOffset = +50;
        }
        if(player.runningRight)   gun.setRotation(player.angle*3f);
        else gun.setRotation(-player.angle*3f);
        gun.setSize(100/PPM,30/PPM);
        gun.setOriginCenter();
    }

    FPSLogger fpsLogger = new FPSLogger();
    @Override
    public void render(float delta) {
        fpsLogger.log();
        update(delta);

        Gdx.gl20.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        if(DEBUGGING)
         box2DDebugRenderer.render(world,gameCamera.combined);
        game.batch.setProjectionMatrix(gameCamera.combined);
        exhaust.setPosition(player.b2body.getPosition().x,player.b2body.getPosition().y+0.2f);
        exhaust_right_eye.setPosition(player.b2body.getPosition().x+0.04f,player.b2body.getPosition().y+0.26f);
        exhaust_left_eye.setPosition(player.b2body.getPosition().x-0.02f,player.b2body.getPosition().y+0.26f);
        exhaust_left_eye.getTransparency().setHigh(0.3f,0.5f);
        exhaust_right_eye.getTransparency().setHigh(0.3f,0.5f);

        game.batch.begin();


        for(Zombie z : zombieArray){
            if(z.destroyed!=true){
            z.draw(game.batch);
                z.drawHealth();

            }
        }
        game.batch.end();
        game.batch.begin();




        exhaust.draw(game.batch,Gdx.graphics.getDeltaTime());
        player.draw(game.batch);
        for(Bullet b : bulletArray){
            if(!b.destroyed)
            b.particleEmitter.draw(game.batch,delta);
           // b.draw(game.batch);
        }

        for(FireBall b : fireBalls){
           b.draw(game.batch,delta);
        }

        //CROSSHAIR
        if(DEBUGGING) {
            shapeRenderer.setProjectionMatrix(gameCamera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(1, 0, 0, 1);
            Vector2 v = player.runningRight ? new Vector2(player.b2body.getPosition().x + 20f / PPM, player.b2body.getPosition().y + player.angle / PPM) : new Vector2(player.b2body.getPosition().x - 20f / PPM, player.b2body.getPosition().y + player.angle / PPM);
            shapeRenderer.rectLine(player.b2body.getPosition(), v, 3 / PPM);
        }
        gun.draw(game.batch);



        exhaust_right_eye.draw(game.batch,Gdx.graphics.getDeltaTime());
        exhaust_left_eye.draw(game.batch,Gdx.graphics.getDeltaTime());

        sRenderer.setProjectionMatrix(gameCamera.combined);
        /*sRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(Rectangle r : buttons){
            sRenderer.rect(gameCamera.position.x+r.getX()/PPM-400/PPM,gameCamera.position.y-200/PPM+r.getY()/PPM,r.getWidth()/PPM,r.getHeight()/PPM);
        }*/



        game.batch.end();
        shapeRenderer.end();
        for(Zombie z : zombieArray){

              z.renderer.end();

        }
        sRenderer.end();

    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    @Override
    public void resize(int width, int height) {

        viewport.update(width,height);

    }

    @Override
    public void show() {

    }
    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        assetManager.dispose();

    }

    public TiledMap getTiledMap() {
        return tiledMap;

    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }

}

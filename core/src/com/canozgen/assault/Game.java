package com.canozgen.assault;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.canozgen.assault.screens.PlayScreen;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Game extends com.badlogic.gdx.Game {

	public static final float V_WIDTH = 960;
	public static final float V_HEIGHT = 416;

	public static AssetManager assetManager;

	public SpriteBatch batch;
	@Override
	public void create () {

		batch = new SpriteBatch();


		assetManager = new AssetManager();
		assetManager.load("Sounds/music.ogg",Music.class);
		assetManager.load("Sounds/bullet.mp3",Sound.class);
		assetManager.load("Sounds/jump.ogg",Sound.class);
		assetManager.finishLoading();


		setScreen(new PlayScreen(this));

	}

	@Override
	public void render () {
		if(assetManager.update()){

		}
		super.render();
	}


	@Override
	public void dispose () {
	}
}

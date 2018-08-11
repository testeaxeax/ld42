package com.thetriumvirate.game;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
	
	// Globally used constants
	public static final Color DEFAULT_FONT_COLOR = Color.WHITE;
	// DO NOT append suffix
	// TODO Set default font
	public static final String RES_DEFAULT_FONT_PLATFORMINDEPENDENT = "fonts/IHateComicSans";
	public static final Random RAND = new Random();
	
	public final String RES_DEFAULT_FONT;
	
	// Globally used variables
	public static int SCREEN_HEIGHT = 800;
	public static int SCREEN_WIDTH = 1024;
	public static float RATIO;
	public static float DEFAULT_BUTTON_WIDTH;
	public static float DEFAULT_BUTTON_HEIGHT;
	public static int DEFAULT_FONTSIZE;
	
	// Globally used variables required for management and rendering
	public SpriteBatch spritebatch;
	public AdvancedAssetManager assetmanager;
	public ScreenManager screenmanager;
	
	public FontLoader fontloader;
	
	public Main(FontLoader fl) {
		RATIO = (float) SCREEN_WIDTH / (float) SCREEN_HEIGHT;
		// TODO Set factors
		DEFAULT_BUTTON_WIDTH = SCREEN_WIDTH * 0.1953125f;
		DEFAULT_BUTTON_HEIGHT = SCREEN_WIDTH * 0.09765625f;
		DEFAULT_FONTSIZE = (int) (SCREEN_WIDTH * 0.029296875f);
		fontloader = fl;
		fontloader.setGame(this);
		RES_DEFAULT_FONT = fontloader.getFullPath(RES_DEFAULT_FONT_PLATFORMINDEPENDENT);
	}
	
	// Required to trigger rendering of active screen
	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void create() {
		spritebatch = new SpriteBatch();
		assetmanager = new AdvancedAssetManager();
		screenmanager = new ScreenManager(this);
		screenmanager.push(new SplashScreen(this));
	}
	
	public void loadGlobalResources() {
		fontloader.load();
	}
	
	@Override
	public void dispose() {
		screenmanager.dispose();
		spritebatch.dispose();
		assetmanager.dispose();
	}
}

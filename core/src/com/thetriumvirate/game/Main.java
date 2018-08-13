package com.thetriumvirate.game;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
	//!! DEBUG MODE !! DON'T FORGET TO CHANGE IT TO FALSE BEFORE RELEASE
	public static boolean DEBUG = false;
	
	// Globally used constants
	public static final Color DEFAULT_FONT_COLOR = Color.BLACK;
	// DO NOT append suffix
	// TODO Set default font
	public static final String RES_DEFAULT_FONT_PLATFORMINDEPENDENT = "fonts/OpenSans-SemiBold";
	public static final Random RAND = new Random();
	
	public final String RES_DEFAULT_FONT;
	
	public final String RES_FONT;
	public final String RES_LITTLE_FONT_NAME;
	public final String RES_TITLE_FONT_NAME;
	public final String RES_INFO_FONT_NAME;
	
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
	
	// Screen objects
	private StartScreen mainMenu;
	private CreditsScreen creditScreen;
	
	public Main(FontLoader fl) {
		RATIO = (float) SCREEN_WIDTH / (float) SCREEN_HEIGHT;
		// TODO Set factors
		DEFAULT_BUTTON_WIDTH = SCREEN_WIDTH * 0.1953125f;
		DEFAULT_BUTTON_HEIGHT = SCREEN_WIDTH * 0.09765625f;
		DEFAULT_FONTSIZE = (int) (SCREEN_WIDTH * 0.029296875f);
		fontloader = fl;
		fontloader.setGame(this);
		RES_DEFAULT_FONT = "df.ttf";
		RES_FONT = "fonts/OpenSans-SemiBold";
		RES_LITTLE_FONT_NAME = "littleFont.ttf";
		RES_TITLE_FONT_NAME = "titleFont.ttf";
		RES_INFO_FONT_NAME = "infoFont.ttf";
		
	}
	
	// Required to trigger rendering of active screen
	@Override
	public void render() {
		super.render();
	}
	
	public CreditsScreen getCreditScreen() {
		if(creditScreen == null)
			creditScreen = new CreditsScreen(this);
		return this.creditScreen;
	}
	
	public StartScreen getMainMenu() {
		if(mainMenu == null)
			mainMenu = new StartScreen(this);
		return mainMenu;
	}
	
	@Override
	public void create() {
		spritebatch = new SpriteBatch();
		assetmanager = new AdvancedAssetManager();
		screenmanager = new ScreenManager(this);
		screenmanager.push(new SplashScreen(this));
	}
	
	public void loadGlobalResources() {
		fontloader.load(RES_DEFAULT_FONT);
		fontloader.load(RES_FONT, Keyblock.getEdgeLength()/4, RES_LITTLE_FONT_NAME);
		fontloader.load(RES_FONT, (int) (Keyblock.getEdgeLength()*2.5f), RES_TITLE_FONT_NAME);
		fontloader.load(RES_FONT, 20, RES_INFO_FONT_NAME);
	}
	
	@Override
	public void dispose() {
		screenmanager.dispose();
		spritebatch.dispose();
		assetmanager.dispose();
	}
}

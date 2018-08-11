package com.thetriumvirate.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Keybutton {
	private int x, y, width, height;
	private String key;
	
	private static Texture background;
	private static final String TEX_PATH = "graphics/buttonbackground.png";
	private static BitmapFont font;
	
	private boolean stateSwitching;
	private boolean state;
	
	public Keybutton(int x, int y, String key, boolean stateSwitching) {
		this.x = x;
		this.y = y;
		this.key = key.toUpperCase();
		
		this.width = 50;
		this.height = 50;
		this.stateSwitching = stateSwitching;
		this.state = false;
		
		if(key.equalsIgnoreCase("ctrl")) {
			this.width *= 2;
		}
	}
	
	public void updateState(String key, boolean down) {
		if(down) {
			if(key.equalsIgnoreCase(this.key)) {
				this.state = this.stateSwitching ? !this.state : true;
			}
		} else {
			if(!this.stateSwitching)
				this.state = false;
		}
		
	}
	
	public void render(Main game) {
		GlyphLayout layout;
		
		game.spritebatch.draw(background, this.x, this.y, this.width, this.height);
		
		layout = new GlyphLayout(font, this.key);
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		font.draw(game.spritebatch, layout, this.x + this.width / 2 - layout.width / 2, this.y + this.height / 2 + layout.height / 2);
	}
	
	public static void load(Main game) {
		background = game.assetmanager.get(TEX_PATH, Texture.class);
		font = game.assetmanager.get(game.RES_DEFAULT_FONT, BitmapFont.class);
	}
	
	public static void prefetch(AssetManager m) {
		m.load(TEX_PATH, Texture.class);
	}
	
	public static void dispose(AdvancedAssetManager m) {
		m.unload(TEX_PATH);
	}
}

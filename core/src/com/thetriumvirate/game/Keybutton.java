package com.thetriumvirate.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Keybutton {
	private int x, y, width, height;
	private String key;
	
	private static Texture background;
	private static final String TEX_PATH = "graphics/buttonbackground";
	
	public Keybutton(int x, int y, String key) {
		this.x = x;
		this.y = y;
		this.key = key.toUpperCase();
		
		this.width = 20;
		this.height = 20;
		
		if(key.equalsIgnoreCase("ctrl")) {
			this.width *= 2;
		}
	}
	
	public void render(SpriteBatch sb) {
		
	}
	
	public static void load(AdvancedAssetManager m) {
		background = m.get(TEX_PATH, Texture.class);
	}
	
	public static void prefetch(AdvancedAssetManager m) {
		m.load(TEX_PATH, Texture.class);
	}
	
	public static void dispose(AdvancedAssetManager m) {
		m.unload(TEX_PATH);
	}
}

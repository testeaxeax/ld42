package com.thetriumvirate.game.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.thetriumvirate.game.FontLoader;
import com.thetriumvirate.game.Main;

public class HtmlFontLoader implements FontLoader {
	
	private static final int[] AVAILABLE_FONTSIZES = {8, 20, 30, 80};
	private static final Map<Color, String> AVAILABLE_COLORS;
	
	// Initalizing AVAILABLE_COLORS
	static {
		HashMap<Color, String> a = new HashMap<Color, String>();
		// Register new colors here
		a.put(Color.BLACK, "black");
		AVAILABLE_COLORS = Collections.unmodifiableMap(a);
	}
	
	private Main game;
	private ArrayList<FontEntry> fontlist;
	
	public HtmlFontLoader() {
		game = null;
		fontlist = new ArrayList<FontEntry>();
	}

	@Override
	public void load(String assetpath, int font_size, Color color, String name) {
		int file_font_size = 1000;
		FontEntry fontentry = new FontEntry();
		String colorname = null;
		for(int i = 0; i < AVAILABLE_FONTSIZES.length; i++) {
			if(font_size <= AVAILABLE_FONTSIZES[i] && AVAILABLE_FONTSIZES[i] < file_font_size) {
				file_font_size = AVAILABLE_FONTSIZES[i];
			}
		}
		colorname = AVAILABLE_COLORS.get(color);
		if(file_font_size == 1000 || colorname == null) {
			System.out.println("Error: Fontsize or color not available");
			Gdx.app.exit();
		}
		if(file_font_size == font_size) {
			fontentry.scale = false;
		}
		String fullassetpath = "html/" + assetpath + "-" + file_font_size + "-" + colorname + ".fnt";
		fontentry.fullassetpath = fullassetpath;
		fontentry.name = name;
		fontentry.font_size = font_size;
		fontlist.add(fontentry);
		game.assetmanager.load(fullassetpath, BitmapFont.class);
	}

	@Override
	public void load(String assetpath, int font_size, String name) {
		load(assetpath, font_size, Main.DEFAULT_FONT_COLOR, name);
	}

	@Override
	public void load(String assetpath, String name) {
		load(assetpath, Main.DEFAULT_FONTSIZE, name);
	}

	@Override
	public void load(String name) {
		load(Main.RES_DEFAULT_FONT_PLATFORMINDEPENDENT, name);
	}

	@Override
	public void setGame(Main p) {
		game = p;
	}
	
	@Override
	public String getFullPath(String assetpath) {
		return "html/" + assetpath + ".fnt";
	}
	
	public BitmapFont get(String name) {
		float scaleXY;
		boolean scale = true;
		String fullassetpath = null;
		int font_size = -1;
		for(FontEntry e : fontlist) {
			if(e.name.equals(name)) {
				fullassetpath = e.fullassetpath;
				font_size = e.font_size;
				scale = e.scale;
				break;
			}
		}
		if(font_size == -1 || fullassetpath == null) {
			System.out.println("Error: Please load the font");
			Gdx.app.exit();
		}
		BitmapFont font = game.assetmanager.easyget(fullassetpath, BitmapFont.class);
		if(scale) {
			scaleXY = font_size / font.getLineHeight();
			font.getData().setScale(scaleXY);
		}
		return font;
	}
	
	private class FontEntry {
		public String name;
		public String fullassetpath;
		public int font_size;
		boolean scale = true;
	}

}

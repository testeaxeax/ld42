package com.thetriumvirate.game.client;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.thetriumvirate.game.FontLoader;
import com.thetriumvirate.game.Main;

public class HtmlFontLoader implements FontLoader {
	
	private Main game = null;

	@Override
	public void load(String assetpath, int font_size, Color color) {
		load(assetpath);
	}

	@Override
	public void load(String assetpath, int font_size) {
		float scaleXY;
		String fullassetpath = "html/" + assetpath + ".fnt";
		BitmapFont font = game.assetmanager.easyget(fullassetpath, BitmapFont.class);
		scaleXY = font_size / font.getLineHeight();
		font.getData().setScale(scaleXY);
	}

	@Override
	public void load(String assetpath) {
		load(assetpath, Main.DEFAULT_FONTSIZE);
	}

	@Override
	public void load() {
		load(Main.RES_DEFAULT_FONT_PLATFORMINDEPENDENT);
	}

	@Override
	public void setGame(Main p) {
		game = p;
	}
	
	@Override
	public String getFullPath(String assetpath) {
		return "html/" + assetpath + ".fnt";
	}

}

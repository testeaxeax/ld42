package com.thetriumvirate.game.desktop;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.thetriumvirate.game.FontLoader;
import com.thetriumvirate.game.Main;

public final class DesktopFontLoader implements FontLoader {
	
	private Main game = null;

	@Override
	public void load(String assetpath, int font_size, Color color) {
		FreeTypeFontLoaderParameter loaderparam = new FreeTypeFontLoaderParameter();
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		String fullassetpath = "desktop/" + assetpath + ".ttf";
		
		FileHandleResolver resolver = new InternalFileHandleResolver();
		game.assetmanager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		game.assetmanager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		param.size = font_size;
		param.color = color;
		loaderparam.fontFileName = fullassetpath;
		loaderparam.fontParameters = param;
		game.assetmanager.load(assetpath, BitmapFont.class, loaderparam);
	}
	
	@Override
	public void load(String assetpath, int font_size) {
		load(assetpath, font_size, Main.DEFAULT_FONT_COLOR);
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
		return "desktop/" + assetpath + ".ttf";
	}
}

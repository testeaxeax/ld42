package com.thetriumvirate.spacebars;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface FontLoader {

	public void load(String assetpath, int font_size, Color color, String name);
	public void load(String assetpath, int font_size, String name);
	public void load(String assetpath, String name);
	public void load(String name);
	public void setGame(Main p);
	public String getFullPath(String assetpath);
	public BitmapFont get(String name);
}

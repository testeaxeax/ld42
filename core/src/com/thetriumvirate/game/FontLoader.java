package com.thetriumvirate.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface FontLoader {

	public void load(String assetpath, int font_size, Color color);
	public void load(String assetpath, int font_size);
	public void load(String assetpath);
	public void load();
	public void setGame(Main p);
	public String getFullPath(String assetpath);
}

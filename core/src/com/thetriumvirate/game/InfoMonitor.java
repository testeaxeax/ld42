package com.thetriumvirate.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class InfoMonitor {

	
	private GlyphLayout messageLayout;
	
	private static final String RES_MONITOR_TEXTURE = "graphics/monitor.png";
	private Texture monitor_texture;
	private int posX, posY, width, height;
	
	private BitmapFont font;
	
	
	public InfoMonitor(int posX, int posY, int width, int height, String message, Main game) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		
		font = game.fontloader.get(game.RES_INFO_FONT_NAME);
		monitor_texture = game.assetmanager.get(RES_MONITOR_TEXTURE);
		
		messageLayout = new GlyphLayout(font, message, Color.BLACK, width, 1, true);
	}
	
	
	public void render(Main game) {
		game.spritebatch.draw(monitor_texture, posX, posY, width, height);
		font.draw(game.spritebatch, messageLayout, posX , 
												   posY + height/2 + messageLayout.height/2);
	}
}

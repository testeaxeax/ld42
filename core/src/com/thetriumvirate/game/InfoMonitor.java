package com.thetriumvirate.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class InfoMonitor {

	private String message;
	private GlyphLayout messageLayout;
	
	private Texture monitor_texture;
	private int posX, posY, width, height;
	
	public InfoMonitor(int posX, int posY, int width, int height, String message) {
		this.message = message;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		

	}
}

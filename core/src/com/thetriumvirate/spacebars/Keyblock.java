package com.thetriumvirate.spacebars;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Keyblock {

	private static int edgeLength = 32;
	private int posX = 0, posY = 0;
	private String letter = "ER";
	private String ascii = "QWERTZUIOPASDFGHJKLYXCVBNM";
	private GlyphLayout layout;
	private boolean pressed = false;
	
	private int arrayPosX, arrayPosY;


	//attention! receives the pos in the array, not the pos in the game world!
	public Keyblock(int arrayPosX, int arrayPosY, int edgeLength, BitmapFont font, GameScreen gs) {
		this.edgeLength = edgeLength;
		this.posX = arrayPosX * edgeLength;
		this.posY = arrayPosY * edgeLength;
		this.arrayPosX = arrayPosX;
		this.arrayPosY = arrayPosY;
		
		
		//gen random char
		letter = String.valueOf(ascii.charAt(gs.getGame().RAND.nextInt(ascii.length())));
		layout = new GlyphLayout(font, letter);
	}
	
	public Keyblock(int arrayPosX, int arrayPosY, int edgeLength, BitmapFont font, SplashScreen ss) {
		this.edgeLength = edgeLength;
		this.posX = arrayPosX * edgeLength;
		this.posY = arrayPosY * edgeLength;
		
		
		
		//gen random char
		letter = String.valueOf(ascii.charAt(ss.getGame().RAND.nextInt(ascii.length())));
		layout = new GlyphLayout(font, letter);
	}

	public Keyblock(int arrayPosX, int arrayPosY, int edgeLength, String letter,BitmapFont font, GameScreen gs) {
		this.edgeLength = edgeLength;
		this.posX = arrayPosX * edgeLength;
		this.posY = arrayPosY * edgeLength;
		this.arrayPosX = arrayPosX;
		this.arrayPosY = arrayPosY;
		
		
		//gen random char
		this.letter = letter;
		layout = new GlyphLayout(font, letter);
	}

	public static int getEdgeLength() {
		return edgeLength;
	}

	
	public boolean isPressed() {
		return pressed;
	}


	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public int getPosX() {
		return posX;
	}


	public void setPosX(int posX) {
		this.posX = posX;
	}


	public int getPosY() {
		return posY;
	}


	public void setPosY(int posY) {
		this.posY = posY;
	}


	public String getLetter() {
		return letter;
	}


	public int getArrayPosX() {
		return arrayPosX;
	}

	
	public int getArrayPosY() {
		return arrayPosY;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}
	
	public GlyphLayout getLayout() {
		return layout;
	}
}

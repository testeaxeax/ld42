package com.thetriumvirate.game;



public class Keyblock {

	private int edgeLength = 20;
	private int posX = 0, posY = 0;
	private String letter = "ER";
	private String ascii = "qwertzuiopasdfghjklyxcvbnm1234567890";
	
	
	//attention! receives the pos in the array, not the pos in the game world!
	public Keyblock(int arrayPosX, int arrayPosY, int edgeLength, GameScreen gs) {
		this.edgeLength = edgeLength;
		this.posX = arrayPosX * edgeLength;
		this.posY = arrayPosY * edgeLength;
		
		//gen random char
		letter = String.valueOf(ascii.charAt(gs.getGame().RAND.nextInt(ascii.length())));
	}


	public int getEdgeLength() {
		return edgeLength;
	}


	public void setEdgeLength(int edgeLength) {
		this.edgeLength = edgeLength;
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


	public void setLetter(String letter) {
		this.letter = letter;
	}
}

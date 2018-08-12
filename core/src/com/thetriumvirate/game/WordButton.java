package com.thetriumvirate.game;

import java.util.ArrayList;

import com.badlogic.gdx.Input;

public class WordButton {
	private ArrayList<Keybutton> buttons;
	private int spacing;
	private WordButtonListener listener;
	private int width, height;
	
	public static final int NORMAL_SPACING = 4;
	
	public WordButton(int x, int y, int spacing, WordButtonListener listener, String word, boolean middle) {
		buttons = new ArrayList<Keybutton>();
		this.spacing = spacing;
		this.listener = listener;
		
		int prevX = x;
		for(int i = 0; i < word.length(); i++) {
			//System.out.println(i + ": " + word.substring(i,  i + 1) + " | " + Input.Keys.valueOf(word.substring(i, i + 1)));
			Keybutton b = new Keybutton(prevX, y, Input.Keys.valueOf(word.substring(i, i + 1).toUpperCase()), true); 
			buttons.add(b);
			prevX += b.getWidth() + this.spacing;
		}
		
		width = 0;
		for(Keybutton b : buttons) {
			width += b.getWidth() + this.spacing;
		}
		
		if(width > 0)
			width -= this.spacing;
		
		height = 0;
		for(Keybutton b : buttons) {
			if(b.getHeight() > height)
				height = b.getHeight();
		}
		
		if(middle) {
			x -= width / 2;
			y -= height / 2;
			setX(x);
			setY(y);
		}
	}
	/*
	public WordButton(int x, int y, int spacing, WordButtonListener listener, int... keycodes) {
		buttons = new ArrayList<Keybutton>();
		this.spacing = spacing;
		this.listener = listener;
		
		int prevX = x;
		for(int key : keycodes) {
			Keybutton b = new Keybutton(prevX, y, key, true);
			buttons.add(b);
			prevX += b.getWidth() + this.spacing;
		}
	}
	*/

	public ArrayList<Keybutton> getButtons() {
		return this.buttons;
	}
	
	public void setX(int x) {
		int prevX = x;
		for(Keybutton b : buttons) {
			b.setX(prevX);
			prevX += b.getWidth() + this.spacing;
		}
	}
	
	public void setY(int y) {
		for(Keybutton b : buttons) {
			b.setY(y);
		}
	}
	
	public void update() {
		boolean finished = true;
		
		for(Keybutton b : buttons) {
			if(!b.isPressed()) {
				finished = false;
				break;
			}
		}
		
		if(finished) {
			this.listener.onFinish(this);
			for(Keybutton b : this.buttons)
				b.setPressed(false);
		}
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	
	public interface WordButtonListener{
		public void onFinish(WordButton btn);
	}
}

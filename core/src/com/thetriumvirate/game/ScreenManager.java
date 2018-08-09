package com.thetriumvirate.game;

import java.util.EmptyStackException;
import java.util.Stack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class ScreenManager {

	private final Stack<Screen> stack;
	private final Game game;
	
	public ScreenManager(final Game game) {
		stack = new Stack<Screen>();
		this.game = game;
	}
	
	public void push(Screen s) {
		stack.push(s);
		game.setScreen(s);
	}
	
	public void pop(boolean keepassets) {
		Screen old = null;
		try {
			old = stack.pop();
			game.setScreen(stack.peek());
		} catch (EmptyStackException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
		if(!keepassets) {
			old.dispose();
		}
	}
	
	public void set(Screen s, boolean keepassets) {
		Screen old = null;
		try {
			old = stack.pop();
		} catch (EmptyStackException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
		stack.push(s);
		game.setScreen(s);
		if(!keepassets) {
			old.dispose();
		}
	}
	
	public Screen peek() {
		try {
			return stack.peek();
		} catch (EmptyStackException e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
		return null;
	}
	
	public void dispose() {
		game.setScreen(null);
		for(Screen s : stack) {
			s.dispose();
		}
		stack.clear();
	}
}

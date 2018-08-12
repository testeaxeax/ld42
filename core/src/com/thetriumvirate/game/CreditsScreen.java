package com.thetriumvirate.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;

public final class CreditsScreen implements Screen, InputProcessor {
	
	private static final int CAM_WIDTH = Main.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Main.SCREEN_HEIGHT;
	
	// Resource paths
	// private static final String RES_SOMETHING = "somewhere/something";
	
	private Main game;
	private OrthographicCamera cam;
	
	private WordButton menu;
	private ArrayList<Keybutton> buttons;
	
	private static final String CREDITS = "Game developed for Ludum Dare 40 within 72 hours by:\n" 
										+ "Inzenhofer Tobias\n"
										+ "Poellinger Maximilian\n" 
										+ "Brunner Moritz\n\n" 
										+ "Special thanks to the following projects:\n" 
										+ "libGDX, lwjgl, JUtils, JInput, JOrbis, Eclipse\n\n" 
										+ "For Licenses view our Github repository or extract this file";
	private GlyphLayout textLayout;
	private BitmapFont font;
	
	public CreditsScreen(final Main game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
	
		buttons = new ArrayList<Keybutton>();
		menu = new WordButton(CAM_WIDTH / 2, 100, WordButton.NORMAL_SPACING, new WordButton.WordButtonListener() {
			
			@Override
			public void onFinish(WordButton btn) {
				game.screenmanager.set(game.getMainMenu(), true);
			}
		}, "Menu", true);
		
		addWord(menu);
	}
	
	private void addWord(WordButton word) {
		for(Keybutton b : word.getButtons())
			addButton(b);
	}
	
	private void addButton(Keybutton button) {
		if(!this.buttons.contains(button))
			this.buttons.add(button);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);

		font = game.assetmanager.get(game.RES_DEFAULT_FONT, BitmapFont.class);

		this.textLayout = new GlyphLayout();
		this.textLayout.setText(font, CREDITS, Color.BLACK, CAM_WIDTH, Align.center, true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.spritebatch.begin();
		
		for(Keybutton b : buttons)
			b.render(game);
		
		if(textLayout != null)
			font.draw(game.spritebatch, textLayout, 0, CAM_HEIGHT / 2 + textLayout.height / 2);
		
		game.spritebatch.end();
		
		menu.update();
	}

	public static void prefetch(AssetManager m) {
		
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	// TODO Unload all non-global resources
	@Override
	public void dispose() {
		
	}

	@Override
	public boolean keyDown(int keycode) {
		boolean ret = false;
		for(Keybutton b : buttons) {
			if(b.updateState(keycode, true))
				ret = true;
		}
		return ret;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean ret = false;
		for(Keybutton b : buttons) {
			if(b.updateState(keycode, false))
				ret = true;
		}
		return ret;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}

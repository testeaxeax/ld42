package com.thetriumvirate.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public final class StartScreen implements Screen, InputProcessor {
	
	private static final int CAM_WIDTH = Main.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Main.SCREEN_HEIGHT;
	
	// Resource paths
	// private static final String RES_SOMETHING = "somewhere/something";
	
	private ArrayList<Keybutton> buttons;
	private WordButton wordPlay;
	
	private Main game;
	private OrthographicCamera cam;
	
	public StartScreen(final Main game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		
		buttons = new ArrayList<Keybutton>();
		
		wordPlay = new WordButton(50, 150, WordButton.NORMAL_SPACING, new WordButton.WordButtonListener() {
			
			@Override
			public void onFinish(WordButton btn) {
				game.screenmanager.set(new GameScreen(game), true);
			}
		}, "Play");
		
		wordPlay.setX(CAM_WIDTH / 2 - wordPlay.getWidth() / 2);
		wordPlay.setY(CAM_HEIGHT / 2 - wordPlay.getHeight() / 2);
		
		for(Keybutton b : wordPlay.getButtons())
			this.buttons.add(b);
		
		//this.buttons.add(new Keybutton(50, 50, Input.Keys.A, false));
		//this.buttons.add(new Keybutton(150, 50, Input.Keys.B, true));
		
		
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
	}

	@Override
	public void show() {
		Keybutton.load(game);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.spritebatch.begin();
		for(Keybutton b : buttons)
			b.render(game);
		game.spritebatch.end();
		
		wordPlay.update();
	}

	public static void prefetch(AssetManager m) {
		Keybutton.prefetch(m);
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
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}

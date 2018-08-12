package com.thetriumvirate.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public final class StartScreen implements Screen, InputProcessor {
	
	private static final int CAM_WIDTH = Main.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Main.SCREEN_HEIGHT;
	
	// Resource paths
	// private static final String RES_SOMETHING = "somewhere/something";
	private static final String  BACKGROUND_TEXTURE = "graphics/background.png";
	private Texture background_texture;
	
	private ArrayList<Keybutton> buttons;
	private WordButton wordPlay;
	private WordButton wordCredits;
	private WordButton wordExit;
	
	private Pixmap backgroundShadePixmap;
	private Texture backgroundShadeTexture;
	
	private Main game;
	private OrthographicCamera cam;
	
	public StartScreen(final Main game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		
		buttons = new ArrayList<Keybutton>();
		
		wordPlay = new WordButton(CAM_WIDTH / 2, CAM_HEIGHT / 2, WordButton.NORMAL_SPACING, new WordButton.WordButtonListener() {
			
			@Override
			public void onFinish(WordButton btn) {
				for(Keybutton b : buttons)b.setPressed(false);
				game.screenmanager.set(new GameScreen(game, 1), true);
				
				
				//for test purposes
				//game.screenmanager.set(new EndOfLevelScreen(game, 3, 0),  true);
			}
		}, "Play", true);
		
		addWord(wordPlay);
		
		wordCredits = new WordButton(CAM_WIDTH / 2, CAM_HEIGHT / 2 - 80, WordButton.NORMAL_SPACING, new WordButton.WordButtonListener() {
			
			@Override
			public void onFinish(WordButton btn) {
				for(Keybutton b : buttons)b.setPressed(false);
				game.screenmanager.set(game.getCreditScreen(), true);
				
				
				//for test purposes
				//game.screenmanager.set(new GameOverScreen(game, "You ran out of space"), true);
			}
		}, "Credits", true);
		
		addWord(wordCredits);
		
		wordExit = new WordButton(CAM_WIDTH / 2, CAM_HEIGHT / 2 - 300, WordButton.NORMAL_SPACING, new WordButton.WordButtonListener() {
			
			@Override
			public void onFinish(WordButton btn) {
				Gdx.app.exit();
			}
		}, "Exit", true);
		
		addWord(wordExit);
		
		//this.buttons.add(new Keybutton(50, 50, Input.Keys.A, false));
		//this.buttons.add(new Keybutton(150, 50, Input.Keys.B, true));
		
		background_texture = game.assetmanager.easyget(BACKGROUND_TEXTURE, Texture.class);
		backgroundShadePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		backgroundShadePixmap.setColor(0f, 0f, 0f, 0.5f);
		backgroundShadePixmap.fill();
		backgroundShadeTexture = new Texture(backgroundShadePixmap);
		backgroundShadePixmap.dispose();
		
		
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
	}
	
	private void addWord(WordButton word) {
		for(Keybutton b : word.getButtons())
			addButton(b);
	}
	
	private void addButton(Keybutton k) {
		if(!this.buttons.contains(k))
			this.buttons.add(k);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.spritebatch.begin();
		//draw background
		game.spritebatch.draw(background_texture, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
		
		game.spritebatch.draw(backgroundShadeTexture, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
		
		for(Keybutton b : buttons)
			b.render(game);
		game.spritebatch.end();
		
		wordPlay.update();
		wordCredits.update();
		wordExit.update();
	}

	public static void prefetch(AssetManager m) {
		m.load(BACKGROUND_TEXTURE, Texture.class);
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

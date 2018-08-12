package com.thetriumvirate.game;

import java.util.ArrayList;

import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.thetriumvirate.game.WordButton.WordButtonListener;

public final class EndOfLevelScreen implements Screen, InputProcessor{
	
	private static final int CAM_WIDTH = Main.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Main.SCREEN_HEIGHT;
	
	// Resource paths
	// private static final String RES_SOMETHING = "somewhere/something";
	private static final String  BACKGROUND_TEXTURE = "graphics/background.png";
	
	private Main game;
	private OrthographicCamera cam;
	
	
	private BitmapFont titleFont;
	private BitmapFont defaultFont;
	
	private GlyphLayout titleLayout;
	private GlyphLayout leftSpaceslayout;
	private int remaining_space;
	
	private Texture background_texture;
	
	private WordButton nextLevelBtn;
	private ArrayList<Keybutton> buttons;
	
	public EndOfLevelScreen(Main game, int remaining_space) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		
		buttons = new ArrayList<Keybutton>();
		
		this.remaining_space = remaining_space;
		
		titleFont = game.assetmanager.easyget(game.RES_TITLE_FONT_NAME, BitmapFont.class);
		defaultFont = game.assetmanager.easyget(game.RES_DEFAULT_FONT, BitmapFont.class); 
		
		background_texture = game.assetmanager.easyget(BACKGROUND_TEXTURE, Texture.class);
		
		initContent();
	}
	
	private void initContent() {
		titleLayout = new GlyphLayout(titleFont, "YOU DID IT!");
		leftSpaceslayout = new GlyphLayout(defaultFont, "You had " + remaining_space + "spaces left!");
		nextLevelBtn = new WordButton(game.SCREEN_WIDTH/2, game.SCREEN_HEIGHT/3, WordButton.NORMAL_SPACING, new WordButtonListener() {
			
			@Override
			public void onFinish(WordButton btn) {
				game.screenmanager.set(new GameScreen(game), true);
			}
		}, "Next", true);
		addWord(nextLevelBtn);
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
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.spritebatch.begin();
		
		game.spritebatch.draw(background_texture, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
		titleFont.draw(game.spritebatch, titleLayout, game.SCREEN_WIDTH/2 - titleLayout.width/2, game.SCREEN_HEIGHT/4*3);
		defaultFont.draw(game.spritebatch, leftSpaceslayout, game.SCREEN_WIDTH/2 - titleLayout.width/2, game.SCREEN_HEIGHT/2 - leftSpaceslayout.height);
		
		for(Keybutton b : buttons)
			b.render(game);
		
		game.spritebatch.end();
		
		nextLevelBtn.update();
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

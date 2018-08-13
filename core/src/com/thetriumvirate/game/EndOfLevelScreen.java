package com.thetriumvirate.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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
	
	private static final String TITLE_BACKGROUND_TEXTURE = "graphics/well_done_title.png";
	private Texture title_background_texture;
	
	private Main game;
	private OrthographicCamera cam;
	
	private static final String RES_KEY_SOUND = "audio/keyboard_pressing_onekey.wav";
	private Sound keySound;
	
	
	private BitmapFont titleFont;
	
	private GlyphLayout titleLayout;
	
	private Texture background_texture;
	
	private Pixmap backgroundShadePixmap;
	private Texture backgroundShadeTexture;
	
	private WordButton nextLevelBtn;
	private WordButton menuBtn;
	private ArrayList<Keybutton> buttons;
	
	private WordButton[] spaceLeftBtns;
	private WordButton[] stageCompleteBtns;
	private String leftSpaceText;
	private String stageCompleteText;
	
	private static final String RES_FANFARE_SOUND = "audio/levelDone.wav";
	private Sound fanfareSound;
	
	private int completedLevel;
	private int lastLevel;
	
	public EndOfLevelScreen(Main game, int remaining_space, int level, int lastlevel) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		
		buttons = new ArrayList<Keybutton>();
		
		
		titleFont = game.assetmanager.easyget(game.RES_TITLE_FONT_NAME, BitmapFont.class);
		
		background_texture = game.assetmanager.easyget(BACKGROUND_TEXTURE, Texture.class);
		title_background_texture = game.assetmanager.easyget(TITLE_BACKGROUND_TEXTURE, Texture.class);
		
		fanfareSound = game.assetmanager.get(RES_FANFARE_SOUND);
		keySound = game.assetmanager.get(RES_KEY_SOUND);
		
		backgroundShadePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		backgroundShadePixmap.setColor(0f, 0f, 0f, 0.5f);
		backgroundShadePixmap.fill();
		backgroundShadeTexture = new Texture(backgroundShadePixmap);
		backgroundShadePixmap.dispose();
		
		leftSpaceText = "You had " + remaining_space + " spaces left";
		stageCompleteText = "Level " + level + " complete";
		
		completedLevel = level;
		this.lastLevel = lastlevel;
		
		initContent();
	}
	
	private void initContent() {
		titleLayout = new GlyphLayout(titleFont, "WELL DONE!");
		
		String[] words = leftSpaceText.split(" ");
		spaceLeftBtns = new WordButton[words.length];
		int wpx = game.SCREEN_WIDTH/5, wpy = game.SCREEN_HEIGHT/2 - 80;
		for(int i = 0; i < words.length; i++) {
			spaceLeftBtns[i] = new WordButton(wpx, wpy, i != 2 ? 40 : 60, WordButton.NORMAL_SPACING/2, new WordButton.WordButtonListener() {
				
				@Override
				public void onFinish(WordButton btn) {}
			}, words[i], false, false);
			addWord(spaceLeftBtns[i]);
			//wpx += spaceLeftBtns[i].getWidth();
		}
		wpx = game.SCREEN_WIDTH/2 - getSentenceLength(spaceLeftBtns)/2 ;
		for(WordButton w : spaceLeftBtns) {
			w.setX(wpx);
			wpx += w.getWidth() + spaceLeftBtns[0].getButtons().get(0).getWidth()/2;
			
		}
		
		
		words = stageCompleteText.split(" ");
		stageCompleteBtns = new WordButton[words.length];
		wpx = game.SCREEN_WIDTH/5;
		wpy = game.SCREEN_HEIGHT/2;
		for(int i = 0; i < words.length; i++) {
			stageCompleteBtns[i] = new WordButton(wpx, wpy, i != 1 ? 40 : 60, WordButton.NORMAL_SPACING/2, new WordButton.WordButtonListener() {
				
				@Override
				public void onFinish(WordButton btn) {}
			}, words[i], false, false);
			addWord(stageCompleteBtns[i]);
			//wpx += stageCompleteBtns[i].getWidth();
		}
		wpx = game.SCREEN_WIDTH/2 - getSentenceLength(stageCompleteBtns)/2 ;
		for(WordButton w : stageCompleteBtns) {
			w.setX(wpx);
			wpx += w.getWidth() + stageCompleteBtns[0].getButtons().get(0).getWidth()/2;
		}
		if(completedLevel != lastLevel) {
			nextLevelBtn = new WordButton(game.SCREEN_WIDTH/4*3, game.SCREEN_HEIGHT/4, WordButton.NORMAL_SPACING, new WordButtonListener() {
			
			@Override
				public void onFinish(WordButton btn) {
				game.screenmanager.set(new GameScreen(game, completedLevel+1), true);
				}
			}, "Next", true);
			addWord(nextLevelBtn);
		}else {
			nextLevelBtn = null;
		}
		
		menuBtn = new WordButton(game.SCREEN_WIDTH/((completedLevel == lastLevel) ? 2 : 4), game.SCREEN_HEIGHT/4,
					  WordButton.NORMAL_SPACING, new WordButtonListener() {
			
			@Override
			public void onFinish(WordButton btn) {
				game.screenmanager.set(new StartScreen(game), true);
			}
		}, "Menu", true);
		addWord(menuBtn);
	}
	
	
	private void addWord(WordButton word) {
		for(Keybutton b : word.getButtons())
			addButton(b);
	}
	
	private void addButton(Keybutton k) {
		if(!this.buttons.contains(k))
			this.buttons.add(k);
	}
	
	private int getSentenceLength(WordButton[] words) {
		int length = 0;
		for(WordButton w : words) {
			length += w.getWidth();
		}
		length += (words.length-1)*words[0].getButtons().get(0).getWidth()/2;
		return length;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		fanfareSound.play(1f);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.spritebatch.begin();
		
		game.spritebatch.draw(background_texture, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
		game.spritebatch.draw(backgroundShadeTexture, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
		
		game.spritebatch.draw(title_background_texture, (game.SCREEN_WIDTH - title_background_texture.getWidth())/2, 
							  game.SCREEN_HEIGHT/4*3);
		
		titleFont.draw(game.spritebatch, titleLayout, game.SCREEN_WIDTH/2 - titleLayout.width/2, game.SCREEN_HEIGHT/4*3 
						+ titleLayout.height + (title_background_texture.getHeight() - titleLayout.height)/2);
		
		for(Keybutton b : buttons)
			b.render(game);
		
		game.spritebatch.end();
		
		if(nextLevelBtn != null) {
			nextLevelBtn.update();
		}
		menuBtn.update();
	}

	public static void prefetch(AssetManager m) {
		m.load(TITLE_BACKGROUND_TEXTURE, Texture.class);
		m.load(RES_FANFARE_SOUND, Sound.class);
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
		if(ret)keySound.play();
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

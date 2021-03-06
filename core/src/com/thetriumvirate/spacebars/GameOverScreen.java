package com.thetriumvirate.spacebars;

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
import com.thetriumvirate.spacebars.WordButton.WordButtonListener;

public class GameOverScreen  implements Screen, InputProcessor{
	
	private static final int CAM_WIDTH = Main.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Main.SCREEN_HEIGHT;
	
	// Resource paths
	// private static final String RES_SOMETHING = "somewhere/something";
	private static final String  BACKGROUND_TEXTURE = "graphics/background.png";
	
	private static final String TITLE_BACKGROUND_TEXTURE = "graphics/well_done_title.png";
	private Texture title_background_texture;
	
	
	
	private static final String RES_GAMEOVER_SOUND = "audio/gameOver.wav";
	private Sound gameOverSound;
	
	private static final String RES_KEY_SOUND = "audio/keyboard_pressing_onekey.wav";
	private Sound keySound;
	
	private Main game;
	private OrthographicCamera cam;
	
	
	private BitmapFont titleFont;
	
	private GlyphLayout titleLayout;
	
	private Texture background_texture;
	
	private Pixmap backgroundShadePixmap;
	private Texture backgroundShadeTexture;
	
	private WordButton retryBtn;
	private WordButton menuBtn;
	private ArrayList<Keybutton> buttons;
	
	private WordButton[] textBtns;
	private String deathMessageText;
	
	private int failedLevel;
	
	public GameOverScreen(Main game, String deathMessage, int lvl) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		
		buttons = new ArrayList<Keybutton>();
		
		
		titleFont = game.fontloader.get(game.RES_TITLE_FONT_NAME);
		
		background_texture = game.assetmanager.easyget(BACKGROUND_TEXTURE, Texture.class);
		
		backgroundShadePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		backgroundShadePixmap.setColor(0f, 0f, 0f, 0.5f);
		backgroundShadePixmap.fill();
		backgroundShadeTexture = new Texture(backgroundShadePixmap);
		backgroundShadePixmap.dispose();
		
		title_background_texture = game.assetmanager.get(TITLE_BACKGROUND_TEXTURE, Texture.class);
		gameOverSound = game.assetmanager.get(RES_GAMEOVER_SOUND);
		keySound = game.assetmanager.get(RES_KEY_SOUND);
		
		
		
		deathMessageText = deathMessage;
		
		failedLevel = lvl;
		
		initContent();
	}
	
	private void initContent() {
		titleLayout = new GlyphLayout(titleFont, "You died!");
		
		String[] words = deathMessageText.split(" ");
		textBtns = new WordButton[words.length];
		int wpx = game.SCREEN_WIDTH/5, wpy = game.SCREEN_HEIGHT/2;
		for(int i = 0; i < words.length; i++) {
			textBtns[i] = new WordButton(wpx, wpy, 40 , WordButton.NORMAL_SPACING/2, new WordButton.WordButtonListener() {
				
				@Override
				public void onFinish(WordButton btn) {}
			}, words[i], false, false);
			addWord(textBtns[i]);
			wpx += textBtns[i].getWidth();
		}
		wpx = game.SCREEN_WIDTH/2 - getSentenceLength(textBtns)/2 ;
		for(WordButton w : textBtns) {
			w.setX(wpx);
			wpx += w.getWidth() + textBtns[0].getButtons().get(0).getWidth()/2;
		}
		
		
		retryBtn = new WordButton(game.SCREEN_WIDTH/4*3, game.SCREEN_HEIGHT/4, WordButton.NORMAL_SPACING, new WordButtonListener() {
			
			@Override
			public void onFinish(WordButton btn) {
				game.screenmanager.set(new GameScreen(game, failedLevel), true);
			}
		}, "Retry", true);
		addWord(retryBtn);
		
		menuBtn = new WordButton(game.SCREEN_WIDTH/4, game.SCREEN_HEIGHT/4,
					  WordButton.NORMAL_SPACING, new WordButtonListener() {
			
			@Override
			public void onFinish(WordButton btn) {
				game.screenmanager.set(new StartScreen(game), true);
			}
		}, "Menu", true);
		addWord(menuBtn);
	}
	
	
	private int getSentenceLength(WordButton[] words) {
		int length = 0;
		for(WordButton w : words) {
			length += w.getWidth();
		}
		length += (words.length-1)*words[0].getButtons().get(0).getWidth()/2;
		return length;
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
		gameOverSound.play(1f);
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
		//defaultFont.draw(game.spritebatch, deathMessageLayout, game.SCREEN_WIDTH/2 - deathMessageLayout.width/2, game.SCREEN_HEIGHT/4*3 - 1.4f*titleLayout.height);
		
		for(Keybutton b : buttons)
			b.render(game);
		
		game.spritebatch.end();
		
		retryBtn.update();
		menuBtn.update();
	}

	public static void prefetch(AssetManager m) {
		m.load(RES_GAMEOVER_SOUND, Sound.class);
		
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
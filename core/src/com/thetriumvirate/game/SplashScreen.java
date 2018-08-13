package com.thetriumvirate.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.TimeUtils;

public final class SplashScreen implements Screen {

	private static final int CAM_WIDTH = Main.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Main.SCREEN_HEIGHT;
	// SplashScreen will be displayed for at least 5 seconds
	private static final int MIN_SHOWTIME = 1;
	
	// Resource paths
	private static final String RES_SPLASH = "graphics/splash.jpg";
	private static final String TEX_PATH_UP = "graphics/keyblock.png";
	private Texture background_blockTexture;

	private Main game;
	private OrthographicCamera cam;
	private BitmapFont font;
	private BitmapFont littleFont;
	
	// Used to center the text
	private GlyphLayout layout;
	private long showtime;
	
	private WordButton loading;
	private int status;
	private long lastAnimTime, curAnimTime;
	private	boolean animDone;
	private static int ANIM_STEP_DURATION = 300;
	
	private int screenBlockWidth = 32, screenBlockHeight = 25;
	private Keyblock[][] backgroundBlocks;
	private Pixmap backgroundShadePixmap;
	private Texture backgroundShadeTexture;

	public SplashScreen(Main game) {
		this.game = game;
		showtime = 0;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		layout = new GlyphLayout();
		game.loadGlobalResources();
		// Getting all resources required for SplashScreen
		font = game.assetmanager.easyget(game.RES_DEFAULT_FONT, BitmapFont.class);
		littleFont = game.assetmanager.easyget(game.RES_LITTLE_FONT_NAME, BitmapFont.class);
		
		initBackground();
		
		loading = new WordButton(CAM_WIDTH / 2, CAM_HEIGHT / 2, 0, new WordButton.WordButtonListener() {
			@Override
			public void onFinish(WordButton btn) {
			}
		}, "loading...", true);
		
		Keybutton.load(game);
		background_blockTexture = game.assetmanager.easyget(TEX_PATH_UP, Texture.class);
	}
	
	private void initBackground() {
		backgroundBlocks = new Keyblock[screenBlockWidth][];
		for(int i = 0; i < backgroundBlocks.length; i++) {
			backgroundBlocks[i] = new Keyblock[screenBlockHeight];
			for(int j = 0; j < backgroundBlocks[i].length; j++) {
				backgroundBlocks[i][j] = new Keyblock(i, j, 32, littleFont, this);
				
			}
		}
		
		backgroundShadePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		backgroundShadePixmap.setColor(0f, 0f, 0f, 0.5f);
		backgroundShadePixmap.fill();
		backgroundShadeTexture = new Texture(backgroundShadePixmap);
		backgroundShadePixmap.dispose();
	}
	

	@Override
	public void show() {
		// TODO Replace ScreenTemplate with actual game/menu screen
		// TODO Prefetch all resources of the actual game
		StartScreen.prefetch(game.assetmanager);
		GameScreen.prefetch(game.assetmanager);
		CreditsScreen.prefetch(game.assetmanager);
		EndOfLevelScreen.prefetch(game.assetmanager);
		
		showtime = TimeUtils.millis();
		lastAnimTime = showtime;
		status = 0;
		animDone = false;
	}

	@Override
	public void render(float delta) {
		if(animDone)
			checkprogress();
		
		if(status > loading.getButtons().size()) {
			if(!animDone)
				ANIM_STEP_DURATION = 200;
			status = 7;
			animDone = true;
		}
		
		curAnimTime = TimeUtils.millis();
		
		if(curAnimTime - lastAnimTime > ANIM_STEP_DURATION) {
			if(status < loading.getButtons().size() && animDone)
				loading.getButtons().get(status).setPressed(!loading.getButtons().get(status).isPressed());
			
			status++;
			lastAnimTime = curAnimTime;
		}
		
		String text = "Progress: " + game.assetmanager.getProgress() * 100 + '%';
		layout.setText(font, text);
		//final Vector2 pos = new Vector2((CAM_WIDTH / 2) - (layout.width / 2), (CAM_HEIGHT / 4) - (layout.height / 2));
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		game.spritebatch.begin();
		
		for(int i = 0; i < backgroundBlocks.length; i++) {
			for(int j = 0; j < backgroundBlocks[i].length; j++) {
				game.spritebatch.draw(background_blockTexture, backgroundBlocks[i][j].getPosX(), backgroundBlocks[i][j].getPosY(), Keyblock.getEdgeLength(), Keyblock.getEdgeLength());
				littleFont.draw(game.spritebatch, backgroundBlocks[i][j].getLayout(), backgroundBlocks[i][j].getPosX() + Keyblock.getEdgeLength()/5,
						backgroundBlocks[i][j].getPosY() + Keyblock.getEdgeLength() - Keyblock.getEdgeLength()/5 - 
						(backgroundBlocks[i][j].isPressed() ? backgroundBlocks[i][j].getLayout().height/5 : 0));
			}
		}
		game.spritebatch.draw(backgroundShadeTexture, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);
		
		if(animDone) {
			for(Keybutton k : loading.getButtons())
				k.render(game);
		} else {
			for(int i = 0; i < status; i++) {
				if(i >= loading.getButtons().size())
					continue;
				loading.getButtons().get(i).render(game);
			}
		}
		
		//font.draw(game.spritebatch, layout, pos.x, pos.y);
		game.spritebatch.end();
	}

	private void checkprogress() {
		if (TimeUtils.timeSinceMillis(showtime) >= MIN_SHOWTIME && game.assetmanager.update()) {
			// TODO Replace ScreenTemplate with actual game/menu screen
			game.screenmanager.set(game.getMainMenu(), false);
		}
	}
	
	public Main getGame() {
		return this.game;
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
}

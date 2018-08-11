package com.thetriumvirate.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

public final class GameScreen implements Screen {
	
	private static final int CAM_WIDTH = Main.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Main.SCREEN_HEIGHT;
	
	// Resource paths
	// private static final String RES_SOMETHING = "somewhere/something";
	private static final String KEYBLOCK_TEXTURE = "assets/graphics/keyblock.png";
	
	private Main game;
	private OrthographicCamera cam;
	
	
	private int pixelGridWidth = 40, pixelGridHeight = 30;
	private Keyblock[][] blocks = new Keyblock[pixelGridWidth][];
	
	
	public GameScreen(Main game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		
		setUpPixelGrid();
	}
	
	private void setUpPixelGrid() {
		//full init of the whole array, every entry is null so far
		for(int i = 0; i < blocks.length; i++) {
			blocks[i] = new Keyblock[pixelGridHeight];
		}
		
		//init the levels blocks here
		
		String levelGen = "22,16".trim();
				
		//translate the String to the level
		
		String[] levelGenCommands = levelGen.split(";");
		
		for(int i = 0; i < levelGenCommands.length; i++) {
			String[] c = levelGenCommands[i].split(",");
			int px = Integer.parseInt(c[0]);
			int py = Integer.parseInt(c[1]);
			
			//TODO implement relative edgeLength
			blocks[px][py] = new Keyblock(px, py, 20, this);
		}
				
	}
	
	

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	
	

	public static void prefetch(AssetManager m) {
		m.load(KEYBLOCK_TEXTURE, Texture.class);
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
	
	public Main getGame() {
		return this.game;
	}

	// TODO Unload all non-global resources
	@Override
	public void dispose() {
		
	}
}

package com.thetriumvirate.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public final class GameScreen implements Screen, InputProcessor {

	private static final int CAM_WIDTH = Main.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Main.SCREEN_HEIGHT;

	// Resource paths
	// private static final String RES_SOMETHING = "somewhere/something";
	private static final String KEYBLOCK_TEXTURE = "graphics/keyblock.png";
	private Texture keyBlock_Texture;

	private BitmapFont font;

	private Main game;
	private OrthographicCamera cam;

	private int pixelGridWidth = 20, pixelGridHeight = 15;
	private Keyblock[][] blocks = new Keyblock[pixelGridWidth][];
	
	private Keybutton[] jumpCount;

	private int jumpsLeft;
	
	public GameScreen(Main game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);

		font = game.assetmanager.easyget(game.RES_DEFAULT_FONT, BitmapFont.class);
		
		loadLevel();
	}
	
	public Keyblock[][] getKeyblocks(){
		return this.blocks;
	}
	
	private void loadLevel() {
		// TODO different setup for each level
		
		jumpsLeft = Main.RAND.nextInt(300);
		
		jumpCount = new Keybutton[3];
		for(int i = 0; i < jumpCount.length; i++) {
			jumpCount[i] = new Keybutton(CAM_WIDTH - 30 - jumpCount.length * (WordButton.NORMAL_SPACING + Keybutton.NORMAL_WIDTH) + i * (Keybutton.NORMAL_WIDTH + WordButton.NORMAL_SPACING) , CAM_HEIGHT - 30 - Keybutton.NORMAL_HEIGHT, Input.Keys.NUM_0, false);
		}
		
		updateJumpCount();
		
		setUpPixelGrid();
	}
	
	private void updateJumpCount() {
		if(jumpsLeft < 0)
			return;
		
		String number = String.valueOf(jumpsLeft);
		if(jumpsLeft < 10) {
			jumpCount[0].updateKeycode(Input.Keys.NUM_0);
			jumpCount[1].updateKeycode(Input.Keys.NUM_0);
			jumpCount[2].updateKeycode(Input.Keys.valueOf(number));
		} else if(jumpsLeft < 100) {
			jumpCount[0].updateKeycode(Input.Keys.NUM_0);
			jumpCount[1].updateKeycode(Input.Keys.valueOf("" + number.charAt(0)));
			jumpCount[2].updateKeycode(Input.Keys.valueOf("" + number.charAt(1)));
		} else {
			jumpCount[0].updateKeycode(Input.Keys.valueOf("" + number.charAt(0)));
			jumpCount[1].updateKeycode(Input.Keys.valueOf("" + number.charAt(1)));
			jumpCount[2].updateKeycode(Input.Keys.valueOf("" + number.charAt(2)));
		}
	}
//	
//	private int getKeycode(int number) {
//		switch(number) {
//		case 0:
//			return Input.Keys.NUM_0;
//		case 1:
//			return Input.Keys.NUM_1;
//		case 2:
//			return Input.Keys.NUM_2;
//		case 3:
//			return Input.Keys.NUM_3;
//		case 4:
//			return Input.Keys.NUM_4;
//		case 5:
//			return Input.Keys.NUM_5;
//		case 6:
//			return Input.Keys.NUM_6;
//		case 7:
//			return input
//		}
//	}

	private void setUpPixelGrid() {
		// full init of the whole array, every entry is null so far
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new Keyblock[pixelGridHeight];
		}

		// init the levels blocks here

		String levelGen = "18,7;4:7,9";

		// translate the String to the level-blocks (Just don't touch anything below ;P)

		String[] levelGenCommands = levelGen.split(";");

		for (int i = 0; i < levelGenCommands.length; i++) {
			String[] commands = levelGenCommands[i].split(",");

			if (!commands[0].contains(":") && !commands[1].contains(":")) {
				int px = Integer.parseInt(commands[0]);
				int py = Integer.parseInt(commands[1]);

				blocks[px][py] = new Keyblock(px, py, Keyblock.getEdgeLength(), font, this);
			} else

			if (commands[0].contains(":")) {
				String[] rangeX = commands[0].split(":");
				for (int x = Integer.parseInt(rangeX[0]); x < Integer.parseInt(rangeX[1]); x++) {
					if (commands[1].contains(":")) {
						String[] rangeY = commands[1].split(":");
						for (int y = Integer.parseInt(rangeY[0]); y < Integer.parseInt(rangeY[1]); y++) {
							blocks[x][y] = new Keyblock(x, y, Keyblock.getEdgeLength(), font, this);
						}
					} else {
						blocks[x][Integer.parseInt(commands[1])] = new Keyblock(x, Integer.parseInt(commands[1]),
								Keyblock.getEdgeLength(), font, this);
					}
				}
			} else

			if (commands[1].contains(":")) {
				String[] rangeY = commands[1].split(":");
				for (int y = Integer.parseInt(rangeY[0]); y < Integer.parseInt(rangeY[1]); y++) {

					blocks[Integer.parseInt(commands[0])][y] = new Keyblock(Integer.parseInt(commands[0]), y,
							Keyblock.getEdgeLength(), font, this);

				}
			}
		}

		// get keyTexture
		keyBlock_Texture = game.assetmanager.get(KEYBLOCK_TEXTURE, Texture.class);

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//GlyphLayout layout = new GlyphLayout();
		
		game.spritebatch.begin();

		// draw the blocks with letters
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				if (blocks[i][j] != null) {
					game.spritebatch.draw(keyBlock_Texture, blocks[i][j].getPosX(), blocks[i][j].getPosY(),
							Keyblock.getEdgeLength(), Keyblock.getEdgeLength());
					GlyphLayout gl = blocks[i][j].getLayout();
					font.draw(game.spritebatch, gl,
							blocks[i][j].getPosX() + Keyblock.getEdgeLength() / 2 - gl.width / 2,
							blocks[i][j].getPosY() + Keyblock.getEdgeLength() / 2 - gl.height / 2);
				}
			}
		}
		
		//layout.setText(font, "Jumps left: ");
		//font.draw(game.spritebatch, layout, CAM_WIDTH - layout.width - 30, CAM_HEIGHT - layout.height - 30);

		for(Keybutton b : jumpCount)
			b.render(game);
		
		game.spritebatch.end();
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

	@Override
	public boolean keyDown(int keycode) {
		boolean ret = false;
		for(Keybutton b : jumpCount) {
			if(b.updateState(keycode, true))
				ret = true;
		}

		jumpsLeft--;
		updateJumpCount();
		
		return ret;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean ret = false;
		for(Keybutton b : jumpCount) {
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

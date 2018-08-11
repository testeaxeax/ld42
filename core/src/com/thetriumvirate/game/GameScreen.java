package com.thetriumvirate.game;

import java.awt.Font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public final class GameScreen implements Screen {

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

	public GameScreen(Main game) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);

		font = game.assetmanager.easyget(game.RES_DEFAULT_FONT, BitmapFont.class);
		
		setUpPixelGrid();
	}

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

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
}

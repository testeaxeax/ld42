package com.thetriumvirate.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.math.Vector2;

public final class GameScreen implements Screen, InputProcessor {

	private static final int CAM_WIDTH = Main.SCREEN_WIDTH;
	private static final int CAM_HEIGHT = Main.SCREEN_HEIGHT;
	
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	// Resource paths
	// private static final String RES_SOMETHING = "somewhere/something";
	private static final String TEX_PATH_UP = "graphics/keyblock.png";
	private static final String TEX_PATH_DOWN = "graphics/keyblock_down.png";
	private Texture keyBlock_texture_up;
	private Texture keyBlock_texture_down;
	
	private static final String  BACKGROUND_TEXTURE = "graphics/background.png";
	private Texture background_texture;

	
	
	
	private BitmapFont font;
	private BitmapFont littleFont;
	
	private static final float SHADE_TARGET = 0.5f;
	private static final float SHADE_STEP = Main.DEBUG ? 0.1f : 0.5f;
	private boolean fadeout;
	private float shadefactor;

	private Main game;
	private OrthographicCamera cam;

	public int pixelGridWidth = 33, pixelGridHeight = 25;
	private Keyblock[][] blocks = new Keyblock[pixelGridWidth][];
	
	private Goal goal;
	
	private Player player;
	
	private Keybutton[] jumpCount;
	
	private int currentLevel;
	
	private boolean showTutorial = false;
	private InfoMonitor[] tutorialMonitors;
	private int currentMonitor = 0;
	
	public GameScreen(Main game, int lvl) {
		this.game = game;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CAM_WIDTH, CAM_HEIGHT);
		cam.update();
		game.spritebatch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0, 0, 1);

		font = game.assetmanager.easyget(game.RES_DEFAULT_FONT, BitmapFont.class);
		littleFont = game.assetmanager.easyget(game.RES_LITTLE_FONT_NAME, BitmapFont.class);
		
		background_texture = game.assetmanager.easyget(BACKGROUND_TEXTURE, Texture.class);
		
		
		
		setUpTutorialMonitors();
		
		this.currentLevel = lvl;
		loadLevel(currentLevel);
		fadeout = false;
		shadefactor = 0f;
	}
	
	public Keyblock[][] getKeyblocks(){
		return this.blocks;
	}
	
	private void loadLevel(int level) {
		// TODO different setup for each level
		jumpCount = new Keybutton[2];
		int spacing = 3;
		for(int i = 0; i < jumpCount.length; i++) {
			jumpCount[i] = new Keybutton(CAM_WIDTH - Keyblock.getEdgeLength() - jumpCount.length * (spacing + Keybutton.NORMAL_WIDTH) + i * (spacing + Keybutton.NORMAL_WIDTH) , CAM_HEIGHT - Keyblock.getEdgeLength() - Keybutton.NORMAL_HEIGHT - 2, Input.Keys.NUM_0, false);
		}
		
		String gridconfig = null;
		float startposfactorx = -1;
		float startposfactory = -1;
		int maxspace = -1;
		boolean doublejumpallowed = false;
		int goalx = -1, goaly = -1;
		
		switch(level) {
		// START LEVEL CONFIG
		case 1:
			gridconfig = "0:32,0;"
					+ "0,0:25;"
					+ "0:32,24;"
					+ "31,5:25;"
					+ "15:31,1;"
					+ "32,0:6";
			startposfactorx = 2f;
			startposfactory = 8f;
			maxspace = 2;
			doublejumpallowed = true;
			goalx = 31;
			goaly = 0;
			showTutorial = true;
			break;
		// END LEVEL CONFIG
		default:
			System.out.print("Error: No such level");
			Gdx.app.exit();
		}
		player = new Player(this, new Vector2(startposfactorx * Keyblock.getEdgeLength(), startposfactory * Keyblock.getEdgeLength()), maxspace, doublejumpallowed);
		updateJumpCount();
		setUpPixelGrid(gridconfig);
		
		this.goal = Goal.createGoal(goalx, goaly, this, this.littleFont);
		if(this.goal == null) {
			System.out.println("Error: Invalid goal position!");
			game.screenmanager.set(game.getMainMenu(), true);
		}
		
		for(Keyblock b : this.goal.getBlocks()) {
			blocks[b.getArrayPosX()][b.getArrayPosY()] = b;
		}
	}
	
	private void updateJumpCount() {
		int jumpsLeft = player.getSpaceRemaining();
		if(jumpsLeft < 0) {
			
			return;
		}
		
		String number = String.valueOf(jumpsLeft);
		if(jumpsLeft < 10) {
			jumpCount[0].updateKeycode(Input.Keys.NUM_0);
			jumpCount[1].updateKeycode(Input.Keys.valueOf(number));
		} else if(jumpsLeft < 100) {
			jumpCount[0].updateKeycode(Input.Keys.valueOf("" + number.charAt(0)));
			jumpCount[1].updateKeycode(Input.Keys.valueOf("" + number.charAt(1)));
		}
	}

	private void setUpPixelGrid(String gridconfig) {
		// full init of the whole array, every entry is null so far
		for (int i = 0; i < blocks.length; i++) {
			blocks[i] = new Keyblock[pixelGridHeight];
    }

		// translate the String to the level-blocks (Just don't touch anything below ;P)

		String[] levelGenCommands = gridconfig.split(";");

		for (int i = 0; i < levelGenCommands.length; i++) {
			String[] commands = levelGenCommands[i].split(",");

			if (!commands[0].contains(":") && !commands[1].contains(":")) {
				int px = Integer.parseInt(commands[0]);
				int py = Integer.parseInt(commands[1]);

				blocks[px][py] = new Keyblock(px, py, Keyblock.getEdgeLength(), littleFont, this);
			} else

			if (commands[0].contains(":")) {
				String[] rangeX = commands[0].split(":");
				for (int x = Integer.parseInt(rangeX[0]); x < Integer.parseInt(rangeX[1]); x++) {
					if (commands[1].contains(":")) {
						String[] rangeY = commands[1].split(":");
						for (int y = Integer.parseInt(rangeY[0]); y < Integer.parseInt(rangeY[1]); y++) {
							blocks[x][y] = new Keyblock(x, y, Keyblock.getEdgeLength(), littleFont, this);
						}
					} else {
						blocks[x][Integer.parseInt(commands[1])] = new Keyblock(x, Integer.parseInt(commands[1]),
								Keyblock.getEdgeLength(), littleFont, this);
					}
				}
			} else

			if (commands[1].contains(":")) {
				String[] rangeY = commands[1].split(":");
				for (int y = Integer.parseInt(rangeY[0]); y < Integer.parseInt(rangeY[1]); y++) {

					blocks[Integer.parseInt(commands[0])][y] = new Keyblock(Integer.parseInt(commands[0]), y,
							Keyblock.getEdgeLength(), littleFont, this);

				}
			}
		}

		// get keyTexture
		keyBlock_texture_up = game.assetmanager.get(TEX_PATH_UP, Texture.class);
		keyBlock_texture_down = game.assetmanager.get(TEX_PATH_DOWN, Texture.class);

	}
	
	
	private void setUpTutorialMonitors() {
		int width = 250, height = 150;
		tutorialMonitors = new InfoMonitor[4];
		
		tutorialMonitors[0] = new InfoMonitor(game.SCREEN_WIDTH/6, game.SCREEN_HEIGHT/3, width, height, "Use\nA and D\nto move\nLEFT and RIGHT!", game);
		tutorialMonitors[1] = new InfoMonitor(game.SCREEN_WIDTH/4, game.SCREEN_HEIGHT/3, width, height, "Use\nSPACE\nto\nJUMP!", game);
		tutorialMonitors[2] = new InfoMonitor(game.SCREEN_WIDTH/2, game.SCREEN_HEIGHT/4*3 - 10, width, height, "Don't jump too often!\nDon't run out of\nSPACE", game);
		tutorialMonitors[3] = new InfoMonitor(game.SCREEN_WIDTH/3*2, game.SCREEN_HEIGHT/3, width, height, "Fit between\nALT and ALT_GR\nto finish the level!", game);
	}
	
	
	private void toggleKeys(String s, boolean press) {
		for(int i = 0; i < blocks.length; i++) {
			for(int j = 0; j < blocks[i].length; j++) {
				if(blocks[i][j] != null && s.equals(blocks[i][j].getLetter()))blocks[i][j].setPressed(press);
			}
		}
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
		
		//draw background
		game.spritebatch.draw(background_texture, 0, 0, game.SCREEN_WIDTH, game.SCREEN_HEIGHT);

		// draw the blocks with letters
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				if (blocks[i][j] != null) {
					game.spritebatch.draw(blocks[i][j].isPressed() ? keyBlock_texture_down : keyBlock_texture_up,
							blocks[i][j].getPosX(), blocks[i][j].getPosY(),
							Keyblock.getEdgeLength(), Keyblock.getEdgeLength());
					GlyphLayout gl = blocks[i][j].getLayout();
					littleFont.draw(game.spritebatch, gl, blocks[i][j].getPosX() + Keyblock.getEdgeLength()/5,
							blocks[i][j].getPosY() + Keyblock.getEdgeLength() - Keyblock.getEdgeLength()/5 - (blocks[i][j].isPressed() ? blocks[i][j].getLayout().height/5 : 0));
				}
			}
		}
		
		//layout.setText(font, "Jumps left: ");
		//font.draw(game.spritebatch, layout, CAM_WIDTH - layout.width - 30, CAM_HEIGHT - layout.height - 30);

		for(Keybutton b : jumpCount)
			b.render(game);
		
		if(showTutorial) {
			//calc which monitor should be shown
			currentMonitor = 0;
			if(player.getPosition().x > 250) {
				currentMonitor = 1;
				if(player.getPosition().x > 400) {
					currentMonitor = 2;
					if(player.getPosition().x > 750) {
						currentMonitor = 3;
					}
				}
			}
			tutorialMonitors[currentMonitor].render(game);
		}
		
		
		
		
		// Render player
		player.update(delta);
		game.spritebatch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, player.getWidth(), player.getHeight());
		
		if(fadeout) {
			if(shadefactor < SHADE_TARGET) {
				shadefactor += SHADE_STEP * delta;
				if(shadefactor > SHADE_TARGET) {
					shadefactor = SHADE_TARGET;
				}
			}
			Pixmap shadepixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			shadepixmap.setColor(0f, 0f, 0f, shadefactor);
			shadepixmap.fill();
			game.spritebatch.draw(new Texture(shadepixmap), 0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
			shadepixmap.dispose();
			if(shadefactor == SHADE_TARGET) {
				game.screenmanager.set(new EndOfLevelScreen(game, player.getSpaceRemaining(), currentLevel), true);
			}
		}
		
		game.spritebatch.end();
	}

	public static void prefetch(AssetManager m) {
		Player.prefetch(m);
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
		player.dispose();
	}
	// TODO Implement
	public void gameOver(int reason) {
		switch(reason) {
			case(0):game.screenmanager.set(new GameOverScreen(game, "You ran out of space", currentLevel), true); break;
			case(1):game.screenmanager.set(new GameOverScreen(game, "You fell out of the world", currentLevel), true); break;
		}
	}
	
	public void endOfLevel() {
		player.setFreeze(true);
		fadeout = true;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode > 28 && keycode < 55)
		toggleKeys(String.valueOf(ALPHABET.charAt(keycode - 29)), true);
		boolean ret = false;
		for(Keybutton b : jumpCount) {
			if(b.updateState(keycode, true))
				ret = true;
		}
		if(keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
			player.setMovementDirection(1);
		}else if(keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
			player.setMovementDirection(2);
		}
		if(keycode == Input.Keys.SPACE) {
			player.decrementSpaceRemaining();
			player.jump();
			player.setPressed(true);
		}
		updateJumpCount();
		
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode > 28 && keycode < 55)
		toggleKeys(String.valueOf(ALPHABET.charAt(keycode - 29)), false);
		boolean ret = false;
		for(Keybutton b : jumpCount) {
			if(b.updateState(keycode, false))
				ret = true;
		}
		if((keycode == Input.Keys.A && player.getMovementDirection() == 1) ||
				(keycode == Input.Keys.D && player.getMovementDirection() == 2) ||
				(keycode == Input.Keys.LEFT && player.getMovementDirection() == 1) ||
				(keycode == Input.Keys.RIGHT && player.getMovementDirection() == 2)) {
			player.setMovementDirection(0);
		}
		if(keycode == Input.Keys.SPACE) {
			player.setPressed(false);
		}
		return true;
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

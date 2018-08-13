package com.thetriumvirate.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player {
	

	private static final String RES_PLAYER_TEXTURE = "graphics/player_texture.png";
	private static final String RES_PLAYER_TEXTURE_DOWN = "graphics/player_texture_down.png";
	// TODO Fix values
	private static final float GRAVITATIONAL_ACCELERATION = Main.DEBUG ? -50 : -800;
	private static final float MOVEMENT_ACCELERATION = Main.DEBUG ? 50 : 800;
	private static final float JUMP_SPEED = Main.DEBUG ? 100 : 300;
	private static final float DOUBLE_JUMP_SPEED = Main.DEBUG ? 100 : 230;
	private static final float AIR_RESISTANCE = Main.DEBUG ? 0.5f : -0.3f;
	private static final int DEFAULT_WIDTH = 30;
	private static final int DEFAULT_HEIGHT = 60;
	
	private Vector2 position;
	private int space_remaining;
	private Texture player_texture;
	private Texture player_texture_down;
	private GameScreen gamescreen;
	private int width;
	private int height;
	private Vector2 speed;
	private int consecutivejumps;
	private boolean startjump;
	private boolean movingleft, movingright;
	private boolean doublejumpallowed;
	private boolean freeze;
	
	private static final String RES_JUMP_SOUND = "audio/jump_spacebar.wav";
	private Sound jumpSound;
	
	private boolean pressed = false;
	
	public Player(GameScreen gamescreen, Vector2 position, int space_remaining, boolean doublejumpallowed) {
		this.position = position;
		this.space_remaining = space_remaining;
		this.gamescreen = gamescreen;
		this.width = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;
		this.doublejumpallowed = doublejumpallowed;
		this.speed = new Vector2(0, 0);
		consecutivejumps = 0;
		
		player_texture = gamescreen.getGame().assetmanager.get(RES_PLAYER_TEXTURE, Texture.class);
		player_texture_down = gamescreen.getGame().assetmanager.get(RES_PLAYER_TEXTURE_DOWN, Texture.class);
		jumpSound = gamescreen.getGame().assetmanager.get(RES_JUMP_SOUND);
	}
	
	public static void prefetch(AssetManager m) {
		m.load(RES_PLAYER_TEXTURE, Texture.class);
		m.load(RES_PLAYER_TEXTURE_DOWN, Texture.class);

		m.load(RES_JUMP_SOUND, Sound.class);
	}
	
	public void update(float delta) {
		if(freeze) {
			return;
		}
		int blockedgelength = Keyblock.getEdgeLength();
		Keyblock[][] blocks = gamescreen.getKeyblocks();
		
		if(movingright) {
			speed.x += MOVEMENT_ACCELERATION * delta;
		}else if(movingleft) {
			speed.x -= MOVEMENT_ACCELERATION * delta;
		}
		speed.x = speed.x * (1f - ((1f - AIR_RESISTANCE) * delta * 3.5f));
		// Gravity
		speed.y += GRAVITATIONAL_ACCELERATION * delta;
		speed.y = speed.y * (1f - ((1f - AIR_RESISTANCE) * delta));
		
		// Updating x-coordinate
		Vector2 previousposition = new Vector2(position);
		position.x += speed.x * delta;
		position.y += speed.y * delta;
		
		int playerleftxboundary = (int) position.x;
		int playerrightxboundary = playerleftxboundary + width;
		int playerloweryboundary = (int) position.y;
		int playerupperyboundary = playerloweryboundary + height;
		
		int previousplayerleftxboundary = (int) previousposition.x;
		int previousplayerrightxboundary = previousplayerleftxboundary + width;
		int previousplayerloweryboundary = (int) previousposition.y;
		int previousplayerupperyboundary = previousplayerloweryboundary + height;
		
		
		int ystartindex = ((int) position.y + height) / blockedgelength;
		int yendindex = ((int) position.y) / blockedgelength;
		int xstartindex = ((int) position.x) / blockedgelength;
		int xendindex = ((int) position.x + width) / blockedgelength;
		
		for(int i = 0; i <= xendindex - xstartindex; i++) {
			int xindex = xstartindex + i;
			if(blocks[xindex] != null) {
				for(int e = 0; e >= yendindex - ystartindex; e--) {
					int yindex = ystartindex + e;
					if(blocks[xindex][yindex] != null) {
						boolean xcollision, ycollision;
						boolean endoflevel = false;
						int leftxboundary = xindex * blockedgelength;
						int rightxboundary = leftxboundary + blockedgelength;
						int loweryboundary = yindex * blockedgelength;
						int upperyboundary = loweryboundary + blockedgelength;
					
						// Checking in x-direction
						if(playerrightxboundary < leftxboundary) {
							xcollision = false;
						}
						else if(playerleftxboundary > rightxboundary) {
							xcollision = false;
						}else {
							xcollision = true;
						}
					
						// Checking in y-direction
						if(playerloweryboundary >= upperyboundary) {
							ycollision = false;
						}
						else if(playerupperyboundary <= loweryboundary) {
							ycollision = false;
						}else {
							ycollision = true;
						}
					
						// left = 0, right = 1, no collision = -1
						int xdirection = -1;
						// top = 0, bottom = 1, no collision = -1
						int ydirection = -1;
					
						// Checking x direction
						if(xcollision) {
							if(previousplayerrightxboundary <= leftxboundary) {
								xdirection = 0;
							}
							if(previousplayerleftxboundary >= rightxboundary) {
								xdirection = 1;
							}
						}
					
						// Checking y direction
						if(ycollision) {
							if(previousplayerloweryboundary >= upperyboundary) {
								ydirection = 0;
							}
							if(previousplayerupperyboundary <= loweryboundary) {
								ydirection = 1;
							}
						}
					
						if(ydirection == 0) {
							if(previousplayerrightxboundary > leftxboundary && previousplayerleftxboundary < rightxboundary) {
								// Player is on top of something
								position.y = (yindex + 1) * blockedgelength;
								speed.y = 0;
								if(blocks[xindex][yindex].getLetter() == "ALT") {
									endoflevel = true;
								}
							}
						}else if(ydirection == 1) {
							if(previousplayerrightxboundary > leftxboundary && previousplayerleftxboundary < rightxboundary) {
								// Player is jumping against something from below
								position.y = yindex * blockedgelength - height;
								speed.y = 0;
							}
						}
						
						// Update y-values
						playerloweryboundary = (int) position.y;
						playerupperyboundary = playerloweryboundary + height;
					
						if(xdirection == 0) {
							// FIXME seems like it needs the + 2 to counteract float rounding
							if(playerloweryboundary + 2 < upperyboundary && playerupperyboundary - 2 > loweryboundary) {
								// Player is on the left side of something
								position.x = xindex * blockedgelength - width;
								speed.x = 0;
							}
						}else if(xdirection == 1) {
							if(playerloweryboundary + 2 < upperyboundary && playerupperyboundary - 2 > loweryboundary) {
								// Player is on the right side of something
								position.x = (xindex + 1) * blockedgelength;
								speed.x = 0;
							}
						}
						
						if(endoflevel) {
							playerleftxboundary = (int) position.x;
							playerrightxboundary = playerleftxboundary + width;
							if(playerleftxboundary >= leftxboundary && playerrightxboundary <= rightxboundary) {
								gamescreen.endOfLevel();
							}
						}
					}
				}
			}
		}

		if(startjump) {
			if(consecutivejumps == 0) {
				speed.y += JUMP_SPEED;
				if(space_remaining >= 0)jumpSound.play(0.8f);
			}else if(consecutivejumps == 1 && doublejumpallowed) {
				speed.y += DOUBLE_JUMP_SPEED;
				if(space_remaining >= 0)jumpSound.play(0.8f);
			}
			startjump = false;
			consecutivejumps++;
		}
		
		if(speed.y == 0) {
			consecutivejumps = 0;
		}
		
		if(space_remaining < 0) {
			// 0 = no space left
			gamescreen.gameOver(0);
		}
		if(position.y < 0) {
			// 1 = you died
			gamescreen.gameOver(1);
		}
		
	}
	
	public Texture getTexture() {
		return pressed ? player_texture_down : player_texture;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public int getSpaceRemaining() {
		return space_remaining;
	}
	
	public void decrementSpaceRemaining() {
		space_remaining -= 1;
	}
	
	public void dispose() {
		gamescreen.getGame().assetmanager.unload(RES_PLAYER_TEXTURE);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	
	// 0 = no movement, 1 = left, 2 = right
	public void setMovementDirection(int d) {
		switch(d) {
		case 0:
			movingright = false;
			movingleft = false;
			break;
		case 1:
			movingright = false;
			movingleft = true;
			break;
		case 2:
			movingright = true;
			movingleft = false;
		}
	}
	
	public int getMovementDirection() {
		if(movingright) {
			return 2;
		}else if(movingleft) {
			return 1;
		}else {
			return 0;
		}
	}
	
	public void jump() {
		startjump = true;
	}
	
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	
	public void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}
	
	public boolean getFreeze() {
		return freeze;
	}
}

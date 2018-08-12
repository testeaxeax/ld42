package com.thetriumvirate.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player {

	private static final String RES_PLAYER_TEXTURE = "graphics/player_texture.png";
	// TODO Fix values
	private static final int GRAVITATIONAL_ACCELERATION = -600;
	private static final int MOVEMENTE_ACCELERATION = 4;
	private static final int JUMP_SPEED = 100;
	private static final int DOUBLE_JUMP_SPEED = 20;
	private static final float AIR_RESISTANCE = 0.9f;
	private static final int DEFAULT_WIDTH = 30;
	private static final int DEFAULT_HEIGHT = 60;
	
	private Vector2 position;
	private int space_remaining;
	private Texture player_texture;
	private GameScreen gamescreen;
	private int width;
	private int height;
	private Vector2 speed;
	private int consecutivejumps;
	private boolean startjump;
	private boolean movingleft, movingright;
	private boolean doublejumpallowed;
	
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
	}
	
	public static void prefetch(AssetManager m) {
		m.load(RES_PLAYER_TEXTURE, Texture.class);
	}
	
	public void update(float delta) {
		int blockedgelength = Keyblock.getEdgeLength();
		int leftblockwidthindex = ( (int) position.x) / blockedgelength;
		int blockin = ( (int) position.x) % blockedgelength;
		int leftblockheightindex = -1;
		int rightblockheightindex = -1;
		int maxblockheight = -1;
		Keyblock[][] blocks = gamescreen.getKeyblocks();
		
		if(movingright) {
			speed.x += MOVEMENTE_ACCELERATION;
		}else if(movingleft) {
			speed.x -= MOVEMENTE_ACCELERATION;
		}
		speed.x *= AIR_RESISTANCE;
		// Updating x-coordinate
		position.x += speed.x * delta;
		for(int i = 0; i < blocks[leftblockwidthindex].length; i++) {
			if(blocks[leftblockwidthindex][i] != null) {
				leftblockheightindex = i;
			}else if(position.y + height < blockedgelength * leftblockheightindex) {
				break;
			}
		}
		if(blockin + width > blockedgelength) {
			for(int i = 0; i < blocks[leftblockwidthindex + 1].length; i++) {
				if(blocks[leftblockwidthindex + 1][i] != null) {
						rightblockheightindex = i;
				}else if(position.y + height < blockedgelength * leftblockheightindex) {
					break;
				}
			}
		}
		
		int leftblockheight = (leftblockheightindex + 1) * blockedgelength;
		int rightblockheight = (rightblockheightindex + 1) * blockedgelength;
		
		// Checking for collision in x-direction
		// FIXME ugly
		boolean left = false;
		boolean right = false;
		float previousxpos = position.x - speed.x * delta;
		if(position.x < (leftblockwidthindex + 1) * blockedgelength && leftblockheightindex > rightblockheightindex && previousxpos > leftblockwidthindex * blockedgelength
				&& position.y < leftblockheight && position.y + height > leftblockheightindex * blockedgelength) {
			left = true;
		}
		else if(position.x < (leftblockwidthindex + 1) * blockedgelength && rightblockheightindex > leftblockheightindex && previousxpos < (leftblockwidthindex + 1) * blockedgelength
				&& position.y < rightblockheight && position.y + height > rightblockheightindex * blockedgelength) {
			right = true;
		}
		// Player's left/right side
		if(left) {
			speed.x = 0;
			position.x = (leftblockwidthindex + 1) * blockedgelength;
		}
		if(right) {
			speed.x = 0;
			position.x = (leftblockwidthindex + 1) * blockedgelength - width + 1;
		}
		
		// Updating y-coordinate
		position.y += speed.y * delta;
		
		// Updating values
		leftblockwidthindex = (int) position.x / blockedgelength;
		blockin = (int) position.x % blockedgelength;
		leftblockheightindex = -1;
		rightblockheightindex = -1;
		
		
		
		for(int i = 0; i < blocks[leftblockwidthindex].length; i++) {
			if(position.y > i * blockedgelength || leftblockheightindex != -1){
				if(blocks[leftblockwidthindex][i] != null) {
					leftblockheightindex = i;
				}else if(position.y + height < blockedgelength * leftblockheightindex) {
					break;
				}
			}
		}
		if(blockin + width > blockedgelength) {
			for(int i = 0; i < blocks[leftblockwidthindex + 1].length; i++) {
				if(position.y > i * blockedgelength || rightblockheightindex != -1){
					if(blocks[leftblockwidthindex + 1][i] != null) {
						rightblockheightindex = i;
					}else if(position.y + height < blockedgelength * leftblockheightindex) {
						break;
					}
				}
			}
		}
		
		leftblockheight = (leftblockheightindex + 1) * blockedgelength;
		rightblockheight = (rightblockheightindex + 1) * blockedgelength;
		
		// Checking for collision in y-direction
		if(leftblockheightindex < rightblockheightindex) {
			maxblockheight = rightblockheight;
		}else {
			maxblockheight = leftblockheight;
		}
		if(position.y > maxblockheight || (leftblockheightindex == -1 && rightblockheightindex == -1)) {
			speed.y += GRAVITATIONAL_ACCELERATION * delta;
			speed.y *= AIR_RESISTANCE;
		}
		// FIXME ugly
		boolean yreset = false;
		//float previousxpos = position.x - speed.x * delta;
		float previousypos = position.y - speed.y * delta;
		if(position.y < leftblockheight && previousypos > leftblockheight && leftblockheightindex == rightblockheightindex) {
			yreset = true;
		}
		else if(position.y < leftblockheight && leftblockheightindex > rightblockheightindex && previousypos > leftblockheight) {
			yreset = true;
		}
		else if(position.y < rightblockheight && rightblockheightindex > leftblockheightindex && previousypos > rightblockheight) {
			yreset = true;
		}
		if(yreset) {
			speed.y = 0;
			position.y = maxblockheight;
		}
		
		if(startjump) {
			if(consecutivejumps == 0) {
				speed.y += JUMP_SPEED;
			}else if(consecutivejumps == 1 && doublejumpallowed) {
				speed.y += DOUBLE_JUMP_SPEED;
			}
			startjump = false;
			consecutivejumps++;
			space_remaining--;
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
		return player_texture;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public int getSpaceRemaining() {
		return space_remaining;
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
}

package com.thetriumvirate.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player {

	private static final String RES_PLAYER_TEXTURE = "graphics/player_texture.png";
	// TODO Fix values
	private static final int GRAVITATIONAL_ACCELERATION = -10;
	private static final int MOVEMENTE_ACCELERATION = 4;
	private static final int JUMP_SPEED = 40;
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
	private int xacceleration;
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
		consecutivejumps = 0;
		player_texture = gamescreen.getGame().assetmanager.get(RES_PLAYER_TEXTURE, Texture.class);
	}
	
	public static void prefetch(AssetManager m) {
		m.load(RES_PLAYER_TEXTURE, Texture.class);
	}
	
	public void update(float delta) {
		System.out.println("updateplayer");
		int blockedgelength = Keyblock.getEdgeLength();
		int widthindex = (int) position.x / blockedgelength;
		int blockin = (int) position.x % blockedgelength;
		int heightindex1 = -1;
		int heightindex2 = -1;
		int blockheight;
		Keyblock[][] blocks = gamescreen.getKeyblocks();
		
		if(movingright) {
			speed.x += MOVEMENTE_ACCELERATION;
		}else if(movingleft) {
			speed.x -= MOVEMENTE_ACCELERATION;
		}
			
		speed.x += xacceleration * delta;
		speed.x *= AIR_RESISTANCE;
		// Updating x-coordinate
		position.x += speed.x * delta;
		for(int i = 0; i < blocks[widthindex].length; i++) {
			if(blocks[widthindex][i] != null) {
				heightindex1 = i;
			}else if(position.y + height < blockedgelength * heightindex1) {
				break;
			}
		}
		if(blockin + width > blockedgelength) {
			for(int i = 0; i < blocks[widthindex + 1].length; i++) {
				if(blocks[widthindex + 1][i] != null) {
						heightindex2 = i;
				}else if(position.y + height < blockedgelength * heightindex1) {
					break;
				}
			}
		}
		
		// Checking for collision in x-direction
		if(position.y < heightindex1 * blockedgelength) {
			position.x = widthindex * blockedgelength;
			speed.x = 0;
		}
		
		if(position.y < heightindex2 * blockedgelength) {
			position.x = (widthindex * blockedgelength) - width;
			speed.x = 0;
		}
		// Updating values
		widthindex = (int) position.x / blockedgelength;
		blockin = (int) position.x % blockedgelength;
		heightindex1 = -1;
		heightindex2 = -1;
		
		for(int i = 0; i < blocks[widthindex].length; i++) {
			if(blocks[widthindex][i] != null) {
				heightindex1 = i;
			}else if(position.y + height < blockedgelength * heightindex1) {
				break;
			}
		}
		if(blockin + width > blockedgelength) {
			for(int i = 0; i < blocks[widthindex + 1].length; i++) {
				if(blocks[widthindex + 1][i] != null) {
						heightindex2 = i;
				}else if(position.y + height < blockedgelength * heightindex1) {
					break;
				}
			}
		}
		
		// Checking for collision in y-direction
		if(heightindex1 < heightindex2) {
			blockheight = heightindex2 * blockedgelength;
		}else {
			blockheight = heightindex1 * blockedgelength;
		}
		if(position.y > blockheight) {
			speed.y += GRAVITATIONAL_ACCELERATION * delta;
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
			speed.y *= AIR_RESISTANCE * delta;
		}
		if(position.y <= blockheight) {
			speed.y = 0;
			position.y = blockheight;
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

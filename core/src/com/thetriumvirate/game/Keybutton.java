package com.thetriumvirate.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Keybutton {
	private int x, y, width, height;
	private String key;
	private int keycode;

	private static Texture background;
	private static Texture background_pressed;
	private static final String TEX_PATH_UP = "graphics/keyblock.png";
	private static final String TEX_PATH_DOWN = "graphics/keyblock_down.png";
	public static final int NORMAL_WIDTH = 50;
	public static final int NORMAL_HEIGHT = 50;
	private static BitmapFont font;

	private boolean stateSwitching;
	private boolean state;

	public Keybutton(int x, int y, int keycode, boolean stateSwitching) {
		this.x = x;
		this.y = y;
		this.keycode = keycode;
		this.key = Input.Keys.toString(keycode);

		this.width = NORMAL_WIDTH;
		this.height = NORMAL_HEIGHT;
		this.stateSwitching = stateSwitching;
		this.state = false;
	}
	
	public void updateKeycode(int keycode) {
		this.keycode = keycode;
		this.key = Input.Keys.toString(this.keycode);
	}

	public boolean updateState(int keycode, boolean down) {
		if (keycode == this.keycode) {
			if (down) {
				this.state = this.stateSwitching ? !this.state : true;
			} else {
				if (!this.stateSwitching)
					this.state = false;
			}
			return true;
		}

		return false;
	}

	public void render(Main game) {
		GlyphLayout layout;

		game.spritebatch.draw(this.state ? background_pressed : background, this.x, this.y, this.width, this.height);

		layout = new GlyphLayout(font, this.key);
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		font.draw(game.spritebatch, layout, this.x + this.width / 2 - layout.width / 2,
				this.y + this.height / 2 + layout.height / 2);
	}

	public static void load(Main game) {
		background = game.assetmanager.get(TEX_PATH_UP, Texture.class);
		background_pressed = game.assetmanager.get(TEX_PATH_DOWN, Texture.class);
		font = game.assetmanager.get(game.RES_DEFAULT_FONT, BitmapFont.class);
	}

	public static void prefetch(AssetManager m) {
		m.load(TEX_PATH_UP, Texture.class);
		m.load(TEX_PATH_DOWN, Texture.class);
	}

	public static void dispose(AdvancedAssetManager m) {
		m.unload(TEX_PATH_UP);
		m.unload(TEX_PATH_DOWN);
	}

	public int getWidth() {
		return this.width;
	}
	
	public boolean isPressed() {
		return this.state;
	}
	
	public int getHeight() {
		return this.height;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public void setPressed(boolean pressed) {
		this.state = pressed;
	}
}

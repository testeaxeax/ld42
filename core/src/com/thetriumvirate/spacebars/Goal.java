package com.thetriumvirate.spacebars;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Goal {
	private Keyblock ctrl, alt, altgr;
	
	private Goal(int xindex, int yindex, GameScreen screen, BitmapFont font) {
		ctrl = new Keyblock(xindex, yindex, Keyblock.getEdgeLength(), "CTRL", font, screen);
		alt = new Keyblock(xindex, yindex + 1, Keyblock.getEdgeLength(), "ALT", font, screen);
		altgr = new Keyblock(xindex, yindex + 4, Keyblock.getEdgeLength(), "ALT\nGR", font, screen);
	}
	
	
	/**
	 * Give blockcoordinates of the lowest part of the goal; size: 1x5 by now
	 * @param xindex
	 * @param yindex
	 * @return
	 */
	public static Goal createGoal(int xindex, int yindex, GameScreen screen, BitmapFont font) {
		if(xindex < 0 || xindex >= screen.pixelGridWidth || yindex < 0 || yindex + 4 >= screen.pixelGridHeight)
			return null;
		
		return new Goal(xindex, yindex, screen, font);
	}
	
	/**
	 * Method used for adding the blocks to the grid
	 * @return The three blocks (ctrl, alt, altgr)
	 */
	public Keyblock[] getBlocks() {
		Keyblock[] ret = {ctrl, alt, altgr};
		return ret;
	}
}

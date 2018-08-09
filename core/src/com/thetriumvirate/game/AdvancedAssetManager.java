package com.thetriumvirate.game;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;

public class AdvancedAssetManager extends AssetManager {

	/**This method ensures that the requested resource got loaded and returns a reference to it
	 * @return Returns a reference to the requested resource*/
	public <T> T easyget(String filename, Class<T> type) {
		if(!super.containsAsset(filename)) {
			super.load(filename, type);
		}
		super.finishLoadingAsset(filename);
		return super.get(filename, type);
	}
	
	public <T> T easyget(String filename, Class<T> type, AssetLoaderParameters<T> param) {
		if(!super.containsAsset(filename)) {
			super.load(filename, type, param);
		}
		super.finishLoadingAsset(filename);
		return super.get(filename, type);
	}
}

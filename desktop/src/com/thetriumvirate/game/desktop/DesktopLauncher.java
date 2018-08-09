package com.thetriumvirate.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thetriumvirate.game.Main;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Main.SCREEN_HEIGHT;
		config.width = Main.SCREEN_WIDTH;
		config.resizable = false;
		// TODO Set title
		config.title = "Game";
		// TODO Create icons
		//config.addIcon("graphics/icon32.png", Files.FileType.Internal);
		//config.addIcon("graphics/icon16.png", Files.FileType.Internal);
		new LwjglApplication(new Main(new DesktopFontLoader()), config);
	}
}

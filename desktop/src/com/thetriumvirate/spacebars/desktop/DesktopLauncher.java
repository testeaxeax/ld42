package com.thetriumvirate.spacebars.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thetriumvirate.spacebars.Main;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Main.SCREEN_HEIGHT;
		config.width = Main.SCREEN_WIDTH;
		config.resizable = false;
		config.title = "Spacebars";
		config.addIcon("graphics/icon_64.png", Files.FileType.Internal);
		config.addIcon("graphics/icon_32.png", Files.FileType.Internal);
		config.addIcon("graphics/icon_16.png", Files.FileType.Internal);
		new LwjglApplication(new Main(new DesktopFontLoader()), config);
	}
}

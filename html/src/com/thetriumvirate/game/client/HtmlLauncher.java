package com.thetriumvirate.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
//import com.google.gwt.user.client.Window;
import com.thetriumvirate.game.Main;

public class HtmlLauncher extends com.badlogic.gdx.backends.gwt.GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
        	// TODO Do we still need this?
        	/*int height, width;
        	float ratio = 1024f / 800f;
        	if(Window.getClientHeight() < Window.getClientWidth()) {
        		height = (int) (Window.getClientHeight() * 0.95f);
        		width = (int) (ratio * height);
        	}else {
        		width = Window.getClientWidth();
        		height = (int) (width / ratio);
        	}
        	Main.SCREEN_WIDTH = width;
        	Main.SCREEN_HEIGHT = height;*/
                return new Main(new HtmlFontLoader());
        }
}
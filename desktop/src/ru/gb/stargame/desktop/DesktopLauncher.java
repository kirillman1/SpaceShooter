package ru.gb.stargame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ru.gb.stargame.Star2DGame;
import ru.gb.stargame.StarGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		float aspect = 3f/4f;
		//float aspect = 480f/854f;
		config.height = 500;
		config.width = (int) (config.height * aspect);
		new LwjglApplication(new Star2DGame(false), config);
	}
}

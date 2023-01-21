package fr.pixdad.Game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.pixdad.Game.TBSGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 920;
		//config.fullscreen = true;
		new LwjglApplication(new TBSGame(), config);
	}
}

package escape.room.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import escape.room.game.EscapeRoomGame;
import com.badlogic.gdx.Files;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "古文字密室逃脫";
		config.width = 700;
		config.height = 480;
		config.resizable = false;

		config.addIcon("images/icons/16x16.png", Files.FileType.Internal);
		config.addIcon("images/icons/32x32.png", Files.FileType.Internal);
		config.addIcon("images/icons/128x128.png", Files.FileType.Internal);

		new LwjglApplication(new EscapeRoomGame(), config);
	}
}

package escape.room.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import escape.room.game.screen.*;

public class EscapeRoomGame extends Game {
	
	private AssetManager assetManager;

	@Override
	public void create() {
		assetManager = new AssetManager();
		setScreen(ScreenManager.getLoadingScreen(this));
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		ScreenManager.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

}

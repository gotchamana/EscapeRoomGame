package escape.room.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class EscapeRoomGame extends Game {
	
	private LoadingScreen loadingScreen;
	private AssetManager assetManager;

	@Override
	public void create() {
		assetManager = new AssetManager();
		loadingScreen = new LoadingScreen(this);

		setScreen(loadingScreen);
	}

	@Override
	public void dispose() {
		loadingScreen.dispose();
		assetManager.dispose();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

}

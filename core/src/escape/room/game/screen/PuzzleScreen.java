package escape.room.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.assets.AssetManager;
import escape.room.game.gameobject.*;
import escape.room.game.EscapeRoomGame;
import com.badlogic.gdx.graphics.*;

public class PuzzleScreen implements Screen {
	
	private EscapeRoomGame game;
	private AssetManager assetsManager;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Map westMapRightDrawer;

	public PuzzleScreen(EscapeRoomGame game) {
		this.game = game;
		assetsManager = game.getAssetManager();
		camera = new OrthographicCamera(640, 480);
		batch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

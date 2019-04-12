package escape.room.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.assets.AssetManager;
import escape.room.game.gameobject.*;
import escape.room.game.EscapeRoomGame;
import escape.room.game.event.*;
import escape.room.game.ui.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class PuzzleScreen implements Screen {
	
	private EscapeRoomGame game;
	private AssetManager assetManager;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ItemTray itemTray;
	private PuzzleBoard westMapRightDrawer;

	public PuzzleScreen(EscapeRoomGame game) {
		this.game = game;

		assetManager = game.getAssetManager();

		itemTray = ItemTray.getItemTray();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, 700, 480);

		batch = new SpriteBatch();
		
		initMap();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		westMapRightDrawer.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		westMapRightDrawer.draw(batch);
		itemTray.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputMultiplexer(new PuzzleScreenInputHandler(this), new UIIputHandler(itemTray)));
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

	public PuzzleBoard getPuzzleBoard() {
		return westMapRightDrawer;
	}

	private void initMap() {
		// 西邊地圖之右邊抽屜
		TextureAtlas uiAtlas = assetManager.get("images/uis/ui.atlas");
		TextureAtlas mapAtlas = assetManager.get("images/maps/west_map_right_drawer/west_map_right_drawer.atlas");
		westMapRightDrawer = new PuzzleBoard();

		// 拼圖
		Puzzle puzzle1 = new Puzzle(mapAtlas.createSprite("puzzle1"));
		puzzle1.flip(false, true);
		puzzle1.setPosition(100, 100);
		puzzle1.setTargetRegion(100, 100, 20);

		Puzzle puzzle2 = new Puzzle(mapAtlas.createSprite("puzzle2"));
		puzzle2.flip(false, true);
		puzzle2.setPosition(200, 100);

		Puzzle puzzle3 = new Puzzle(mapAtlas.createSprite("puzzle3"));
		puzzle3.flip(false, true);
		puzzle3.setPosition(100, 200);

		Puzzle puzzle4 = new Puzzle(mapAtlas.createSprite("puzzle4"));
		puzzle4.flip(false, true);
		puzzle4.setPosition(100, 300);

		// 箭頭
		TouchableSprite arrowDown = new Arrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.flip(false, true);
		arrowDown.setOnTouchDown(e -> {
			game.setScreen(ScreenManager.getERScreen(game));
			return true;
		});

		westMapRightDrawer.addSprites(puzzle1, puzzle2, puzzle3, puzzle4, arrowDown);
	}
}

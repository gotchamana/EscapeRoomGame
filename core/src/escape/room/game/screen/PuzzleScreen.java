package escape.room.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import escape.room.game.*;
import escape.room.game.ui.*;
import escape.room.game.event.*;
import escape.room.game.gameobject.*;
import escape.room.game.ui.*;

public class PuzzleScreen implements Screen {
	
	private EscapeRoomGame game;
	private AssetManager assetManager;
	private OrthographicCamera camera;
	private GameData gameData;
	private SpriteBatch batch;
	private ItemTray itemTray;
	private PuzzleBoard westMapRightDrawer;
	private Puzzle[] puzzles;

	public PuzzleScreen(EscapeRoomGame game) {
		this.game = game;

		assetManager = game.getAssetManager();
		itemTray = ItemTray.getItemTray();
		gameData = GameData.getGameData();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, 700, 480);

		batch = new SpriteBatch();
		
		puzzles = new Puzzle[8];
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

		// 背景
		CustomSprite bg = new CustomSprite(mapAtlas.createSprite("bg"));
		bg.flip(false, true);

		// 拼圖
		Puzzle puzzle1 = new Puzzle(mapAtlas.createSprite("puzzle1"));
		puzzle1.flip(false, true);
		puzzle1.setPosition(50, 200);
		puzzle1.setTargetRegion(266, 15, 20);
		puzzles[0] = puzzle1;
		puzzle1.setOnTouchUp(getTouchEventHandler(puzzle1));

		Puzzle puzzle2 = new Puzzle(mapAtlas.createSprite("puzzle2"));
		puzzle2.flip(false, true);
		puzzle2.setPosition(200, 100);
		puzzle2.setTargetRegion(309, 31, 20);
		puzzles[1] = puzzle2;
		puzzle2.setOnTouchUp(getTouchEventHandler(puzzle2));

		Puzzle puzzle3 = new Puzzle(mapAtlas.createSprite("puzzle3"));
		puzzle3.flip(false, true);
		puzzle3.setPosition(300, 200);
		puzzle3.setTargetRegion(239, 95, 20);
		puzzles[2] = puzzle3;
		puzzle3.setOnTouchUp(getTouchEventHandler(puzzle3));

		Puzzle puzzle4 = new Puzzle(mapAtlas.createSprite("puzzle4"));
		puzzle4.flip(false, true);
		puzzle4.setPosition(500, 200);
		puzzle4.setTargetRegion(310, 95, 20);
		puzzles[3] = puzzle4;
		puzzle4.setOnTouchUp(getTouchEventHandler(puzzle4));

		Puzzle puzzle5 = new Puzzle(mapAtlas.createSprite("puzzle5"));
		puzzle5.flip(false, true);
		puzzle5.setPosition(100, 0);
		puzzle5.setTargetRegion(246, 309, 20);
		puzzles[4] = puzzle5;
		puzzle5.setOnTouchUp(getTouchEventHandler(puzzle5));

		Puzzle puzzle6 = new Puzzle(mapAtlas.createSprite("puzzle6"));
		puzzle6.flip(false, true);
		puzzle6.setPosition(300, 150);
		puzzle6.setTargetRegion(210, 308, 20);
		puzzles[5] = puzzle6;
		puzzle6.setOnTouchUp(getTouchEventHandler(puzzle6));

		Puzzle puzzle7 = new Puzzle(mapAtlas.createSprite("puzzle7"));
		puzzle7.flip(false, true);
		puzzle7.setPosition(400, 250);
		puzzle7.setTargetRegion(227, 179, 20);
		puzzle7.setOnTouchUp(getTouchEventHandler(puzzle7));
		puzzles[6] = puzzle7;

		Puzzle puzzle8 = new Puzzle(mapAtlas.createSprite("puzzle8"));
		puzzle8.flip(false, true);
		puzzle8.setPosition(500, 100);
		puzzle8.setTargetRegion(270, 177, 20);
		puzzle8.setOnTouchUp(getTouchEventHandler(puzzle8));
		puzzles[7] = puzzle8;

		// 箭頭
		TouchableSprite arrowDown = new Arrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.flip(false, true);
		arrowDown.setOnTouchDown(e -> {
			game.setScreen(ScreenManager.getERScreen(game));
			return true;
		});

		westMapRightDrawer = new PuzzleBoard();
		westMapRightDrawer.addDrawableObjects(bg, puzzle1, puzzle2, puzzle3, puzzle4, puzzle5, puzzle6, puzzle7, puzzle8, arrowDown);
	}

	private boolean checkWindowBrokenEvent() {
		return gameData.getDecimalCycle() == DecimalCycle.DECIMAL_CYCLE7 && gameData.getDuodecimalCycle() == DuodecimalCycle.DUODECIMAL_CYCLE9 && gameData.isPuzzleFinished();
	}

	private boolean areAllPuzzlesCorrect() {
		for (Puzzle puzzle : puzzles) {
			if (!puzzle.isCorrectPosition()) {
				return false;
			}
		}
		return true;
	}

	private TouchEventHandler getTouchEventHandler(Puzzle puzzle) {
		return e -> {
			puzzle.setIsTouch(false);
			Rectangle targetRegion = puzzle.getTargetRegion();
			if (targetRegion.contains(puzzle.getX(), puzzle.getY())) {
				puzzle.setIsCorrectPosition(true);
				puzzle.setTouchable(false);
				puzzle.setPosition(targetRegion.x + targetRegion.width / 2, targetRegion.y + targetRegion.height / 2);
			}
			if (puzzle.isCorrectPosition() && areAllPuzzlesCorrect()) {
				gameData.setIsPuzzleFinished(true);
			}
			gameData.getWindowBrokenFun().accept(checkWindowBrokenEvent());
			return true;
		};
	}
}

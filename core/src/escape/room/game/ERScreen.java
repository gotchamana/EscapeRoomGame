package escape.room.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.assets.AssetManager;

public class ERScreen implements Screen {

	private enum ArrowType {
		DOWN("arrow_down_black"), LEFT("arrow_left_black"), RIGHT("arrow_right_black");

		private String name;

		private ArrowType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private final int VIEWPORT_WIDTH = 640;
	private final int VIEWPORT_HEIGHT = 480;

	private EscapeRoomGame game;
	private AssetManager assetManager;
	private OrthographicCamera camera;

	private SpriteBatch batch;

	private ItemTray itemTray;
	private Map currentMap, eastMap, southMap, westMap, northMap;

	public ERScreen(EscapeRoomGame game) {
		this.game = game;

		assetManager = game.getAssetManager();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, 700, 480);

		Item.setItemIconAtlas(assetManager.get("images/icons/icon.atlas"));

		ItemTray.setUiAtlas(assetManager.get("images/uis/ui.atlas"));
		itemTray = ItemTray.getItemTray();

		initMap();

		batch = new SpriteBatch();

		Gdx.input.setInputProcessor(new InputMultiplexer(new ERScreenInputHandler(this), new UIIputHandler(itemTray)));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		itemTray.draw(batch);
		currentMap.draw(batch);
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

	public Map getCurrentMap() {
		return currentMap;
	}

	private void initMap() {
		TextureAtlas uiAtlas = assetManager.get("images/uis/ui.atlas");

		// 東邊地圖
		TextureAtlas mapAtlas = assetManager.get("images/maps/east_map/east_map.atlas");

		eastMap = new Map();

		// 背景
		Sprite bg = createSprite(mapAtlas, "bg");

		// 壁爐頂端
		TouchableSprite fireplaceTop = new TouchableSprite(createSprite(mapAtlas, "fireplace_top"));
		fireplaceTop.setCenterX(321);
		fireplaceTop.setY(312 - fireplaceTop.getHeight());
		fireplaceTop.setOnTouchDown(e -> {
			System.out.println("Touch down");
			return true;
		});

		// 工具箱
		TouchableSprite toolboxClose = new TouchableSprite(createSprite(mapAtlas, "toolbox_close"));
		TouchableSprite toolboxOpen = new TouchableSprite(createSprite(mapAtlas, "toolbox_open"), false);

		toolboxClose.setX(440);
		toolboxClose.setY(422 - toolboxClose.getHeight());
		toolboxClose.setOnTouchDown(e -> {
			toolboxClose.setVisible(false);
			toolboxClose.setTouchable(false);

			toolboxOpen.setVisible(true);
			toolboxOpen.setTouchable(true);
			return true;
		});

		toolboxOpen.setCenter(toolboxClose.getX() + toolboxClose.getWidth() / 2, toolboxClose.getY() + toolboxClose.getHeight() / 2);
		toolboxOpen.setOnTouchDown(e -> {
			toolboxClose.setVisible(true);
			toolboxClose.setTouchable(true);

			toolboxOpen.setVisible(false);
			toolboxOpen.setTouchable(false);
			return true;
		});

		TouchableSprite item1 = new TouchableSprite(createSprite(mapAtlas, "item"));
		item1.setOnTouchDown(e -> {
			eastMap.removeSprite(item1);
			itemTray.addItem(Item.LIGHTER);
			return true;
		});
		item1.setCenter(640 / 2, 480 / 2);

		// 箭頭
		TouchableSprite arrowLeft = createArrow(ArrowType.LEFT, uiAtlas);
		arrowLeft.setOnTouchDown(e -> {
			System.out.println("Down");
			return true;
		});

		TouchableSprite arrowRight = createArrow(ArrowType.RIGHT, uiAtlas);
		arrowRight.setOnTouchDown(e -> {
			currentMap = southMap;
			return true;
		});

		eastMap.addSprites(bg, fireplaceTop, toolboxClose, toolboxOpen, arrowLeft, arrowRight);

		// 南邊地圖
		mapAtlas = assetManager.get("images/maps/south_map/south_map.atlas");

		bg = createSprite(mapAtlas, "bg");

		arrowLeft = createArrow(ArrowType.LEFT, uiAtlas);
		arrowLeft.setOnTouchDown(e -> {
			currentMap = eastMap;
			return true;
		});

		arrowRight = createArrow(ArrowType.RIGHT, uiAtlas);
		arrowRight.setOnTouchDown(e -> {
			System.out.println("Down");
			return false;
		});

		southMap = new Map();
		southMap.addSprites(bg, arrowLeft, arrowRight);

		currentMap = eastMap;
	}

	private TouchableSprite createArrow(ArrowType type, TextureAtlas textureAtlas) {
		final int PADDING = 20;

		TouchableSprite touchableSprite = new TouchableSprite(createSprite(textureAtlas, type.getName()));
		touchableSprite.setScale(0.8f);

		switch (type) {
			case DOWN: 
				touchableSprite.setCenterX(VIEWPORT_WIDTH / 2);
				touchableSprite.setY(VIEWPORT_HEIGHT - touchableSprite.getHeight() - PADDING);
				break;

			case LEFT:
				touchableSprite.setX(20);
				touchableSprite.setCenterY(VIEWPORT_HEIGHT / 2);
				break;

			case RIGHT:
				touchableSprite.setX(VIEWPORT_WIDTH - touchableSprite.getWidth() - PADDING);
				touchableSprite.setCenterY(VIEWPORT_HEIGHT / 2);
				break;
		}

		return touchableSprite;
	}

	private Sprite createSprite(TextureAtlas textureAtlas, String fileName) {
		Sprite sprite = textureAtlas.createSprite(fileName);
		sprite.flip(false, true);

		return sprite;
	}
}

package escape.room.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import escape.room.game.*;
import escape.room.game.event.*;
import escape.room.game.gameobject.*;
import escape.room.game.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class ERScreen implements Screen {

	private EscapeRoomGame game;
	private AssetManager assetManager;
	private OrthographicCamera camera;

	private SpriteBatch batch;

	private ItemTray itemTray;
	private Map currentMap, eastMap, eastMapFireplaceTop, eastMapFireplaceFront, southMap, westMap, northMap;

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
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		itemTray.draw(batch);
		currentMap.draw(batch, delta);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(new InputMultiplexer(new ERScreenInputHandler(this), new UIIputHandler(itemTray)));
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

		// 空洞
		TouchableSprite hole = new TouchableSprite(createSprite(mapAtlas, "hole"));
		hole.setPosition(297, 141);

		// 木頭
		TouchableSprite wood = new TouchableSprite(createSprite(mapAtlas, "wood"));
		wood.setPosition(305, 405);
		wood.setOnTouchDown(e -> {
			currentMap = eastMapFireplaceFront;
			return true;
		});

		// 壁爐頂端
		TouchableSprite fireplaceTop = new TouchableSprite(createSprite(mapAtlas, "fireplace_top"));
		fireplaceTop.setCenterX(323);
		fireplaceTop.setY(318 - fireplaceTop.getHeight());
		fireplaceTop.setOnTouchDown(e -> {
			currentMap = eastMapFireplaceTop;
			return true;
		});

		// 鎚子
		TouchableSprite hammer = new TouchableSprite(createSprite(mapAtlas, "hammer"), false);
		hammer.setPosition(460, 400);
		hammer.setOnTouchDown(e -> {
			eastMap.removeDrawableObject(hammer);
			itemTray.addItem(Item.HAMMER);
			return true;
		});

		// 鋸子
		TouchableSprite saw = new TouchableSprite(createSprite(mapAtlas, "saw"), false);
		saw.setPosition(450, 360);
		saw.setOnTouchDown(e -> {
			eastMap.removeDrawableObject(saw);
			itemTray.addItem(Item.SAW);
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

			hammer.setVisible(true);
			hammer.setTouchable(true);

			saw.setVisible(true);
			saw.setTouchable(true);
			return true;
		});

		toolboxOpen.setCenter(toolboxClose.getX() + toolboxClose.getWidth() / 2, toolboxClose.getY() + toolboxClose.getHeight() / 2);
		toolboxOpen.setOnTouchDown(e -> {
			toolboxClose.setVisible(true);
			toolboxClose.setTouchable(true);

			toolboxOpen.setVisible(false);
			toolboxOpen.setTouchable(false);

			hammer.setVisible(false);
			hammer.setTouchable(false);

			saw.setVisible(false);
			saw.setTouchable(false);
			return true;
		});

		// 箭頭
		TouchableSprite arrowLeft = createArrow(Arrow.ArrowType.LEFT, uiAtlas);
		arrowLeft.setOnTouchDown(e -> {
			game.setScreen(ScreenManager.getPuzzleScreen(game));
			return true;
		});

		TouchableSprite arrowRight = createArrow(Arrow.ArrowType.RIGHT, uiAtlas);
		arrowRight.setOnTouchDown(e -> {
			currentMap = southMap;
			return true;
		});

		// 火焰
		Animation<com.badlogic.gdx.graphics.g2d.Sprite> fireAnim = new Animation<>(0.10f, mapAtlas.createSprites("fire"), Animation.PlayMode.LOOP);
		for (com.badlogic.gdx.graphics.g2d.Sprite fire: fireAnim.getKeyFrames()) {
			fire.setScale(0.8f);
			fire.setPosition(290, 360);
			fire.flip(false, true);
		}

		eastMap.addDrawableObjects(bg, hole, wood, fireAnim, fireplaceTop, toolboxClose, toolboxOpen, hammer, saw, arrowLeft, arrowRight);

		// 東邊地圖之壁爐上方
		mapAtlas = assetManager.get("images/maps/east_map_fireplace_top/east_map_fireplace_top.atlas");
		eastMapFireplaceTop = new Map();

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 加工後龜甲
		TouchableSprite tortoiseshellAfterProcess = new TouchableSprite(createSprite(mapAtlas, "tortoiseshell_after_process"), false);
		tortoiseshellAfterProcess.setPosition(350, 30);
		tortoiseshellAfterProcess.setOnTouchDown(e -> {
			return true;
		});

		// 龜甲輪廓
		TouchableSprite tortoiseshellOutline = new TouchableSprite(createSprite(mapAtlas, "tortoiseshell_outline"));
		tortoiseshellOutline.setPosition(350, 30);
		tortoiseshellOutline.setOnTouchDown(e -> {
			tortoiseshellAfterProcess.setVisible(true);
			tortoiseshellAfterProcess.setTouchable(true);
			return true;
		});

		// 打火機
		TouchableSprite lighter = new TouchableSprite(createSprite(mapAtlas, "lighter"));
		lighter.setPosition(100, 200);
		lighter.setOnTouchDown(e -> {
			eastMapFireplaceTop.removeDrawableObject(lighter);
			itemTray.addItem(Item.LIGHTER);
			return true;
		});

		// 箭頭
		TouchableSprite arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = eastMap;
			return true;
		});

		eastMapFireplaceTop.addDrawableObjects(bg, lighter, tortoiseshellOutline, tortoiseshellAfterProcess, arrowDown);

		// 東邊地圖之壁爐前方
		mapAtlas = assetManager.get("images/maps/east_map_fireplace_front/east_map_fireplace_front.atlas");
		eastMapFireplaceFront = new Map();

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 鑰匙
		TouchableSprite key = new TouchableSprite(createSprite(mapAtlas, "key"));
		key.setPosition(275, 425);
		key.setOnTouchDown(e -> {
			eastMapFireplaceFront.removeDrawableObject(key);
			itemTray.addItem(Item.KEY);
			return true;
		});

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = eastMap;
			return true;
		});

		eastMapFireplaceFront.addDrawableObjects(bg, key, arrowDown);

		// 南邊地圖
		mapAtlas = assetManager.get("images/maps/south_map/south_map.atlas");

		bg = createSprite(mapAtlas, "bg");

		arrowLeft = createArrow(Arrow.ArrowType.LEFT, uiAtlas);
		arrowLeft.setOnTouchDown(e -> {
			currentMap = eastMap;
			return true;
		});

		arrowRight = createArrow(Arrow.ArrowType.RIGHT, uiAtlas);
		arrowRight.setOnTouchDown(e -> {
			System.out.println("Down");
			return false;
		});

		southMap = new Map();
		southMap.addDrawableObjects(bg, arrowLeft, arrowRight);

		currentMap = eastMap;
	}

	private Arrow createArrow(Arrow.ArrowType type, TextureAtlas textureAtlas) {
		Arrow arrow = new Arrow(type, textureAtlas);
		arrow.flip(false, true);

		return arrow;
	}

	private Sprite createSprite(TextureAtlas textureAtlas, String fileName) {
		Sprite sprite = new Sprite(textureAtlas.createSprite(fileName));
		sprite.flip(false, true);

		return sprite;
	}
}

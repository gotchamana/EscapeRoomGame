package escape.room.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import escape.room.game.*;
import escape.room.game.event.*;
import escape.room.game.gameobject.*;
import escape.room.game.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.*;

public class ERScreen implements Screen {

	private EscapeRoomGame game;
	private AssetManager assetManager;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ItemTray itemTray;
	private GameData gameData;

	private Map currentMap, eastMap, eastMapFireplaceTop, eastMapFireplaceFront;
	private Map southMap, southMapAquarium,southMapLowerDrawer, southMapUpperDrawer, southMapSocket;
	private Map westMap, westMapPlant, westMapTV;
	private Map northMap, northMapDesk, northMapMap, northMapStar, northMapClock, northMapDiary, northMapPasswordBox;

	public ERScreen(EscapeRoomGame game) {
		this.game = game;

		assetManager = game.getAssetManager();

		camera = new OrthographicCamera();
		camera.setToOrtho(true, 700, 480);

		Item.setItemIconAtlas(assetManager.get("images/icons/icon.atlas"));
		ItemTray.setUiAtlas(assetManager.get("images/uis/ui.atlas"));
		itemTray = ItemTray.getItemTray();

		gameData = GameData.getGameData();
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
		CustomSprite bg = createSprite(mapAtlas, "bg");

		// 石板
		CustomSprite eastSlate = createSprite(mapAtlas, "slate", false);
		eastSlate.setPosition(299, 142);

		// 空洞
		TouchableSprite hole = new TouchableSprite(createSprite(mapAtlas, "hole"));
		hole.setPosition(297, 141);
		hole.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.EAST_SLATE) {
				cell.removeItem();
				eastSlate.setVisible(true);
				gameData.setIsEastHoleEmpty(false);
				return true;
			}
			return false;
		});

		// 木頭
		TouchableSprite wood = new TouchableSprite(createSprite(mapAtlas, "wood"));
		wood.setPosition(305, 405);
		wood.setOnTouchDown(e -> {
			if (!gameData.isFireBurning()) {
				currentMap = eastMapFireplaceFront;
			}
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
			currentMap = northMap;
			return true;
		});

		TouchableSprite arrowRight = createArrow(Arrow.ArrowType.RIGHT, uiAtlas);
		arrowRight.setOnTouchDown(e -> {
			currentMap = southMap;
			return true;
		});

		// 火焰
		CustomAnimation<Sprite> fireAnim = new CustomAnimation<>(0.10f, mapAtlas.createSprites("fire"), CustomAnimation.PlayMode.LOOP);
		for (com.badlogic.gdx.graphics.g2d.Sprite fire: fireAnim.getKeyFrames()) {
			fire.setScale(0.8f);
			fire.setPosition(290, 360);
			fire.flip(false, true);
		}

		eastMap.addDrawableObjects(bg, hole, eastSlate, wood, fireAnim, fireplaceTop, toolboxClose, toolboxOpen, hammer, saw, arrowLeft, arrowRight);

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

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 水族箱
		TouchableSprite aquarium = new TouchableSprite(createSprite(mapAtlas, "aquarium"));
		aquarium.setPosition(218, 232);
		aquarium.setOnTouchDown(e -> {
			currentMap = southMapAquarium;
			return true;
		});

		// 石板
		CustomSprite southSlate = createSprite(mapAtlas, "slate", false);
		southSlate.setPosition(299, 142);
		
		// 空洞
		hole = new TouchableSprite(createSprite(mapAtlas, "hole"));
		hole.setPosition(297, 141);
		hole.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.SOUTH_SLATE) {
				cell.removeItem();
				southSlate.setVisible(true);
				gameData.setIsSouthHoleEmpty(false);
				return true;
			}
			return false;
		});

		// 暗座燈
		CustomSprite lampDark = createSprite(mapAtlas, "lamp_dark");
		lampDark.setPosition(96, 171);

		// 亮座燈
		CustomSprite lampLight = createSprite(mapAtlas, "lamp_light", false);
		lampLight.setPosition(96, 171);

		// 下方抽屜
		TouchableSprite lowerDrawer = new TouchableSprite(createSprite(mapAtlas, "lower_drawer"));
		lowerDrawer.setPosition(220, 377);
		lowerDrawer.setOnTouchDown(e -> {
			currentMap = southMapLowerDrawer;
			return true;
		});

		// 上方抽屜
		TouchableSprite upperDrawer = new TouchableSprite(createSprite(mapAtlas, "upper_drawer"));
		upperDrawer.setPosition(220, 333);
		upperDrawer.setOnTouchDown(e -> {
			if (gameData.isDrawerLock()) {
				Cell cell = itemTray.getSelectedCell(); 
				if (cell != null && cell.getItem() == Item.KEY) { 
					cell.removeItem();
					gameData.setIsDrawerLock(false);
				}
			} else {
				currentMap = southMapUpperDrawer;
			}
			return true;
		});

		// 插座
		TouchableSprite socket = new TouchableSprite(createSprite(mapAtlas, "socket"));
		socket.setPosition(63, 392);
		socket.setOnTouchDown(e -> {
			currentMap = southMapSocket;
			return true;
		});

		// 箭頭
		arrowLeft = createArrow(Arrow.ArrowType.LEFT, uiAtlas);
		arrowLeft.setOnTouchDown(e -> {
			currentMap = eastMap;
			return true;
		});

		arrowRight = createArrow(Arrow.ArrowType.RIGHT, uiAtlas);
		arrowRight.setOnTouchDown(e -> {
			currentMap = westMap;
			return true;
		});

		southMap = new Map();
		southMap.addDrawableObjects(bg, aquarium, hole, southSlate, lowerDrawer, upperDrawer, lampDark, lampLight, socket, arrowLeft, arrowRight);

		// 南邊地圖之水族箱
		mapAtlas = assetManager.get("images/maps/south_map_aquarium/south_map_aquarium.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 烏龜身體
		CustomSprite turtleBody = createSprite(mapAtlas, "body");
		turtleBody.setPosition(95, 79);

		// 龜殼
		TouchableSprite turtleShell = new TouchableSprite(createSprite(mapAtlas, "shell"));
		turtleShell.setTouchable(false);
		turtleShell.setPosition(112, 136);
		turtleShell.setOnTouchDown(e -> {
			southMapAquarium.removeDrawableObject(turtleShell);
			itemTray.addItem(Item.SHELL);
			return true;
		});

		// 水
		CustomSprite water = createSprite(mapAtlas, "water");
		water.setPosition(20, 17);

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = southMap;
			return true;
		});

		southMapAquarium = new Map();
		southMapAquarium.addDrawableObjects(bg, turtleBody, turtleShell, water, arrowDown);

		// 南邊地圖之下方抽屜
		mapAtlas = assetManager.get("images/maps/south_map_lower_drawer/south_map_lower_drawer.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = southMap;
			return true;
		});

		southMapLowerDrawer = new Map();
		southMapLowerDrawer.addDrawableObjects(bg, arrowDown);
		
		// 南邊地圖之上方抽屜
		mapAtlas = assetManager.get("images/maps/south_map_upper_drawer/south_map_upper_drawer.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 手機
		TouchableSprite phone = new TouchableSprite(createSprite(mapAtlas, "phone"));
		phone.setPosition(265, 125);

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = southMap;
			return true;
		});

		southMapUpperDrawer = new Map();
		southMapUpperDrawer.addDrawableObjects(bg, phone, arrowDown);

		// 南邊地圖之插座處
		mapAtlas = assetManager.get("images/maps/south_map_socket/south_map_socket.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 完好的電線
		CustomSprite completeWire = createSprite(mapAtlas, "socket_complete", false);
		completeWire.setPosition(114, 144);

		// 斷掉的電線
		TouchableSprite brokenWire = new TouchableSprite(createSprite(mapAtlas, "socket_broken"));
		brokenWire.setPosition(114, 144);
		brokenWire.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.TAPE) {
				cell.removeItem();
				
				// 把電線接起來
				completeWire.setVisible(true);
				southMapSocket.removeDrawableObject(brokenWire);

				// 讓燈亮起來
				lampLight.setVisible(true);
				southMap.removeDrawableObject(lampDark);

				// 烏龜出來曬燈光
				turtleShell.setTouchable(true);
				southMapAquarium.removeDrawableObject(turtleBody);
				southMapAquarium.addDrawableObject(turtleBody);
				turtleBody.rotate(90);
				turtleBody.setPosition(325, 80);

				return true;
			}
			return false;
		});

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = southMap;
			return true;
		});

		southMapSocket = new Map();
		southMapSocket.addDrawableObjects(bg, brokenWire, completeWire, arrowDown);

		// 西邊地圖
		mapAtlas = assetManager.get("images/maps/west_map/west_map.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 石板
		CustomSprite westSlate = createSprite(mapAtlas, "slate", false);
		westSlate.setPosition(299, 142);

		// 空洞
		hole = new TouchableSprite(createSprite(mapAtlas, "hole"));
		hole.setPosition(297, 141);
		hole.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.WEST_SLATE) {
				cell.removeItem();
				westSlate.setVisible(true);
				gameData.setIsWestHoleEmpty(false);
				return true;
			}
			return false;
		});

		// 左邊抽屜
		TouchableSprite leftDrawerClose = new TouchableSprite(createSprite(mapAtlas, "left_drawer_close"));
		TouchableSprite leftDrawerOpen = new TouchableSprite(createSprite(mapAtlas, "left_drawer_open"), false);

		leftDrawerClose.setPosition(215, 365);
		leftDrawerClose.setOnTouchDown(e -> {
			leftDrawerClose.setVisible(false);
			leftDrawerClose.setTouchable(false);

			leftDrawerOpen.setVisible(true);
			leftDrawerOpen.setTouchable(true);
			return true;
		});

		leftDrawerOpen.setPosition(215, 358);
		leftDrawerOpen.setOnTouchDown(e -> {
			leftDrawerClose.setVisible(true);
			leftDrawerClose.setTouchable(true);

			leftDrawerOpen.setVisible(false);
			leftDrawerOpen.setTouchable(false);
			return true;
		});

		// 右邊抽屜
		TouchableSprite rightDrawerClose = new TouchableSprite(createSprite(mapAtlas, "right_drawer_close"));
		TouchableSprite rightDrawerOpen = new TouchableSprite(createSprite(mapAtlas, "right_drawer_open"), false);

		rightDrawerClose.setPosition(326, 365);
		rightDrawerClose.setOnTouchDown(e -> {
			rightDrawerClose.setVisible(false);
			rightDrawerClose.setTouchable(false);

			rightDrawerOpen.setVisible(true);
			rightDrawerOpen.setTouchable(true);
			return true;
		});

		rightDrawerOpen.setPosition(375, 356);
		rightDrawerOpen.setOnTouchDown(e -> {
			rightDrawerClose.setVisible(true);
			rightDrawerClose.setTouchable(true);

			rightDrawerOpen.setVisible(false);
			rightDrawerOpen.setTouchable(false);
			return true;
		});

		// 盆栽
		TouchableSprite plant = new TouchableSprite(createSprite(mapAtlas, "plant"));
		plant.setPosition(53, 185);
		plant.setOnTouchDown(e -> {
			currentMap = westMapPlant;
			return true;
		});

		// 拼圖
		TouchableSprite puzzle = new TouchableSprite(createSprite(mapAtlas, "puzzle"));
		puzzle.setPosition(330, 391);
		puzzle.setOnTouchDown(e -> {
			game.setScreen(ScreenManager.getPuzzleScreen(game));
			return true;
		});

		// 膠帶
		TouchableSprite tape = new TouchableSprite(createSprite(mapAtlas, "tape"));
		tape.setPosition(278, 386);
		tape.setOnTouchDown(e -> {
			westMap.removeDrawableObject(tape);
			itemTray.addItem(Item.TAPE);
			return true;
		});

		// 電視
		TouchableSprite tv = new TouchableSprite(createSprite(mapAtlas, "tv"));
		tv.setPosition(222, 219);
		tv.setOnTouchDown(e -> {
			currentMap = westMapTV;
			return true;
		});

		// 箭頭
		arrowLeft = createArrow(Arrow.ArrowType.LEFT, uiAtlas);
		arrowLeft.setOnTouchDown(e -> {
			currentMap = southMap;
			return true;
		});

		arrowRight = createArrow(Arrow.ArrowType.RIGHT, uiAtlas);
		arrowRight.setOnTouchDown(e -> {
			currentMap = northMap;
			return true;
		});

		westMap = new Map();
		westMap.addDrawableObjects(bg, hole, westSlate, plant, tape, puzzle, leftDrawerClose, leftDrawerOpen, rightDrawerClose, rightDrawerOpen, tv, arrowLeft, arrowRight);

		// 西邊地圖之盆栽
		mapAtlas = assetManager.get("images/maps/west_map_plant/west_map_plant.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 石板
		TouchableSprite touchableEastSlate = new TouchableSprite(createSprite(mapAtlas, "slate"));
		touchableEastSlate.setPosition(339, 194);
		touchableEastSlate.setOnTouchDown(e -> {
			westMapPlant.removeDrawableObject(touchableEastSlate);
			itemTray.addItem(Item.EAST_SLATE);
			return true;
		});

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = westMap;
			return true;
		});

		westMapPlant = new Map();
		westMapPlant.addDrawableObjects(bg, touchableEastSlate, arrowDown);

		// 西邊地圖之電視
		mapAtlas = assetManager.get("images/maps/west_map_tv/west_map_tv.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 石板
		TouchableSprite touchableSouthSlate = new TouchableSprite(createSprite(mapAtlas, "slate"), false);
		touchableSouthSlate.setPosition(290, 274);
		touchableSouthSlate.setOnTouchDown(e -> {
			westMapTV.removeDrawableObject(touchableSouthSlate);
			itemTray.addItem(Item.SOUTH_SLATE);
			return true;
		});

		// 電視
		TouchableSprite completeTV = new TouchableSprite(createSprite(mapAtlas, "tv_complete"));
		completeTV.setPosition(97, 117);
		completeTV.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.HAMMER) {
				westMapTV.removeDrawableObject(completeTV);

				touchableSouthSlate.setVisible(true);
				touchableSouthSlate.setTouchable(true);
			}
			return true;
		});

		CustomSprite brokenTV = createSprite(mapAtlas, "tv_broken");
		brokenTV.setPosition(97, 117);

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = westMap;
			return true;
		});

		westMapTV = new Map();
		westMapTV.addDrawableObjects(bg, brokenTV, touchableSouthSlate, completeTV, arrowDown);

		// 北邊地圖
		mapAtlas = assetManager.get("images/maps/north_map/north_map.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 時鐘
		TouchableSprite clock = new TouchableSprite(createSprite(mapAtlas, "clock"));
		clock.setPosition(211, 272);
		clock.setOnTouchDown(e -> {
			currentMap = northMapClock;
			return true;
		});

		// 日記
		TouchableSprite diary = new TouchableSprite(createSprite(mapAtlas, "diary"));
		diary.setPosition(284, 287);
		diary.setOnTouchDown(e -> {
			currentMap = northMapDiary;
			return true;
		});

		// 抽屜
		TouchableSprite drawer = new TouchableSprite(createSprite(mapAtlas, "drawer"));
		drawer.setPosition(229, 310);
		drawer.setOnTouchDown(e -> {
			currentMap = northMapDesk;
			return true;
		});

		// 石板
		CustomSprite northSlate = createSprite(mapAtlas, "slate", false);
		northSlate.setPosition(299, 142);

		// 空洞
		hole = new TouchableSprite(createSprite(mapAtlas, "hole"));
		hole.setPosition(297, 141);
		hole.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.NORTH_SLATE) {
				cell.removeItem();
				northSlate.setVisible(true);
				gameData.setIsNorthHoleEmpty(false);
				return true;
			}
			return false;
		});

		// 密碼箱
		TouchableSprite passwordBox = new TouchableSprite(createSprite(mapAtlas, "password_box"));
		passwordBox.setPosition(349, 230);
		passwordBox.setOnTouchDown(e -> {
			currentMap = northMapPasswordBox;
			return true;
		});

		// 星星
		TouchableSprite star = new TouchableSprite(createSprite(mapAtlas, "star"));
		star.setPosition(466, 431);
		star.setOnTouchDown(e -> {
			currentMap = northMapStar;
			return true;
		});

		// 窗戶
		CustomSprite completeWindow = createSprite(mapAtlas, "window_complete");
		completeWindow.setPosition(457, 85);

		CustomSprite brokenWindow = createSprite(mapAtlas, "window_broken", false);
		brokenWindow.setPosition(457, 85);

		// 箭頭
		arrowLeft = createArrow(Arrow.ArrowType.LEFT, uiAtlas);
		arrowLeft.setOnTouchDown(e -> {
			currentMap = westMap;
			return true;
		});

		arrowRight = createArrow(Arrow.ArrowType.RIGHT, uiAtlas);
		arrowRight.setOnTouchDown(e -> {
			currentMap = eastMap;
			return true;
		});

		northMap = new Map();
		northMap.addDrawableObjects(bg, star, clock, diary, passwordBox, drawer, hole, northSlate, completeWindow, brokenWindow, arrowLeft, arrowRight);

		// 北邊地圖之時鐘
		mapAtlas = assetManager.get("images/maps/north_map_clock/north_map_clock.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 天干
		TouchableSprite[] decimalCycles = new TouchableSprite[10];
		for (int i = 0; i < decimalCycles.length; i++) {
			decimalCycles[i] = new TouchableSprite(createSprite(mapAtlas, "decimal_cycle" + (i + 1)), false);
			decimalCycles[i].setPosition(94, 183);
		}

		for (int i = 0; i < decimalCycles.length; i++) {
			decimalCycles[i].setOnTouchDown(e -> {
			});
		}

		// 地支
		TouchableSprite[] duodecimalCycles = new TouchableSprite[12];
		for (int i = 0; i < duodecimalCycles.length; i++) {
			duodecimalCycles[i] = new TouchableSprite(createSprite(mapAtlas, "duodecimal_cycle" + (i + 1)), false);
			duodecimalCycles[i].setPosition(382, 183);
		}

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = northMap;
			return true;
		});

		northMapClock = new Map();
		northMapClock.addDrawableObjects(bg, arrowDown);
		northMapClock.addDrawableObjects(decimalCycles);
		northMapClock.addDrawableObjects(duodecimalCycles);

		// 北邊地圖之書桌
		mapAtlas = assetManager.get("images/maps/north_map_desk/north_map_desk.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 刀子
		TouchableSprite knife = new TouchableSprite(createSprite(mapAtlas, "knife"), false);
		knife.setPosition(164, 197);
		knife.setOnTouchDown(e -> {
			northMapDesk.removeDrawableObject(knife);
			itemTray.addItem(Item.KNIFE);
			return true;
		});

		// 干支對應表
		TouchableSprite map = new TouchableSprite(createSprite(mapAtlas, "map"), false);
		map.setPosition(362, 190);
		map.setOnTouchDown(e -> {
			currentMap = northMapMap;
			return true;
		});

		// 抽屜
		TouchableSprite closeDrawer = new TouchableSprite(createSprite(mapAtlas, "drawer_close"));
		TouchableSprite openDrawer = new TouchableSprite(createSprite(mapAtlas, "drawer_open"), false);

		closeDrawer.setPosition(115, 156);
		closeDrawer.setOnTouchDown(e -> {
			closeDrawer.setVisible(false);
			closeDrawer.setTouchable(false);

			openDrawer.setVisible(true);
			openDrawer.setTouchable(true);

			knife.setVisible(true);
			knife.setTouchable(true);

			map.setVisible(true);
			map.setTouchable(true);

			return true;
		});

		openDrawer.setPosition(115, 156);
		openDrawer.setOnTouchDown(e -> {
			openDrawer.setVisible(false);
			openDrawer.setTouchable(false);

			closeDrawer.setVisible(true);
			closeDrawer.setTouchable(true);

			knife.setVisible(false);
			knife.setTouchable(false);

			map.setVisible(false);
			map.setTouchable(false);

			return true;
		});

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = northMap;
			return true;
		});

		northMapDesk = new Map();
		northMapDesk.addDrawableObjects(bg, openDrawer, closeDrawer, knife, map, arrowDown);

		// 北邊地圖之日記
		mapAtlas = assetManager.get("images/maps/north_map_diary/north_map_diary.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 日記
		TouchableSprite closeDiary = new TouchableSprite(createSprite(mapAtlas, "diary_close"));
		TouchableSprite openDiary = new TouchableSprite(createSprite(mapAtlas, "diary_open"), false);

		closeDiary.setPosition(187, 44);
		closeDiary.setOnTouchDown(e -> {
			closeDiary.setVisible(false);
			closeDiary.setTouchable(false);

			openDiary.setVisible(true);
			openDiary.setTouchable(true);

			return true;
		});

		openDiary.setOnTouchDown(e -> {
			openDiary.setVisible(false);
			openDiary.setTouchable(false);

			closeDiary.setVisible(true);
			closeDiary.setTouchable(true);

			return true;
		});

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = northMap;
			return true;
		});

		northMapDiary = new Map();
		northMapDiary.addDrawableObjects(bg, closeDiary, openDiary, arrowDown);

		// 北邊地圖之干支對應表
		mapAtlas = assetManager.get("images/maps/north_map_map/north_map_map.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = northMapDesk;
			return true;
		});

		northMapMap = new Map();
		northMapMap.addDrawableObjects(bg, arrowDown);

		// 北邊地圖之密碼箱
		mapAtlas = assetManager.get("images/maps/north_map_password_box/north_map_password_box.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 密碼箱
		CustomSprite closePasswordBox = createSprite(mapAtlas, "password_box_close");
		closePasswordBox.setPosition(155, 11);

		CustomSprite openPasswordBox = createSprite(mapAtlas, "password_box_open", false);
		openPasswordBox.setPosition(155, 11);

		// 石板
		TouchableSprite touchableWestSlate = new TouchableSprite(createSprite(mapAtlas, "slate"), false);
		touchableWestSlate.setPosition(258, 351);
		touchableWestSlate.setOnTouchDown(e -> {
			northMapPasswordBox.removeDrawableObject(touchableWestSlate);
			itemTray.addItem(Item.WEST_SLATE);
			return true;
		});

		// 密碼
		TouchableSprite[] codes1 = new TouchableSprite[4];
		for (int i = 0; i < codes1.length; i++) {
			codes1[i] = new TouchableSprite(createSprite(mapAtlas, "code" + (i + 1)));
			codes1[i].setPosition(192, 124);
		}

		TouchableSprite[] codes2 = new TouchableSprite[4];
		for (int i = 0; i < codes2.length; i++) {
			codes2[i] = new TouchableSprite(createSprite(mapAtlas, "code" + (i + 1)));
			codes2[i].setPosition(340, 124);
		}

		TouchableSprite[] codes3 = new TouchableSprite[4];
		for (int i = 0; i < codes3.length; i++) {
			codes3[i] = new TouchableSprite(createSprite(mapAtlas, "code" + (i + 1)));
			codes3[i].setPosition(342, 266);
		}

		TouchableSprite[] codes4 = new TouchableSprite[4];
		for (int i = 0; i < codes4.length; i++) {
			codes4[i] = new TouchableSprite(createSprite(mapAtlas, "code" + (i + 1)));
			codes4[i].setPosition(191, 266);
		}

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = northMap;
			return true;
		});

		northMapPasswordBox = new Map();
		northMapPasswordBox.addDrawableObjects(bg, closePasswordBox, openPasswordBox, touchableWestSlate, arrowDown);
		northMapPasswordBox.addDrawableObjects(codes1);
		northMapPasswordBox.addDrawableObjects(codes2);
		northMapPasswordBox.addDrawableObjects(codes3);
		northMapPasswordBox.addDrawableObjects(codes4);

		// 北邊地圖之星星
		mapAtlas = assetManager.get("images/maps/north_map_star/north_map_star.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 石板
		TouchableSprite touchableNorthSlate = new TouchableSprite(createSprite(mapAtlas, "slate"), false);
		touchableNorthSlate.setPosition(260, 172);
		touchableNorthSlate.setOnTouchDown(e -> {
			northMapStar.removeDrawableObject(touchableNorthSlate);
			itemTray.addItem(Item.NORTH_SLATE);
			return true;
		});

		// 星星
		CustomSprite brokenStar = createSprite(mapAtlas, "star_broken", false);
		brokenStar.setPosition(88, 124);

		TouchableSprite completeStar = new TouchableSprite(createSprite(mapAtlas, "star_complete"), false);
		completeStar.setPosition(200, 127);
		completeStar.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.HAMMER) {
				// 星星破掉
				brokenStar.setVisible(true);

				// 石板顯現
				touchableNorthSlate.setVisible(true);
				touchableNorthSlate.setTouchable(true);

				// 移除完整的星星
				northMapStar.removeDrawableObject(completeStar);
			}
			return false;
		});

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = northMap;
			return true;
		});

		northMapStar = new Map();
		northMapStar.addDrawableObjects(bg, completeStar, brokenStar, touchableNorthSlate, arrowDown);

		currentMap = eastMap;
	}

	private Arrow createArrow(Arrow.ArrowType type, TextureAtlas textureAtlas) {
		Arrow arrow = new Arrow(type, textureAtlas);
		arrow.flip(false, true);

		return arrow;
	}

	private CustomSprite createSprite(TextureAtlas textureAtlas, String fileName) {
		CustomSprite sprite = new CustomSprite(textureAtlas.createSprite(fileName));
		sprite.flip(false, true);

		return sprite;
	}

	private CustomSprite createSprite(TextureAtlas textureAtlas, String fileName, boolean isVisible) {
		CustomSprite sprite = createSprite(textureAtlas, fileName);
		sprite.setVisible(isVisible);

		return sprite;
	}
}

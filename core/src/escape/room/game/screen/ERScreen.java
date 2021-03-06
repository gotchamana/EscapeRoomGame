package escape.room.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import escape.room.game.*;
import escape.room.game.event.*;
import escape.room.game.gameobject.*;
import escape.room.game.ui.*;
import java.util.function.Consumer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class ERScreen implements Screen {

	private class AtomicInteger {
		private int integer;

		AtomicInteger(int integer) {
			this.integer = integer;
		}

		int get() {
			return integer;
		}

		void set(int integer) {
			this.integer = integer;
		}

		int incrementAndGet() {
			integer++;
			return integer;
		}
	}

	private EscapeRoomGame game;
	private AssetManager assetManager;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private ItemTray itemTray;
	private GameData gameData;

	private Map currentMap, eastMap, eastMapFireplaceTop, eastMapFireplaceFront;
	private Map southMap, southMapAquarium,southMapLowerDrawer, southMapUpperDrawer, southMapPhone, southMapSocket;
	private Map westMap, westMapPlant, westMapTV;
	private Map northMap, northMapDesk, northMapMap, northMapStar, northMapClock, northMapDiary, northMapPasswordBox;
	private Map gameStart, gameOver;
	private TouchableSprite completeStar;

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
		if (!(currentMap == gameStart || currentMap == gameOver)) {
			itemTray.draw(batch);
		}
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
		Sound wind = assetManager.get("sounds/wind.mp3");
		CustomAnimation<Sprite> fireAnim = new CustomAnimation<>(0.10f, mapAtlas.createSprites("fire"), CustomAnimation.PlayMode.LOOP);
		Consumer<Boolean> fireBurnOutFun = e -> {
			if (e) {
				wind.play();
				eastMap.removeDrawableObject(fireAnim);
				gameData.setIsFireBurning(false);
			}
		};
		TouchableSprite hole = new TouchableSprite(createSprite(mapAtlas, "hole"));
		hole.setPosition(297, 141);
		hole.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.EAST_SLATE) {
				cell.removeItem();
				eastSlate.setVisible(true);
				gameData.setIsEastHoleEmpty(false);
				fireBurnOutFun.accept(checkFireBurnOutEvent());
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

		// 原始龜甲
		TouchableSprite tortoiseshellOutline = new TouchableSprite(createSprite(mapAtlas, "tortoiseshell_outline"));
		TouchableSprite tortoiseshellAfterProcess = new TouchableSprite(createSprite(mapAtlas, "tortoiseshell_after_process"), false);
		TouchableSprite tortoiseshellAfterUse = new TouchableSprite(createSprite(mapAtlas, "tortoiseshell_after_use"), false);
		CustomSprite tortoiseshellWithCode = createSprite(mapAtlas, "tortoiseshell_with_code", false);
		TouchableSprite tortoiseshell = new TouchableSprite(createSprite(mapAtlas, "tortoiseshell"), false);

		tortoiseshell.setPosition(295, 35);
		tortoiseshell.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.SAW) {
				cell.removeItem();
				eastMapFireplaceTop.removeDrawableObject(tortoiseshell);
				eastMapFireplaceTop.removeDrawableObject(tortoiseshellOutline);
				tortoiseshellAfterProcess.setVisible(true);
				tortoiseshellAfterProcess.setTouchable(true);
			}
			return true;
		});

		// 加工後龜甲
		tortoiseshellAfterProcess.setPosition(292, 32);
		tortoiseshellAfterProcess.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.KNIFE) {
				cell.removeItem();
				eastMapFireplaceTop.removeDrawableObject(tortoiseshellAfterProcess);
				tortoiseshellAfterUse.setVisible(true);
				tortoiseshellAfterUse.setTouchable(true);
			}
			return true;
		});

		// 刻字後龜甲
		tortoiseshellAfterUse.setPosition(292, 32);
		tortoiseshellAfterUse.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.LIGHTER) {
				cell.removeItem();
				eastMapFireplaceTop.removeDrawableObject(tortoiseshellAfterUse);
				tortoiseshellWithCode.setVisible(true);
			}
			return true;
		});

		// 燒灼後龜甲
		tortoiseshellWithCode.setPosition(292, 32);
		
		// 龜甲輪廓
		tortoiseshellOutline.setPosition(295, 35);
		tortoiseshellOutline.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.SHELL) {
				cell.removeItem();
				tortoiseshell.setVisible(true);
				tortoiseshell.setTouchable(true);
			}
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

		eastMapFireplaceTop.addDrawableObjects(bg, lighter, tortoiseshellOutline, tortoiseshell, tortoiseshellAfterProcess, tortoiseshellAfterUse, tortoiseshellWithCode, arrowDown);

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
		
		// 完整烏龜
		CustomSprite turtle = createSprite(mapAtlas, "turtle");
		turtle.setPosition(224, 282);

		// 分離之龜殼
		CustomSprite smallTurtleShell = createSprite(mapAtlas, "turtle_shell", false);
		smallTurtleShell.setPosition(224, 289);

		// 分離之烏龜
		CustomSprite smallTurtleBody = createSprite(mapAtlas, "turtle_body", false);
		smallTurtleBody.setPosition(274, 271);

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
				fireBurnOutFun.accept(checkFireBurnOutEvent());
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
		Sound unlock = assetManager.get("sounds/unlock.mp3");
		TouchableSprite upperDrawer = new TouchableSprite(createSprite(mapAtlas, "upper_drawer"));
		upperDrawer.setPosition(220, 333);
		upperDrawer.setOnTouchDown(e -> {
			if (gameData.isDrawerLock()) {
				Cell cell = itemTray.getSelectedCell(); 
				if (cell != null && cell.getItem() == Item.KEY) { 
					unlock.play();
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

		TouchableSprite socketConnect = new TouchableSprite(createSprite(mapAtlas, "socket_connect"), false);
		socketConnect.setPosition(63, 392);
		socketConnect.setOnTouchDown(e -> {
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
		southMap.addDrawableObjects(bg, aquarium, turtle, smallTurtleBody, smallTurtleShell, hole, southSlate, lowerDrawer, upperDrawer, lampDark, lampLight, socket, socketConnect, arrowLeft, arrowRight);

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

			southMap.removeDrawableObject(smallTurtleShell);
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
		phone.setOnTouchDown(e -> {
			currentMap = southMapPhone;
			return true;
		});

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = southMap;
			return true;
		});

		southMapUpperDrawer = new Map();
		southMapUpperDrawer.addDrawableObjects(bg, phone, arrowDown);

		// 南邊地圖之手機
		mapAtlas = assetManager.get("images/maps/south_map_phone/south_map_phone.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		// 號碼
		CustomFont font = new CustomFont(assetManager.get("fonts/font.fnt"), true);
		font.setColor(Color.WHITE);
		font.setPosition(220, 100);
		font.setTargetWidth(200);
		font.setHalign(16);

		// 按鈕
		Sound beep = assetManager.get("sounds/phone_beep.mp3");
		TouchableSprite[] buttons = new TouchableSprite[13];
		for (int i = 0; i < buttons.length - 1; i++) {
			buttons[i] = new TouchableSprite(createSprite(mapAtlas, "button" + i));
			buttons[i].setPosition(229 + 60 * (i % 3), 156 + (i / 3) * 54);
			switch (i) {
				case 9: 
					buttons[i].setOnTouchDown(e -> {
						font.setContent(font.getContent() + "*");
						beep.play();
						return true;
					});
					break;
					
				case 10: 
					buttons[i].setOnTouchDown(e -> {
						font.setContent(font.getContent() + 0);
						beep.play();
						return true;
					});
					break;

				case 11: 
					buttons[i].setOnTouchDown(e -> {
						font.setContent(font.getContent() + "#");
						beep.play();
						return true;
					});
					break;

				default:
					AtomicInteger index = new AtomicInteger(i);
					buttons[i].setOnTouchDown(e -> {
						font.setContent(font.getContent() + (index.get() + 1));
						beep.play();
						return true;
					});
			}
		}

		buttons[12] = new TouchableSprite(createSprite(mapAtlas, "call"));
		buttons[12].setPosition(289, 375);
		buttons[12].setOnTouchDown(e -> {
			switch (font.getContent()) {
				case "110": 
				case "119":
				case "112":
					currentMap = gameOver;
					break;
				default:
					font.setContent("");
					beep.play();
			}
			return true;
		});

		// 箭頭
		arrowDown = createArrow(Arrow.ArrowType.DOWN, uiAtlas);
		arrowDown.setOnTouchDown(e -> {
			currentMap = southMapUpperDrawer;
			return true;
		});

		southMapPhone = new Map();
		southMapPhone.addDrawableObjects(bg, font, arrowDown);
		southMapPhone.addDrawableObjects(buttons);

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

				// 全局地圖也要把電線接起來
				southMap.removeDrawableObject(socket);
				socketConnect.setVisible(true);
				socketConnect.setTouchable(true);

				// 讓燈亮起來
				lampLight.setVisible(true);
				southMap.removeDrawableObject(lampDark);

				// 烏龜出來曬燈光
				turtleShell.setTouchable(true);
				southMapAquarium.removeDrawableObject(turtleBody);
				southMapAquarium.addDrawableObject(turtleBody);
				turtleBody.rotate(90);
				turtleBody.setPosition(325, 80);

				// 全局地圖也要改變烏龜的狀態
				southMap.removeDrawableObject(turtle);
				smallTurtleShell.setVisible(true);
				smallTurtleBody.setVisible(true);

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
				fireBurnOutFun.accept(checkFireBurnOutEvent());
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

		TouchableSprite tvBroken = new TouchableSprite(createSprite(mapAtlas, "tv_broken"), false);
		tvBroken.setPosition(222, 219);
		tvBroken.setOnTouchDown(e -> {
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
		westMap.addDrawableObjects(bg, hole, westSlate, plant, tape, puzzle, leftDrawerClose, leftDrawerOpen, rightDrawerClose, rightDrawerOpen, tv, tvBroken, arrowLeft, arrowRight);

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
		Sound tvBreak = assetManager.get("sounds/tv_break.mp3");
		TouchableSprite completeTV = new TouchableSprite(createSprite(mapAtlas, "tv_complete"));
		completeTV.setPosition(97, 117);
		completeTV.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.HAMMER) {
				tvBreak.play();
				westMapTV.removeDrawableObject(completeTV);

				touchableSouthSlate.setVisible(true);
				touchableSouthSlate.setTouchable(true);

				// 全局地圖也要改變電視狀態
				westMap.removeDrawableObject(tv);
				tvBroken.setVisible(true);
				tvBroken.setTouchable(true);
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
				fireBurnOutFun.accept(checkFireBurnOutEvent());
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
		TouchableSprite star = new TouchableSprite(createSprite(mapAtlas, "star"), false);
		star.setPosition(466, 431);
		star.setOnTouchDown(e -> {
			currentMap = northMapStar;
			return true;
		});

		TouchableSprite starBroken = new TouchableSprite(createSprite(mapAtlas, "star_broken"), false);
		starBroken.setPosition(456, 427);
		starBroken.setOnTouchDown(e -> {
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
		northMap.addDrawableObjects(bg, star, starBroken, clock, diary, passwordBox, drawer, hole, northSlate, completeWindow, brokenWindow, arrowLeft, arrowRight);

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

		AtomicInteger decimalIndex = new AtomicInteger(0);
		Sound windowBroken = assetManager.get("sounds/window_break.mp3");
		Consumer<Boolean> windowBrokenFun = e -> {
			if (e) {
				windowBroken.play();
				northMap.removeDrawableObject(completeWindow);
				brokenWindow.setVisible(true);
				star.setVisible(true);
				star.setTouchable(true);
				completeStar.setVisible(true);
				completeStar.setTouchable(true);
			}
		};
		gameData.setWindowBrokenFun(windowBrokenFun);

		for (int i = 0; i < decimalCycles.length; i++) {
			decimalCycles[i].setOnTouchDown(e -> {
				int currentIndex = decimalIndex.get();
				int nextIndex = currentIndex + 1; 
				nextIndex = (nextIndex == decimalCycles.length) ? 0 : nextIndex;

				gameData.setDecimalCycle(DecimalCycle.valueOf("DECIMAL_CYCLE" + (nextIndex + 1)));
				windowBrokenFun.accept(checkWindowBrokenEvent());
				fireBurnOutFun.accept(checkFireBurnOutEvent());

				decimalCycles[currentIndex].setVisible(false);
				decimalCycles[currentIndex].setTouchable(false);

				decimalCycles[nextIndex].setVisible(true);
				decimalCycles[nextIndex].setTouchable(true);

				decimalIndex.set(decimalIndex.incrementAndGet() % 10);
				return true;
			});
		}
		decimalCycles[0].setVisible(true);
		decimalCycles[0].setTouchable(true);

		// 地支
		TouchableSprite[] duodecimalCycles = new TouchableSprite[12];
		for (int i = 0; i < duodecimalCycles.length; i++) {
			duodecimalCycles[i] = new TouchableSprite(createSprite(mapAtlas, "duodecimal_cycle" + (i + 1)), false);
			duodecimalCycles[i].setPosition(382, 183);
		}

		AtomicInteger duodecimalIndex = new AtomicInteger(0);
		for (int i = 0; i < duodecimalCycles.length; i++) {
			duodecimalCycles[i].setOnTouchDown(e -> {
				int currentIndex = duodecimalIndex.get();
				int nextIndex = currentIndex + 1; 
				nextIndex = (nextIndex == duodecimalCycles.length) ? 0 : nextIndex;

				gameData.setDuodecimalCycle(DuodecimalCycle.valueOf("DUODECIMAL_CYCLE" + (nextIndex + 1)));
				windowBrokenFun.accept(checkWindowBrokenEvent());
				fireBurnOutFun.accept(checkFireBurnOutEvent());

				duodecimalCycles[currentIndex].setVisible(false);
				duodecimalCycles[currentIndex].setTouchable(false);

				duodecimalCycles[nextIndex].setVisible(true);
				duodecimalCycles[nextIndex].setTouchable(true);

				duodecimalIndex.set(duodecimalIndex.incrementAndGet() % 12);
				return true;
			});
		}
		duodecimalCycles[0].setVisible(true);
		duodecimalCycles[0].setTouchable(true);

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
		TouchableSprite[] codes2 = new TouchableSprite[4];
		TouchableSprite[] codes3 = new TouchableSprite[4];
		TouchableSprite[] codes4 = new TouchableSprite[4];
		for (int i = 0; i < codes1.length; i++) {
			codes1[i] = new TouchableSprite(createSprite(mapAtlas, "code" + (i + 1)), false);
			codes1[i].setPosition(192, 124);
		}

		AtomicInteger code1Index = new AtomicInteger(0);
		for (int i = 0; i < codes1.length; i++) {
			codes1[i].setOnTouchDown(e -> {
				int currentIndex = code1Index.get();
				int nextIndex = currentIndex + 1; 
				nextIndex = (nextIndex == codes1.length) ? 0 : nextIndex;

				gameData.setCode1(Code.valueOf("CODE" + (nextIndex + 1)));
				if (checkPasswordCorrect()) {
					northMapPasswordBox.removeDrawableObject(closePasswordBox);
					northMapPasswordBox.removeDrawableObjects(codes1);
					northMapPasswordBox.removeDrawableObjects(codes2);
					northMapPasswordBox.removeDrawableObjects(codes3);
					northMapPasswordBox.removeDrawableObjects(codes4);
					touchableWestSlate.setVisible(true);
					touchableWestSlate.setTouchable(true);
					openPasswordBox.setVisible(true);
				}

				codes1[currentIndex].setVisible(false);
				codes1[currentIndex].setTouchable(false);

				codes1[nextIndex].setVisible(true);
				codes1[nextIndex].setTouchable(true);

				code1Index.set(code1Index.incrementAndGet() % 4);
				return true;
			});
		}
		codes1[0].setVisible(true);
		codes1[0].setTouchable(true);

		for (int i = 0; i < codes2.length; i++) {
			codes2[i] = new TouchableSprite(createSprite(mapAtlas, "code" + (i + 1)), false);
			codes2[i].setPosition(340, 124);
		}

		AtomicInteger code2Index = new AtomicInteger(0);
		for (int i = 0; i < codes2.length; i++) {
			codes2[i].setOnTouchDown(e -> {
				int currentIndex = code2Index.get();
				int nextIndex = currentIndex + 1; 
				nextIndex = (nextIndex == codes2.length) ? 0 : nextIndex;

				gameData.setCode2(Code.valueOf("CODE" + (nextIndex + 1)));
				if (checkPasswordCorrect()) {
					northMapPasswordBox.removeDrawableObject(closePasswordBox);
					northMapPasswordBox.removeDrawableObjects(codes1);
					northMapPasswordBox.removeDrawableObjects(codes2);
					northMapPasswordBox.removeDrawableObjects(codes3);
					northMapPasswordBox.removeDrawableObjects(codes4);
					touchableWestSlate.setVisible(true);
					touchableWestSlate.setTouchable(true);
					openPasswordBox.setVisible(true);
				}

				codes2[currentIndex].setVisible(false);
				codes2[currentIndex].setTouchable(false);

				codes2[nextIndex].setVisible(true);
				codes2[nextIndex].setTouchable(true);

				code2Index.set(code2Index.incrementAndGet() % 4);
				return true;
			});
		}
		codes2[0].setVisible(true);
		codes2[0].setTouchable(true);

		for (int i = 0; i < codes3.length; i++) {
			codes3[i] = new TouchableSprite(createSprite(mapAtlas, "code" + (i + 1)), false);
			codes3[i].setPosition(191, 266);
		}

		AtomicInteger code3Index = new AtomicInteger(0);
		for (int i = 0; i < codes3.length; i++) {
			codes3[i].setOnTouchDown(e -> {
				int currentIndex = code3Index.get();
				int nextIndex = currentIndex + 1; 
				nextIndex = (nextIndex == codes3.length) ? 0 : nextIndex;

				gameData.setCode3(Code.valueOf("CODE" + (nextIndex + 1)));
				if (checkPasswordCorrect()) {
					northMapPasswordBox.removeDrawableObject(closePasswordBox);
					northMapPasswordBox.removeDrawableObjects(codes1);
					northMapPasswordBox.removeDrawableObjects(codes2);
					northMapPasswordBox.removeDrawableObjects(codes3);
					northMapPasswordBox.removeDrawableObjects(codes4);
					touchableWestSlate.setVisible(true);
					touchableWestSlate.setTouchable(true);
					openPasswordBox.setVisible(true);
				}

				codes3[currentIndex].setVisible(false);
				codes3[currentIndex].setTouchable(false);

				codes3[nextIndex].setVisible(true);
				codes3[nextIndex].setTouchable(true);

				code3Index.set(code3Index.incrementAndGet() % 4);
				return true;
			});
		}
		codes3[0].setVisible(true);
		codes3[0].setTouchable(true);

		for (int i = 0; i < codes4.length; i++) {
			codes4[i] = new TouchableSprite(createSprite(mapAtlas, "code" + (i + 1)), false);
			codes4[i].setPosition(342, 266);
		}

		AtomicInteger code4Index = new AtomicInteger(0);
		for (int i = 0; i < codes4.length; i++) {
			codes4[i].setOnTouchDown(e -> {
				int currentIndex = code4Index.get();
				int nextIndex = currentIndex + 1; 
				nextIndex = (nextIndex == codes4.length) ? 0 : nextIndex;

				gameData.setCode4(Code.valueOf("CODE" + (nextIndex + 1)));
				if (checkPasswordCorrect()) {
					northMapPasswordBox.removeDrawableObject(closePasswordBox);
					northMapPasswordBox.removeDrawableObjects(codes1);
					northMapPasswordBox.removeDrawableObjects(codes2);
					northMapPasswordBox.removeDrawableObjects(codes3);
					northMapPasswordBox.removeDrawableObjects(codes4);
					touchableWestSlate.setVisible(true);
					touchableWestSlate.setTouchable(true);
					openPasswordBox.setVisible(true);
				}

				codes4[currentIndex].setVisible(false);
				codes4[currentIndex].setTouchable(false);

				codes4[nextIndex].setVisible(true);
				codes4[nextIndex].setTouchable(true);

				code4Index.set(code4Index.incrementAndGet() % 4);
				return true;
			});
		}
		codes4[0].setVisible(true);
		codes4[0].setTouchable(true);

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
		Sound starBreak = assetManager.get("sounds/star_break.mp3");
		CustomSprite brokenStar = createSprite(mapAtlas, "star_broken", false);
		brokenStar.setPosition(88, 124);

		completeStar = new TouchableSprite(createSprite(mapAtlas, "star_complete"), false);
		completeStar.setPosition(200, 127);
		completeStar.setOnTouchDown(e -> {
			Cell cell = itemTray.getSelectedCell();

			if (cell != null && cell.getItem() == Item.HAMMER) {
				// 星星破掉
				starBreak.play();
				brokenStar.setVisible(true);

				// 石板顯現
				touchableNorthSlate.setVisible(true);
				touchableNorthSlate.setTouchable(true);

				// 移除完整的星星
				northMapStar.removeDrawableObject(completeStar);

				// 全局地圖也要改變星星狀態
				northMap.removeDrawableObject(star);
				starBroken.setVisible(true);
				starBroken.setTouchable(true);
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

		// 遊戲開始
		mapAtlas = assetManager.get("images/maps/game_start/game_start.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");
		
		// 開始按鈕
		TouchableSprite door = new TouchableSprite(createSprite(mapAtlas, "door"));
		door.setPosition(195, 24);
		door.setOnTouchDown(e -> {
			currentMap = eastMap;
			return true;
		});

		gameStart = new Map();
		gameStart.addDrawableObjects(bg, door);

		// 遊戲結束
		mapAtlas = assetManager.get("images/maps/game_over/game_over.atlas");

		// 背景
		bg = createSprite(mapAtlas, "bg");

		gameOver = new Map();
		gameOver.addDrawableObjects(bg);

		// 背景音樂
		Music bgm = assetManager.get("musics/bgm.mp3");
		bgm.play();
		bgm.setLooping(true);

		currentMap = gameStart;
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

	private boolean checkWindowBrokenEvent() {
		return gameData.getDecimalCycle() == DecimalCycle.DECIMAL_CYCLE7 && gameData.getDuodecimalCycle() == DuodecimalCycle.DUODECIMAL_CYCLE9 && gameData.isPuzzleFinished();
	}

	private boolean checkFireBurnOutEvent() {
		return !(gameData.isEastHoleEmpty() || gameData.isSouthHoleEmpty() || gameData.isWestHoleEmpty() || gameData.isNorthHoleEmpty()) && (gameData.getDecimalCycle() == DecimalCycle.DECIMAL_CYCLE3 && gameData.getDuodecimalCycle() == DuodecimalCycle.DUODECIMAL_CYCLE9);
	}

	private boolean checkPasswordCorrect() {
		return gameData.getCode1() == Code.CODE1 && gameData.getCode2() == Code.CODE2 && gameData.getCode3() == Code.CODE3 && gameData.getCode4() == Code.CODE4;
	}
}

package escape.room.game.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import escape.room.game.EscapeRoomGame;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class LoadingScreen implements Screen {
	
	private EscapeRoomGame game;
	private AssetManager assetManager;
	private SpriteBatch batch;
	private BitmapFont font;
	private GlyphLayout layout;

	public LoadingScreen(EscapeRoomGame game) {
		this.game = game;

		assetManager = game.getAssetManager();
		loadAssets();

		batch = new SpriteBatch();
		font = assetManager.get("fonts/font.fnt");
		layout = new GlyphLayout(font, "Loading 100%");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (assetManager.update()) {
			game.setScreen(ScreenManager.getERScreen(game));
		}

		String progress = "Loading " + (int)(assetManager.getProgress() * 100) + "%";

		batch.begin();
		font.draw(batch, progress, Gdx.graphics.getWidth() / 2 - layout.width / 2, Gdx.graphics.getHeight() / 2 + layout.height / 2);
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

	private void loadAssets() {
		assetManager.load("musics/bgm.mp3", Music.class);
		assetManager.load("sounds/phone_beep.mp3", Sound.class);
		assetManager.load("sounds/wind.mp3", Sound.class);
		assetManager.load("sounds/window_break.mp3", Sound.class);
		assetManager.load("sounds/tv_break.mp3", Sound.class);
		assetManager.load("sounds/star_break.mp3", Sound.class);
		assetManager.load("sounds/unlock.mp3", Sound.class);

		assetManager.load("fonts/font.fnt", BitmapFont.class);
		assetManager.load("images/uis/ui.atlas", TextureAtlas.class);
		assetManager.load("images/icons/icon.atlas", TextureAtlas.class);

		assetManager.load("images/maps/game_start/game_start.atlas", TextureAtlas.class);
		assetManager.load("images/maps/game_over/game_over.atlas", TextureAtlas.class);

		assetManager.load("images/maps/east_map/east_map.atlas", TextureAtlas.class);
		assetManager.load("images/maps/east_map_fireplace_top/east_map_fireplace_top.atlas", TextureAtlas.class);
		assetManager.load("images/maps/east_map_fireplace_front/east_map_fireplace_front.atlas", TextureAtlas.class);

		assetManager.load("images/maps/south_map/south_map.atlas", TextureAtlas.class);
		assetManager.load("images/maps/south_map_aquarium/south_map_aquarium.atlas", TextureAtlas.class);
		assetManager.load("images/maps/south_map_upper_drawer/south_map_upper_drawer.atlas", TextureAtlas.class);
		assetManager.load("images/maps/south_map_lower_drawer/south_map_lower_drawer.atlas", TextureAtlas.class);
		assetManager.load("images/maps/south_map_phone/south_map_phone.atlas", TextureAtlas.class);
		assetManager.load("images/maps/south_map_socket/south_map_socket.atlas", TextureAtlas.class);

		assetManager.load("images/maps/west_map/west_map.atlas", TextureAtlas.class);
		assetManager.load("images/maps/west_map_plant/west_map_plant.atlas", TextureAtlas.class);
		assetManager.load("images/maps/west_map_tv/west_map_tv.atlas", TextureAtlas.class);
		assetManager.load("images/maps/west_map_right_drawer/west_map_right_drawer.atlas", TextureAtlas.class);

		assetManager.load("images/maps/north_map/north_map.atlas", TextureAtlas.class);
		assetManager.load("images/maps/north_map_clock/north_map_clock.atlas", TextureAtlas.class);
		assetManager.load("images/maps/north_map_desk/north_map_desk.atlas", TextureAtlas.class);
		assetManager.load("images/maps/north_map_diary/north_map_diary.atlas", TextureAtlas.class);
		assetManager.load("images/maps/north_map_map/north_map_map.atlas", TextureAtlas.class);
		assetManager.load("images/maps/north_map_password_box/north_map_password_box.atlas", TextureAtlas.class);
		assetManager.load("images/maps/north_map_star/north_map_star.atlas", TextureAtlas.class);

		assetManager.finishLoadingAsset("fonts/font.fnt");
	}
}

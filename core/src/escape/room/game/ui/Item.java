package escape.room.game.ui;

import com.badlogic.gdx.graphics.g2d.*;
import java.util.Objects;

public enum Item {

	LIGHTER("lighter_icon"), HAMMER("hammer_icon"), SAW("saw_icon"), KEY("key_icon");

	private static TextureAtlas itemIconAtlas;
	private String fileName;
	private Sprite sprite;

	private Item(String fileName) {
		this.fileName = fileName;
	}

	public static void setItemIconAtlas(TextureAtlas itemIconAtlas) {
		Item.itemIconAtlas = itemIconAtlas;
	}

	public Sprite getSprite() {
		if (sprite == null) {
			Objects.requireNonNull(itemIconAtlas, "icon TextureAtlas is null");
			sprite = itemIconAtlas.createSprite(fileName);
			sprite.flip(false, true);
		}

		return sprite;
	}
}

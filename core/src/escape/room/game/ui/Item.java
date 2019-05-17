package escape.room.game.ui;

import com.badlogic.gdx.graphics.g2d.*;
import java.util.Objects;

public enum Item {

	EAST_SLATE("east_slate_icon"),
	HAMMER("hammer_icon"),
	KEY("key_icon"),
	KNIFE("knife_icon"),
	LIGHTER("lighter_icon"), 
	NORTH_SLATE("north_slate_icon"),
	PUZZLE("puzzle_icon"),
	SAW("saw_icon"),
	SHELL("shell_icon"),
	SOUTH_SLATE("south_slate_icon"),
	TAPE("tape_icon"),
	WEST_SLATE("west_slate_icon");

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
			Objects.requireNonNull(itemIconAtlas, "Icon TextureAtlas is null");
			sprite = itemIconAtlas.createSprite(fileName);
			sprite.flip(false, true);
		}

		return sprite;
	}
}

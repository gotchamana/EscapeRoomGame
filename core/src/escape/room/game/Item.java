package escape.room.game;

import com.badlogic.gdx.graphics.g2d.*;

public enum Item {

	LIGHTER("lighter_icon");

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
			if (itemIconAtlas == null) {
				throw new NullPointerException("itemIconAtlas is null");
			}
			sprite = itemIconAtlas.createSprite(fileName);
		}

		return sprite;
	}
}

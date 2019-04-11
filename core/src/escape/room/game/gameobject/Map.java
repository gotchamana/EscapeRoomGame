package escape.room.game.gameobject;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import java.util.Arrays;

public class Map {
	
	private Array<Sprite> sprites;
	private Array<TouchableSprite> touchableSprites;

	public Map() {
		sprites = new Array<>();
		touchableSprites = new Array<>();
	}

	public void addSprite(Sprite sprite) {
		sprites.add(sprite);

		if (sprite instanceof TouchableSprite) {
			touchableSprites.add((TouchableSprite)sprite);
		}
	}

	public void addSprites(Sprite... sprites) {
		this.sprites.addAll(sprites);

		Arrays.stream(sprites).filter(s -> s instanceof TouchableSprite).forEach(s -> touchableSprites.add((TouchableSprite)s));
	}

	public void removeSprite(Sprite sprite) {
		sprites.removeValue(sprite, true);

		if (sprite instanceof TouchableSprite) {
			touchableSprites.removeValue((TouchableSprite)sprite, true);
		}
	}

	public TouchableSprite[] getTouchableSprites() {
		touchableSprites.reverse();
		TouchableSprite[] rlt = touchableSprites.toArray(TouchableSprite.class);
		touchableSprites.reverse();

		return rlt;
	}

	public void draw(Batch batch) {
		for (Sprite sprite : sprites) {
			if (sprite instanceof TouchableSprite && !((TouchableSprite)sprite).isVisible()) {
				continue;
			}
			sprite.draw(batch);
		}
	}
}

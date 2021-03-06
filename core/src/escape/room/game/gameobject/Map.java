package escape.room.game.gameobject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import escape.room.game.Drawable;
import java.util.Arrays;

public class Map {
	
	private Array<Drawable> drawableObjects;
	private Array<TouchableSprite> touchableSprites;

	public Map() {
		drawableObjects = new Array<>();
		touchableSprites = new Array<>();
	}

	public void addDrawableObject(Drawable drawableObject) {
		drawableObjects.add(drawableObject);

		if (drawableObject instanceof TouchableSprite) {
			touchableSprites.add((TouchableSprite)drawableObject);
		}
	}

	public void addDrawableObjects(Drawable... drawableObjects) {
		this.drawableObjects.addAll(drawableObjects);

		Arrays.stream(drawableObjects)
			.filter(s -> s instanceof TouchableSprite)
			.forEach(s -> touchableSprites.add((TouchableSprite)s));
	}

	public void removeDrawableObject(Drawable drawableObject) {
		drawableObjects.removeValue(drawableObject, true);

		if (drawableObject instanceof TouchableSprite) {
			touchableSprites.removeValue((TouchableSprite)drawableObject, true);
		}
	}

	public void removeDrawableObjects(Drawable... drawableObjects) {
		for (Drawable d : drawableObjects) {
			this.drawableObjects.removeValue(d, true);

			if (d instanceof TouchableSprite) {
				touchableSprites.removeValue((TouchableSprite)d, true);
			}
		}
	}

	public TouchableSprite[] getTouchableSprites() {
		touchableSprites.reverse();
		TouchableSprite[] rlt = touchableSprites.toArray(TouchableSprite.class);
		touchableSprites.reverse();

		return rlt;
	}

	public void draw(Batch batch) {
		for (Drawable drawableObject : drawableObjects) {
			drawableObject.draw(batch);
		}
	}

	public void draw(Batch batch, float delta) {
		for (Drawable drawableObject : drawableObjects) {
			drawableObject.draw(batch, delta);
		}
	}
}

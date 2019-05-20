package escape.room.game.gameobject;

import escape.room.game.Drawable;
import com.badlogic.gdx.graphics.g2d.*;

public class CustomSprite extends Sprite implements Drawable {

	protected boolean isVisible;

	public CustomSprite(Sprite sprite) {
		super(sprite);
		isVisible = true;
	}

	public CustomSprite(Sprite sprite, boolean isVisible) {
		super(sprite);
		this.isVisible = isVisible;
	}

	@Override
	public void draw(Batch batch) {
		if (isVisible) {
			super.draw(batch);
		}
	}

	@Override
	public void draw(Batch batch, float delta) {
		Drawable.super.draw(batch, delta);
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}

package escape.room.game.gameobject;

import escape.room.game.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.*;

public class CustomAnimation<T extends Sprite> extends Animation<T> implements Drawable {
	
	private boolean isVisible;
	private float elapse;

	public CustomAnimation(float frameDuration, Array<? extends T> keyFrames) {
		super(frameDuration, keyFrames);
		isVisible = true;
	}

	public CustomAnimation(float frameDuration, Array<? extends T> keyFrames, Animation.PlayMode playMode) {
		super(frameDuration, keyFrames, playMode);
		isVisible = true;
	}

	@Override
	public void draw(Batch batch) {
		if (isVisible) {
			getKeyFrame(elapse).draw(batch);
		}
	}

	@Override
	public void draw(Batch batch, float delta) {
		if (isVisible) {
			elapse += delta;
			getKeyFrame(elapse).draw(batch);
		}
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

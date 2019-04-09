package escape.room.game;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class TouchableSprite extends Sprite implements Touchable {

	private boolean isVisible, isTouchable;
	private TouchEventHandler touchDownHandler, touchUpHandler;

	public TouchableSprite(Sprite sprite) {
		super(sprite);
		isVisible = true;
		isTouchable = true;
	}

	public TouchableSprite(Sprite sprite, boolean isVisible) {
		super(sprite);
		this.isVisible = isVisible;
		isTouchable = isVisible;
	}

	public TouchableSprite(Sprite sprite, boolean isVisible, boolean isTouchable) {
		super(sprite);
		this.isVisible = isVisible;
		this.isTouchable = isTouchable;
	}

	@Override
	public void onTouchDown() {
		if (touchDownHandler != null) {
			touchDownHandler.handle();
		}
	}

	@Override
	public void onTouchDragged(int deltaX, int deltaY) {
		
	}

	@Override
	public void onTouchUp() {
		if (touchUpHandler != null) {
			touchUpHandler.handle();
		}
	}

	@Override
	public boolean isTouchable() {
		return isTouchable;
	}

	@Override
	public void setTouchable(boolean isTouchable) {
		this.isTouchable = isTouchable;
	}

	public void setTouchDownHandler(TouchEventHandler touchDownHandler) {
		this.touchDownHandler = touchDownHandler;
	}

	public void setTouchUpHandler(TouchEventHandler touchUpHandler) {
		this.touchUpHandler = touchUpHandler;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}

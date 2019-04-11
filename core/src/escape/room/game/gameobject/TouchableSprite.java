package escape.room.game.gameobject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import escape.room.game.event.*;
import escape.room.game.Touchable;

public class TouchableSprite extends Sprite implements Touchable {

	private boolean isVisible, isTouchable;
	private TouchEventHandler touchDownHandler, touchDraggedHandler, touchUpHandler;

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
	public boolean onTouchDown(TouchEvent event) {
		if (touchDownHandler != null && isTouchable) {
			return touchDownHandler.handle(event);
		}
		return false;
	}

	@Override
	public boolean onTouchDragged(TouchEvent event) {
		if (touchDraggedHandler != null && isTouchable) {
			return touchDownHandler.handle(event);
		}
		return false;
	}

	@Override
	public boolean onTouchUp(TouchEvent event) {
		if (touchUpHandler != null && isTouchable) {
			return touchUpHandler.handle(event);
		}
		return false;
	}

	@Override
	public boolean isTouchable() {
		return isTouchable;
	}

	@Override
	public void setTouchable(boolean isTouchable) {
		this.isTouchable = isTouchable;
	}

	public void setOnTouchDown(TouchEventHandler touchDownHandler) {
		this.touchDownHandler = touchDownHandler;
	}

	public void setOnTouchUp(TouchEventHandler touchUpHandler) {
		this.touchUpHandler = touchUpHandler;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}

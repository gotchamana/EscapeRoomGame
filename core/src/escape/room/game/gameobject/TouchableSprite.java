package escape.room.game.gameobject;

import escape.room.game.event.*;
import escape.room.game.Touchable;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TouchableSprite extends CustomSprite implements Touchable {

	protected boolean isTouchable;
	private TouchEventHandler touchDownHandler, touchDraggedHandler, touchUpHandler;

	public TouchableSprite(Sprite sprite) {
		super(sprite);
		isTouchable = true;
	}

	public TouchableSprite(Sprite sprite, boolean isVisible) {
		super(sprite, isVisible);
		isTouchable = isVisible;
	}

	public TouchableSprite(Sprite sprite, boolean isVisible, boolean isTouchable) {
		super(sprite, isVisible);
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
			return touchDraggedHandler.handle(event);
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

	public void setOnTouchDragged(TouchEventHandler touchDraggedHandler) {
		this.touchDraggedHandler = touchDraggedHandler;
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

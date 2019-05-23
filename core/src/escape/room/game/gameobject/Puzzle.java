package escape.room.game.gameobject;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Puzzle extends TouchableSprite {
	
	private int touchX, touchY;
	private boolean isTouch, isCorrectPosition;

	private Rectangle targetRegion;

	public Puzzle(Sprite sprite) {
		super(sprite);

		targetRegion = new Rectangle();

		setOnTouchDown(e -> {
			isTouch = true;

			touchX = e.getScreenX();
			touchY = e.getScreenY();
			return true;
		});

		setOnTouchDragged(e -> {
			isTouch = true;

			translateX(e.getScreenX() - touchX);
			translateY(e.getScreenY() - touchY);

			touchX = e.getScreenX();
			touchY = e.getScreenY();
			return true;
		});
	}

	public void setIsTouch(boolean isTouch) {
		this.isTouch = isTouch;
	}

	public boolean isTouch() {
		return isTouch;
	}

	public void setIsCorrectPosition(boolean isCorrectPosition) {
		this.isCorrectPosition = isCorrectPosition;
	}

	public boolean isCorrectPosition() {
		return isCorrectPosition;
	}

	public Rectangle getTargetRegion() {
		return targetRegion;
	}

	public void setTargetRegion(int targetX, int targetY, int targetSize) {
		targetRegion.x = targetX - targetSize / 2;
		targetRegion.y = targetY - targetSize / 2;
		targetRegion.width = targetSize;
		targetRegion.height = targetSize;
	}
}

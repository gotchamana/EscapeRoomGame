package escape.room.game.gameobject;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Sprite;

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

		setOnTouchUp(e -> {
			isTouch = false;
			if (targetRegion.contains(getX(), getY())) {
				isCorrectPosition = true;
				setTouchable(false);
				setPosition(targetRegion.x + targetRegion.width / 2, targetRegion.y + targetRegion.height / 2);
			}
			return true;
		});
	}

	public boolean isTouch() {
		return isTouch;
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

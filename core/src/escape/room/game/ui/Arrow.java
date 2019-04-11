package escape.room.game.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import escape.room.game.gameobject.TouchableSprite;

public class Arrow extends TouchableSprite {
	
	public enum ArrowType {
		DOWN("arrow_down_black"), LEFT("arrow_left_black"), RIGHT("arrow_right_black");

		private String name;

		private ArrowType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public Arrow(ArrowType type, TextureAtlas uiAtlas) {
		super(uiAtlas.createSprite(type.getName()));

		final int VIEWPORT_WIDTH = 640;
		final int VIEWPORT_HEIGHT = 480;
		final int PADDING = 20;

		setScale(0.8f);

		switch (type) {
			case DOWN: 
				setCenterX(VIEWPORT_WIDTH / 2);
				setY(VIEWPORT_HEIGHT - getHeight() - PADDING);
				break;

			case LEFT:
				setX(20);
				setCenterY(VIEWPORT_HEIGHT / 2);
				break;

			case RIGHT:
				setX(VIEWPORT_WIDTH - getWidth() - PADDING);
				setCenterY(VIEWPORT_HEIGHT / 2);
				break;
		}
	}
}

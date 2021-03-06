package escape.room.game.event;

import com.badlogic.gdx.*;
import escape.room.game.gameobject.TouchableSprite;
import escape.room.game.screen.ERScreen;

public class ERScreenInputHandler extends InputAdapter {
	
	private ERScreen screen;

	public ERScreenInputHandler(ERScreen screen) {
		this.screen = screen;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		TouchableSprite[] touchableSprites = screen.getCurrentMap().getTouchableSprites();

		for (TouchableSprite touchableSprite : touchableSprites) {
			if (touchableSprite.getBoundingRectangle().contains(screenX, screenY)) {
				if (touchableSprite.onTouchDown(new TouchEvent(screenX, screenY, pointer, button))) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		TouchableSprite[] touchableSprites = screen.getCurrentMap().getTouchableSprites();

		for (TouchableSprite touchableSprite : touchableSprites) {
			if (touchableSprite.getBoundingRectangle().contains(screenX, screenY)) {
				if (touchableSprite.onTouchUp(new TouchEvent(screenX, screenY, pointer, button))) {
					return true;
				}
			}
		}

		return false;
	}
}

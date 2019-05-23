package escape.room.game.event;

import com.badlogic.gdx.InputAdapter;
import escape.room.game.gameobject.TouchableSprite;
import escape.room.game.screen.PuzzleScreen;

public class PuzzleScreenInputHandler extends InputAdapter {
	
	private PuzzleScreen screen;

	public PuzzleScreenInputHandler(PuzzleScreen screen) {
		this.screen = screen;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		TouchableSprite[] touchableSprites = screen.getPuzzleBoard().getTouchableSprites();

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
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		TouchableSprite[] touchableSprites = screen.getPuzzleBoard().getTouchableSprites();

		for (TouchableSprite touchableSprite : touchableSprites) {
			if (touchableSprite.getBoundingRectangle().contains(screenX, screenY)) {
				if (touchableSprite.onTouchDragged(new TouchEvent(screenX, screenY, pointer))) {
					return true;
				}
			}
		}

		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		TouchableSprite[] touchableSprites = screen.getPuzzleBoard().getTouchableSprites();

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

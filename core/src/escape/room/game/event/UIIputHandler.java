package escape.room.game.event;

import com.badlogic.gdx.InputAdapter;
import escape.room.game.gameobject.TouchableSprite;
import escape.room.game.ui.*;

public class UIIputHandler extends InputAdapter {

	private ItemTray itemTray;

	public UIIputHandler(ItemTray itemTray) {
		this.itemTray = itemTray;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean result = false;

		Cell[] cells = itemTray.getCells();
		Cell preSelectedCell = null;

		for (Cell cell : cells) {
			// 先篩掉空的道具格
			if (!cell.isEmpty()) {

				// 檢查有無已經選取的道具格，若有則記錄之
				if (cell.isSelected()) {
					preSelectedCell = cell;
				}

				TouchableSprite sprite = cell.getItemSprite();
				
				// 檢查滑鼠是否座落於道具方格中，以決定是否執行sprite的onTouchDown方法
				if (sprite.getBoundingRectangle().contains(screenX, screenY)) {
					result = sprite.onTouchDown(new TouchEvent(screenX, screenY, pointer, button));
				}

			}
		}

		// 如果有已經選取的道具格，而且使用者點在其他方格的話，則把原本的道具格給取消選取
		if (preSelectedCell != null && result) {
			preSelectedCell.setSelected(false);
		}

		for (TouchableSprite sprite : itemTray.getTriangles()) {
			if (sprite.getBoundingRectangle().contains(screenX, screenY)) {
				if (sprite.onTouchDown(new TouchEvent(screenX, screenY, pointer, button))) {
					break;
				}
			}
		}

		return result;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Cell[] cells = itemTray.getCells();

		for (Cell cell : cells) {
			if (!cell.isEmpty()) {
				TouchableSprite sprite = cell.getItemSprite();
				
				if (sprite.getBoundingRectangle().contains(screenX, screenY)) {
					sprite.onTouchUp(new TouchEvent(screenX, screenY, pointer, button));
				}

				return true;
			}
		}

		return false;
	}
}

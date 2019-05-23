package escape.room.game.ui;

import com.badlogic.gdx.graphics.g2d.*;
import escape.room.game.Drawable;
import escape.room.game.gameobject.TouchableSprite;

public class Cell implements Drawable {
	
	private int posX, posY;
	private boolean isEmpty, isSelected, isVisible;
	private Item item;
	private Sprite unselectedSprite, selectedSprite;
	private TouchableSprite itemSprite;

	public Cell(TextureAtlas uiAtlas, int posX, int posY) {
		this(uiAtlas, posX, posY, true);
	}

	public Cell(TextureAtlas uiAtlas, int posX, int posY, boolean isVisible) {
		this.posX = posX;
		this.posY = posY;

		unselectedSprite = uiAtlas.createSprite("unselected_cell");
		unselectedSprite.setPosition(posX, posY);

		selectedSprite = uiAtlas.createSprite("selected_cell");
		selectedSprite.setPosition(posX, posY);

		this.isVisible = isVisible;
		isSelected = false;
		isEmpty = true;
	}

	public void setItem(Item item) {
		isEmpty = false;
		this.item = item;

		itemSprite = new TouchableSprite(item.getSprite(), isVisible);
		itemSprite.setOnTouchDown(e -> {
			isSelected = true;
			return true;
		});
		itemSprite.setPosition(posX, posY);
	}

	public Item getItem() {
		return item;
	}

	public TouchableSprite getItemSprite() {
		return itemSprite;
	}

	public void removeItem() {
		isEmpty = true;
		isSelected = false;

		item = null;
		itemSprite = null;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;

		if (!isEmpty) {
			itemSprite.setVisible(isVisible);
			itemSprite.setTouchable(isVisible);
		}
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void draw(Batch batch) {
		if (isVisible) {
			if (isSelected) {
				selectedSprite.draw(batch);
			} else {
				unselectedSprite.draw(batch);
			}

			if (!isEmpty) {
				itemSprite.draw(batch);
			}
		}
	}
}

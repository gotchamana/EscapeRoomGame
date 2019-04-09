package escape.room.game;

import com.badlogic.gdx.graphics.g2d.*;

public class Cell {
	
	private int posX, posY;
	private boolean isEmpty, isSelected;
	private Item item;
	private Sprite unselectedSprite, selectedSprite;
	private TouchableSprite itemSprite;

	public Cell(TextureAtlas uiAtlas, int posX, int posY) {
		this.posX = posX;
		this.posY = posY;

		unselectedSprite = uiAtlas.createSprite("unselected_cell");
		unselectedSprite.setPosition(posX, posY);

		selectedSprite = uiAtlas.createSprite("selected_cell");
		selectedSprite.setPosition(posX, posY);

		isSelected = false;
		isEmpty = true;
	}

	public void setItem(Item item) {
		isEmpty = false;
		this.item = item;

		itemSprite = new TouchableSprite(item.getSprite());
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

	public void draw(Batch batch) {
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

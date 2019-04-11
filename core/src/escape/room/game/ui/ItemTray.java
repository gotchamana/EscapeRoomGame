package escape.room.game.ui;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import java.util.Objects;

public class ItemTray {
	
	private final int CELL_NUMBER = 18;
	private final int CELL_SPACING = 12, CELL_LEFT_PADDING = 10, CELL_TOP_PADDING = 12, CELL_SIZE = 40;
	private static TextureAtlas uiAtlas;
	private static ItemTray itemTray;
	private Sprite traySprite;
	private Array<Cell> cells;

	private ItemTray() {
		Objects.requireNonNull(uiAtlas, "UI TextureAtlas is null");

		traySprite = uiAtlas.createSprite("bar");
		traySprite.setPosition(640, 0);

		cells = new Array<>();
		for (int i = 0; i < CELL_NUMBER / 2; i++) {
			cells.add(new Cell(uiAtlas, 640 + CELL_LEFT_PADDING, CELL_TOP_PADDING + i * (CELL_SIZE + CELL_SPACING)));
		}
	}

	public static ItemTray getItemTray() {
		if (itemTray == null) {
			itemTray = new ItemTray();
		}

		return itemTray;
	}

	public static void setUiAtlas(TextureAtlas uiAtlas) {
		ItemTray.uiAtlas = uiAtlas;
	}

	public void addItem(Item item) {
		for (Cell cell : cells) {
			if (cell.isEmpty()) {
				cell.setItem(item);
				break;
			}
		}
	}

	public Cell[] getCells() {
		return cells.toArray(Cell.class);
	}

	public void draw(Batch batch) {
		traySprite.draw(batch);

		for (Cell cell : cells) {
			cell.draw(batch);
		}
	}
}

package escape.room.game.ui;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Array;
import java.util.Objects;
import escape.room.game.gameobject.TouchableSprite;

public class ItemTray {
	
	private final int CELL_NUMBER = 18;
	private final int CELL_SPACING = 12, CELL_LEFT_PADDING = 10, CELL_TOP_PADDING = 2, CELL_SIZE = 40;
	private static TextureAtlas uiAtlas;
	private static ItemTray itemTray;
	private Sprite traySprite;
	private TouchableSprite rightTriangle, leftTriangle;
	private Array<Cell> firstColumnCells, secondColumnCells;

	private ItemTray() {
		Objects.requireNonNull(uiAtlas, "UI TextureAtlas is null");

		// 道具欄
		traySprite = uiAtlas.createSprite("bar");
		traySprite.setPosition(640, 0);

		// 道具格
		firstColumnCells = new Array<>();
		secondColumnCells = new Array<>();
		for (int i = 0; i < CELL_NUMBER / 2; i++) {
			firstColumnCells.add(new Cell(uiAtlas, 640 + CELL_LEFT_PADDING, CELL_TOP_PADDING + i * (CELL_SIZE + CELL_SPACING)));
			secondColumnCells.add(new Cell(uiAtlas, 640 + CELL_LEFT_PADDING, CELL_TOP_PADDING + i * (CELL_SIZE + CELL_SPACING), false));
		}

		// 翻頁箭頭
		leftTriangle = new TouchableSprite(uiAtlas.createSprite("left_triangle"), false);
		rightTriangle = new TouchableSprite(uiAtlas.createSprite("right_triangle"));

		leftTriangle.flip(false, true);
		leftTriangle.setCenterX(670);
		leftTriangle.setY(460);
		leftTriangle.setOnTouchDown(e -> {
			leftTriangle.setVisible(false);
			leftTriangle.setTouchable(false);

			rightTriangle.setVisible(true);
			rightTriangle.setTouchable(true);

			setFirstColumnCellsVisible(true);
			setSecondColumnCellsVisible(false);
			return true;
		});

		rightTriangle.flip(false, true);
		rightTriangle.setCenterX(670);
		rightTriangle.setY(460);
		rightTriangle.setOnTouchDown(e -> {
			rightTriangle.setVisible(false);
			rightTriangle.setTouchable(false);

			leftTriangle.setVisible(true);
			leftTriangle.setTouchable(true);

			setFirstColumnCellsVisible(false);
			setSecondColumnCellsVisible(true);
			return true;
		});
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
		for (Cell cell : firstColumnCells) {
			if (cell.isEmpty()) {
				cell.setItem(item);
				return;
			}
		}

		for (Cell cell : secondColumnCells) {
			if (cell.isEmpty()) {
				cell.setItem(item);
				return;
			}
		}
	}

	public Cell[] getCells() {
		Array<Cell> rlt = new Array<>(CELL_NUMBER);
		rlt.addAll(firstColumnCells);
		rlt.addAll(secondColumnCells);

		return rlt.toArray(Cell.class);
	}

	public Cell getSelectedCell() {
		for (Cell cell : getCells()) {
			if (cell.isSelected()) {
				return cell;
			}
		}
		return null;
	}

	public TouchableSprite[] getTriangles() {
		return new TouchableSprite[]{leftTriangle, rightTriangle};
	}

	public void draw(Batch batch) {
		traySprite.draw(batch);

		for (Cell cell : firstColumnCells) {
			cell.draw(batch);
		}

		for (Cell cell : secondColumnCells) {
			cell.draw(batch);
		}

		leftTriangle.draw(batch);
		rightTriangle.draw(batch);
	}

	private void setFirstColumnCellsVisible(boolean isVisible) {
		for (Cell cell : firstColumnCells) {
			cell.setVisible(isVisible);
		}
	}

	private void setSecondColumnCellsVisible(boolean isVisible) {
		for (Cell cell : secondColumnCells) {
			cell.setVisible(isVisible);
		}
	}
}

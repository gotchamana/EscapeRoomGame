package escape.room.game.gameobject;

import com.badlogic.gdx.utils.Array;
import escape.room.game.Drawable;

public class PuzzleBoard extends Map {
	
	private Array<Puzzle> puzzles;

	public PuzzleBoard() {
		super();
		puzzles = new Array<>(8);
	}

	@Override
	public void addDrawableObject(Drawable drawableObject) {
		super.addDrawableObject(drawableObject);
		if (drawableObject instanceof Puzzle) {
			puzzles.add((Puzzle)drawableObject);
		}
	}

	@Override
	public void addDrawableObjects(Drawable... drawableObjects) {
		super.addDrawableObjects(drawableObjects);
		for (Drawable drawableObject : drawableObjects) {
			if (drawableObject instanceof Puzzle) {
				puzzles.add((Puzzle)drawableObject);
			}
		}
	}

	public void update() {
		Puzzle touched = getTouchedPuzzle();
		if (touched == null) {
			for (Puzzle puzzle : puzzles) {
				if (!puzzle.isCorrectPosition()) {
					puzzle.setTouchable(true);
				}
			}
		} else {
			for (Puzzle puzzle : puzzles) {
				if (puzzle != touched) {
					puzzle.setTouchable(false);
				}
			}
		}
	}

	private Puzzle getTouchedPuzzle() {
		Puzzle touched = null;
		for (Puzzle puzzle : puzzles) {
			if (puzzle.isTouch()) {
				touched = puzzle;
				break;
			}
		}

		return touched;
	}
}

package escape.room.game.gameobject;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class PuzzleBoard extends Map {
	
	private Array<Puzzle> puzzles;

	public PuzzleBoard() {
		super();
		puzzles = new Array<>(8);
	}

	@Override
	public void addSprite(Sprite sprite) {
		super.addSprite(sprite);
		if (sprite instanceof Puzzle) {
			puzzles.add((Puzzle)sprite);
		}
	}

	@Override
	public void addSprites(Sprite... sprites) {
		super.addSprites(sprites);
		for (Sprite sprite : sprites) {
			if (sprite instanceof Puzzle) {
				puzzles.add((Puzzle)sprite);
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

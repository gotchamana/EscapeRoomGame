package escape.room.game.screen;

import escape.room.game.EscapeRoomGame;

public class ScreenManager {
	
	private static LoadingScreen loadingScreen;
	private static ERScreen gameScreen;
	private static PuzzleScreen puzzleScreen;

	private  ScreenManager() {
	}

	public static LoadingScreen getLoadingScreen(EscapeRoomGame game) {
		if (loadingScreen == null) {
			loadingScreen = new LoadingScreen(game);
		}

		return loadingScreen;
	}

	public static ERScreen getERScreen(EscapeRoomGame game) {
		if (gameScreen == null) {
			gameScreen = new ERScreen(game);
		}

		return gameScreen;
	}

	public static PuzzleScreen getPuzzleScreen(EscapeRoomGame game) {
		if (puzzleScreen == null) {
			puzzleScreen = new PuzzleScreen(game);
		}

		return puzzleScreen;
	}

	public static void dispose() {
		loadingScreen.dispose();
		gameScreen.dispose();
		puzzleScreen.dispose();
	}
}

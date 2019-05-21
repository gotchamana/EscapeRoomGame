package escape.room.game;

public class GameData {

	private static GameData gameData;
	private boolean isEastHoleEmpty, isSouthHoleEmpty, isWestHoleEmpty, isNorthHoleEmpty;
	private boolean isFireBurning, isDrawerLock;

	private GameData() {
		isEastHoleEmpty = true;
		isSouthHoleEmpty = true;
		isWestHoleEmpty = true;
		isNorthHoleEmpty = true;
		isFireBurning = true;
		isDrawerLock = true;
	}

	public static GameData getGameData() {
		if (gameData == null) {
			gameData = new GameData();
		}
		return gameData;
	}

	public void setIsEastHoleEmpty(boolean isEastHoleEmpty) {
		this.isEastHoleEmpty = isEastHoleEmpty;
	}

	public boolean isEastHoleEmpty() {
		return isEastHoleEmpty;
	}

	public void setIsSouthHoleEmpty(boolean isSouthHoleEmpty) {
		this.isSouthHoleEmpty = isSouthHoleEmpty;
	}

	public boolean isSouthHoleEmpty() {
		return isSouthHoleEmpty;
	}

	public void setIsWestHoleEmpty(boolean isWestHoleEmpty) {
		this.isWestHoleEmpty = isWestHoleEmpty;
	}

	public boolean isWestHoleEmpty() {
		return isWestHoleEmpty;
	}

	public void setIsNorthHoleEmpty(boolean isNorthHoleEmpty) {
		this.isNorthHoleEmpty = isNorthHoleEmpty;
	}

	public boolean isNorthHoleEmpty() {
		return isNorthHoleEmpty;
	}

	public void setIsFireBurning(boolean isFireBurning) {
		this.isFireBurning = isFireBurning;
	}

	public boolean isFireBurning() {
		return isFireBurning;
	}

	public void setIsDrawerLock(boolean isDrawerLock) {
		this.isDrawerLock = isDrawerLock;
	}

	public boolean isDrawerLock() {
		return isDrawerLock;
	}
}

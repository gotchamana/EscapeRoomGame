package escape.room.game;

import escape.room.game.gameobject.*;
import java.util.function.Consumer;

public class GameData {

	private static GameData gameData;
	private boolean isEastHoleEmpty, isSouthHoleEmpty, isWestHoleEmpty, isNorthHoleEmpty;
	private boolean isFireBurning, isDrawerLock, isPuzzleFinished;
	private DecimalCycle decimalCycle;
	private DuodecimalCycle duodecimalCycle;
	private Code code1, code2, code3, code4;
	private Consumer<Boolean> windowBrokenFun;

	private GameData() {
		isEastHoleEmpty = true;
		isSouthHoleEmpty = true;
		isWestHoleEmpty = true;
		isNorthHoleEmpty = true;
		isFireBurning = true;
		isDrawerLock = true;
		isPuzzleFinished = false;

		decimalCycle = DecimalCycle.DECIMAL_CYCLE1;
		duodecimalCycle = DuodecimalCycle.DUODECIMAL_CYCLE1;

		code1 = Code.CODE1;
		code2 = Code.CODE1;
		code3 = Code.CODE1;
		code4 = Code.CODE1;
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

	public void setIsPuzzleFinished(boolean isPuzzleFinished) {
		this.isPuzzleFinished = isPuzzleFinished;
	}

	public boolean isPuzzleFinished() {
		return isPuzzleFinished;
	}

	public void setDecimalCycle(DecimalCycle decimalCycle) {
		this.decimalCycle = decimalCycle;
	}

	public DecimalCycle getDecimalCycle() {
		return decimalCycle;
	}

	public void setDuodecimalCycle(DuodecimalCycle duodecimalCycle) {
		this.duodecimalCycle = duodecimalCycle;
	}

	public DuodecimalCycle getDuodecimalCycle() {
		return duodecimalCycle;
	}

	public void setCode1(Code code1) {
		this.code1 = code1;
	}

	public Code getCode1() {
		return code1;
	}

	public void setCode2(Code code2) {
		this.code2 = code2;
	}

	public Code getCode2() {
		return code2;
	}
	public void setCode3(Code code3) {
		this.code3 = code3;
	}

	public Code getCode3() {
		return code3;
	}
	public void setCode4(Code code4) {
		this.code4 = code4;
	}

	public Code getCode4() {
		return code4;
	}

	public void setWindowBrokenFun(Consumer<Boolean> windowBrokenFun) {
		this.windowBrokenFun = windowBrokenFun;
	}

	public Consumer<Boolean> getWindowBrokenFun() {
		return windowBrokenFun;
	}
}

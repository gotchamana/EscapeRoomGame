package escape.room.game;

public class TouchEvent {
	
	private int screenX, screenY, pointer, button, amount;

	public TouchEvent(int amount) {
		this(-1, -1, -1, -1);
		this.amount = amount;
	}

	public TouchEvent(int screenX, int screenY) {
		this(screenX, screenY, -1, -1);
	}

	public TouchEvent(int screenX, int screenY, int pointer) {
		this(screenX, screenY, pointer, -1);
	}

	public TouchEvent(int screenX, int screenY, int pointer, int button) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.pointer = pointer;
		this.button = button;
	}

	public int getScreenX() {
		return screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public int getPointer() {
		return pointer;
	}

	public int getButton() {
		return button;
	}

	public int getAmount() {
		return amount;
	}
}

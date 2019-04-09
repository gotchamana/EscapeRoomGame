package escape.room.game;

public interface Touchable {

	public void onTouchDown();

	public void onTouchDragged(int deltaX, int deltaY);

	public void onTouchUp();

	public boolean isTouchable();

	public void setTouchable(boolean isTouchable);
}

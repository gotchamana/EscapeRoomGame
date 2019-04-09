package escape.room.game;

public interface Touchable {

	public boolean onTouchDown(TouchEvent event);

	public boolean onTouchDragged(TouchEvent event);

	public boolean onTouchUp(TouchEvent event);

	public boolean isTouchable();

	public void setTouchable(boolean isTouchable);
}

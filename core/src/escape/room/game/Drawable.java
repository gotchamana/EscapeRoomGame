package escape.room.game;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Drawable {

	public void draw(Batch batch);

	default public void draw(Batch batch, float delta) {
		draw(batch);
	}

	public boolean isVisible();

	public void setVisible(boolean isVisible);
}

package escape.room.game.gameobject;

import com.badlogic.gdx.graphics.g2d.*;
import escape.room.game.*;

public class CustomFont extends BitmapFont implements Drawable {
	
	private boolean isVisible, wrap;
	private String str;
	private float x, y, targetWidth;
	private int halign;

	public CustomFont(BitmapFont font, boolean flip) {
		super(font.getData().getFontFile(), flip);
		str = "";
		isVisible = true;
	}

	@Override
	public void draw(Batch batch) {
		if (isVisible) {
			draw(batch, str, x, y, targetWidth, halign, wrap);
		}
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setTargetWidth(float targetWidth) {
		this.targetWidth = targetWidth;
	}

	public void setContent(String str) {
		if (str.length() > 8) {
			str = str.substring(0, 8);
		}
		this.str = str;
	}

	public String getContent() {
		return str;
	}

	public void setWrap(boolean wrap) {
		this.wrap = wrap;
	}

	public void setHalign(int halign) {
		this.halign = halign;
	}
}

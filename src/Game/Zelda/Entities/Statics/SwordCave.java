package Game.Zelda.Entities.Statics;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.BaseEntity;
import Main.Handler;
import Resources.Images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class SwordCave extends BaseEntity {

	Rectangle interactBounds;

	public SwordCave(int x, int y, BufferedImage sprite, Handler handler) {
		super(x, y, sprite, handler);
		interactBounds = (Rectangle) bounds.clone();
		interactBounds.y += (height / 2);
		interactBounds.height /= 2;
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(Images.Sword, 2 * (ZeldaGameState.stageWidth / 16) + ZeldaGameState.xOffset + 210,
				(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset + (14 * 2) + 80, 12, 32, null);
	}

	public Rectangle getInteractBounds() {
		return interactBounds;
	}
}

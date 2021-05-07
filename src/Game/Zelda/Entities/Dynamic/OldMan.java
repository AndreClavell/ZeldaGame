package Game.Zelda.Entities.Dynamic;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.BaseEntity;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class OldMan extends BaseMovingEntity {

	private final int animSpeed = 120;
	
	public OldMan(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		
		BufferedImage[] animList = new BufferedImage[3];
		animList[0] = sprite[0];
		animList[1] = sprite[1];
		animList[2] = sprite[2];
		animation = new Animation(animSpeed*3, animList);
	}

	@Override
	public void tick() {
		animation.tick();
	}
	@Override
	public void render(Graphics g) {
		g.drawImage(animation.getCurrentFrame(), x, y, width, height, null);
	}

}

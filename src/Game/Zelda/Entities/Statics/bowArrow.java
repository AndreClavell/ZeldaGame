package Game.Zelda.Entities.Statics;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Dynamic.BaseMovingEntity;
import Game.Zelda.Entities.Dynamic.Direction;
import Main.Handler;
import Resources.Animation;

public class bowArrow extends weapon {
	
	int speed;
	Direction direction;
	Animation animation;
	BufferedImage[] sprites;
	boolean moving = false;
	boolean dead = false;
	public boolean pushback = false;
	public int hitCooldown = 0;
	public int arrowTime = 60 * 5;
	
	public bowArrow(int x, int y, Direction direction, BufferedImage sprite, Handler handler) {
		super(x, y, sprite, handler);
		this.direction = direction;

		speed = 3;

	}

	@Override
	public void tick() {
		for (BaseMovingEntity entity: handler.getZeldaGameState().enemies.get(handler.getZeldaGameState().getMapX()).get(handler.getZeldaGameState().getMapY())) {
			if(bounds.intersects(entity.bounds)) {					
				if (entity.hitCooldown <= 0) {
					entity.damage(2);
					arrowTime = 0;
				}
			}
		}
		if(arrowTime == 0) {
			bounds.x = 0;
			bounds.y = 0;
		}
		//Moves the arrow the direction its fired
		if(direction == Direction.UP) {
			y-=speed;
			changeIntersectingBounds();
		}else if(direction == Direction.DOWN) {
			y+=speed;
			changeIntersectingBounds();
		}else if(direction == Direction.RIGHT) {
			x+=speed;
			changeIntersectingBounds();
		}else if(direction == Direction.LEFT) {
			x-=speed;
			changeIntersectingBounds();
		}
		
	}
	
	@Override
	public void render(Graphics g) {
		if(direction == Direction.UP || direction == Direction.DOWN) {
			g.drawImage(sprite, x, y, 16, 32, null);
		}else if(direction == Direction.RIGHT || direction == Direction.LEFT) {
			g.drawImage(sprite, x, y, 32, 32, null);
		}
	
	}
	
	public void changeIntersectingBounds() {
		switch (direction) {
		case DOWN:
			bounds.y += speed;
			break;
		case UP:
			bounds.y -= speed;
			break;
		case LEFT:
			bounds.x -= speed;
			break;
		case RIGHT:
			bounds.x += speed;
			break;
		}
	}

}

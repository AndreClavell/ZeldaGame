package Game.Zelda.Entities.Statics;

import Game.GameStates.Zelda.ZeldaMMGameState;
import Game.Zelda.Entities.MMBaseEntity;
import Game.Zelda.Entities.Dynamic.Direction;
import Game.Zelda.Entities.Dynamic.MMBaseMovingEntity;
import Game.Zelda.Entities.Dynamic.MMLink;
import Game.Zelda.World.Map;
import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class MMDirectionTile extends MMBaseEntity {

	public int linkedX,linkedY;
	public Direction direction;
	public Map map;
	int amount = 20;
	int coolDown = amount;
	
	public MMDirectionTile(int x, int y, Direction direction, BufferedImage sprite, Handler handler) {
		super(x, y, sprite,handler);
		this.direction = direction;
		bounds = new Rectangle(x ,y ,width,height);

	}
	public Direction move(Direction direction) {
		//Recursive method to move Link, the cooldown was added for a smoother transition
		
		if(handler.getState() instanceof ZeldaMMGameState && ((ZeldaMMGameState)handler.getState()).map.link.interactBounds.intersects(bounds)) {
			if(direction == Direction.UP) {
				((ZeldaMMGameState)handler.getState()).map.link.y-=1;
				((ZeldaMMGameState)handler.getState()).map.link.move(direction);
				if(coolDown > 0) {
					coolDown--;
				}else {
					coolDown = amount;
				return move(direction);
				}
			}else if(direction == Direction.DOWN) {
				((ZeldaMMGameState)handler.getState()).map.link.y+=1;
				((ZeldaMMGameState)handler.getState()).map.link.move(direction);
				if(coolDown > 0) {
					coolDown--;
				}else {
					coolDown = amount;
				return move(direction);
				}
			}else if(direction == Direction.RIGHT) {
				((ZeldaMMGameState)handler.getState()).map.link.x+=1;
				((ZeldaMMGameState)handler.getState()).map.link.move(direction);
				if(coolDown > 0) {
					coolDown--;
				}else {
					coolDown = amount;
				return move(direction);
				}
			}else if(direction == Direction.LEFT) {
				((ZeldaMMGameState)handler.getState()).map.link.x-=1;
				((ZeldaMMGameState)handler.getState()).map.link.move(direction);
				if(coolDown > 0) {
					coolDown--;
				}else {
					coolDown = amount;
				return move(direction);
				}
			}
		}
		return null;
	}
	@Override
	public void tick() {
		if(handler.getState() instanceof ZeldaMMGameState && ((ZeldaMMGameState)handler.getState()).map.link.interactBounds.intersects(bounds)) {
			move(direction);
		}
		
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(sprite,x ,y,width,height,null);
	}
}

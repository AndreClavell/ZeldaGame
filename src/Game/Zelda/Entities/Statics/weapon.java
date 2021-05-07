package Game.Zelda.Entities.Statics;

import java.awt.image.BufferedImage;

import Game.Zelda.Entities.BaseEntity;
import Game.Zelda.Entities.Dynamic.BaseMovingEntity;
import Main.Handler;

//Base class for all weapons

public class weapon extends BaseEntity {
	
	public weapon(int x, int y, BufferedImage sprite, Handler handler) {
		super(x, y, sprite, handler);
	
	}
	@Override
	public void tick() {
		//When bounds of weapon intersect enemies damage then with a cooldown
		
		for (BaseMovingEntity entity: handler.getZeldaGameState().enemies.get(handler.getZeldaGameState().getMapX()).get(handler.getZeldaGameState().getMapY())) {
			if(bounds.intersects(entity.bounds)) {					
				if (entity.hitCooldown <= 0) {
					entity.damage(2);
				}
			}
		}
		//Remove bounds if Link isnt attacking
		if(!handler.getZeldaGameState().link.linkAttack) {
			bounds.x = 0;
			bounds.y = 0;
		}
	}
}

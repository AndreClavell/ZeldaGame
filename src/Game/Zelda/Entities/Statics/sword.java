package Game.Zelda.Entities.Statics;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.BaseEntity;
import Game.Zelda.Entities.Dynamic.BaseMovingEntity;
import Game.Zelda.Entities.Dynamic.Direction;
import Game.Zelda.Entities.Dynamic.enemyTektite;
import Main.Handler;
import Resources.Images;

public class sword extends weapon {
	
	public sword(int x, int y, BufferedImage sprite, Handler handler) {
		super(x, y, sprite, handler);
	
	}
	@Override
	public void render(Graphics g) {
		
		//Draws Link's sword hurtbox (yellow rectangle)
		if(handler.getZeldaGameState().link.getDirection() == Direction.UP) {
			int[] xPoints = new int[]{x+17,x + 8,x + 8,x+17};
			int[] yPoints = new int[]{y+6,y+6,y+32,y+ 32};
			g.drawPolygon(xPoints,yPoints , 4);
			
		}else if(handler.getZeldaGameState().link.getDirection() == Direction.DOWN){
			int[] xPoints = new int[]{x+21,x + 12,x + 12,x+21};
			int[] yPoints = new int[]{y+1,y+1,y+25,y+ 25};
			g.drawPolygon(xPoints,yPoints , 4);
			
		}else if (handler.getZeldaGameState().link.getDirection() == Direction.LEFT) {
			int[] xPoints = new int[]{x - 23,x + 2,x + 2,x - 23};
			int[] yPoints = new int[]{y+14,y+14,y+23,y+ 23};
			g.drawPolygon(xPoints,yPoints , 4);
			
		}else if (handler.getZeldaGameState().link.getDirection() == Direction.RIGHT) {
			int[] xPoints = new int[]{x+23,x - 2,x - 2,x+23};
			int[] yPoints = new int[]{y+14,y+14,y+23,y+ 23};
			g.drawPolygon(xPoints,yPoints , 4);
		}
	}
}

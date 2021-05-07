package Game.Zelda.Entities.Dynamic;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;


import Game.Zelda.Entities.Statics.SectionDoor;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Main.Handler;
import Resources.Animation;
import Resources.Images;


public class enemyTektite extends BaseMovingEntity{

	private final int animSpeed = 120;
	Random random = new Random();
	int movingCooldown = 120;
	int movingCounter = 0;
	Direction direction;

	public enemyTektite(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		speed = 1;
		health = 4;
		BufferedImage[] animList = new BufferedImage[2];
		animList[0] = sprite[0];
		animList[1] = sprite[1];
		animation = new Animation(animSpeed * 3, animList);


	}
	@Override
	public void tick() {
		if(!dead) {
			if(!moving) {
				//Moves the tektike to a random direction in a random timer
				int randMov = random.nextInt(50);
				if (randMov == 1) {
					moving = true;
					direction = Direction.UP;
					BufferedImage[] animList = new BufferedImage[2];
					animList[0] = Images.bouncyEnemyFrames[0];
					animList[1] = Images.bouncyEnemyFrames[1];
					animation = new Animation(animSpeed * 3, animList);
					
				}else if (randMov == 2) {
					moving = true;
					direction = Direction.DOWN;
					BufferedImage[] animList = new BufferedImage[2];
					animList[0] = Images.bouncyEnemyFrames[0];
					animList[1] = Images.bouncyEnemyFrames[1];
					animation = new Animation(animSpeed * 3, animList);
					
				}else if (randMov == 3) {
					moving = true;
					direction = Direction.LEFT;
					BufferedImage[] animList = new BufferedImage[2];
					animList[0] = Images.bouncyEnemyFrames[0];
					animList[1] = Images.bouncyEnemyFrames[1];
					animation = new Animation(animSpeed * 3, animList);
					
				}else if (randMov == 4) {
					moving = true;
					direction = Direction.RIGHT;
					BufferedImage[] animList = new BufferedImage[2];
					animList[0] = Images.bouncyEnemyFrames[0];
					animList[1] = Images.bouncyEnemyFrames[1];
					animation = new Animation(animSpeed * 3, animList);
					
				}
			}
			if (moving) {
				if (movingCooldown <= 0 ) {
					moving = false;
					movingCooldown = 120;
					movingCounter = 0;
				}else {
					movingCooldown --;


					if (movingCounter <= 60) {
						animation.tick();
						movingCounter++;
						move(direction);
					}

				}
			}
			//Moves the enemy back a little when is hit by Link
			if(pushback) {
				if(handler.getZeldaGameState().link.getDirection() == Direction.UP) {
					y-=10;
				}else if(handler.getZeldaGameState().link.getDirection() == Direction.DOWN) {
					y+=10;
				}else if(handler.getZeldaGameState().link.getDirection() == Direction.LEFT) {
					x-=10;
				}else if(handler.getZeldaGameState().link.getDirection() == Direction.RIGHT) {
					x+=10;
				}
				BufferedImage[] animList = new BufferedImage[2];
				animList[0] = Images.bouncyEnemyHurtFrames[0];
				animList[1] = Images.bouncyEnemyHurtFrames[1];
				animation = new Animation(animSpeed * 3, animList);
				pushback = false;
				
				bounds.x = x;
				bounds.y = y;
				changeIntersectingBounds();
			}
		}else if (dead) {
			while(moving) {
				BufferedImage[] animList = new BufferedImage[2];
				animList[0] = Images.enemyDeath[0];
				animList[1] = Images.enemyDeath[1];
				animation = new Animation(animSpeed * 5, animList);
				moving = false;
			}
			animation.tick();
		}else if (wasHit) {
			animation.tick();
		}
		
		if(hitCooldown > 0) {
			hitCooldown--;
		}else {
			wasHit = false;
		}
		

	}
	@Override
	public void render(Graphics g) {
		if(handler.getZeldaGameState().mapX == 7 && handler.getZeldaGameState().mapY == 7 && !dead)
		g.drawImage(animation.getCurrentFrame(),x,y, width, height, null);
		if(dead && !animation.end) {
			g.drawImage(animation.getCurrentFrame(),x,y, width, height, null);
				bounds.x = -1000;
				bounds.y = -1000;
				changeIntersectingBounds();
			}
		}
	

	@Override
	public void move(Direction direction) {
		moving = true;
		changeIntersectingBounds();
		if(!dead) {
		for (SolidStaticEntities objects : handler.getZeldaGameState().objects.get(handler.getZeldaGameState().mapX)
				.get(handler.getZeldaGameState().mapY)) {
			if (!(objects instanceof SectionDoor) && objects.bounds.intersects(interactBounds)) {
				// dont move
				return;
			}
		}

		switch (direction) {
		case RIGHT:
			x += speed;
			break;
		case LEFT:
			x -= speed;
			break;
		case UP:
			y -= speed;
			break;
		case DOWN:
			y += speed;
			break;
		}
		bounds.x = x;
		bounds.y = y;
		changeIntersectingBounds();
		
		

	}
	}
}


package Game.Zelda.Entities.Dynamic;

import Game.GameStates.Zelda.ZeldaGameState;
import Game.Zelda.Entities.Statics.DungeonDoor;
import Game.Zelda.Entities.Statics.SectionDoor;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Game.Zelda.Entities.Statics.bowArrow;
import Game.Zelda.Entities.Statics.sword;
import Main.Handler;
import Resources.Animation;
import Resources.Images;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static Game.GameStates.Zelda.ZeldaGameState.worldScale;
import static Game.Zelda.Entities.Dynamic.Direction.DOWN;
import static Game.Zelda.Entities.Dynamic.Direction.UP;

/**
 * Created by AlexVR on 3/15/2020
 */
public class Link extends BaseMovingEntity {

	private final int animSpeed = 120;
	int newMapX = 0, newMapY = 0, xExtraCounter = 0, yExtraCounter = 0;
	public boolean movingMap = false;
	Direction movingTo;
	public int healthTotal = 3;
	public boolean showSword = false;
	public boolean hasSword = false;
	public boolean itemGet = false;
	public boolean linkAttack = false;
	int getItemTimer = 60;
	int attackTimer = 30;
	int hitTimer = 30;
	int scale = 2;

	boolean hitAnim = false;
	boolean itemGetAnim = false;
	boolean linkAttackAnim = false;
	boolean right = false;
	boolean left = false;
	boolean up = false;
	boolean down = false;

	public sword LinkSword;
	public bowArrow arrow;
	public boolean sendArrow = false;
	
	// Used for left and up animations when attacking
	int xSwap = 0;
	int ySwap = 0;
	public int weaponCode = 0;

	boolean justAttacked = false;
	Direction direction;

	public Link(int x, int y, BufferedImage[] sprite, Handler handler) {
		super(x, y, sprite, handler);
		speed = 4;
		health = 6;
		BufferedImage[] animList = new BufferedImage[2];
		animList[0] = sprite[4];
		animList[1] = sprite[5];

		animation = new Animation(animSpeed, animList);

	}

	@Override
	public void tick() {

		//Swap Weapon
		if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_P) && hasSword) {
			//0 - Sword
			//1 - Bow
			if(weaponCode == 1) {
				weaponCode = 0;
			}else {
				weaponCode++;
			}
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_ENTER) && hasSword) {
			linkAttack = true;
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_H)) {
			if (health / 2 == healthTotal) {
				healthTotal++;
			}
			health++;
		}
		if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_J) && health > 0) {
			wasHit = true;
		}
		if (movingMap) {
			switch (movingTo) {
			case RIGHT:
				handler.getZeldaGameState().cameraOffsetX += scale;
				newMapX += scale;
				if (xExtraCounter > 0) {
					x += 2 * scale;
					xExtraCounter -= scale;
					animation.tick();

				} else {
					x -= scale;
				}
				break;
			case LEFT:
				handler.getZeldaGameState().cameraOffsetX -= scale;
				newMapX -= scale;
				if (xExtraCounter > 0) {
					x -= 2 * scale;
					xExtraCounter -= scale;
					animation.tick();

				} else {
					x += scale;
				}
				break;
			case UP:
				handler.getZeldaGameState().cameraOffsetY -= scale;
				newMapY += scale;
				if (yExtraCounter > 0) {
					y -= 2 * scale;
					yExtraCounter -= scale;
					animation.tick();

				} else {
					y += scale;
				}
				break;
			case DOWN:
				handler.getZeldaGameState().cameraOffsetY += scale;
				newMapY -= scale;
				if (yExtraCounter > 0) {
					y += 2 * scale;
					yExtraCounter -= scale;
					animation.tick();
				} else {
					y -= scale;
				}
				break;
			}
			bounds = new Rectangle(x, y, width, height);
			changeIntersectingBounds();
			if (newMapX == 0 && newMapY == 0) {
				movingMap = false;
				movingTo = null;
				newMapX = 0;
				newMapY = 0;
			}
		} else {
			if (!wasHit) {
				if (!itemGetAnim) {
					if (!linkAttack) {
						if (handler.getKeyManager().up) {
							if (direction != UP || justAttacked == true) {
								BufferedImage[] animList = new BufferedImage[2];
								animList[0] = sprites[4];
								animList[1] = sprites[5];
								animation = new Animation(animSpeed, animList);
								direction = UP;
								sprite = sprites[4];
								justAttacked = false;
							}
							animation.tick();
							move(direction);

						} else if (handler.getKeyManager().down) {
							if (direction != DOWN || justAttacked == true) {
								BufferedImage[] animList = new BufferedImage[2];
								animList[0] = sprites[0];
								animList[1] = sprites[1];
								animation = new Animation(animSpeed, animList);
								direction = DOWN;
								sprite = sprites[0];
								justAttacked = false;
							}
							animation.tick();
							move(direction);
						} else if (handler.getKeyManager().left) {
							if (direction != Direction.LEFT || justAttacked == true) {
								BufferedImage[] animList = new BufferedImage[2];
								animList[0] = Images.flipHorizontal(sprites[2]);
								animList[1] = Images.flipHorizontal(sprites[3]);
								animation = new Animation(animSpeed, animList);
								direction = Direction.LEFT;
								sprite = Images.flipHorizontal(sprites[3]);
								justAttacked = false;
							}
							animation.tick();
							move(direction);
						} else if (handler.getKeyManager().right) {
							if (direction != Direction.RIGHT || justAttacked == true) {
								BufferedImage[] animList = new BufferedImage[2];
								animList[0] = (sprites[2]);
								animList[1] = (sprites[3]);
								animation = new Animation(animSpeed, animList);
								direction = Direction.RIGHT;
								sprite = (sprites[3]);
								justAttacked = false;
							}
							animation.tick();
							move(direction);
						} else {
							moving = false;
						}

					}
					//Sword Attack
					if (linkAttack && weaponCode == 0) {
						if (attackTimer <= 0) {
							linkAttack = false;
							linkAttackAnim = false;
							attackTimer = 30;
							justAttacked = true;
						} else {
							if (linkAttackAnim) {
								animation.tick();
								attackTimer--;
							} else {
								if (direction == UP) {

									BufferedImage[] linkAtt = new BufferedImage[4];
									linkAtt[0] = Images.linkUpAttack[0];
									linkAtt[1] = Images.linkUpAttack[1];
									linkAtt[2] = Images.linkUpAttack[2];
									linkAtt[3] = Images.linkUpAttack[3];
									animation = new Animation(animSpeed, linkAtt);

									LinkSword = new sword(bounds.x, bounds.y - bounds.height, Images.Sword, handler);
								} else if (direction == DOWN) {

									BufferedImage[] linkAtt = new BufferedImage[4];
									linkAtt[0] = Images.linkDownAttack[0];
									linkAtt[1] = Images.linkDownAttack[1];
									linkAtt[2] = Images.linkDownAttack[2];
									linkAtt[3] = Images.linkDownAttack[3];
									animation = new Animation(animSpeed, linkAtt);

									LinkSword = new sword(bounds.x, bounds.y + bounds.height, Images.Sword, handler);
								} else if (direction == Direction.RIGHT) {
									BufferedImage[] linkAtt = new BufferedImage[4];
									linkAtt[0] = Images.linkSideAttack[0];
									linkAtt[1] = Images.linkSideAttack[1];
									linkAtt[2] = Images.linkSideAttack[2];
									linkAtt[3] = Images.linkSideAttack[3];
									animation = new Animation(animSpeed, linkAtt);

									LinkSword = new sword(bounds.x + bounds.width, bounds.y, Images.Sword, handler);
								} else if (direction == Direction.LEFT) {
									BufferedImage[] linkAtt = new BufferedImage[4];
									linkAtt[0] = Images.linkSideAttack[0];
									linkAtt[1] = Images.linkSideAttack[1];
									linkAtt[2] = Images.linkSideAttack[2];
									linkAtt[3] = Images.linkSideAttack[3];

									linkAtt[0] = Images.flipHorizontal(linkAtt[0]);
									linkAtt[1] = Images.flipHorizontal(linkAtt[1]);
									linkAtt[2] = Images.flipHorizontal(linkAtt[2]);
									linkAtt[3] = Images.flipHorizontal(linkAtt[3]);
									animation = new Animation(animSpeed, linkAtt);

									LinkSword = new sword(bounds.x - 1, bounds.y, Images.Sword, handler);
								}
								linkAttackAnim = true;
							}
						}
					//Bow Attack
					}else if(linkAttack && weaponCode == 1) {
						if (attackTimer <= 0) {
							linkAttack = false;
							linkAttackAnim = false;
							attackTimer = 30;
							justAttacked = true;
						} else {
							if (linkAttackAnim) {
								animation.tick();
								attackTimer-=2;
							} else {
								if(direction == Direction.UP) {
									BufferedImage[] linkAtt = new BufferedImage[2];
									linkAtt[0] = Images.linkUpThrow[0];
									linkAtt[1] = Images.linkUpThrow[1];
									animation = new Animation(animSpeed, linkAtt);

									arrow = new bowArrow(bounds.x + (bounds.width / 4), bounds.y - bounds.height, direction, Images.arrow[0], handler);

								}else if(direction == Direction.DOWN) {
									BufferedImage[] linkAtt = new BufferedImage[2];
									linkAtt[0] = Images.linkDownThrow[0];
									linkAtt[1] = Images.linkDownThrow[1];
									animation = new Animation(animSpeed, linkAtt);

									arrow = new bowArrow(bounds.x + (bounds.width / 4), bounds.y + bounds.height, direction, Images.flipVertical(Images.arrow[0]), handler);

								}else if(direction == Direction.RIGHT) {
									BufferedImage[] linkAtt = new BufferedImage[2];
									linkAtt[0] = Images.linkSideThrow[0];
									linkAtt[1] = Images.linkSideThrow[1];
									animation = new Animation(animSpeed, linkAtt);

									arrow = new bowArrow(bounds.x + bounds.width, bounds.y + (bounds.height / 4), direction, Images.arrow[1], handler);

								}else if(direction == Direction.LEFT) {
									BufferedImage[] linkAtt = new BufferedImage[2];
									linkAtt[0] = Images.flipHorizontal(Images.linkSideThrow[0]);
									linkAtt[1] = Images.flipHorizontal(Images.linkSideThrow[1]);
									animation = new Animation(animSpeed, linkAtt);

									arrow = new bowArrow(bounds.x - bounds.width, bounds.y + (bounds.height / 4), direction, Images.flipHorizontal(Images.arrow[1]), handler);
								}
							}
							linkAttackAnim = true;
						}
					}

				}
				//Anim when getting item (Both Swords and Bow are obtained when the sword is picked up)
				if (itemGet) {
					LinkSword = new sword(0, 0, null, handler);
					arrow = new bowArrow(0,0,null,null,handler);
					if (getItemTimer <= 0) {
						itemGet = false;
						itemGetAnim = false;
						showSword = false;
						getItemTimer = 60;
					} else {
						if (itemGetAnim) {
							animation.tick();
							getItemTimer--;
						} else {
							BufferedImage[] animList = new BufferedImage[1];
							animList[0] = Images.linkItemGet[1];
							animation = new Animation(animSpeed, animList);
							showSword = true;
							itemGetAnim = true;
						}
					}
				}
			}
			if (hitCooldown > 0) {
				hitCooldown--;
			}
			//Hurt anim
			if (wasHit) {
				if (hitTimer <= 0) {
					wasHit = false;
					hitAnim = false;
					hitTimer = 30;
					hitCooldown = 90;
				} else {
					if (hitAnim) {
						animation.tick();
						hitTimer--;
					} else {
						if (direction == UP) {

							BufferedImage[] linkHurt = new BufferedImage[3];
							linkHurt[0] = Images.linkDamageUp[0];
							linkHurt[1] = Images.linkDamageUp[1];
							linkHurt[2] = Images.linkDamageUp[2];

							animation = new Animation(animSpeed * 2, linkHurt);

						} else if (direction == DOWN) {

							BufferedImage[] linkHurt = new BufferedImage[3];
							linkHurt[0] = Images.linkDamageDown[0];
							linkHurt[1] = Images.linkDamageDown[1];
							linkHurt[2] = Images.linkDamageDown[2];

							animation = new Animation(animSpeed * 2, linkHurt);

						} else if (direction == Direction.RIGHT) {
							BufferedImage[] linkHurt = new BufferedImage[3];
							linkHurt[0] = Images.linkDamageSide[0];
							linkHurt[1] = Images.linkDamageSide[1];
							linkHurt[2] = Images.linkDamageSide[2];

							animation = new Animation(animSpeed * 2, linkHurt);

						} else if (direction == Direction.LEFT) {
							BufferedImage[] linkHurt = new BufferedImage[4];
							linkHurt[0] = Images.linkDamageSide[0];
							linkHurt[1] = Images.linkDamageSide[1];
							linkHurt[2] = Images.linkDamageSide[2];

							linkHurt[0] = Images.flipHorizontal(linkHurt[0]);
							linkHurt[1] = Images.flipHorizontal(linkHurt[1]);
							linkHurt[2] = Images.flipHorizontal(linkHurt[2]);

							animation = new Animation(animSpeed * 2, linkHurt);

						}
						hitAnim = true;
					}
					//Mover link back when he is hit by an enemy
					if (pushback) {
						if (direction == UP) {
							y += 1;

						} else if (direction == DOWN) {
							y -= 1;

						} else if (direction == Direction.RIGHT) {
							x -= 1;

						} else if (direction == Direction.LEFT) {
							x += 1;

						}

						bounds.x = x;
						bounds.y = y;
						changeIntersectingBounds();
					}
				}
			}
		}
	}


	@Override
	public void render(Graphics g) {
		if (!wasHit) {
			if (!linkAttack) {
				if (moving) {
					g.drawImage(animation.getCurrentFrame(), x, y, width, height, null);

				} else {
					if (movingMap) {
						g.drawImage(animation.getCurrentFrame(), x, y, width, height, null);
					}
					g.drawImage(sprite, x, y, width, height, null);
				}
			}
		}

		if (wasHit) {
			g.drawImage(animation.getCurrentFrame(), x, y, width, height, null);
		}
		if (linkAttack) {
			if (weaponCode == 0) {
				LinkSword.render(g);
			}else if(weaponCode == 1) {
				sendArrow = true;
			}
			if (direction == Direction.UP) {
				// Moves link vertical position
				ySwap = y;
				if (animation.getCurrentFrame().getHeight() == 28) {
					ySwap = y - 24;
				} else if (animation.getCurrentFrame().getHeight() == 27) {
					ySwap = y - 22;
				} else if (animation.getCurrentFrame().getHeight() == 19) {
					ySwap = y - 6;
				}
				g.drawImage(animation.getCurrentFrame(), x, ySwap, width, animation.getCurrentFrame().getHeight() * 2,
						null);
			} else if (direction == Direction.DOWN) {
				g.drawImage(animation.getCurrentFrame(), x, y, width, animation.getCurrentFrame().getHeight() * 2,
						null);
			} else if (direction == Direction.RIGHT) {
				g.drawImage(animation.getCurrentFrame(), x, y, animation.getCurrentFrame().getWidth() * 2, height,
						null);
			} else if (direction == Direction.LEFT) {
				// Moves link horizontal position
				xSwap = x;
				if (animation.getCurrentFrame().getWidth() == 27) {
					xSwap = x - 22;
				} else if (animation.getCurrentFrame().getWidth() == 23) {
					xSwap = x - 14;
				} else if (animation.getCurrentFrame().getWidth() == 19) {
					xSwap = x - 6;
				}
				g.drawImage(animation.getCurrentFrame(), xSwap, y, animation.getCurrentFrame().getWidth() * 2, height,
						null);
			}
		}
		if(sendArrow) {
			if(arrow.arrowTime <=0) {
				sendArrow = false;
			}else {
				arrow.render(g);
				arrow.arrowTime--;
			}
		}
		
	}


@Override
public void move(Direction direction) {
	moving = true;
	changeIntersectingBounds();
	// chack for collisions
	if (ZeldaGameState.inCave) {
		for (SolidStaticEntities objects : handler.getZeldaGameState().caveObjects) {
			if ((objects instanceof DungeonDoor) && objects.bounds.intersects(bounds)
					&& direction == ((DungeonDoor) objects).direction) {
				if (((DungeonDoor) objects).name.equals("caveStartLeave")) {
					ZeldaGameState.inCave = false;
					x = ((DungeonDoor) objects).nLX;
					y = ((DungeonDoor) objects).nLY;
					direction = DOWN;
				}
			} else if (!(objects instanceof DungeonDoor) && objects.bounds.intersects(interactBounds)) {
				// dont move
				return;
			}
		}
	} else {
		for (SolidStaticEntities objects : handler.getZeldaGameState().objects.get(handler.getZeldaGameState().mapX)
				.get(handler.getZeldaGameState().mapY)) {
			if ((objects instanceof SectionDoor) && objects.bounds.intersects(bounds)
					&& direction == ((SectionDoor) objects).direction) {
				if (!(objects instanceof DungeonDoor)) {
					movingMap = true;
					movingTo = ((SectionDoor) objects).direction;
					switch (((SectionDoor) objects).direction) {
					case RIGHT:
						newMapX = -(((handler.getZeldaGameState().mapWidth) + 1) * worldScale);
						newMapY = 0;
						handler.getZeldaGameState().mapX++;
						xExtraCounter = 8 * worldScale + (2 * worldScale);
						break;
					case LEFT:
						newMapX = (((handler.getZeldaGameState().mapWidth) + 1) * worldScale);
						newMapY = 0;
						handler.getZeldaGameState().mapX--;
						xExtraCounter = 8 * worldScale + (2 * worldScale);
						break;
					case UP:
						newMapX = 0;
						newMapY = -(((handler.getZeldaGameState().mapHeight) + 1) * worldScale);
						handler.getZeldaGameState().mapY--;
						yExtraCounter = 8 * worldScale + (2 * worldScale);
						break;
					case DOWN:
						newMapX = 0;
						newMapY = (((handler.getZeldaGameState().mapHeight) + 1) * worldScale);
						handler.getZeldaGameState().mapY++;
						yExtraCounter = 8 * worldScale + (2 * worldScale);
						break;
					}
					return;
				} else {
					if (((DungeonDoor) objects).name.equals("caveStartEnter")) {
						ZeldaGameState.inCave = true;
						x = ((DungeonDoor) objects).nLX;
						y = ((DungeonDoor) objects).nLY;
						direction = UP;
					}
				}
			} else if (!(objects instanceof SectionDoor) && objects.bounds.intersects(interactBounds)) {
				// dont move
				return;
			}
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

public boolean isHasSword() {
	return hasSword;
}

public void setHasSword(boolean hasSword) {
	this.hasSword = hasSword;
}

public boolean isItemGet() {
	return itemGet;
}

public void setItemGet(boolean itemGet) {
	this.itemGet = itemGet;
}

public Direction getDirection() {
	return direction;
}
}

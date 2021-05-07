package Game.GameStates.Zelda;

import Game.GameStates.State;
import Game.Zelda.Entities.Dynamic.BaseMovingEntity;
import Game.Zelda.Entities.Dynamic.Direction;
import Game.Zelda.Entities.Dynamic.Link;
import Game.Zelda.Entities.Dynamic.OldMan;
import Game.Zelda.Entities.Dynamic.enemyTektite;
import Game.Zelda.Entities.Statics.DungeonDoor;
import Game.Zelda.Entities.Statics.SectionDoor;
import Game.Zelda.Entities.Statics.SolidStaticEntities;
import Game.Zelda.Entities.Statics.SwordCave;

import Main.Handler;
import Resources.Images;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by AlexVR on 3/14/2020
 */
public class ZeldaGameState extends State {

	public static int xOffset, yOffset, stageWidth, stageHeight, worldScale;
	public int cameraOffsetX, cameraOffsetY;
	// map is 16 by 7 squares, you start at x=7,y=7 starts counting at 0
	public int mapX, mapY, mapWidth, mapHeight;

	public ArrayList<ArrayList<ArrayList<SolidStaticEntities>>> objects;
	public ArrayList<ArrayList<ArrayList<BaseMovingEntity>>> enemies;
	public Link link;
	public static boolean inCave = false;
	public ArrayList<SolidStaticEntities> caveObjects;
	public SwordCave swordCave;
	public enemyTektite firstEnemy;
	public OldMan oldman;
	public ZeldaGameState(Handler handler) {
		super(handler);
		xOffset = handler.getWidth() / 3;
		yOffset = handler.getHeight() / 3;
		stageWidth = handler.getWidth() / 4 + (handler.getWidth() / 15);
		stageHeight = handler.getHeight() / 3;
		worldScale = 2;
		mapX = 7;
		mapY = 7;
		mapWidth = 256;
		mapHeight = 176;
		cameraOffsetX = ((mapWidth * mapX) + mapX -8) * worldScale;
		cameraOffsetY = ((mapHeight * mapY) + mapY -2) * worldScale;
		objects = new ArrayList<>();
		enemies = new ArrayList<>();
		caveObjects = new ArrayList<>();
		for (int i = 0; i < 16; i++) {
			objects.add(new ArrayList<>());
			enemies.add(new ArrayList<>());
			for (int j = 0; j < 8; j++) {
				objects.get(i).add(new ArrayList<>());
				enemies.get(i).add(new ArrayList<>());
			}
		}

		addWorldObjects();

		link = new Link(xOffset + (stageWidth / 2), yOffset + (stageHeight / 2), Images.zeldaLinkFrames, handler);

		swordCave = new SwordCave(2 * (ZeldaGameState.stageWidth / 16) + ZeldaGameState.xOffset + 210,
				(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset + (14 * worldScale) + 90, Images.Sword,
				handler);
		oldman = new OldMan( 2 * (ZeldaGameState.stageWidth / 16) + ZeldaGameState.xOffset + 200, 
				(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset + (14 * worldScale) + 40, Images.oldMan, handler);
		firstEnemy = new enemyTektite(900, 550, Images.bouncyEnemyFrames, handler);
		enemies.get(7).get(7).add(firstEnemy);
	}

	@Override
	public void tick() {
		link.tick();
			
		//Ticks for weapons
		if(link.hasSword) {
			if(link.weaponCode == 0) {
				link.LinkSword.tick();
			}else if(link.weaponCode == 1) {
				link.arrow.tick();
			}
			
		}
		if (inCave) {
			oldman.tick();
			if (swordCave.getInteractBounds().intersects(link.getInteractBounds()) && !link.isHasSword()) {
				link.setHasSword(true);
				link.setItemGet(true);
				handler.getMusicHandler().playEffect("zelda_get_item.wav");
			}
			if (!link.isItemGet()) {
				handler.getMusicHandler().resumeMusic();
			}
		} else {
			if (!link.movingMap) {
				for (SolidStaticEntities entity : objects.get(mapX).get(mapY)) {
					entity.tick();
				}
				for (BaseMovingEntity entity : enemies.get(mapX).get(mapY)) {
					entity.tick();
					if (entity.getInteractBounds().intersects(link.getInteractBounds())) {
						if(link.hitCooldown <= 0) {
							link.damage(1);
						}
						
					}
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		

		if (inCave) {
			for (SolidStaticEntities entity : caveObjects) {
				entity.render(g);
			}
			if (!link.isHasSword()) {
				if(!link.hasSword) {
				g.setColor(Color.WHITE);
				g.setFont(new Font("TimesRoman", Font.BOLD, 32));
				g.drawString("  IT ' S  DANGEROUS  TO  GO",
						(2 * (ZeldaGameState.stageWidth / 16)) + ZeldaGameState.xOffset,
						(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset + ((14 * worldScale)));
				g.drawString("  ALONE !   TAKE  THIS", (3 * (ZeldaGameState.stageWidth / 16)) + ZeldaGameState.xOffset,
						(5 * (ZeldaGameState.stageHeight / 14)) + ZeldaGameState.yOffset - ((2 * worldScale)));
				}
				oldman.render(g);
				g.drawImage(Images.Flame, 2 * (ZeldaGameState.stageWidth / 16) + ZeldaGameState.xOffset + 100 ,
						(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset + (14 * worldScale) + 40, 32,
						32, null);
				g.drawImage(Images.Flame, 2 * (ZeldaGameState.stageWidth / 16) + ZeldaGameState.xOffset + 300,
						(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset + (14 * worldScale) + 40, 32,
						32, null);

				swordCave.render(g);
				link.render(g);
			} else {
				// Renders sword above link's head
				if (link.showSword) {
					g.drawImage(Images.Sword, link.x + 11, link.y - 35, 14, 34, null);
				}
				//If link has the sword the message wont show
				if(!link.hasSword) {
				g.setColor(Color.WHITE);
				g.setFont(new Font("TimesRoman", Font.BOLD, 32));
				g.drawString("  IT ' S  DANGEROUS  TO  GO",
						(2 * (ZeldaGameState.stageWidth / 16)) + ZeldaGameState.xOffset,
						(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset + ((14 * worldScale)));
				g.drawString("  ALONE !   TAKE  THIS", (3 * (ZeldaGameState.stageWidth / 16)) + ZeldaGameState.xOffset,
						(5 * (ZeldaGameState.stageHeight / 14)) + ZeldaGameState.yOffset - ((2 * worldScale)));
				}
				oldman.render(g);
				g.drawImage(Images.Flame, 2 * (ZeldaGameState.stageWidth / 16) + ZeldaGameState.xOffset + 100,
						(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset + (14 * worldScale) + 40, 32,
						32, null);
				g.drawImage(Images.Flame, 2 * (ZeldaGameState.stageWidth / 16) + ZeldaGameState.xOffset + 300,
						(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset + (14 * worldScale) + 40, 32,
						32, null);

				link.render(g);
			}

		} else {
			g.drawImage(Images.zeldaMap, -cameraOffsetX + xOffset, -cameraOffsetY + yOffset,
					Images.zeldaMap.getWidth() * worldScale, Images.zeldaMap.getHeight() * worldScale, null);
			if (!link.movingMap) {
				for (SolidStaticEntities entity : objects.get(mapX).get(mapY)) {
					entity.render(g);
				}
				for (BaseMovingEntity entity : enemies.get(mapX).get(mapY)) {
					entity.render(g);
				}
			}

			link.render(g);
			firstEnemy.render(g);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, xOffset, handler.getHeight());
			g.fillRect(xOffset + stageWidth, 0, handler.getWidth(), handler.getHeight());
			g.fillRect(0, 0, handler.getWidth(), yOffset);
			g.fillRect(0, yOffset + stageHeight, handler.getWidth(), handler.getHeight());
		
			//Portrait of Link's current weapon
			if(link.hasSword) {
				g.setColor(Color.WHITE);
				g.setFont(new Font("TimesRoman", Font.BOLD, 24));
				g.drawString("P", ZeldaGameState.stageWidth / 2, handler.getHeight() - (handler.getHeight() / 2) - 140);
				
				if(link.weaponCode == 0) {
					g.drawImage(Images.Sword, ZeldaGameState.stageWidth / 2, handler.getHeight() - (handler.getHeight() / 2) - 200, 16, 32, null);
				}else if(link.weaponCode == 1) {
					g.drawImage(Images.bow, ZeldaGameState.stageWidth / 2, handler.getHeight() - (handler.getHeight() / 2) - 200, 16, 32, null);
				}
			}
			
			for (int i = 0; i < link.healthTotal; i++) {
				g.drawImage(Images.zeldaEmptyHeart,
						(handler.getWidth() - handler.getWidth() + handler.getWidth() / 48) + ((20 * 2) * i) + 50,
						handler.getHeight() - (handler.getHeight() / 2) - 200, handler.getWidth() / 64,
						handler.getHeight() / 64, null);
			}
			for (int i = 0; i < link.health; i++) {
				if(i %2 == 0) {
				g.drawImage(Images.zeldaHalfHeart,
						((handler.getWidth() - handler.getWidth() + handler.getWidth() / 48) + ((20 * 2) * i) + 50)/2 + 41,
						handler.getHeight() - (handler.getHeight() / 2) - 200, handler.getWidth() / 64,
						handler.getHeight() / 64, null);
				}
			}
			for (int i = 0; i < link.health / 2; i++) {
				g.drawImage(Images.zeldaFullHeart,
						(handler.getWidth() - handler.getWidth() + handler.getWidth() / 48) + ((20 * 2) * i) + 50,
						handler.getHeight() - (handler.getHeight() / 2) - 200, handler.getWidth() / 64,
						handler.getHeight() / 64, null);
			}

		}

	}

	private void addWorldObjects() {
		// cave
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 11; j++) {
				if (i >= 2 && i <= 13 && j >= 2 && j < 9) {
					continue;
				} else {
					if (j >= 9) {
						if (i > 1 && i < 14) {
							if ((i == 7 || i == 8)) {
								continue;
							} else {
								caveObjects.add(new SolidStaticEntities(i, j, Images.caveTiles.get(2), handler));
							}
						} else {
							caveObjects.add(new SolidStaticEntities(i, j, Images.caveTiles.get(5), handler));
						}
					} else {
						caveObjects.add(new SolidStaticEntities(i, j, Images.caveTiles.get(5), handler));
					}
				}
			}
		}
		caveObjects.add(new DungeonDoor(7, 9, 16 * worldScale * 2, 16 * worldScale * 2, Direction.DOWN,
				"caveStartLeave", handler, (4 * (ZeldaGameState.stageWidth / 16)) + ZeldaGameState.xOffset,
				(2 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset));

		// 7,7
		ArrayList<SolidStaticEntities> solids = new ArrayList<>();
		ArrayList<BaseMovingEntity> monster = new ArrayList<>();
		solids.add(new SectionDoor(0, 5, 16 * worldScale, 16 * worldScale, Direction.LEFT, handler));
		solids.add(new SectionDoor(7, 0, 16 * worldScale * 2, 16 * worldScale, Direction.UP, handler));
		solids.add(new DungeonDoor(3, 1, 16 * worldScale+ 30, 16 * worldScale, Direction.UP, "caveStartEnter", handler,
				(7 * (ZeldaGameState.stageWidth / 16)) + ZeldaGameState.xOffset,
				(9 * (ZeldaGameState.stageHeight / 11)) + ZeldaGameState.yOffset));
		solids.add(new SectionDoor(15, 5, 16 * worldScale, 16 * worldScale, Direction.RIGHT, handler));
		solids.add(new SolidStaticEntities(6, 0, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(5, 1, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(6, 1, Images.forestTiles.get(6), handler));
		solids.add(new SolidStaticEntities(3, 2, Images.forestTiles.get(6), handler));
		solids.add(new SolidStaticEntities(2, 3, Images.forestTiles.get(6), handler));
		solids.add(new SolidStaticEntities(1, 4, Images.forestTiles.get(6), handler));
		solids.add(new SolidStaticEntities(1, 6, Images.forestTiles.get(3), handler));
		solids.add(new SolidStaticEntities(1, 7, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(1, 8, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(2, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(3, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(4, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(5, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(6, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(7, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(8, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(9, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(10, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(11, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(12, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(13, 9, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(14, 8, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(14, 7, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(14, 6, Images.forestTiles.get(2), handler));
		solids.add(new SolidStaticEntities(14, 4, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(13, 4, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(12, 4, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(11, 4, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(10, 4, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(9, 4, Images.forestTiles.get(4), handler));
		solids.add(new SolidStaticEntities(8, 3, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(9, 2, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(9, 1, Images.forestTiles.get(5), handler));
		solids.add(new SolidStaticEntities(9, 0, Images.forestTiles.get(5), handler));
		objects.get(7).set(7, solids);

		// 6,7
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		solids.add(new SectionDoor(0, 2, 16 * worldScale, 16 * worldScale * 7, Direction.LEFT, handler));
		solids.add(new SectionDoor(12, 0, 16 * worldScale * 2, 16 * worldScale, Direction.UP, handler));
		solids.add(new SectionDoor(15, 5, 16 * worldScale, 16 * worldScale, Direction.RIGHT, handler));
		objects.get(6).set(7, solids);

		// 7,6
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		solids.add(new SectionDoor(0, 4, 16 * worldScale, 16 * worldScale * 3, Direction.LEFT, handler));
		solids.add(new SectionDoor(7, 10, 16 * worldScale * 2, 16 * worldScale, Direction.DOWN, handler));
		solids.add(new SectionDoor(15, 4, 16 * worldScale, 16 * worldScale * 3, Direction.RIGHT, handler));
		objects.get(7).set(6, solids);

		// 8,7
		monster = new ArrayList<>();
		solids = new ArrayList<>();
		solids.add(new SectionDoor(0, 5, 16 * worldScale, 16 * worldScale, Direction.LEFT, handler));
		solids.add(new SectionDoor(2, 0, 16 * worldScale * 13, 16 * worldScale, Direction.UP, handler));
		solids.add(new SectionDoor(15, 2, 16 * worldScale, 16 * worldScale * 7, Direction.RIGHT, handler));
		objects.get(8).set(7, solids);
	}

	@Override
	public void refresh() {

	}

	public int getMapX() {
		return mapX;
	}

	public int getMapY() {
		return mapY;
	}
}

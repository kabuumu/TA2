package factories;

import java.util.Random;
import main.Controller;
import entities.actors.Actor;
import entities.actors.ActorClass;
import entities.actors.AttributeModifier;
import entities.actors.Enemy;
import entities.actors.NPC;
import entities.actors.Player;
import entities.actors.Race;
import entities.map.Dungeon;
import entities.map.Tile;
import entities.map.TileMap;
import enums.ActorClassType;
import enums.NPCType;
import enums.TileType;

public class ActorFactory {
	
	public static Actor getActor(int playerLevel, Tile tile){
		Actor actor = null;
		int threshold = 195;
		TileType tileType = tile.getType();
		
		int random = new Random().nextInt(200);
		if(tileType==TileType.TOWN){
			threshold = 180;
		}
		if(tileType==TileType.DUNGEON){
			threshold = 170;
		}
		if(tileType==TileType.CAVE){
			threshold = 180;
		}
		
		if(random<threshold);
		else {
			if(tileType!=TileType.TOWN&&tileType!=TileType.DOOR&&tileType!=TileType.ROAD){
				actor = getEnemy(playerLevel, tile);
			}
			else if(tileType!=TileType.DOOR&&tileType!=TileType.ROAD){
				actor = getNPC(playerLevel);
			}
		}
		
		return actor;
	}
	
	public static Actor getEnemy(int level, Tile tile) {
		TileType tileType = tile.getType();
		
		if(tileType==TileType.DUNGEON){
			level = new Random().nextInt(level)+1+(level/4);
		}
		else if (tileType==TileType.CAVE){
			level = new Random().nextInt(level)+1;
		}
		else level = new Random().nextInt((level/2)+1)+1;
		
		String enemyName = getEnemyName(level, tileType);
		int enemyGold = getEnemyGold(level, tileType);
		Race enemyRace = RaceFactory.getRace(tile, level);
		ActorClass enemyClass= getActorClass(enemyRace);
		
		Actor enemy = new Enemy(enemyName, level, enemyGold, enemyRace, enemyClass);
		
		ItemFactory.getStartingItems(enemy);
		
		return enemy;
	}
	
	private static ActorClass getActorClass(Race enemyRace) {
		int random = new Random().nextInt(enemyRace.getAvailableClasses().length);
		return enemyRace.getAvailableClasses()[random];
	}

	private static int getEnemyGold(int level, TileType tileType) {
		int randomAdd = new Random().nextInt(level)+1;
		int randomMult = new Random().nextInt(level)+1;
		int enemyGold = (level*randomMult)+randomAdd;
		if(tileType!=TileType.DUNGEON && tileType != TileType.CAVE){
			enemyGold/=2;
		}
		return enemyGold;
	}

	
	private static String getEnemyName(int level, TileType tileType){
		String name = "";
		
		int random = (new Random().nextInt(2))+ level;
		
		if(tileType==TileType.DUNGEON||tileType==TileType.CAVE){
			switch(random){
			case 1:
				name = "BAT";
				break;
			case 2:
			case 3:
				name = "SPIDER";
				break;
			case 4:
			case 5:
			case 6:
				name = "GOBLIN";
				break;
			case 7:
			case 8:
			case 9:
				name = "KOBOLD";
				break;
			case 10:
			case 11:
			case 12:
				name = "ORC";
				break;
			case 13:
			case 14:
			case 15:
				name = "TROLL";
				break;
			case 16:
			case 17:
			case 18:
				name = "OGRE";
				break;
			default:
				name = "DRAGON";
				break;
			}
		}
		else{
			switch(random){
			case 1:
				name = "RAT";
				break;
			case 2:
			case 3:
			case 4:
				name = "FOX";
				break;
			case 5:
			case 6:
			case 7:
			case 8:
				name = "WOLF";
				break;
			case 9:
			case 10:
			case 11:
				name = "BEAR";
				break;
			case 12:
			case 13:
				name = "LION";
				break;
			default:
				name = "WEREWOLF";
				break;
			}
		}

		return name;
	}

	private static Actor getNPC(int playerLevel) {
		NPCType npcType = getNPCType();
		Race npcRace = RaceFactory.getNPCRace();
		ActorClass npcClass = RaceFactory.getNPCClass(npcType);

		NPC npc = new NPC(npcType.toString(),playerLevel, npcType, npcRace, npcClass);

		if(npc.getNpcType()==NPCType.MERCHANT){
			int random = 0;
			int itemCount = 0;
			while(random<3){
				npc.addItem(ItemFactory.getItem(npc));
				random = new Random().nextInt(4)+itemCount;
				itemCount++;
			}
		}
		return npc;
	}

	private static NPCType getNPCType() {
		int random = new Random().nextInt(5);
		switch (random){
		case 0:
		case 1:
		case 2:
			return NPCType.MERCHANT;
		case 3:
		case 4:
			return NPCType.QUEST;
		case 5:
			return NPCType.TRAINER;//No longer possible
		default:
			return null;
		}
	}

	public static void getEnemies(Player player, TileMap tileMap) {
		for(int x = player.getX()-Controller.getProximity();x<player.getX()+Controller.getProximity();x++){
			yLoop:
			for(int y = player.getY()-Controller.getProximity();y<player.getY()+Controller.getProximity();y++){
				Tile tile = tileMap.getTile(x,y);
				if(player.getVisibleTiles().contains(tile)==false){
					if(tile.getType()==TileType.DUNGEON&&(
							player.getTile().getType()==TileType.DUNGEON
							||
							player.getTile().getType()==TileType.DOOR)
							){
						for(Dungeon dungeon:tileMap.getDungeons()){
							if(dungeon.getTileArea().contains(tile)){
								continue yLoop;//return to y loop and test next tile.
							}
						}
					}
					if(tile!=null && tile.enemyAdded==false&&Tile.isValidQuestTile(tile)){
						Actor actor = getActor(player.getLevel(), tile);
						if(actor!=null){
							tile.addActor(actor);
						}
						tile.enemyAdded=true;
					}
				}
			}
		}
	}
	
	public static Actor getQuestEnemy(){
		String name = "BAHAMUT";
		int level = 99;
		int gold = 99999;
		Race race = RaceFactory.getQuestRace();
		ActorClass actorClass = new ActorClass("",ActorClassType.BEAST,new AttributeModifier[0]);
		Enemy enemy = new Enemy(name, level, gold, race, actorClass);
		return enemy;
	}
}

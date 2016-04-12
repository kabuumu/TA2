package factories;

import java.awt.geom.Point2D;
import java.util.Random;

import main.Controller;

import entities.actors.Actor;
import entities.actors.Player;
import entities.actors.Race;
import entities.items.Item;
import entities.map.Dungeon;
import entities.map.Tile;
import entities.map.TileMap;
import entities.quest.ClearQuest;
import entities.quest.CollectQuest;
import entities.quest.CullQuest;
import entities.quest.KillQuest;
import entities.quest.Quest;
import enums.ItemType;
import enums.QuestType;
import enums.RaceType;

public class QuestFactory {
	public static Quest getQuest(Player player, TileMap map){

		Item rewardItem = ItemFactory.getItem(player);
		int rewardGold = (player.getLevel()+new Random().nextInt(player.getLevel()+1))*10;
		int rewardExp = (player.getLevel()+new Random().nextInt(player.getLevel()+1))*10;

		QuestType type = getQuestType(new Random().nextInt(4));
		Quest quest;

		int x = 0;
		int y = 0;
		Tile tile = null;
		while(tile == null){
			do{
				x = player.getX()+(new Random().nextInt(31)-15); //Get random x number near to player.
				y = player.getY()+(new Random().nextInt(31)-15); //Get random y number near to player.
			} while (x==player.getX() && y==player.getY()); //While the new coordinates match the player number.
			try{
				tile=map.getTile(x,y);
				if(Tile.isValidQuestTile(tile)==false){
					tile=null;
				}
			}
			catch (Exception e){		
			}
		}

		switch(type){
		case KILL:
			Actor enemy = ActorFactory.getEnemy((int)(player.getLevel()+2*2),map.getTile(x,y));
			map.getTile(x, y).removeActor();
			map.getTile(x, y).addActor(enemy);
			quest = new KillQuest(rewardItem, rewardGold, rewardExp, QuestType.KILL, enemy);
			break;
		case CULL:
			Race cullEnemyRace = getCullEnemy(tile);
			int amountNeeded = getCullNeeded();
			quest = new CullQuest(rewardItem, rewardGold, rewardExp, QuestType.CULL, amountNeeded, cullEnemyRace);
			break;
		case COLLECT:
			Item collectItem = getCollectItem(tile);
			amountNeeded = getCollectNeeded();
			quest = new CollectQuest(rewardItem, rewardGold, rewardExp, QuestType.COLLECT, amountNeeded, collectItem.getName());
			break;
		case CLEAR:
			Dungeon questDungeon = getQuestDungeon();
			quest = new ClearQuest(rewardItem, rewardGold*2, rewardExp*2, QuestType.CLEAR, questDungeon);
			break;
		default:
			quest=null;
			break;
		}

		player.addQuest(quest);

		return quest;
	}

	private static Dungeon getQuestDungeon() {
		Dungeon questDungeon=null;
		Boolean uniqueDungeon = true;
		for(Dungeon dungeon:Controller.getTileMap().getDungeons()){
			do{
				if(questDungeon==null && dungeon.isVisited==false){
					questDungeon=dungeon;
				}
				else if(Point2D.distance(dungeon.getOrigin().getX(),dungeon.getOrigin().getY(),Controller.getPlayer().getX(),Controller.getPlayer().getX())
						<
						Point2D.distance(questDungeon.getOrigin().getX(),questDungeon.getOrigin().getY(),Controller.getPlayer().getX(),Controller.getPlayer().getX())
						&&
						dungeon.isVisited==false
						&&
						checkQuestDungeonUnique(dungeon)==true){
					questDungeon=dungeon;
				}
				

			}while(uniqueDungeon==false);
		}

		return questDungeon;
	}
	
	private static boolean checkQuestDungeonUnique(Dungeon dungeon){
		for(Quest quest:Controller.getPlayer().getQuests()){
			if(quest.getType()==QuestType.CLEAR){
				ClearQuest clearQuest = (ClearQuest)quest;
				if(clearQuest.getDungeon()==dungeon){
					return false;
				}
			}
		}
		return true;
	}

	private static int getCullNeeded() {
		return (new Random().nextInt(5)+2);
	}

	private static Race getCullEnemy(Tile tile) {
		Boolean uniqueEnemy = true;

		Race cullEnemyRace = null;
		do{
			uniqueEnemy = true;
			//Getting the enemy name to hunt.
			cullEnemyRace = RaceFactory.getRace(tile, Controller.getPlayer().getLevel());
			for(Quest quest:Controller.getPlayer().getQuests()){ //For all of the quests that the player currently has
				if(quest.getType()==QuestType.CULL){ //If they are cull quests
					CullQuest cullQuest = (CullQuest)quest; //Create a new variable and cast it to collect quest
					if(cullEnemyRace==cullQuest.getEnemyRace()){ //If the new race matches an already existing race
						uniqueEnemy=false; //The new Enemy is not unique, so get a different one
						break;
					}
				}
			}
		}while(uniqueEnemy==false);
		return cullEnemyRace;
	}

	private static int getCollectNeeded() {
		return (new Random().nextInt(3)+1);
	}

	private static Item getCollectItem(Tile tile) {
		//TODO broken code.
		Boolean uniqueItem=true;

		Item collectItem = null;
		do{
			uniqueItem=true;
			Actor enemy = ActorFactory.getEnemy(Controller.getPlayer().getLevel(), tile);
			do{
				ItemFactory.getStartingItems(enemy);
				collectItem = enemy.getInventoryRaw().get(new Random().nextInt(enemy.getInventoryRaw().size()));
			}
			while (collectItem.getType()!=ItemType.MISC);

			for(Quest quest:Controller.getPlayer().getQuests()){ //For all of the quests that the player currently has
				if(quest.getType()==QuestType.COLLECT){ //If they are collect quests
					CollectQuest collectQuest = (CollectQuest)quest; //Create a new variable and cast it to collect quest
					if(collectItem.getBaseName().equalsIgnoreCase(collectQuest.getItemName())){ //If the 
						uniqueItem=false; //The new item is not unique, so get a different one
						break;
					}
				}
			}
		}while(uniqueItem==false);
		return collectItem;
	}

	private static QuestType getQuestType(int random) {
		switch(random){
		case 0:
			return QuestType.COLLECT;
		case 1:
			return QuestType.KILL;
		case 2:
			return QuestType.CULL;
		case 3:
			return QuestType.CLEAR;
		default:
			return null;
		}
	}
}

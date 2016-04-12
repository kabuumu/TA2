package entities.quest;

import entities.actors.Player;
import entities.items.Item;
import entities.map.Dungeon;
import enums.QuestType;

public class ClearQuest extends Quest {
	private final Dungeon dungeon;
	
	public ClearQuest(Item rewardItem, int rewardGold, int rewardExp,
			QuestType type, Dungeon dungeon) {
		super(rewardItem, rewardGold, rewardExp, type);
		this.dungeon=dungeon;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}
	
	public int getY(){
		return dungeon.getEntrance().getY();
	}
	
	public int getX(){
		return dungeon.getEntrance().getX();
	}
	
	public String getDirection(Player player){
		String direction = "";
		if(player.getY()>getY()){
			direction += "SOUTH";
		}
		else if (player.getY()<getY()){
			direction += "NORTH";
		}
		if(player.getX()<getX()){
			direction+= "EAST";
			}
		else if(player.getX()>getX()){
			direction += "WEST";
		}
		return direction;
	}

}

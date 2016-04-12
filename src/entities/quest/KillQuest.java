package entities.quest;

import entities.actors.Actor;
import entities.actors.Player;
import entities.items.Item;
import enums.QuestType;

public class KillQuest extends Quest {
	private final Actor target;
	
	public KillQuest(Item rewardItem, int rewardGold,
			int rewardExp, QuestType type, Actor target) {
		super(rewardItem, rewardGold, rewardExp, type);
		this.target = target;
	}
	
	public int getX() {
		return target.getX();
	}

	public int getY() {
		return target.getY();
	}
	
	public Actor getTarget() {
		return target;
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

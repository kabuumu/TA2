package org.kabuumu.entities.quest;

import org.kabuumu.entities.actors.Actor;
import org.kabuumu.entities.actors.Player;
import org.kabuumu.entities.items.Item;
import org.kabuumu.enums.QuestType;

public class TalkQuest extends Quest {
	private final Actor target;
	
	public TalkQuest(Item rewardItem, int rewardGold,
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

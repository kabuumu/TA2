package entities.quest;

import entities.actors.Player;
import entities.items.Item;
import enums.QuestType;

public abstract class Quest {
	private final Item rewardItem;
	private final int rewardGold;
	private final int rewardExp;
	private final QuestType type;
	private Quest leadingQuest = null;
	
	public Quest(Item rewardItem, int rewardGold, int rewardExp, QuestType type){
		this.rewardItem = rewardItem;
		this.rewardGold = rewardGold;
		this.rewardExp = rewardExp;
		this.type = type;
	}
	
	public boolean completeQuest(Player player){
		player.addItem(getRewardItem());
		player.setGold(player.getGold()+getRewardGold());
		player.removeQuest(this);
		if(getLeadingQuest()!=null){
			player.addQuest(getLeadingQuest());
		}
		if(player.gainExp(rewardExp)){
			return true;
		}
		return false;
	}

	public Item getRewardItem() {
		return rewardItem;
	}

	public int getRewardGold() {
		return rewardGold;
	}

	public int getRewardExp() {
		return rewardExp;
	}

	public QuestType getType() {
		return type;
	}

	public void setLeadingQuest(Quest leadingQuest) {
		this.leadingQuest = leadingQuest;
	}

	public Quest getLeadingQuest() {
		return leadingQuest;
	}
}

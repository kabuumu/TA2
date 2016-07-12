package org.kabuumu.entities.quest;

import org.kabuumu.entities.items.Item;
import org.kabuumu.enums.QuestType;

public class CollectQuest extends Quest {
	private final int needed;
	private int current = 0;
	private final String itemName;
	
	public CollectQuest(Item rewardItem, int rewardGold,
			int rewardExp, QuestType type, int needed, String itemName) {
		super(rewardItem, rewardGold, rewardExp, type);
		this.needed = needed;
		this.itemName = itemName;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getNeeded() {
		return needed;
	}

	public String getItemName() {
		return itemName.toUpperCase();
	}
}

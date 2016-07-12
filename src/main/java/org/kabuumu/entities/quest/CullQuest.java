package org.kabuumu.entities.quest;

import org.kabuumu.entities.actors.Race;
import org.kabuumu.entities.items.Item;
import org.kabuumu.enums.QuestType;

public class CullQuest extends Quest {
	private final int needed;
	private int current = 0;
	private final Race enemyRace;
	
	public CullQuest(Item rewardItem, int rewardGold,
			int rewardExp, QuestType type, int needed, Race enemyRace) {
		super(rewardItem, rewardGold, rewardExp, type);
		this.needed = needed;
		this.enemyRace = enemyRace;
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

	public Race getEnemyRace() {
		return enemyRace;
	}
}

package entities.items;

import enums.AttributeName;

public class Enchantment {
	private final AttributeName attribute;
	private final int amount;
	
	public Enchantment(AttributeName attribute, int amount){
		this.amount = amount;
		this.attribute = attribute;
	}

	public AttributeName getAttribute() {
		return attribute;
	}

	public int getAmount() {
		return amount;
	}
}

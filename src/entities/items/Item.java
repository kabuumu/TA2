package entities.items;

import interfaces.HasName;
import enums.ItemType;

public class Item implements HasName{
	private final String name;
	private String displayName;
	private final ItemType type;
	private final int level;
	private final int value;
	private final Enchantment enchantment;
	
	public Item(String name, ItemType type, int level, int value) {
		this(name,type,level, value, null);
	}
	
	public Item(String name, ItemType type, int level, int value, Enchantment enchantment) {
		this.name = name;
		this.type = type;
		this.level = level;
		this.value = value;
		displayName = name;
		this.enchantment = enchantment;
	}
	
	public String getBaseName(){
		return name.toUpperCase();
	}
	
	public void setName(String name){
		this.displayName = name;
	}
	
	public String getName() {
		return displayName.toUpperCase();
	}

	public ItemType getType() {
		return type;
	}

	public int getLevel() {
		return level;
	}

	public int getValue() {
		return value;
	}

	public Enchantment getEnchantment() {
		return enchantment;
	}
}

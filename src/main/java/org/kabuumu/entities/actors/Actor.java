package org.kabuumu.entities.actors;

import org.kabuumu.interfaces.*;

import java.util.ArrayList;
import java.util.Random;

import org.kabuumu.utility.ShadowCaster;
import org.kabuumu.main.Controller;
import org.kabuumu.entities.StatusEffect;
import org.kabuumu.entities.items.Enchantment;
import org.kabuumu.entities.items.Item;
import org.kabuumu.entities.map.Tile;
import org.kabuumu.enums.ActorType;
import org.kabuumu.enums.AttributeName;

public abstract class Actor implements HasInventory, HasName{

	private static final int TurnCounterMax = 50;
	private final String name;
	private ArrayList<Item> inventory = new ArrayList<Item>();
	private int maxHealth;
	private int level;
	private int currentHealth;
	private int gold;
	private final ActorType type;
	private int x;
	private int y;
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	private Actor target = null;
	private int baseHealth = 10;
	private final Race race;
	private final ActorClass actorClass;
	private final ArrayList<StatusEffect> statusEffects = new ArrayList<StatusEffect>();
	
	private Item equippedWeapon;
	private Item equippedArmour;
	public int TurnCounter = TurnCounterMax;

	private static final int itemLimit = 8;
	private int inventoryMin = 0;
	private int inventoryMax = itemLimit;

	private ArrayList<Tile> visibleTiles;
	
	public void setAttack(){

	}

	public int getBaseHealth() {
		return baseHealth;
	}

	public void setBaseHealth(int baseHealth) {
		this.baseHealth = baseHealth;
	}

	public Actor(String name, ActorType type, int level, Race race, ActorClass actorClass){
		if(type==ActorType.PLAYER){
			this.name = name;
		}
		else{
			if(actorClass.getName().length()>0){
				this.name = race.getName() + " " + actorClass.getName();
			}
			else this.name = race.getName();
		}

		this.type = type;
		this.setLevel(level);
		this.race = race;
		this.actorClass = actorClass;

		attributes.add(new Attribute(AttributeName.STRENGTH,0));
		attributes.add(new Attribute(AttributeName.AGILITY,0));
		attributes.add(new Attribute(AttributeName.FORTITUDE,0));
		attributes.add(new Attribute(AttributeName.PERCEPTION,0));
		attributes.add(new Attribute(AttributeName.WILLPOWER,0));

		for(Attribute attribute:attributes){

			//Get the relevant race attribute modifier.
			float raceModifier = race.getAttributeModifier(attribute.getAttributeName()); 

			//Get the relevant class attribute modifier.
			float classModifier = actorClass.getAttributeModifier(attribute.getAttributeName()); 

			//Multiply the level by the race and class modifiers.
			setAttribute(attribute.getAttributeName(), (int)Math.max(
					10
					*
					Math.max(level/2,1)
					*
					raceModifier
					*
					classModifier
					,1f)); 
		}
		setCurrentHealth(getMaxHealth());
		updateVisibleTiles();
	}

	public ActorType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Item> getInventoryRaw(){
		return inventory;
	}

	public ArrayList<Item> getInventory() {
		ArrayList<Item> returnItems = new ArrayList<Item>();
		for(int i=0;i<inventory.size();i++){
			int itemCount=0;
			for(int j=i+1; j<inventory.size(); j++){
				if(inventory.get(i).getBaseName().equals(inventory.get(j).getBaseName())&&
						inventory.get(i).getLevel()==inventory.get(j).getLevel()){
					itemCount++;
				}
			}
			Boolean itemAdded = false;
			for(Item item:returnItems){
				if(item.getBaseName().equals(inventory.get(i).getBaseName())&&
						item.getLevel()==inventory.get(i).getLevel()){
					itemAdded=true;
					break;
				}
			}
			if(itemAdded==false){
				if(itemCount!=0){
					itemCount++;
					inventory.get(i).setName(itemCount + " * " + inventory.get(i).getBaseName());
				}

				returnItems.add(inventory.get(i));
			}
		}
		return returnItems;
	}

	public void addItem(Item newItem) {
		newItem.setName(newItem.getBaseName());
		int index = 0;
		for(Item oldItem:getInventoryRaw()){
			index++;
			if(oldItem.getBaseName().equals(newItem.getBaseName())){
				break;
			}
		}
		inventory.add(index, newItem);
	}

	public void removeItem(Item item){
		inventory.remove(item);

		if(equippedWeapon==item){
			unequipWeapon();
		}
		else if(equippedArmour==item){
			unequipArmour();
		}
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
		if(this.currentHealth<=0){
			this.currentHealth=0;
		}
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void attack(Actor target){
		if(target.getCurrentHealth()>0){
			int attack = getAttack();
			int damage = target.getDamage(attack);
			target.setTarget(this);
			Controller.OutputAttack(this, target, attack, damage);
		}
	}

	public int getDamage(int attack) {
		if(attack>0){
			if(attack<=getDefence()){
				setCurrentHealth(getCurrentHealth()-1);
				return 1;
			}
			else{
				attack = attack - getDefence();
				setCurrentHealth(getCurrentHealth()-attack);
				return attack;
			}
		}
		else{
			attack = Math.max(attack, getCurrentHealth()-getMaxHealth());
			//System.out.println(attack);
			setCurrentHealth(Math.min(getCurrentHealth()-attack,getMaxHealth()));
			return attack;
		}

	}

	public int getAttack(){
		int attack = 10;
		Item equippedWeapon = getEquippedWeapon();
		attack += getAttribute(AttributeName.STRENGTH)/3;

		if(equippedWeapon!=null){
			attack += getEquippedWeapon().getLevel()*2;
		}
		attack = new Random().nextInt((attack/2)+1) + (attack/2);
		return attack;
	}

	public int getDefence(){
		int defence = 0;
		if(getEquippedArmour()!=null){
			defence += getEquippedArmour().getLevel()*2;
		}
		return defence;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void moveActor(int newX, int newY){
		Controller.getTileMap().getTile(getX(), getY()).removeActor();
		Controller.getTileMap().getTile(newX, newY).addActor(this);
		updateVisibleTiles();
	}

	public Tile getTile(){
		return Controller.getTileMap().getTile(x,y);
	}

	abstract public void processAI();

	abstract public ArrayList<Actor> getValidEnemies();

	public int getRange() {
		return 1;
	}

	public Actor getTarget() {
		return target;
	}

	public void setTarget(Actor target) {
		this.target = target;
	}

	public int getAttribute(AttributeName name){
		int offset = 0;
		for(Attribute attribute:attributes){
			if(attribute.getAttributeName()==name){
				return attribute.getValue()+offset;
			}
		}
		return 0;
	}

	public void setAttribute(AttributeName name, int value){
		for(Attribute attribute:attributes){
			if(attribute.getAttributeName()==name){
				attribute.setValue(value);
				//Fortitude check and relevant health increase.
				setMaxHealth(((
						(Math.max(getAttribute(AttributeName.FORTITUDE)/2,1))
						+getLevel())
						*8)
						+getBaseHealth()
						);
				break;
			}
		}
	}

	public int getExpAmount(){
		int exp = 0;
		for(Attribute attribute:attributes){
			exp+=attribute.getValue();
		}
		exp*=getLevel();
		exp/=2;
		return exp;
	}
	
	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public Race getRace() {
		return race;
	}

	public ActorClass getActorClass() {
		return actorClass;
	}


	public Item getEquippedWeapon() {
		return equippedWeapon;
	}

	public Item getEquippedArmour() {
		return equippedArmour;
	}

	public boolean equipWeapon(Item weapon){
		if(equippedWeapon==weapon){
			unequipWeapon();
			return false;
		}
		else{
			equippedWeapon=weapon;
			if(equippedWeapon.getEnchantment()!=null){
				Enchantment enchantment = equippedWeapon.getEnchantment();
				AttributeName attributeName = enchantment.getAttribute();
				setAttribute(attributeName,getAttribute(attributeName)+enchantment.getAmount());
			}
			return true;
		}
	}

	private void unequipWeapon() {
		if(equippedWeapon.getEnchantment()!=null){
			Enchantment enchantment = equippedWeapon.getEnchantment();
			AttributeName attributeName = enchantment.getAttribute();
			setAttribute(attributeName,getAttribute(attributeName)-enchantment.getAmount());
		}
		equippedWeapon=null;
		
	}

	public boolean equipArmour(Item armour){
		if(equippedArmour==armour){
			unequipArmour();
			return false;
		}
		else{
			equippedArmour=armour;
			if(equippedArmour.getEnchantment()!=null){
				Enchantment enchantment = equippedArmour.getEnchantment();
				AttributeName attributeName = enchantment.getAttribute();
				setAttribute(attributeName,getAttribute(attributeName)+enchantment.getAmount());
			}
			return true;
		}
	}

	private void unequipArmour() {
		if(equippedArmour.getEnchantment()!=null){
			Enchantment enchantment = equippedArmour.getEnchantment();
			AttributeName attributeName = enchantment.getAttribute();
			setAttribute(attributeName,getAttribute(attributeName)-enchantment.getAmount());
		}
		equippedArmour=null;
		
	}

	public boolean getTurn(){
		TurnCounter -= getAttribute(AttributeName.AGILITY);
		if(TurnCounter<=0){
			TurnCounter = TurnCounterMax+TurnCounter;
			checkStatusEffects();
			return true;
		}
		else{
			return false;
		}
	}

	private void checkStatusEffects(){
		ArrayList<StatusEffect> removeList = new ArrayList<StatusEffect>();
		for(StatusEffect statusEffect:statusEffects){
			if(statusEffect.isActive()==false){
				removeList.add(statusEffect);
			}
		}
		for(StatusEffect statusEffect:removeList){
			removeStatusEffect(statusEffect);
		}
	}

	public int getMin() {
		setInventoryMinMax();
		return inventoryMin;
	}

	public int getMax() {
		setInventoryMinMax();
		return inventoryMax;
	}

	private void setInventoryMinMax(){
		if(getInventory().contains(Controller.getSelectedObject())){
			if(Controller.getObjectChoiceIndex()<=inventoryMin){
				inventoryMin = Controller.getObjectChoiceIndex();
				inventoryMax = inventoryMin+itemLimit;
			}
			else if(Controller.getObjectChoiceIndex()>=inventoryMax){
				inventoryMax = Controller.getObjectChoiceIndex()+1;
				inventoryMin = inventoryMax-itemLimit;
			}
		}
	}

	public ArrayList<Item> getDisplayInventory() {
		ArrayList<Item> trimmedItemList = new ArrayList<Item>();
		ArrayList<Item> rawInventory = getInventory();
		int inventoryLength = rawInventory.size();

		setInventoryMinMax();

		for(int i = inventoryMin;i<Math.min(inventoryMax,inventoryLength);i++){
			trimmedItemList.add(rawInventory.get(i));
		}

		return trimmedItemList;
	}

	public void removeStatusEffect(StatusEffect statusEffect){
		statusEffects.remove(statusEffect);
		switch (statusEffect.getEffect()){
		case STRENGTH:
			setAttribute(AttributeName.STRENGTH,getAttribute(AttributeName.STRENGTH)- statusEffect.getAmount());
			break;
		case AGILITY:
			setAttribute(AttributeName.AGILITY,getAttribute(AttributeName.AGILITY)- statusEffect.getAmount());
			break;
		}
	}

	public void addStatusEffect(StatusEffect statusEffect){
		statusEffects.add(new StatusEffect(statusEffect));
		switch (statusEffect.getEffect()){
		case STRENGTH:
			setAttribute(AttributeName.STRENGTH,getAttribute(AttributeName.STRENGTH)+statusEffect.getAmount());
			break;
		case AGILITY:
			setAttribute(AttributeName.AGILITY,getAttribute(AttributeName.AGILITY)+statusEffect.getAmount());
			break;
		case SLOW:
			setAttribute(AttributeName.AGILITY, Math.max(getAttribute(AttributeName.AGILITY)-statusEffect.getAmount(),0));
		case WEAKEN:
			setAttribute(AttributeName.STRENGTH, Math.max(getAttribute(AttributeName.STRENGTH)-statusEffect.getAmount(),0));
		}
	}

	public ArrayList<StatusEffect> getStatusEffects() {
		return statusEffects;
	}
	
	public ArrayList<Tile> getVisibleTiles(){
		return visibleTiles;
	}
	
	public void updateVisibleTiles(){
		visibleTiles = ShadowCaster.getVisibleTiles(this);
	}
}

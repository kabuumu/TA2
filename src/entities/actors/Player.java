package entities.actors;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import main.Controller;
import entities.Spell;
import entities.StatusEffect;
import entities.StatusEffect.Effect;
import entities.items.Item;
import entities.quest.Quest;
import enums.ActorType;
import enums.AttributeName;
import factories.RaceFactory;

public class Player extends Actor {
	private int exp;
	private ArrayList<Quest> quests = new ArrayList<Quest>();

	private int expCap = startingExpCap;

	//Constants
	private static final int startingExpCap = 250;
	private static final float xpIncrement=1.5f; //Amount to increase experience cap by.
	public static final int startingLevel = 1; //For test purposes, in game should always be 1.

	private int maxMana;
	private int mana;
	
	private final ArrayList<Spell> spells = new ArrayList<Spell>();
	private int baseMana = 7;
	

	public Player(String name) {
		this(name, startingLevel, 0 , 0, 0, 0, startingExpCap);
	}

	public Player(String name, int level, int gold, int currentHealth, int maxHealth, int exp, int expCap){
		super(name, ActorType.PLAYER, level, RaceFactory.getPlayerRace(), RaceFactory.getPlayerClass());
		this.setGold(gold);
		this.setBaseHealth(-28);
		this.exp=exp;
		this.expCap=expCap;

		//Quick fix for health bug.
		this.setAttribute(AttributeName.FORTITUDE, getAttribute(AttributeName.FORTITUDE));
		setCurrentHealth(getMaxHealth());
		setMana(getMaxMana());
	}

	public boolean gainExp(int exp){
		this.exp += exp;
		if(this.exp>=expCap){ //If current exp is more than the experience cap.
			levelUp(); //Level up!
			return true; //Return true that the player has levelled up.
		}
		else return false; //Return false that the player has not levelled up.
	}

	private void levelUp(){
		exp=exp-expCap;//Set current XP to current XP minus expCap.
		setLevel(getLevel()+1); //Increase level by 1.
		expCap*=xpIncrement; //Increase experience cap for next level.

		/*if(exp>=expCap){//If EXP is still higher than the level cap.
			levelUp(); //Level up again.
		}*/
	}


	public boolean usePotion(Item potion){
		if(potion.getBaseName().equals("HEALTH POTION")){	
			if(getCurrentHealth()==getMaxHealth()){
				return false;
			}
			else {
				setCurrentHealth(getMaxHealth());
				return true;
			}
		}
		else if(potion.getBaseName().equals("STRONG POTION")){
			StatusEffect statusEffect = new StatusEffect(Effect.STRENGTH,5,20);
			addStatusEffect(statusEffect);
			return true;
		}
		else if(potion.getBaseName().equals("SWIFT POTION")){
			StatusEffect statusEffect = new StatusEffect(Effect.AGILITY,5,20);
			addStatusEffect(statusEffect);
			return true;
		}
		return false;
	}

	public int eatMeat(Item item) {
		if(getCurrentHealth()==getMaxHealth()){
			return 0;
		}
		else {
			int returnHealth;
			if(getMaxHealth()-getCurrentHealth()<(getMaxHealth()/5)){
				returnHealth=getMaxHealth()-getCurrentHealth();
				setCurrentHealth(getMaxHealth());
			}
			else {
				returnHealth = getMaxHealth()/5;
				setCurrentHealth(getCurrentHealth() + (getMaxHealth()/5));
			}
			return returnHealth;
		}
	}


	public int getExp() {
		return exp;
	}

	public int getExpCap() {
		return expCap;
	}

	public void addQuest(Quest quest) {
		quests.add(quest);
	}

	public void removeQuest(Quest quest){
		quests.remove(quest);
	}

	public ArrayList<Quest> getQuests(){
		return quests;
	}



	public ArrayList<Actor> getValidEnemies(){
		ArrayList<Actor> validEnemies = new ArrayList<Actor>();
		for(int x = getX()-getRange();x<=getX()+getRange();x++){
			for(int y = getY()-getRange();y<=getY()+getRange();y++){
				Actor actor = Controller.getTileMap().getTile(x,y).getActor();
				//if(actor!=null && actor.getType()==ActorType.ENEMY&&Point2D.distance(x, y, getX(), getY())<=getRange()){
				if(actor!=null &&actor.getType()==ActorType.ENEMY){
					validEnemies.add(actor);	
				}
				//}
			}
		}
		return validEnemies;
	}
	
	public ArrayList<Actor> getValidEnemies(int range){
		ArrayList<Actor> validEnemies = new ArrayList<Actor>();
		for(int x = getX()-range;x<=getX()+range;x++){
			for(int y = getY()-range;y<=getY()+range;y++){
				Actor actor = Controller.getTileMap().getTile(x,y).getActor();
				if(actor!=null && actor.getType()==ActorType.ENEMY
						&&
						Point2D.distance(x, y, getX(), getY())<=range
						&&
						getVisibleTiles().contains(actor.getTile()
						)){
					validEnemies.add(actor);	
				}
			}
		}
		return validEnemies;
	}

	public ArrayList<Actor> getValidNPCs(){
		ArrayList<Actor> validNPCs = new ArrayList<Actor>();
		for(int x = getX()-getRange();x<=getX()+getRange();x++){
			for(int y = getY()-getRange();y<=getY()+getRange();y++){
				Actor actor = Controller.getTileMap().getTile(x,y).getActor();
				//if(actor!=null && actor.getType()==ActorType.NPC&&Point2D.distance(x, y, getX(), getY())<=getRange()){
				if(actor!=null &&actor.getType()==ActorType.NPC){
					validNPCs.add(actor);
				}
				//}

			}
		}
		return validNPCs;
	}



	@Override
	public void processAI() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setAttribute(AttributeName name, int value) {
		super.setAttribute(name, value);
		setMaxMana(((
				(getAttribute(AttributeName.WILLPOWER)/3)
				+getLevel())*3
				+baseMana
		)
		);
		if(name == AttributeName.PERCEPTION){
			updateVisibleTiles();
		}
	}

	public void setMaxMana(int maxMana){
		this.maxMana = maxMana;
	}

	public boolean castSpell(Spell spell, Actor target, boolean isScroll){
		if(mana>=spell.getManaCost() || isScroll){
			if(isScroll==false){
				mana-=spell.getManaCost();
			}
			if(spell.getStatusEffect()!=null){
				target.addStatusEffect(spell.getStatusEffect());
			}
			int damage = 0;
			if(spell.getDamage()!=0){
				damage = (spell.getDamage()*2) * (getAttribute(AttributeName.WILLPOWER)/5);
				if(damage>0){
					damage = (damage/2) + new Random().nextInt(damage/2+1);
				}
				damage = target.getDamage(damage);
			}
			Controller.outputSpell(spell, target, damage);
			target.setTarget(this);
			return true;
		}
		else{
			return false;
		}
	}
	
	public void addSpell(Spell spell){
		spells.add(spell);
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public ArrayList<Spell> getSpells() {
		return spells;
	}
	
	public ArrayList<Spell> getAvailableSpells() {
		ArrayList<Spell> spells = new ArrayList<Spell>();
		for(Spell spell:getSpells()){
			if(spell.getManaCost()<=getMana()){
				spells.add(spell);
			}
		}
		return spells;
	}

}
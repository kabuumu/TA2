package org.kabuumu.factories;

import java.util.Random;

import org.kabuumu.entities.Spell;
import org.kabuumu.entities.actors.Actor;
import org.kabuumu.entities.actors.Race;
import org.kabuumu.entities.items.Enchantment;
import org.kabuumu.entities.items.Item;
import org.kabuumu.entities.items.MagicItem;
import org.kabuumu.enums.*;

public class ItemFactory {

	public static Item getItem(Actor actor) {
		ItemType type;

		if(actor.getRace().getRaceType()==RaceType.BEAST){
			type=getAnimalItemType();
		}
		else {
			type = getItemType();
		}
		
		if(type==ItemType.WEAPON){
			return getWeapon(null,actor.getLevel());
		}
		else if (type==ItemType.ARMOUR){
			return getArmour(null,actor.getLevel());
		}

		int level = getItemLevel(actor.getLevel(), type);

		int value = getItemValue(level, type, null);

		if(type == ItemType.TOME||
				type==ItemType.SCROLL){
			Spell spell = SpellFactory.GetSpell(level);
			String name = getItemName(actor, type, level, spell);
			return new MagicItem(name, type, level, value, spell);
		}
		else{
			String name = getItemName(actor, type, level);
			return new Item(name, type, level , value);
		}
	}

	private static String getItemName(Actor actor, ItemType type, int level,
			Spell spell) {
		if(type == ItemType.SCROLL){
			return spell.getName() + " SCROLL";
		}
		else if(type ==ItemType.TOME){
			return spell.getName() + " TOME";
		}
		return null;
	}

	private static ItemType getAnimalItemType(){
		ItemType type;

		if(new Random().nextInt(2)==0){
			type = ItemType.FOOD;
		}
		else type = ItemType.MISC;

		return type;
	}

	private static int getItemValue(int level, ItemType type, Enchantment enchantment) {
		if(type==ItemType.POTION){
			return 50;
		}
		else if(type==ItemType.FOOD){
			return 5;
		}
		else if(type==ItemType.MISC){
			return new Random().nextInt(16)+10;
		}
		else if(type==ItemType.SCROLL){
			int randomAdd = new Random().nextInt(level)+1;
			int goldValue = (level*level*2)+randomAdd;
			goldValue += 8;
			goldValue*=2;
			return goldValue;
		}
		else if(type==ItemType.TOME){
			int randomAdd = new Random().nextInt(level)+1;
			int goldValue = (level*level*2)+randomAdd;
			goldValue += 8;
			goldValue*=5;
			return goldValue;
		}
		else{
			int randomAdd = new Random().nextInt(level)+1;
			int goldValue = (level*level*2)+randomAdd;
			if(enchantment!=null){
				goldValue+=enchantment.getAmount()*3;
			}
			goldValue += 8;
			goldValue*=3;
			return goldValue;
		}
	}

	private static int getItemLevel(int level, ItemType type) {
		if(type == ItemType.POTION || type == ItemType.FOOD || type == ItemType.MISC){
			return 0;
		}
		else{
			int random1 = new Random().nextInt(level+(level/4)+1)+1;
			//int random2 = new Random().nextInt(level);
			return random1; //+ random2;
		}
	}

	private static String getItemName(Actor actor, ItemType type, int level) {
		if(type == ItemType.POTION){
			switch(new Random().nextInt(3)){
			case 0:
				return "HEALTH POTION";
			case 1:
				return "STRONG POTION";
			case 2:
				return "SWIFT POTION";
			default:
				return null;
			}
		}
		else if(type == ItemType.FOOD){
			return actor.getRace().getName() + " MEAT";
		}
		else if (type == ItemType.MISC){
			if(actor.getRace().getRaceType()==RaceType.BEAST&&new Random().nextInt(2)==0){
				return actor.getRace().getName() + " PELT";
			}
			else{
				return actor.getRace().getName() + " BONE";
			}
		}
		else if(type==ItemType.WEAPON){
			int random = new Random().nextInt(14);
			String name = getMaterial(level/3) + " " + getMeleeWeaponName(random);
			return name;
		}
		else if(type==ItemType.ARMOUR){
			int random = new Random().nextInt(12);
			String name = getMaterial(level/3) + " " + getArmourName(random);
			return name;
		}
		else return null;
	}

	private static String getMaterial(int random){
		switch (random){
		case 0:
			return"WOOD";
		case 1:
			return"BRONZE";
		case 2:
			return"COPPER";
		case 3:
			return"BRASS";
		case 4:
			return"IRON";
		case 5:
			return"STEEL";
		case 6:
			return"SILVER";
		case 7:
			return"GOLD";
		case 8:
			return"DIAMOND";
		case 9:
			return"OBSIDIAN";
		case 10:
			return"ADAMANTIUM";
		default:
			return null;
		}
	}

	private static String getMeleeWeaponName(int random){
		switch (random){
		case 0:
			return "KNIFE";
		case 1:
			return "DAGGER";
		case 2:
			return "SHORTSWORD";
		case 3:
			return "LONGSWORD";
		case 4:
			return "RAPIER";
		case 5:
			return "SCIMITAR";
		case 6:
			return "HAMMER";
		case 7:
			return "MACE";
		case 8:
			return "FLAIL";
		case 9:
			return "SPEAR";
		case 10:
			return "PIKE";
		case 11:
			return "AXE";
		case 12:
			return "HATCHET";
		case 13:
			return "KNUCKLES";
		default:
			return null;
		}
	}

	private static String getArmourName(int random){
		switch (random){
		case 0:
			return "PAULDRON";
		case 1:
			return "HELMET";
		case 2:
			return "BUCKLER";
		case 3:
			return "SHIELD";
		case 4:
			return "BRACER";
		case 5:
			return "GAUNTLET";
		case 6:
			return "CUIRASS";
		case 7:
			return "COIF";
		case 8:
			return "GORGET";
		case 9:
			return "HAUBERK";
		case 10:
			return "VAMBRACE";
		case 11:
			return "GREAVES";
		default:
			return null;
		}
	}

	private static ItemType getItemType() {
		int random = new Random().nextInt(5)+1;
		switch(random){
		case 1:
			return ItemType.ARMOUR;
		case 2:
			return ItemType.WEAPON;
		case 3:
			return ItemType.POTION;
		case 4:
			return ItemType.SCROLL;
		case 5:
			return ItemType.TOME;
		default:
			return null;
		}
	}

	private static Item getBoneItem(Race race){
		String name = race.getName() + " BONE";
		ItemType type = ItemType.MISC;
		int level = 0;
		int value = 8;

		Item item = new Item(name, type, level, value);

		return item;
	}

	private static Item getSkullItem(Race race){
		String name = race.getName() + " SKULL";
		ItemType type = ItemType.MISC;
		int level = 0;
		int value = 10;

		Item item = new Item(name, type, level, value);

		return item;
	}

	private static Item getPeltItem(Race race){
		String name = race.getName() + " PELT";
		ItemType type = ItemType.MISC;
		int level = 0;
		int value = 20;

		Item item = new Item(name, type, level, value);

		return item;
	}

	private static Item getMeatItem(Race race){
		String name = race.getName() + " MEAT";
		ItemType type = ItemType.FOOD;
		int level = 0;
		int value = 5;

		Item item = new Item(name, type, level, value);

		return item;
	}

	public static void getStartingItems(Actor actor){
		Race race = actor.getRace();

		actor.addItem(getSkullItem(race));
		actor.addItem(getBoneItem(race));
		actor.addItem(getBoneItem(race));

		if(race.getRaceType()==RaceType.BEAST){
			actor.addItem(getMeatItem(race));
			actor.addItem(getPeltItem(race));
		}

		ActorClassType actorClassType = actor.getActorClass().getType();

		if(actorClassType == ActorClassType.FIGHTER||
				actorClassType == ActorClassType.SCOUT){

			Item weapon = getWeapon(actorClassType,actor.getLevel());
			actor.addItem(weapon);
			actor.equipWeapon(weapon);

			Item armour = getArmour(actorClassType,actor.getLevel());
			actor.addItem(armour);
			actor.equipArmour(armour);
		}


		;
	}

	private static Item getArmour(ActorClassType actorClassType, int actorLevel) {
		ItemType type = ItemType.ARMOUR;


		int level = getItemLevel(actorLevel, type);
		int random = new Random().nextInt(12);
		String name = getMaterial(level/3) + " " + getArmourName(random);

		Enchantment enchantment = getEnchantment(level);
		int value = getItemValue(level, type, enchantment);
		
		Item armour = new Item(name,type,level,value, enchantment);

		return armour;
	}

	private static Item getWeapon(ActorClassType actorClassType, int actorLevel){
		ItemType type = ItemType.WEAPON; 
		String name = null;
		int level = getItemLevel(actorLevel, type);


		int random = new Random().nextInt(14);
		name = getMaterial(level/3) + " " + getMeleeWeaponName(random);

		Enchantment enchantment = getEnchantment(level);
		int value = getItemValue(level,type, enchantment);
		
		Item weapon = new Item(name,type,level,value, enchantment);
		return weapon;
	}

	private static Enchantment getEnchantment(int level) {
		int random = new Random().nextInt(level*3);
		if(random>10){
			int enchantmentLevel = random;
			random = new Random().nextInt(AttributeName.values().length);
			return new Enchantment(AttributeName.values()[random], enchantmentLevel);
		}
		return null;
	}

	public static Item getQuestWeapon(){
		String name = "EXCALIBUR";
		ItemType type = ItemType.WEAPON;
		int level = 99;
		int value = 9999;		

		Item weapon = new Item(name, type, level, value);

		return weapon;
	}
}

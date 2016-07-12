package org.kabuumu.factories;

import org.kabuumu.interfaces.HasInventory;
import org.kabuumu.interfaces.HasName;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import org.kabuumu.main.Controller;
import org.kabuumu.entities.Spell;
import org.kabuumu.entities.StatusEffect;
import org.kabuumu.entities.actors.Actor;
import org.kabuumu.entities.actors.Attribute;
import org.kabuumu.entities.actors.Player;
import org.kabuumu.entities.actors.Race;
import org.kabuumu.entities.items.Item;
import org.kabuumu.entities.items.MagicItem;
import org.kabuumu.entities.map.Dungeon;
import org.kabuumu.entities.quest.ClearQuest;
import org.kabuumu.entities.quest.CollectQuest;
import org.kabuumu.entities.quest.CullQuest;
import org.kabuumu.entities.quest.KillQuest;
import org.kabuumu.entities.quest.Quest;
import org.kabuumu.enums.*;

public class TextFactory {
	public static String getAvailableCommandText(GameState gameState){
		String commands = "";

		commands += ("<body bgcolor=\"000000\"");
		commands += ("<FONT COLOR=\"FFFFFF\">");

		switch (gameState){
		case FIELD:
			commands += "[C]AST";
			if(Controller.getPlayer().getInventory().size()>0){
				commands += ", [U]SE, [D]ROP";
			}
			if(Controller.getPlayerTile().getInventory().size()>0){
				commands += ", [G]ET";
			}
			if(Controller.getPlayer().getValidEnemies().size()>0){
				commands+= ", [A]TTACK";
			}
			if(Controller.getPlayer().getValidNPCs().size()>0){
				commands+= ", [T]ALK";
			}
			break;
		case MERCHANT:
			commands += "[esc]CANCEL, [B]UY";
			if(Controller.getPlayer().getInventory().size()>0){
				commands += ", [S]ELL, [U]SE";
			}
			break;
		case BUY:
		case SELL:
		case ATTACK:
		case CAST:
		case CAST_TARGET:
		case GET:
		case DROP:
		case USE:
		case TALK:
			commands += "[esc]CANCEL, [enter]SELECT";
			break;
		default:
			break;
		}
		commands += "</FONT>";

		return commands;
	}

	public static String getInventoryHeader(HasInventory target){
		String inventoryText = "";
		inventoryText+=("<pre>");

		inventoryText+=(target.getName()+"'s INVENTORY");
		if(target==Controller.getPlayer()){
			inventoryText+="<br>";
			inventoryText+="GOLD: " + Controller.getPlayer().getGold();
		}
		inventoryText+="<br>";
		inventoryText+=("|NAME                     |TYPE      |LVL  |VAL  |");//Output table headings.
		inventoryText+="<br>";
		inventoryText+=("|-------------------------|----------|-----|-----|");

		//inventoryText+=("</pre>");
		return inventoryText;
	}

	public static String getInventoryText(HasInventory target){
		StringBuilder inventoryText = new StringBuilder();
		//inventoryText+=("<pre>");
		inventoryText.append(getInventoryHeader(target));

		ArrayList<Item> displayInventory = target.getDisplayInventory();

		if(target.getMin()>0){
			inventoryText.append("<br>");
			inventoryText.append("                    ▲▲▲▲▲                  ");
		}

		for(Item item:displayInventory){
			inventoryText.append("<br>");
			if(item==Controller.getSelectedObject()){ //if the item is the selected item in the controller.
				inventoryText.append("<FONT COLOR=\"#00FF00\">"); // Set it to green
			}
			else if(item.getEnchantment()!=null){
				inventoryText.append("<FONT COLOR=\"#00FFFF\">"); // Set it to turqoise
			}
			inventoryText.append(("|")); //Output delimiter.
			//Printing name
			String name = item.getName()+"                    ";//Set string variable as name with whitespace.
			inventoryText.append((name.substring(0,25))); //Cut size down to 20 characters and output.
			inventoryText.append(("|")); //Output delimiter.
			//Printing type
			String type = item.getType()+"          ";//Set string variable as type with whitespace.
			inventoryText.append((type.substring(0,9))); //Cut size down to 9 characters and output.

			if(target==Controller.getPlayer()&&(
					item==Controller.getPlayer().getEquippedArmour()||
					item==Controller.getPlayer().getEquippedWeapon()
			)){
				inventoryText.append("E");
			}
			else{
				inventoryText.append(" ");
			}
			inventoryText.append(("|")); //Output delimiter.
			//Printing level
			String level;
			if(item.getLevel()==0){
				level = "N/A  ";//Set string variable as N/A with whitespace, for potions.
			}
			else{
				level = item.getLevel()+"     ";//Set string variable as level with whitespace.
			}
			inventoryText.append((level.substring(0,5))); //Cut size down to 5 characters and output.
			inventoryText.append(("|")); //Output delimiter.
			//Printing value
			String value = item.getValue()+"     ";//Set string variable as value with whitespace.
			inventoryText.append((value.substring(0,5))); //Cut size down to 5 characters and output.
			inventoryText.append(("|")); //Output delimiter.
			//inventoryText+="<br>";
			if(item==Controller.getSelectedObject()||item.getEnchantment()!=null){
				inventoryText.append("</FONT>");
			}
		}

		if(target.getMax()<target.getInventory().size()){
			inventoryText.append("<br>");
			inventoryText.append("                 ▼▼▼▼▼                ");
		}

		inventoryText.append(("</pre>"));
		return inventoryText.toString();
	}

	public static String getTownName() {
		String name = "";
		int random = new Random().nextInt(10);
		switch(random){
		case 0:
			name += "Green";
			break;
		case 1:
			name += "Wolver";
			break;
		case 2:
			name += "Iron";
			break;
		case 3:
			name += "Tel";
			break;
		case 4:
			name += "Shif";
			break;
		case 5:
			name += "Cos";
			break;
		case 6:
			name += "Bil";
			break;
		case 7:
			name += "Old";
			break;
		case 8:
			name += "Coal";
			break;
		case 9:
			name += "Black";
			break;
		}

		random = new Random().nextInt(10);
		switch(random){
		case 0:
			name += "ton";
			break;
		case 1:
			name += "ford";
			break;
		case 2:
			name += "bridge";
			break;
		case 3:
			name += "brook";
			break;
		case 4:
			name += "nal";
			break;
		case 5:
			name += "ham";
			break;
		case 6:
			name += "don";
			break;
		case 7:
			name += "bury";
			break;
		case 8:
			name += "ley";
			break;
		case 9:
			name += "worth";
			break;
		}
		return name;
	}

	public static String getDungeonName() {
		String name = "";
		int random = new Random().nextInt(10);
		switch(random){
		case 0:
			name += "Khaz";
			break;
		case 1:
			name += "Dur";
			break;
		case 2:
			name += "Bal";
			break;
		case 3:
			name += "Grash";
			break;
		case 4:
			name += "Az";
			break;
		case 5:
			name += "Gah";
			break;
		case 6:
			name += "Krag";
			break;
		case 7:
			name += "Khum";
			break;
		case 8:
			name += "Rho";
			break;
		case 9:
			name += "Mal";
			break;
		}

		random = new Random().nextInt(10);
		switch(random){
		case 0:
			name += "al";
			break;
		case 1:
			name += "a";
			break;
		case 2:
			name += "oth";
			break;
		case 3:
			name += "ith";
			break;
		case 4:
			name += "ad";
			break;
		case 5:
			name += "goth";
			break;
		case 6:
			name += "zul";
			break;
		case 7:
			name += "ss";
			break;
		case 8:
			name += "iz";
			break;
		case 9:
			name += "";
			break;
		}
		random = new Random().nextInt(10);

		switch(random){
		case 0:
			name += "'Dum";
			break;
		case 1:
			name += "az";
			break;
		case 2:
			name += "al";
			break;
		case 3:
			name += "'Zul";
			break;
		case 4:
			name += "'Mal";
			break;
		case 5:
			name += "'Zho";
			break;
		case 6:
			name += "'Rath";
			break;
		case 7:
			name += "ur";
			break;
		case 8:
			name += "il";
			break;
		case 9:
			name += "";
			break;
		}
		return name;
	}

	public static String getAvailableObjects(){
		StringBuilder objectList = new StringBuilder();

		//			"<FONT COLOR=\""+fontColour+"\">"+sprite+"</FONT>"
		for(HasName hasName:Controller.getObjectChoice()){
			Actor actor = (Actor)hasName;
			objectList.append("<FONT COLOR=\"");

			if(hasName==Controller.getSelectedObject()){
				objectList.append("#00FF00"); //Setting the font colour to white if selected.
			}
			else{
				objectList.append("#FFFFFF"); //Setting the font colour to grey otherwise.
			}

			objectList.append("\">");

			objectList.append(actor.getName());
			if(actor.getType()==ActorType.ENEMY){
				objectList.append(" " + actor.getCurrentHealth() + "/" + actor.getMaxHealth());
				/*
				objectList.append(" " + actor.getAttribute(AttributeName.STRENGTH));
				objectList.append(" " + actor.getAttribute(AttributeName.AGILITY));
				objectList.append(" " + actor.getAttribute(AttributeName.FORTITUDE));
				objectList.append(" " + actor.getAttribute(AttributeName.PERCEPTION));
				objectList.append(" " + actor.getAttribute(AttributeName.WILLPOWER));
				 */
			}

			objectList.append("</FONT>");
			objectList.append("<br>");
		}

		return objectList.toString();
	}

	public static String getAvailableSpells(){
		StringBuilder spellList = new StringBuilder();

		spellList.append(getSpellHeader());

		ArrayList<Spell> availableSpells = Controller.getPlayer().getAvailableSpells();

		//			"<FONT COLOR=\""+fontColour+"\">"+sprite+"</FONT>"
		for(Spell spell:Controller.getPlayer().getSpells()){
			spellList.append(getSpellText(spell, availableSpells));	
		}

		return spellList.toString();
	}
	private static String getSpellHeader(){
		StringBuilder spellHeader = new StringBuilder();

		spellHeader.append("<pre>");
		spellHeader.append("<br>");
		spellHeader.append("|NAME           |MANA|TYPE |DMG|RNG|EFFECT       |");//Output table headings.
		spellHeader.append("<br>");
		spellHeader.append("|---------------|----|-----|---|---|-------------|");
		spellHeader.append("<br>");

		return spellHeader.toString();
	}

	private static String getSpellText(Spell spell, ArrayList<Spell> availableSpells){
		StringBuilder spellText = new StringBuilder();

		spellText.append("<FONT COLOR=\"");
		if(spell==Controller.getSelectedObject()){
			spellText.append("#00FF00"); //Setting the font colour to green if selected.
		}
		else{
			if(availableSpells==null||
					availableSpells.contains(spell)){
				spellText.append("#FFFFFF"); //Setting the font colour to white otherwise.
			}
			else{
				spellText.append("#708090"); //Setting the font colour to grey if mana is too low.
			}
		}

		spellText.append("\">");

		//Printing spell name
		spellText.append("|");
		String spellName = spell.getName();
		spellName += "               ";
		spellText.append(spellName.substring(0, 15));
		spellText.append("|");

		//Printing spell mana cost
		if(spell.getManaCost()>Controller.getPlayer().getMana()
				&&
				availableSpells!=null){
			spellText.append("<FONT COLOR=\"");
			spellText.append("#FF0000");
			spellText.append("\">");
		}

		String manaCost = ""+spell.getManaCost();
		manaCost += "    ";
		spellText.append(manaCost.substring(0, 4));
		if(spell.getManaCost()>Controller.getPlayer().getMana()
				&&
				availableSpells!=null){
			spellText.append("</FONT>");
		}
		spellText.append("|");


		//Printing spell damage type
		String damageType;
		if(spell.getDamage()==0){
			damageType = "N/A";
		}
		else{
			damageType = ""+spell.getDamageType();
		}
		damageType += "    ";
		spellText.append(damageType.substring(0, 5));
		spellText.append("|");

		//Printing spell damage
		String damage;
		if(spell.getDamage()==0){
			damage = "N/A";
		}
		else{
			damage = ""+spell.getDamage();
		}
		damage += "   ";
		spellText.append(damage.substring(0, 3));
		spellText.append("|");

		//Printing spell range
		String range = ""+spell.getRange();
		range += "   ";
		spellText.append(range.substring(0, 3));
		spellText.append("|");

		//Printing spell effect
		String effect = "";
		if(spell.getStatusEffect()!=null){
			effect += ""+spell.getStatusEffect().getEffect();
			effect += " " + spell.getStatusEffect().getAmount();
			effect += "(" + spell.getStatusEffect().getTime() + ")";
		}
		effect += "               ";
		spellText.append(effect.substring(0, 13));
		spellText.append("|");

		spellText.append("</FONT>");
		spellText.append("<br>");

		return spellText.toString();
	}

	public static String getAvailableEnemies(ArrayList<Actor> enemies){
		String enemyList = "";

		for(Actor enemy:enemies){ //For each enemy in the provided list
			enemyList += enemy.getName() + ", "; //Add its name to the return variable, followed by a space
		}

		return enemyList; //Return this list of enemies
	}

	public static String getAvailableItems(HasInventory target){
		String itemList = "";

		for(Item item:target.getInventory()){ //For each item in the provided list
			itemList += item.getName() + ", "; //Add its name to the return variable, followed by a space
		}

		return itemList; //Return this list of items
	}

	public static String getCharacterInfo(Actor actor){
		StringBuilder characterText = new StringBuilder();
		characterText.append("<pre>");
		characterText.append( "NAME:       "+actor.getName() + "<br>");
		characterText.append( "LEVEL:      "+actor.getLevel() + "<br>");
		if(actor.getType()==ActorType.PLAYER){
			Player player = (Player)actor;
			characterText.append("EXP:        "+ player.getExp() + "/" + player.getExpCap() + "<br>");
		}
		characterText.append("HEALTH:     ");
		characterText.append("<FONT COLOR=\"");
		if(actor.getCurrentHealth()<actor.getMaxHealth()/3){
			characterText.append("#FF0000");
		}
		else if(actor.getCurrentHealth()<actor.getMaxHealth()/2){
			characterText.append("#FFA500");
		}
		else if(actor.getCurrentHealth()<actor.getMaxHealth()){
			characterText.append("#FFFF00");
		}
		else{
			characterText .append ("#00FF00");
		}
		characterText .append( "\">");
		characterText.append(actor.getCurrentHealth());
		characterText.append("/" + actor.getMaxHealth() + "<br>");
		characterText.append( "</FONT>");

		if(actor.getType()==ActorType.PLAYER){
			Player player = (Player)actor;
			characterText.append("MANA:       ");
			characterText.append("<FONT COLOR=\"#9932CC\">");
			characterText.append(player.getMana() + "/" + player.getMaxMana() + "<br>");
			characterText.append( "</FONT>");
		}

		characterText.append("<br>");


		//Displaying attributes
		for(Attribute attribute:actor.getAttributes()){
			String attributeName = attribute.getName()+"      "; //Add the attribute name and enough space to make it in line.
			if(attribute==Controller.getSelectedObject()){
				characterText.append("<FONT COLOR=\"#00FF00\">");
			}
			characterText.append(attributeName.substring(0,12));
			characterText.append(attribute.getValue()); //Add the attribute value.
			if(attribute==Controller.getSelectedObject()){
				characterText.append( "</FONT>");
			}
			characterText.append("<br>");
		}
		characterText.append("</pre>");
		characterText.append("<br>");

		if(actor.getStatusEffects().size()>0){
			characterText.append("<FONT COLOR=\"#00FFFF\">");
			for(StatusEffect statusEffect:actor.getStatusEffects()){
				characterText.append(statusEffect.getEffect() + " + " + statusEffect.getAmount() + "(" + statusEffect.getTime() + "), ");

			}
			characterText.append( "</FONT>");
			characterText.append("<br>");
		}


		if(actor.getType()==ActorType.PLAYER){
			Player player = (Player)actor;
			characterText.append(getQuestInfo(player));
		}

		return characterText.toString();
	}

	private static String getQuestInfo(Player player) {
		StringBuilder questText = new StringBuilder();
		questText.append("QUESTS <br>");
		for(Quest quest:player.getQuests()){
			if(quest.getType()==QuestType.KILL){
				KillQuest killQuest = (KillQuest)quest;
				questText.append("KILL ");
				questText.append(killQuest.getTarget().getName());
				questText.append(" IN ");
				questText.append(killQuest.getDirection(player));
			}
			else if(quest.getType()==QuestType.COLLECT){
				CollectQuest collectQuest = (CollectQuest)quest;
				questText.append("COLLECT ");
				questText.append(collectQuest.getCurrent() + "/" + collectQuest.getNeeded() + " ");
				questText.append(collectQuest.getItemName());
				if(collectQuest.getNeeded()>1){
					questText.append("S");
				}
			}
			else if(quest.getType()==QuestType.CULL){
				CullQuest cullQuest = (CullQuest)quest;
				questText.append("KILL ");
				questText.append(cullQuest.getCurrent() + "/" + cullQuest.getNeeded() + " ");
				questText.append(cullQuest.getEnemyRace().getName());
				if(cullQuest.getNeeded()>1){
					questText.append("S");
				}
			}
			else if(quest.getType()==QuestType.CLEAR){
				ClearQuest clearQuest = (ClearQuest)quest;
				questText.append("CLEAR DUNGEON ");
				questText.append(clearQuest.getDungeon().getName().toUpperCase());
				if(clearQuest.getDungeon().getTileArea().contains(Controller.getPlayerTile())==false){
					questText.append(" IN ");
					questText.append(clearQuest.getDirection(player));
				}
			}
			questText.append("<BR>");
		}

		return questText.toString();
	}

	public static String getEnemySprite(Actor actor) {
		Race race = actor.getRace();
		return race.getName().substring(0,1);
	}

	public static Color getSpriteColour(Actor actor) {
		RaceType raceType = actor.getRace().getRaceType();
		switch (raceType){
		case BEAST:
			return new Color(170,80,0);
		case GOBLINOID:
			return new Color(0,224,102);
		case UNDEAD:
			return new Color(204,0,204);
		case NPC:
			return new Color(51,153,255);
		default:
			return Color.GRAY;
		}
	}

	public static String getItemDescription(){
		StringBuilder description = new StringBuilder();

		Item item = (Item)Controller.getSelectedObject();

		if(item.getEnchantment()!=null){
			description.append("ENCHANTED ");
		}
		description.append(item.getBaseName());
		if(item.getEnchantment()!=null){
			description.append(" (");
			description.append(item.getEnchantment().getAttribute());
			description.append(" ");
			description.append(item.getEnchantment().getAmount());
			description.append(")");
		}
		description.append("<br>");	
		if(item.getLevel()!=0){
			description.append("LEVEL " + item.getLevel());
		}
		description.append("<br>");			
		//Needs to be changed to add the item description.
		description.append(getItemDescription(item));
		description.append("<br>");
		if(item.getType()==ItemType.SCROLL||item.getType()==ItemType.TOME){
			if(item.getType()==ItemType.SCROLL){
				description.append("When used, casts the following spell:");
			}
			if(item.getType()==ItemType.TOME){
				description.append("When used, teaches the following spell:");
			}
			description.append(getSpellHeader());
			final MagicItem magicItem = (MagicItem)item;
			description.append(getSpellText(magicItem.getSpell(), null));
		}


		return description.toString();
	}

	public static String getItemDescription(Item item){
		StringBuilder description = new StringBuilder();

		switch(item.getType()){
		case WEAPON:
			description.append("A weapon, it is equipped when used, and allows the user to deal more damage. ");
			if(item.getEnchantment()!=null){
				description.append("This particular weapon is enchanted, and will increase " + item.getEnchantment().getAttribute().toString().toLowerCase() + " when equipped");
			}
			break;
		case ARMOUR:
			description.append("A piece of armour, it is equipped when used, and reduces the damage dealt to the wearer. ");
			if(item.getEnchantment()!=null){
				description.append("This particular armour is enchanted, and will increase " + item.getEnchantment().getAttribute().toString().toLowerCase() + " when equipped");
			}
			break;
		case FOOD:
			description.append("A chunk of meat from a dead animal, it restores a portion of health when used. ");
			break;
		case POTION:
			description.append("A magical potion, ");
			if(item.getBaseName().equals("HEALTH POTION")){
				description.append("it completely heals the user. ");
			}
			else if(item.getBaseName().equals("SWIFT POTION")){
				description.append("it makes the user faster and more agile. ");
			}
			else if(item.getName().equals("STRONG POTION")){
				description.append("it makes the user stronger and able to deal more damage. ");
			}
			break;
		case MISC:
			description.append("A miscellaneous item, it has no use, but can be sold to merchants. ");
			break;
		case SCROLL:
			description.append("A magical scroll used to cast magic without learning it. ");
			break;
		case TOME:
			description.append("A magical tome used to learn new magic. ");
			break;
		}

		return description.toString();
	}
}

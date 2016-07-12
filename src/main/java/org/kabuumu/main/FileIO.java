package org.kabuumu.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import org.kabuumu.entities.Spell;
import org.kabuumu.entities.StatusEffect;
import org.kabuumu.entities.Spell.SpellType;
import org.kabuumu.entities.StatusEffect.Effect;
import org.kabuumu.entities.actors.Attribute;
import org.kabuumu.entities.actors.Player;
import org.kabuumu.entities.items.Enchantment;
import org.kabuumu.entities.items.Item;
import org.kabuumu.entities.items.MagicItem;
import org.kabuumu.enums.AttributeName;
import org.kabuumu.enums.DamageType;
import org.kabuumu.enums.ItemType;

public class FileIO {
	private static DocumentBuilder docBuilder;
	private static Document doc;

	static{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		try {
			docBuilder = docFactory.newDocumentBuilder();

			//Root element
			doc = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static Player loadXML(){
		try{
			File fXmlFile = new File("player.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			Node nNode = doc.getFirstChild();

			Element playerElement = (Element)nNode;

			//String name, int level, int gold, int currentHealth, int maxHealth, int exp, int expCap

			String name = playerElement.getElementsByTagName("name").item(0).getTextContent();
			int level = Integer.parseInt(playerElement.getElementsByTagName("level").item(0).getTextContent());
			int gold = Integer.parseInt(playerElement.getElementsByTagName("gold").item(0).getTextContent());
			int currentHealth = Integer.parseInt(playerElement.getElementsByTagName("currentHealth").item(0).getTextContent());
			int maxHealth = Integer.parseInt(playerElement.getElementsByTagName("maxHealth").item(0).getTextContent());
			int exp = Integer.parseInt(playerElement.getElementsByTagName("exp").item(0).getTextContent());
			int expCap = Integer.parseInt(playerElement.getElementsByTagName("expCap").item(0).getTextContent());

			Player player = new Player(name,level,gold,currentHealth,maxHealth,exp,expCap);

			for(Attribute attr:player.getAttributes()){
				player.setAttribute(attr.getAttributeName(), Integer.parseInt(playerElement.getElementsByTagName(attr.getName()).item(0).getTextContent()));
			}

			NodeList itemList = playerElement.getElementsByTagName("item");

			for(int i=0;i<itemList.getLength();i++){
				Element itemElement = (Element)itemList.item(i);

				String itemName = (itemElement.getElementsByTagName("name").item(0).getTextContent());
				ItemType itemType = ItemType.valueOf((itemElement.getElementsByTagName("type").item(0).getTextContent()));
				int itemLevel = Integer.parseInt(itemElement.getElementsByTagName("level").item(0).getTextContent());
				int itemValue = Integer.parseInt(itemElement.getElementsByTagName("value").item(0).getTextContent());

				Enchantment itemEnchantment = null;

				//Loading item enchantment (if present)
				if(itemElement.getElementsByTagName("enchantment").getLength()>0){
					Element enchantmentElement = (Element)itemElement.getElementsByTagName("enchantment").item(0);

					AttributeName enchantmentAttribute = AttributeName.valueOf(enchantmentElement.getElementsByTagName("attribute").item(0).getTextContent());
					int enchantmentAmount = Integer.parseInt(enchantmentElement.getElementsByTagName("amount").item(0).getTextContent());
					itemEnchantment = new Enchantment(enchantmentAttribute,enchantmentAmount);
				}

				//Loading item spell (if present)
				if(itemType==ItemType.SCROLL||itemType==ItemType.TOME){

				}
				else{
					player.addItem(new Item(itemName,itemType,itemLevel,itemValue,itemEnchantment));
				}
			}
			//Equipping Weapon (if equipped)
			if(playerElement.getElementsByTagName("equippedWeapon").getLength()>0){
				int equippedWeaponIndex = Integer.parseInt(playerElement.getElementsByTagName("equippedWeapon").item(0).getTextContent());
				player.equipWeapon(player.getInventoryRaw().get(equippedWeaponIndex));
			}

			//Equipping Armour (if equipped)
			if(playerElement.getElementsByTagName("equippedArmour").getLength()>0){
				int equippedArmourIndex = Integer.parseInt(playerElement.getElementsByTagName("equippedArmour").item(0).getTextContent());
				player.equipArmour(player.getInventoryRaw().get(equippedArmourIndex));
			}

			NodeList spellList = playerElement.getElementsByTagName("spell");

			//Loading spells
			for(int i=0;i<spellList.getLength();i++){
				Element spellElement = (Element)spellList.item(i);

				if (!spellElement.getParentNode().getNodeName().equals("Player")) {
					continue;
				}

				String spellName = (spellElement.getElementsByTagName("name").item(0).getTextContent());
				DamageType damageType = DamageType.valueOf((spellElement.getElementsByTagName("damageType").item(0).getTextContent()));
				int damage = Integer.parseInt(spellElement.getElementsByTagName("damage").item(0).getTextContent());
				int manaCost = Integer.parseInt(spellElement.getElementsByTagName("manaCost").item(0).getTextContent());

				StatusEffect statusEffect = null;

				//Loading status effect (if present)
				if(spellElement.getElementsByTagName("statusEffect").getLength()>0){
					Element statusEffectElement = (Element)spellElement.getElementsByTagName("statusEffect").item(0);

					StatusEffect.Effect effect = StatusEffect.Effect.valueOf(statusEffectElement.getElementsByTagName("effect").item(0).getTextContent());
					int amount = Integer.parseInt(statusEffectElement.getElementsByTagName("amount").item(0).getTextContent());
					int time = Integer.parseInt(statusEffectElement.getElementsByTagName("time").item(0).getTextContent());
					statusEffect = new StatusEffect(effect,amount,time);
				}

				SpellType spellType = SpellType.valueOf(spellElement.getElementsByTagName("type").item(0).getTextContent());
				int range = Integer.parseInt(spellElement.getElementsByTagName("range").item(0).getTextContent());

				Spell spell = new Spell(spellName,damageType, damage, manaCost, statusEffect, spellType, range);
				player.addSpell(spell);
			}

			Controller.setPlayer(player);
			Controller.Log("Save file loaded.");
		}
		catch (FileNotFoundException e){
			Controller.Log("No save file found.");
		}
		catch(Exception e){
			e.printStackTrace();
			Controller.Log("An error occurred.");
		}
		return null;
	}

	public static boolean writeXML(){
		Player player = Controller.getPlayer();

		try{
			//Root Element
			Element rootElement = doc.createElement("Player");
			doc.appendChild(rootElement);

			// name element
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(player.getName()));
			rootElement.appendChild(name);

			// level element
			Element level = doc.createElement("level");
			level.appendChild(doc.createTextNode(""+player.getLevel()));
			rootElement.appendChild(level);

			// gold element
			Element gold = doc.createElement("gold");
			gold.appendChild(doc.createTextNode(""+player.getGold()));
			rootElement.appendChild(gold);

			// current health element
			Element currentHealth = doc.createElement("currentHealth");
			currentHealth.appendChild(doc.createTextNode(""+player.getCurrentHealth()));
			rootElement.appendChild(currentHealth);

			// current maxHealth element
			Element maxHealth = doc.createElement("maxHealth");
			maxHealth.appendChild(doc.createTextNode(""+player.getMaxHealth()));
			rootElement.appendChild(maxHealth);

			// current exp element
			Element exp = doc.createElement("exp");
			exp.appendChild(doc.createTextNode(""+player.getExp()));
			rootElement.appendChild(exp);

			// current expCap element
			Element expCap = doc.createElement("expCap");
			expCap.appendChild(doc.createTextNode(""+player.getExpCap()));
			rootElement.appendChild(expCap);

			//Attributes
			for(Attribute attr:player.getAttributes()){
				Element attribute = doc.createElement(attr.getName());
				int attrValue = attr.getValue();

				//Removing any enchanted weapon bonus.
				if(player.getEquippedWeapon()!=null 
						&& 
						player.getEquippedWeapon().getEnchantment()!=null 
						&& 
						player.getEquippedWeapon().getEnchantment().getAttribute()==attr.getAttributeName()){
					attrValue-=player.getEquippedWeapon().getEnchantment().getAmount();
				}

				//Removing any enchanted armour bonus.
				if(player.getEquippedArmour()!=null 
						&& 
						player.getEquippedArmour().getEnchantment()!=null 
						&& 
						player.getEquippedArmour().getEnchantment().getAttribute()==attr.getAttributeName()){
					attrValue-=player.getEquippedArmour().getEnchantment().getAmount();
				}

				//Removing any status effect bonus.
				for(StatusEffect status:player.getStatusEffects()){
					if(status.getEffect()==StatusEffect.Effect.STRENGTH && attr.getAttributeName()==AttributeName.STRENGTH){
						attrValue-=status.getAmount();
					}
					else if(status.getEffect()==StatusEffect.Effect.AGILITY && attr.getAttributeName()==AttributeName.AGILITY){
						attrValue-=status.getAmount();
					}
				}


				attribute.appendChild(doc.createTextNode(attrValue+""));
				rootElement.appendChild(attribute);
			}

			int equippedWeaponIndex = 2147483647;
			int equippedArmourIndex = 2147483647;

			int i=0;
			for(Item item:player.getInventoryRaw()){
				Element itemElement = doc.createElement("item");
				rootElement.appendChild(itemElement);
				itemElement.setAttribute("index", ""+i);

				if(player.getEquippedWeapon()==item){
					equippedWeaponIndex=i;
				}
				else if(player.getEquippedArmour()==item){
					equippedArmourIndex=i;
				}

				writeXML(itemElement, item);

				i++;
			}

			if(equippedWeaponIndex!=2147483647){
				// current expCap element
				Element equippedWeapon = doc.createElement("equippedWeapon");
				equippedWeapon.appendChild(doc.createTextNode(""+equippedWeaponIndex));
				rootElement.appendChild(equippedWeapon);
			}

			if(equippedArmourIndex!=2147483647){
				// current expCap element
				Element equippedWeapon = doc.createElement("equippedArmour");
				equippedWeapon.appendChild(doc.createTextNode(""+equippedArmourIndex));
				rootElement.appendChild(equippedWeapon);
			}

			i=0;
			for(Spell spell:player.getSpells()){
				Element spellElement = doc.createElement("spell");
				rootElement.appendChild(spellElement);
				spellElement.setAttribute("index", ""+i);

				writeXML(spellElement, spell);

				i++;
			}



			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("player.xml"));

			transformer.transform(source, result);

			return true;
		}
		catch (Exception e){
			return false;
		}
	}

	private static void writeXML(Element itemElement, Item item) {

		Element itemName = doc.createElement("name");
		itemName.appendChild(doc.createTextNode(""+item.getBaseName()));
		itemElement.appendChild(itemName);

		Element itemType = doc.createElement("type");
		itemType.appendChild(doc.createTextNode(""+item.getType()));
		itemElement.appendChild(itemType);

		Element itemLevel = doc.createElement("level");
		itemLevel.appendChild(doc.createTextNode(""+item.getLevel()));
		itemElement.appendChild(itemLevel);

		Element itemValue = doc.createElement("value");
		itemValue.appendChild(doc.createTextNode(""+item.getValue()));
		itemElement.appendChild(itemValue);

		if(item.getEnchantment()!=null){
			Enchantment enchantment = item.getEnchantment();
			Element itemEnchantment = doc.createElement("enchantment");
			itemElement.appendChild(itemEnchantment);

			Element enchantmentAttribute = doc.createElement("attribute");
			enchantmentAttribute.appendChild(doc.createTextNode(""+enchantment.getAttribute()));
			itemEnchantment.appendChild(enchantmentAttribute);

			Element enchantmentAmount = doc.createElement("amount");
			enchantmentAmount.appendChild(doc.createTextNode(""+enchantment.getAmount()));
			itemEnchantment.appendChild(enchantmentAmount);
		}

		if(item.getType()==ItemType.SCROLL||item.getType()==ItemType.TOME){
			MagicItem magicItem = (MagicItem)item;
			Spell spell = magicItem.getSpell();

			Element itemSpell = doc.createElement("spell");
			itemElement.appendChild(itemSpell);

			writeXML(itemSpell,spell);
		}	
	}

	private static void writeXML(Element spellElement, Spell spell) {

		Element spellName = doc.createElement("name");
		spellName.appendChild(doc.createTextNode(""+spell.getName()));
		spellElement.appendChild(spellName);

		Element spellDamageType = doc.createElement("damageType");
		spellDamageType.appendChild(doc.createTextNode(""+spell.getDamageType()));
		spellElement.appendChild(spellDamageType);

		Element spellDamage = doc.createElement("damage");
		spellDamage.appendChild(doc.createTextNode(""+spell.getDamage()));
		spellElement.appendChild(spellDamage);

		Element spellManaCost = doc.createElement("manaCost");
		spellManaCost.appendChild(doc.createTextNode(""+spell.getManaCost()));
		spellElement.appendChild(spellManaCost);

		if(spell.getStatusEffect()!=null){
			StatusEffect statusEffect = spell.getStatusEffect();

			Element spellStatusEffect = doc.createElement("statusEffect");
			spellElement.appendChild(spellStatusEffect);

			Element statusEffectType = doc.createElement("effect");
			statusEffectType.appendChild(doc.createTextNode(""+statusEffect.getEffect()));
			spellStatusEffect.appendChild(statusEffectType);

			Element statusEffectAmount = doc.createElement("amount");
			statusEffectAmount.appendChild(doc.createTextNode(""+statusEffect.getAmount()));
			spellStatusEffect.appendChild(statusEffectAmount);

			Element statusEffectTime = doc.createElement("time");
			statusEffectTime.appendChild(doc.createTextNode(""+statusEffect.getTime()));
			spellStatusEffect.appendChild(statusEffectTime);
		}

		Element spellType = doc.createElement("type");
		spellType.appendChild(doc.createTextNode(""+spell.getSpellType()));
		spellElement.appendChild(spellType);

		Element spellRange = doc.createElement("range");
		spellRange.appendChild(doc.createTextNode(""+spell.getRange()));
		spellElement.appendChild(spellRange);
	}
}

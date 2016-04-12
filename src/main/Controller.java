package main;

import interfaces.HasInventory;
import interfaces.HasName;

import java.util.ArrayList;
import java.util.Random;

import entities.Spell;
import entities.Spell.SpellType;
import entities.actors.Actor;
import entities.actors.Attribute;
import entities.actors.NPC;
import entities.actors.Player;
import entities.items.Item;
import entities.items.MagicItem;
import entities.map.Dungeon;
import entities.map.Tile;
import entities.map.TileMap;
import entities.map.Town;
import entities.quest.ClearQuest;
import entities.quest.CollectQuest;
import entities.quest.CullQuest;
import entities.quest.KillQuest;
import entities.quest.Quest;
import enums.*;
import factories.ActorFactory;
import factories.QuestFactory;
import factories.RaceFactory;
import factories.SpellFactory;
import factories.TextFactory;

public final class Controller {

	//Game Variables
	private static int size = 500;
	private final static String version = "v0.82";

	private static int trainMultiplier = 30;

	private static Item startingWeapon = new Item("TRAINING SPOON", ItemType.WEAPON, 1, 1);
	private static Item startingArmour = new Item("COTTON SHIRT", ItemType.ARMOUR, 1, 1);
	private static Item potion1 = new Item("HEALTH POTION", ItemType.POTION, 0,50);
	private static Item potion2 = new Item("HEALTH POTION", ItemType.POTION, 0,50);

	private static TileMap tileMap;
	private static GameState gameState = GameState.START;
	private static Player player;
	private static Tile lastTile;
	private static int questLimit = 4;

	private static Actor target = null;

	private static GameState lastGameState;

	private static Object selectedObject = null;
	private static ArrayList<? extends HasName> objectChoice = null;
	private static int objectChoiceIndex = 0;
	private static Spell selectedSpell;
	private static Item scrollInUse;

	public static Spell getSelectedSpell() {
		return selectedSpell;
	}

	private static final int proximity = 20;

	private static ArrayList<Attribute> attributes = new ArrayList<Attribute>();

	private Controller(){	
	}

	private Controller(int size){
		Controller.size = size;
	}

	public static void start(){
		setGameState(GameState.START);//Set the starting game state to START.

		setAttributes();

		tileMap = new TileMap(size);
		tileMap.initialise();
		RaceFactory.initialise();

		tileMap.getTile(size/2, size/2).removeActor();

		Log("Starting Game");
		preStart();
	}


	private static void setAttributes(){
		attributes.add(new Attribute(AttributeName.STRENGTH,0));
		attributes.add(new Attribute(AttributeName.AGILITY,0));
		attributes.add(new Attribute(AttributeName.FORTITUDE,0));
		attributes.add(new Attribute(AttributeName.PERCEPTION,0));
		attributes.add(new Attribute(AttributeName.WILLPOWER,0));
	}

	public static int getSize() {
		return size;
	}

	public static void setSize(int size) {
		Controller.size = size;
	}

	public static TileMap getTileMap() {
		return tileMap;
	}

	public static void setTileMap(TileMap tileMap) {
		Controller.tileMap = tileMap;
	}

	public static GameState getGameState() {
		return gameState;
	}

	public static void setGameState(GameState gameState) {
		Controller.lastGameState = getGameState();
		//System.out.println("Changing gamestate from " + getLastGameState() + " to " + gameState + ".");
		setSelectedObject(null);
		Controller.gameState = gameState;
	}

	public static Player getPlayer() {
		return player;
	}

	public static void setPlayer(Player player) {
		Controller.player = player;
	}

	public static void testProcess(String input){
		process(input);
	}

	private static void process(String input){
		switch(gameState){
		case START:
			startGame(input);
			break;
		case FIELD:
			processFieldCommand(input);
			break;
		case GET:
			processGetCommand(input);
			break;
		case USE:
			processUseCommand(input);
			break;
		case DROP:
			processDropCommand(input);
			break;
		case ATTACK:
			processAttackCommand(input);
			break;
		case MERCHANT:
			processMerchantCommand(input);
			break;
		case BUY:
			processBuyCommand(input);
			break;
		case SELL:
			processSellCommand(input);
			break;
		case TALK:
			processTalkCommand(input);
			break;
		case TRAINER:
			processTrainerCommand(input);
			break;
		case LEVEL:
			processLevelCommand(input);
			break;
		case CAST:
			processCastCommand(input);
			break;
		case CAST_TARGET:
			processCastTargetCommand(input);
			break;
		case DEATH:
			output("The game has ended.");
		}
	}

	private static void processCastCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){ //If the player selects escape
			setGameState(GameState.FIELD); //Set the gamestate to field
			setObjectChoice(null); //Set the list of objects to the tile inventory.
			setSelectedObject(null); //Set the selected object to null
			setObjectChoiceIndex(0); //Set the object choice index to 0
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){ //If the player selects up or down
			iterateSelectedObject(input); //Iterate with the corresponding key.
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){

			Spell spell = (Spell)getSelectedObject();

			if(spell!=null){
				if(spell.getSpellType()==SpellType.TARGET){
					if((getPlayer().getValidEnemies(spell.getRange()).size()>0)){
						setGameState(GameState.CAST_TARGET);
						setObjectChoice(getPlayer().getValidEnemies(spell.getRange())); //Set the list of objects to the available enemies in range.
						setSelectedObject(getObjectChoice().get(0));
						setObjectChoiceIndex(0);
						selectedSpell=spell;
					}
					else {
						output("There are no enemies within range of that spell.");
						outputLn();
					}
				}
				else{
					getPlayer().castSpell(spell, getPlayer(), false);
					setGameState(GameState.FIELD);
				}
			}
		}
	}

	private static void processCastTargetCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			if(getLastGameState()==GameState.CAST){
				setGameState(GameState.CAST);
				setObjectChoice(player.getAvailableSpells()); //Set the list of objects to the tile inventory.
				if(getObjectChoice().size()>0){
					setSelectedObject(getObjectChoice().get(0));
				}
				setObjectChoiceIndex(0);
				selectedSpell=null;
			}
			else if(getLastGameState()==GameState.FIELD){ //Actually was USE, but because of the way it works, it's changed to FIELD first.
				setGameState(GameState.USE);
				setObjectChoice(getPlayer().getInventory()); //Set the list of objects to the tile inventory.
				setSelectedObject(getObjectChoice().get(0));
				setObjectChoiceIndex(0);
				scrollInUse=null;
			}
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){
			iterateSelectedObject(input);
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){
			//System.out.println(getLastGameState());
			if(getLastGameState()==GameState.FIELD){ //Actually was USE, but because of the way it works, it's changed to FIELD first.
				player.removeItem(scrollInUse);
			}

			Actor castTarget = (Actor)getSelectedObject(); //Fixing a bug where the target is set to null by changing the gamestate.
			setGameState(GameState.FIELD);
			if(scrollInUse!=null){
				player.castSpell(selectedSpell, castTarget, true);
			}
			else{
				player.castSpell(selectedSpell, castTarget, false);
			}
			setObjectChoiceIndex(0);
		}
	}

	private static void processTrainerCommand(String input) {
		if(input.toUpperCase().matches("NORTH|SOUTH|EAST|WEST")){
			movePlayer(Direction.valueOf(input.toUpperCase()));
		}
		else if(input.toUpperCase().matches("TRAIN")){
			trainPlayer();
		}
		else{
			output("Command not recognised.");
		}	
	}

	private static void processMerchantCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			cancelCommand();
			target=null;
		}
		else if(input.toUpperCase().equals("USE")){
			setGameState(GameState.USE);
			setObjectChoice(getPlayer().getInventory()); //Set the list of objects to the tile inventory.
			setSelectedObject(getObjectChoice().get(0));
			setObjectChoiceIndex(0);;
		}
		else if(input.toUpperCase().equals("BUY")){
			setGameState(GameState.BUY);
			setObjectChoice(getTarget().getInventory()); //Set the list of objects to the target's inventory.
			setSelectedObject(getObjectChoice().get(0));
			setObjectChoiceIndex(0);;
		}
		else if(input.toUpperCase().equals("SELL")){
			setGameState(GameState.SELL);
			setObjectChoice(getPlayer().getInventory()); //Set the list of objects to the player's inventory.
			setSelectedObject(getObjectChoice().get(0));
			setObjectChoiceIndex(0);;
		}

	}

	private static void processUseCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			cancelCommand();
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){
			iterateSelectedObject(input);
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){
			useItem((Item)getSelectedObject());
		}
	}

	private static void processLevelCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			cancelCommand();
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){
			iterateSelectedObject(input);
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){
			double healthRatio = (double)getPlayer().getCurrentHealth()/(double)getPlayer().getMaxHealth();
			double manaRatio = (double)getPlayer().getMana()/(double)getPlayer().getMaxMana();
			increaseAttribute((Attribute)getSelectedObject());

			//Setting mana and health to new ratio on level up.
			getPlayer().setCurrentHealth((int)(getPlayer().getMaxHealth()*healthRatio));
			getPlayer().setMana((int)(getPlayer().getMaxMana()*manaRatio));
		}
	}

	private static void processTalkCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			cancelCommand();
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){
			iterateSelectedObject(input);
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){
			NPC npc = (NPC)getSelectedObject();
			target=npc;
			switch(npc.getNpcType()){
			case MERCHANT:
				startMerchant();
				break;
			case TRAINER:
				startTrainer();
				break;
			case QUEST:
				startQuest();
				break;
			}
		}
	}

	private static void processDropCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			cancelCommand();
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){
			iterateSelectedObject(input);
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){
			moveItem((Item)getSelectedObject(),player,getPlayerTile());
		}
	}

	private static void processAttackCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			cancelCommand();
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){
			iterateSelectedObject(input);
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){
			attackEnemy((Actor)getSelectedObject());
		}
	}

	private static void processGetCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			cancelCommand();
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){
			iterateSelectedObject(input);
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){
			moveItem((Item)getSelectedObject(),getPlayerTile(),player);
		}
	}

	private static void processBuyCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			cancelCommand();
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){
			iterateSelectedObject(input);
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){
			buyItem((Item)getSelectedObject());
		}
	}

	private static void processSellCommand(String input) {
		if(input.toUpperCase().equals("CANCEL")){
			cancelCommand();
		}
		else if(input.toUpperCase().equals("NORTH")||input.toUpperCase().equals("SOUTH")){
			iterateSelectedObject(input);
		}
		else if(input.toUpperCase().equals("EAST"));
		else if(input.toUpperCase().equals("WEST"));
		else if(input.toUpperCase().equals("ENTER")){
			sellItem((Item)getSelectedObject());
		}
	}

	private static void processFieldCommand(String input) {
		if(input.toUpperCase().matches("NORTH|SOUTH|EAST|WEST")){
			movePlayer(Direction.valueOf(input.toUpperCase()));
		}
		else if(input.toUpperCase().equals("USE")){
			setGameState(GameState.USE);
			setObjectChoice(getPlayer().getInventory()); //Set the list of objects to the tile inventory.
			setSelectedObject(getObjectChoice().get(0));
			setObjectChoiceIndex(0);
		}
		else if(input.toUpperCase().equals("DROP")){
			setGameState(GameState.DROP);
			setObjectChoice(getPlayer().getInventory()); //Set the list of objects to the tile inventory.
			setSelectedObject(getObjectChoice().get(0));
			setObjectChoiceIndex(0);
		}

		else if(input.toUpperCase().matches("GET")){
			if(getPlayerTile().getInventory().size()>0){
				setGameState(GameState.GET);
				setObjectChoice(getPlayerTile().getInventory()); //Set the list of objects to the tile inventory.
				setSelectedObject(getObjectChoice().get(0));
				setObjectChoiceIndex(0);
			}
		}

		else if(input.toUpperCase().matches("LOOK")){
			showInventory(getPlayerTile());
		}
		else if(input.toUpperCase().matches("SAVE")){
			saveGame();
		}
		else if(input.toUpperCase().equals("ATTACK")){
			if(player.getValidEnemies().size()>0){
				setGameState(GameState.ATTACK);
				setObjectChoice(player.getValidEnemies()); //Set the list of objects to the tile inventory.
				setSelectedObject(getObjectChoice().get(0));
				setObjectChoiceIndex(0);
			}
		}
		else if(input.toUpperCase().equals("TALK")){
			if(player.getValidNPCs().size()>0){
				setGameState(GameState.TALK);
				setObjectChoice(player.getValidNPCs()); //Set the list of objects to the tile inventory.
				setSelectedObject(getObjectChoice().get(0));
				setObjectChoiceIndex(0);
			}
		}

		else if(input.toUpperCase().equals("CAST")){
			if(player.getSpells().size()>0){
				setGameState(GameState.CAST);
				setObjectChoice(player.getAvailableSpells()); //Set the list of objects to the tile inventory.
				if(getObjectChoice().size()>0){
					setSelectedObject(getObjectChoice().get(0));
				}
				setObjectChoiceIndex(0);
			}
		}
	}

	private static void cancelCommand(){
		if(getGameState()==GameState.MERCHANT){
			setGameState(GameState.FIELD);
		}
		//Fixing a bug around selecting a scroll, then cancelling the target and cancelling the selection of the spell.
		else if(getGameState()==GameState.USE &&getLastGameState()==GameState.CAST_TARGET){
			setGameState(GameState.FIELD);
		}
		else{
			setGameState(getLastGameState());
		}
		setObjectChoiceIndex(0);
		setSelectedObject(null);
	}

	private static void trainPlayer() {
		if(player.getGold()>=(player.getLevel()+1)*trainMultiplier){
			player.setGold(player.getGold()-(player.getLevel()+1)*trainMultiplier);
			player.gainExp(player.getExpCap());
			output("\"Now you are slightly wiser and I am much richer.\"");
			outputLn();
			output("You are now level " + player.getLevel());
			outputLn();
		}
		else{
			output("\"You are poor! Seek me out when you are richer.\"");
			outputLn();
		}
		output("The mysterious man disappeared.");
		outputLn();
		//getPlayerTile().getActor().clear();
		setGameState(GameState.FIELD);
	}

	private static void buyItem(Item item){
		Actor npc = getTarget();

		if(player.getGold()>=item.getValue()){
			player.setGold(player.getGold()-item.getValue());
			player.addItem(item);
			npc.removeItem(item);
			checkCollectQuest();
			if(npc.getInventory().size()>0){
				setObjectChoice(npc.getInventory());
				setObjectChoiceIndex(Math.min(getObjectChoiceIndex(),getObjectChoice().size()-1));
				setSelectedObject(getObjectChoice().get(getObjectChoiceIndex()));
			}
			else{
				output("The MERCHANT has nothing left, he is leaving...");
				getActorTile(npc).removeActor();;
				setGameState(GameState.FIELD);
			}
		}
		else{
			output("You need " + item.getValue() + " GOLD to buy that, but you only have " + player.getGold());
			outputLn();
		}

	}

	private static void sellItem(Item item){
		Actor npc = getTarget();
		player.setGold(player.getGold()+item.getValue());
		player.removeItem(item);
		npc.addItem(item);
		checkCollectQuest();
		if(player.getInventory().size()>0){ 
			setObjectChoice(player.getInventory());
			setObjectChoiceIndex(Math.min(getObjectChoiceIndex(),getObjectChoice().size()-1));
			setSelectedObject(getObjectChoice().get(getObjectChoiceIndex()));
		}
		else{
			setGameState(GameState.MERCHANT);
			setObjectChoice(null);
			setObjectChoiceIndex(0);
			setSelectedObject(null);
		}
	}

	private static void attackEnemy(Actor target) {
		setGameState(GameState.FIELD);
		player.attack(target);
		processAI();
	}

	private static void actorDeath(Actor actor){
		if(actor.getType().equals(ActorType.ENEMY)){
			Tile actorTile = tileMap.getTile(actor.getX(),actor.getY());
			actorTile.removeActor();
			for(Item item:actor.getInventory()){//For all items in the enemy's inventory.
				if(new Random().nextInt(10)==0||item.getName().equals("MAGIC SHARD")){
					actorTile.addItem(item);//Add to the tile.
					//output("The " + actor.getName() + " dropped " + item.getBaseName());
					//outputLn();
				}
			}
			int xp = actor.getExpAmount(); //Set xp to the amount of xp that the enemy gives upon dying.
			player.setGold(player.getGold()+actor.getGold()); //Add the gold that the enemy gives to the player.
			output(player.getName() + " gained " + xp + " experience and " + + actor.getGold() + " gold!"); //Output the xp and gold gain.
			outputLn();//Output a \n

			if(player.gainExp(xp)){ //If the player has levelled up as a result of gaining the xp.
				levelUp(); //Output the level up bits.
			}

			Quest questComplete = null;

			for(Quest quest:player.getQuests()){
				if(quest.getType()==QuestType.KILL){
					KillQuest killQuest = (KillQuest)quest;
					if(actor==killQuest.getTarget()){
						output(player.getName() + " completed the quest!");
						outputLn();
						output(player.getName() + " gained " + quest.getRewardGold() + " GOLD, " + quest.getRewardExp() + " EXP, and a " + quest.getRewardItem().getName() + "!");
						outputLn();
						questComplete = quest;
					}
				}
				else if(quest.getType()==QuestType.CULL){
					CullQuest cullQuest =(CullQuest)quest;
					if(actor.getRace()==cullQuest.getEnemyRace()){
						cullQuest.setCurrent(cullQuest.getCurrent()+1);
						if(cullQuest.getCurrent()>=cullQuest.getNeeded()){
							output(player.getName() + " completed the quest!");
							outputLn();
							output(player.getName() + " gained " + quest.getRewardGold() + " GOLD, " + quest.getRewardExp() + " EXP, and a " + quest.getRewardItem().getName() + "!");
							outputLn();
							questComplete = quest;
						}
					}
				}
				else if(quest.getType()==QuestType.CLEAR){
					ClearQuest clearQuest = (ClearQuest)quest;
					boolean dungeonClear = true;
					for(Tile tile:clearQuest.getDungeon().getTileArea()){
						if(clearQuest.getDungeon().isVisited==false){
							dungeonClear = false;
							break;
						}
						else if(tile.getActor()==null||tile.getActor().getType()==ActorType.PLAYER){
							continue;
						}
						else{
							dungeonClear = false;
							break;
						}
					}
					if(dungeonClear){	
						output(player.getName() + " completed the quest!");
						outputLn();
						output(player.getName() + " gained " + quest.getRewardGold() + " GOLD, " + quest.getRewardExp() + " EXP, and a " + quest.getRewardItem().getName() + "!");
						outputLn();
						questComplete = quest;
					}
				}
			}
			if(questComplete!=null){
				if(questComplete.completeQuest(player)){
					levelUp();
				}
			}

		}
		else if(actor.getType().equals(ActorType.PLAYER)){
			playerDeath();
		}
	}


	private static void levelUp() {
		output(player.getName() + " levelled up! " + player.getName() + " is now level " + player.getLevel() + ".");
		outputLn();
		setGameState(GameState.LEVEL);
		setObjectChoice(player.getAttributes());
		setSelectedObject(player.getAttributes().get(0));
		setObjectChoiceIndex(0);
	}

	/*private void showDirections() {
		output("Type north, south, east or west to continue");
		outputLn();
		if(player.getQuest()!=null){
			output("Type QUEST to give you the direction of your QUEST!");
			outputLn();
		}
	}*/

	private static void playerDeath() {
		output("Game Over...");
		outputLn();
		setGameState(GameState.DEATH);
	}

	private static void preStart(){  
		Player loadPlayer = FileIO.loadXML();
		if(loadPlayer!=null){
			player = loadPlayer;
			output("Save found. Continuing as " + player.getName());
			outputLn();

			startGame("Debug");
		}else{
			startGame(Output.getName());
		}
	}

	private static void startGame(String playerName){
		Log("Game started");

		if(playerName.equalsIgnoreCase("Matt")){
			playerName = "M@TT";
		}
		if(player==null){
			player=new Player(playerName);
			player.addItem(startingWeapon);
			player.equipWeapon(startingWeapon);
			player.addItem(startingArmour);
			player.equipArmour(startingArmour);
			player.addItem(potion1);
			player.addItem(potion2);
			player.addSpell(new Spell("RESTORATION", DamageType.CURE, -1, 10, null, SpellType.SELF, 1));
			player.addSpell(SpellFactory.GetSpell(SpellType.TARGET,player.getLevel()));
		}

		checkCollectQuest();

		player.moveActor(size/2, size/2);

		enterTown(tileMap.getTowns().get(0));


		//getPlayerTile().addActor(player); //Player no longer uses tile actor arrayList.
		output("Welcome " + player.getName() + ", use the arrow keys to move.");
		outputLn();
		setGameState(GameState.FIELD);
	}

	private static void useItem(Item item) {
		setGameState(getLastGameState());
		MagicItem magicItem;
		switch (item.getType()){
		case POTION:
			if(player.usePotion(item)){
				output(player.getName() + " used a potion and was restored to full health.");
				player.removeItem(item); //Remove item here to prevent concurrent modification error.
			}
			else{
				output(player.getName() + " is already at full health and so does not need to drink a potion.");
			}
			break;
		case FOOD:
			int healthIncrease = player.eatMeat(item);
			if(healthIncrease > 0){
				output(player.getName() + " ate the " + item.getBaseName() + " and restored " + healthIncrease + " health.");
				player.removeItem(item); //Remove item here to prevent concurrent modification error.					}
			}
			else{
				output(player.getName() + " is already at full health and so does not need to eat the "  + item.getBaseName() + ".");
			}
			break;
		case WEAPON:
			if(player.equipWeapon(item)){
				output(player.getName() + " equipped " + item.getName() + ".");
			}
			else{
				output(player.getName() + " unequipped " + item.getName() + ".");
			}
			break;
		case ARMOUR:
			if(player.equipArmour(item)){
				output(player.getName() + " equipped " + item.getName() + ".");
			}
			else{
				output(player.getName() + " unequipped " + item.getName() + ".");
			}
			break;
		case SCROLL:
			magicItem = (MagicItem)item;
			Spell spell = magicItem.getSpell();
			if(spell.getSpellType()==SpellType.SELF){
				getPlayer().castSpell(spell, getPlayer(), true);
				getPlayer().removeItem(item);
			}
			else if(spell.getSpellType()==SpellType.TARGET){
				if((getPlayer().getValidEnemies(spell.getRange()).size()>0)){
					setGameState(GameState.CAST_TARGET);
					setObjectChoice(getPlayer().getValidEnemies(spell.getRange())); //Set the list of objects to the available enemies in range.
					setSelectedObject(getObjectChoice().get(0));
					setObjectChoiceIndex(0);
					selectedSpell=spell;
					scrollInUse=item;
				}
				else {
					output("There are no enemies within range of that spell.");
					outputLn();
				}
			}
			break;
		case MISC:
			output(item.getName() + " cannot be used.");
			break;
		case TOME:
			output(player.getName() + " learned the secrets of the " + item.getBaseName() + ".");
			magicItem = (MagicItem)item;
			player.addSpell(magicItem.getSpell());
			player.removeItem(item);
			break;
		}
		processAI();
		outputLn();
	}

	private static void moveItem(Item item, HasInventory origin, HasInventory target){
		origin.removeItem(item); //Remove item here to prevent concurrent modification error.
		target.addItem(item);

		if(origin.getInventory().size()==0){
			setGameState(getLastGameState());
		}
		else{
			setObjectChoice(origin.getInventory());
			setObjectChoiceIndex(Math.min(getObjectChoiceIndex(),getObjectChoice().size()-1));
			setSelectedObject(getObjectChoice().get(getObjectChoiceIndex()));
		}
		checkCollectQuest();
	}

	private static void checkCollectQuest() {
		Quest questComplete = null;
		for(Quest quest:player.getQuests()){
			if(quest.getType()==QuestType.COLLECT){
				ArrayList<Item> itemRemoveList = new ArrayList<Item>();
				CollectQuest collectQuest = (CollectQuest)quest;
				int itemCount = 0;
				for(Item item:player.getInventoryRaw()){
					if(item.getBaseName().equalsIgnoreCase(collectQuest.getItemName())){
						itemCount++;
						itemRemoveList.add(item);
					}
				}
				collectQuest.setCurrent(itemCount);

				if(collectQuest.getCurrent()>=collectQuest.getNeeded()){
					output(player.getName() + " completed the quest!");
					outputLn();
					output(player.getName() + " gained " + quest.getRewardGold() + " GOLD, " + quest.getRewardExp() + " EXP, and a " + quest.getRewardItem().getName() + "!");
					outputLn();
					questComplete = quest; //Set quest complete here to remove it later.
					for(Item item:itemRemoveList){
						player.removeItem(item); //
					}				

				}
			}
		}
		if(questComplete!=null){
			if(questComplete.completeQuest(player)){
				levelUp();
			}
		}
	}

	private static void movePlayer(Direction direction){
		//Tile originTile = tileMap.getTile(player.getX(), player.getY()); //The tile the player is moving from.
		int newX = getX(player.getX(),direction); //X location the player is trying to move to.
		int newY = getY(player.getY(),direction); //Y location the player is trying to move to.
		try{
			Tile newTile = tileMap.getTile(newX, newY); //Get the new tile
			if(Tile.isValidTile(newTile)&&newTile.getActor()==null){
				lastTile = getPlayerTile();
				player.moveActor(newX, newY);

				ActorFactory.getEnemies(player, tileMap);
				newTile.enemyAdded=false;


				setGameState(GameState.FIELD);

				boolean lastTileTown = false;
				boolean newTileTown = false;

				for(Town town:tileMap.getTowns()){
					for(Tile tile:town.getTileArea()){
						if(lastTile==tile){
							lastTileTown=true;
							break;
						}
						if(newTile==tile){
							newTileTown=true;
							if(lastTileTown==true){
								break;
							}
						}
					}
					if(lastTileTown == false && newTileTown == true){
						enterTown(town);
						break;
					}
				}

				boolean lastTileDungeon = false;
				boolean newTileDungeon = false;

				for(Dungeon dungeon:tileMap.getDungeons()){
					for(Tile tile:dungeon.getTileArea()){
						if(lastTile==tile){ //If the last tile is found in a dungeon.
							lastTileDungeon=true;
							break;
						}
						if(newTile==tile){ //If the new tile is found in a dungeon.
							newTileDungeon=true;
							if(lastTileDungeon==true){
								break;
							}
						}
					}
					if(lastTileDungeon == false && newTileDungeon == true){
						//System.out.println("Dungeon!");
						enterDungeon(dungeon);
						break;
					}
				}
				processAI();

			}
			else{
				//output("There is " + newTile.getType() + " to the " + direction + ", it is impassable.");
			}
		}
		catch(ArrayIndexOutOfBoundsException e){ //If the array has hit the edge.
			//output("Out of bounds.");
			outputLn();
		}

	}

	private static void enterDungeon(Dungeon dungeon) {
		/*if(dungeon.isVisited==false){
			dungeon.getOrigin().addActor(ActorFactory.getEnemy((getPlayer().getLevel()*2)+1, dungeon.getOrigin()));
		}
		Was causing an issue with entering dungeons
		*/
		dungeon.isVisited=true;

	}

	private static void enterTown(Town town) {
		if(town.isVisited==false){
			output("\"Welcome to " + town.getName() + ", " + player.getName() + "!\"");
			outputLn();
			for(Tile tile:town.getTileArea()){
				Actor actor = ActorFactory.getActor(player.getLevel(), town.getTileArea().get(0));
				if(actor!=null&&tile.getActor()==null){
					tile.addActor(actor);
				}
			}
			town.isVisited=true;
		}
	}

	private static void startTrainer() {
		outputLn();
		output("A mysterious man appeared!");
		outputLn();
		output("\"If you are lazy, I will train you!");
		outputLn();
		output("It will cost you " + (player.getLevel()+1)*trainMultiplier + "."); //Type TRAIN to receive my glorious knowledge!\"");
		outputLn();
		//showDirections();
		setGameState(GameState.TRAINER);
	}

	private static void startQuest() {
		outputLn();
		if(player.getQuests().size()<questLimit ){
			Quest quest = QuestFactory.getQuest(player, tileMap);
			output("\"Help me " + player.getName() + ", you're my only hope!");
			outputLn();
			if(quest.getType()==QuestType.KILL){
				KillQuest killQuest = (KillQuest)quest;
				output("There is a monstrous, level " + killQuest.getTarget().getLevel() + " " + killQuest.getTarget().getName() + ", " + killQuest.getDirection(player) + " from here.");
				outputLn();
				output("If you slay it, I will give you " + quest.getRewardGold() + " gold, and a " + quest.getRewardItem().getName() + "!\"");
				outputLn();
			}
			else if(quest.getType()==QuestType.COLLECT){
				CollectQuest collectQuest = (CollectQuest)quest;
				output("I need " + collectQuest.getNeeded() + " " + collectQuest.getItemName());
				if(collectQuest.getNeeded()>1){
					output("S");
				}
				outputLn();
				output("If you get them for me, I will give you "+ quest.getRewardGold() + " gold, and a " + quest.getRewardItem().getName() + "!\"");
				outputLn();
				checkCollectQuest();
			}
			else if(quest.getType()==QuestType.CULL){
				CullQuest cullQuest = (CullQuest)quest;
				output("I hate " + cullQuest.getEnemyRace().getName() + "s!");
				outputLn();
				output("If you kill "+ cullQuest.getNeeded() + " of them for me, I will give you "+ quest.getRewardGold() + " gold, and a " + quest.getRewardItem().getName() + "!\"");
				outputLn();
			}
			else if(quest.getType()==QuestType.CLEAR){
				ClearQuest clearQuest = (ClearQuest)quest;
				output("The dungeon " + clearQuest.getDungeon().getName() +" is crawling with " + clearQuest.getDungeon().getRaceType() + " monsters!");
				outputLn();
				output("If you kill them for me, I will give you "+ quest.getRewardGold() + " gold, and a " + quest.getRewardItem().getName() + "!\"");
				outputLn();
			}
			tileMap.getTile(getTarget().getX(), getTarget().getY()).removeActor();
		}
		else{
			output("\"You already have too many quests. Come back when you've got more free time.\"");
			outputLn();
		}
		setGameState(GameState.FIELD);
		setSelectedObject(null);
		setTarget(null);
	}

	private static void startMerchant() {
		setGameState(GameState.MERCHANT);
	}

	private static void showInventory(HasInventory target){
		output(TextFactory.getInventoryText(target));
	}

	private static void output(String output){
		Output.setOutputField(output);
	}
	private static void outputLn(){
		Output.setOutputField("\n");

	}

	private static int getX(int origin, Direction direction){
		switch (direction){
		case EAST: //If direction is east
			return origin+1; //Set x to +1
		case WEST: //If direction is west
			return origin-1; //Set x to -1
		default: //If direction is north or south
			return origin; //Set x to 0
		}
	}

	private static int getY(int origin, Direction direction){
		switch (direction){
		case NORTH: //If direction is north
			return origin+1; //Set y to +1
		case SOUTH: //If direction is south
			return origin-1; //set y to -1
		default: //If direction is east or west
			return origin; //Set y to 0
		}
	}

	public static Tile getPlayerTile(){
		return tileMap.getTile(player.getX(), player.getY());
	}

	public static Tile getActorTile(Actor actor){
		return(tileMap.getTile(actor.getX(), actor.getY()));
	}

	public static String getVersion() {
		return version;
	}

	private static void saveGame(){
		if(FileIO.writeXML()){
			output("File successfully saved.");
		}
		else{
			output("File could not be saved.");
		}
		outputLn();
	}

	public static void setObjectChoice(ArrayList<? extends HasName> objectChoice) {
		Controller.objectChoice = objectChoice;
	}

	public static ArrayList<? extends HasName> getObjectChoice() {
		return objectChoice;
	}

	public static void setSelectedObject(Object selectedObject) {
		Controller.selectedObject = selectedObject;
	}

	public static Object getSelectedObject() {
		return selectedObject;
	}

	public static void setObjectChoiceIndex(int objectChoiceIndex) {
		Controller.objectChoiceIndex = objectChoiceIndex;
	}

	public static int getObjectChoiceIndex() {
		return objectChoiceIndex;
	}

	private static void iterateSelectedObject(String direction){
		if(getObjectChoice().size()>0){
			if(direction.toUpperCase().matches("WEST||NORTH")){
				if(getObjectChoiceIndex()==0){
					setObjectChoiceIndex(getObjectChoice().size()-1);
				}
				else{
					setObjectChoiceIndex(getObjectChoiceIndex()-1);
				}
			}
			if(direction.toUpperCase().matches("EAST||SOUTH")){
				if(getObjectChoiceIndex()==getObjectChoice().size()-1){
					setObjectChoiceIndex(0);
				}
				else{
					setObjectChoiceIndex(getObjectChoiceIndex()+1);
				}
			}
			setSelectedObject(getObjectChoice().get(getObjectChoiceIndex()));
		}
	}

	public static GameState getLastGameState() {
		return lastGameState;
	}

	public static Actor getTarget() {
		return target;
	}

	public static int getProximity() {
		return proximity ;
	}

	private static void processAI(){
		Boolean playerTurn = false;
		do{
			ArrayList<Actor> actorsActed = new ArrayList<Actor>();
			for(int x = getPlayer().getX()-getProximity();x<=getPlayer().getX()+getProximity();x++){
				for(int y = getPlayer().getY()-getProximity();y<=getPlayer().getY()+getProximity();y++){
					Actor actor = tileMap.getTile(x, y).getActor();
					if(actor != null && actor.getType() != ActorType.PLAYER && actorsActed.contains(actor)==false){
						actor.processAI();
						actorsActed.add(actor);
					}
				}
			}
			playerTurn=getPlayer().getTurn();
		}while(playerTurn==false);

		if(player.getMana()<player.getMaxMana()){
			player.setMana(Math.min(player.getMana()+(player.getMaxMana()/30),player.getMaxMana()));
		}
	}

	public static void OutputAttack(Actor attacker, Actor defender, int attack, int damage) {
		output(attacker.getName() + " attacked " + defender.getName() + " for " + damage + " damage");
		if(attack!=damage){
			output(", after " + (attack-(damage)) + " damage was blocked. ");
		}
		else{
			output(". ");
		}

		if(defender.getCurrentHealth()==0){
			output (defender.getName() + " died. ");
			actorDeath(defender);
		}
		outputLn();
	}

	public static void Log(String log){
		System.out.println(System.currentTimeMillis() + " " + log);
	}


	private static void increaseAttribute(Attribute attribute) {
		int currentValue = player.getAttribute(AttributeName.valueOf(attribute.getName()));
		player.setAttribute(AttributeName.valueOf(attribute.getName()), currentValue+5);

		setGameState(GameState.FIELD);
	}

	public static void setTarget(Actor target) {
		Controller.target = target;
	}

	public static void outputSpell(Spell spell, Actor defender, int damage) {
		output(getPlayer().getName() + " cast " + spell.getName() + " on " + defender.getName());
		if(damage>0){
			output(", dealing " + damage + " damage");
		}
		else if(damage<0){
			output(", healing " + -damage + " damage");
		}
		if(spell.getStatusEffect()!=null){
			output(", causing " + spell.getStatusEffect().getEffect());
		}
		output(". ");
		if(defender.getCurrentHealth()==0){
			output (defender.getName() + " died. ");
			actorDeath(defender);
		}
		outputLn();
		//processAI();
	}
}

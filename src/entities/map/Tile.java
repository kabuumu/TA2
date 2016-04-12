package entities.map;

import interfaces.HasInventory;

import java.util.ArrayList;

import main.Controller;


import entities.actors.Actor;
import entities.items.Item;
import enums.TileType;

public class Tile implements HasInventory{
	private final TileType type;	//What type of tile this is.
	private final int x; //The x position of the tile.
	private final int y; //The y position of the tile.
	private Actor actor;
	private ArrayList<Item> inventory = new ArrayList<Item>();
	private final String name; //The name of the tile, this is produced from the tile type and "Tile"
	public boolean enemyAdded;
	public boolean isLocated = false;
	
	private static final int itemLimit = 8;
	private int inventoryMin = 0;
	private int inventoryMax = itemLimit;
	
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
	
	public String getName(){
		return name;
	}
	
	public void addItem(Item newItem) {
		newItem.setName(newItem.getBaseName());
		inventory.add(newItem);
	}
	
	public void removeItem(Item item){
		inventory.remove(item);
	}
	
	public Tile(String name, TileType type, int x, int y){
		this.name=name;
		this.type=type;
		this.x=x;
		this.y=y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public TileType getType() {
		return type;
	}
	
	public void addActor(Actor actor) {
		this.actor=actor;
		actor.setX(x);
		actor.setY(y);
	}
	
	public void removeActor(){
		actor=null;
	}
	
	public Actor getActor(){
		return actor;
	}
	
	public static boolean isValidTile(Tile tile){
		if(tile.getType()==TileType.WALL||
				tile.getType()==TileType.WATER||
				tile.getType()==TileType.ROCK||
				tile.getType()==TileType.DUNGEONWALL||
				tile.getType()==TileType.FOREST||
				tile.getActor()!=null
				){
			return false;
		}
		else return true;
	}

	public static boolean isValidQuestTile(Tile tile) {
		if(!isValidTile(tile)){
			return false;
		}
		else if(tile.getType()==TileType.ROAD||tile.getType()==TileType.TOWN){
			return false;
		}
		else return true;
	}

	public static boolean isBlockedTile(Tile tile) {
		if(tile.getType()==TileType.WALL||
				tile.getType()==TileType.ROCK||
				tile.getType()==TileType.DUNGEONWALL||
				tile.getType()==TileType.FOREST
				){
			return true;
		}
		else return false;
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
}

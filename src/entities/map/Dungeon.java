package entities.map;

import enums.RaceType;
import factories.TextFactory;

public class Dungeon extends Area {
	private final String name;
	public boolean isVisited = false;
	public boolean hasEntrance = false;
	private Tile origin;
	private Tile entrance;
	private final RaceType raceType;
	
	public Tile getOrigin() {
		return origin;
	}

	public void setOrigin(Tile origin) {
		this.origin = origin;
	}

	public Dungeon(RaceType raceType){
		name = TextFactory.getDungeonName();
		this.raceType = raceType; 
	}
	
	public RaceType getRaceType(){
		return raceType;
	}
	
	public String getName(){
		return name;
	}

	public void setEntrance(Tile entrance) {
		this.entrance = entrance;
	}

	public Tile getEntrance() {
		return entrance;
	}
}

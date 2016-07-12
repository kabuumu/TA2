package org.kabuumu.entities.actors;

import org.kabuumu.enums.RaceType;
import org.kabuumu.enums.TileType;

public class Race extends ActorClassification{
	private final TileType[] availableTileTypes;
	private final ActorClass[] availableClasses;
	private final RaceType raceType;
	private final int minimumLevel;
	private final int maximumLevel;
	
	public Race(RaceType raceType, String name, AttributeModifier[] attributeModifiers, TileType[] availableTiles, ActorClass[] availableClasses, int minimumLevel, int maximumLevel){
		super(name,attributeModifiers);
		this.availableTileTypes = availableTiles;
		this.availableClasses = availableClasses;
		this.raceType = raceType;
		this.minimumLevel = minimumLevel;
		this.maximumLevel = maximumLevel;
	}

	public ActorClass[] getAvailableClasses() {
		return availableClasses;
	}

	public TileType[] getAvailableTileTypes() {
		return availableTileTypes;
	}

	public RaceType getRaceType() {
		return raceType;
	}

	public int getMinimumLevel() {
		return minimumLevel;
	}

	public int getMaximumLevel() {
		return maximumLevel;
	}
}

package org.kabuumu.factories;

import java.util.Random;

import org.kabuumu.entities.map.Tile;
import org.kabuumu.enums.TileType;

public class TileFactory {
	public static Tile getTile(int x, int y, TileType type){
		if (type==null){
			type = getType(); //Get random type of new tile
		}
		Tile tile = new Tile(type.toString()+ " TILE",type,x,y); //Create the new tile with the set type and location
		
		return tile; //Return the created tile
	}

	private static TileType getType(){
		int random = new Random().nextInt(20);
		
		if(random<8){
			return TileType.DIRT;
		}
		else if(random<11){
			return TileType.SWAMP;
		}
		else if(random<14){
			return TileType.FIELD;
		}
		else if (random<16){
			return TileType.DUNGEON;
		}
		else if (random<18){
			return TileType.CAVE;
		}
		else {
			return TileType.TOWN;
		}
	}
}

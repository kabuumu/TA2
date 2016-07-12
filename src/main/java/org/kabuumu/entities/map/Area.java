package org.kabuumu.entities.map;

import java.util.ArrayList;


public abstract class Area {
	private final ArrayList<Tile> tileArea = new ArrayList<Tile>();

	public void addTile(Tile tile) {
		tileArea.add(tile);
	}

	public ArrayList<Tile> getTileArea() {
		return tileArea;
	}

}

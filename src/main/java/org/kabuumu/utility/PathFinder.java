package org.kabuumu.utility;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.kabuumu.main.Controller;

import org.kabuumu.entities.actors.Actor;
import org.kabuumu.entities.map.Tile;
import org.kabuumu.entities.map.TileMap;

public class PathFinder{
	public static Tile getNextTile(Actor actor, Actor target){
		Tile targetTile = null;
		TileMap tileMap = Controller.getTileMap();
		for(int x=actor.getX()-1; x<=actor.getX()+1; x++){
			for(int y=actor.getY()-1; y<=actor.getY()+1;y++){
				if(x==actor.getX()^y==actor.getY()){
					if(Tile.isValidTile(tileMap.getTile(x,y))
							&&
							(targetTile==null || Point2D.distance(target.getX(), target.getY(), x, y)
									<
									Point2D.distance(target.getX(), target.getY(), targetTile.getX(), targetTile.getY())
							)){
						targetTile = tileMap.getTile(x,y);
					}
				}
			}
		}
		if(targetTile==null){
			targetTile=actor.getTile();
		}
		return targetTile;
	}
}
package org.kabuumu.utility;

import java.util.ArrayList;


import org.kabuumu.main.Controller;
import org.kabuumu.entities.actors.Actor;
import org.kabuumu.entities.map.Tile;
import org.kabuumu.enums.AttributeName;

public class ShadowCaster {
	public static ArrayList<Tile> getVisibleTiles(Actor actor){
		ArrayList<Tile> visibleTiles = new ArrayList<Tile>();

		int actorX = actor.getX();
		int actorY = actor.getY();
		int perception;
		
		perception = actor.getAttribute(AttributeName.PERCEPTION)/3;
		//perception = Math.min(perception, Controller.getProximity());
		
		Tile playerTile = Controller.getTileMap().getTile(actorX,actorY);

		for(int x = actorX-perception;x<=actorX+perception;x++){
			int offset = Math.abs(x-actorX);
			offset = (int)Math.round(Math.sqrt((perception*perception)-(offset*offset)));
			for(int y = actorY-offset;y<=actorY+offset;y++){
				try{
					Tile tile = Controller.getTileMap().getTile(x,y);
					if(isVisible(playerTile, tile)){
						visibleTiles.add(tile);
					}
				}
				catch(Exception e){
					continue;
				}
			}
		}

		return visibleTiles;
	}
	private static boolean isVisible(Tile origin, Tile target){
		int originX=origin.getX();
		int originY=origin.getY();
		int targetX=target.getX();
		int targetY=target.getY();

		boolean route1Blocked = false;
		boolean route2Blocked = false;
		boolean route3Blocked = false;

		//Route 1 (horizontal first)
		while(targetX!=originX||targetY!=originY){ //If it is not the same tile.
			if(targetX<originX){
				targetX++;
			}
			else if(targetX>originX){
				targetX--;
			}
			if(Tile.isBlockedTile(Controller.getTileMap().getTile(targetX, targetY))){
				route1Blocked=true;
				break;
			}
			if(targetY<originY){
				targetY++;
			}
			else if(targetY>originY){
				targetY--;
			}
			if(Tile.isBlockedTile(Controller.getTileMap().getTile(targetX, targetY))){
				route1Blocked=true;
				break;
			}
		}


		//Route 2 (vertical first)
		targetX=target.getX();
		targetY=target.getY();
		while(targetX!=originX||targetY!=originY){
			if(targetY<originY){
				targetY++;
			}
			else if(targetY>originY){
				targetY--;
			}
			if(Tile.isBlockedTile(Controller.getTileMap().getTile(targetX, targetY))){
				route2Blocked=true;
				break;
			}
			if(targetX<originX){
				targetX++;
			}
			else if(targetX>originX){
				targetX--;
			}
			if(Tile.isBlockedTile(Controller.getTileMap().getTile(targetX, targetY))){
				route2Blocked=true;
				break;
			}
		}

		//Route 3 (diagonal)
		targetX=target.getX();
		targetY=target.getY();
		while(targetX!=originX||targetY!=originY){
			if(targetY<originY){
				targetY++;
			}
			else if(targetY>originY){
				targetY--;
			}
			if(targetX<originX){
				targetX++;
			}
			else if(targetX>originX){
				targetX--;
			}
			if(Tile.isBlockedTile(Controller.getTileMap().getTile(targetX, targetY))){
				route3Blocked=true;
				break;
			}
		}

		if(route1Blocked&&route2Blocked&&route3Blocked){
			return false;
		}
		else return true;
	}
}

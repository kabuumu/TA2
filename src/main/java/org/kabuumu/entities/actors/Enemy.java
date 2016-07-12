package org.kabuumu.entities.actors;


import java.util.ArrayList;
import java.util.Random;

import org.kabuumu.utility.PathFinder;

import org.kabuumu.main.Controller;
import org.kabuumu.entities.map.Tile;
import org.kabuumu.entities.map.TileMap;
import org.kabuumu.enums.ActorType;

public class Enemy extends Actor {	

	public Enemy(String name, int level, int gold , Race race, ActorClass actorClass) {
		super(name, ActorType.ENEMY, level, race, actorClass);
		setGold(gold);
	}

	@Override
	public void processAI() {
		if(getTurn()){
			ArrayList<Tile> visibleTiles = getVisibleTiles();

			Actor target = getTarget();
			
			for(Actor actor:getValidEnemies()){
				attack(actor);
				return;//To only do it once.
			}

			for(Tile tile:visibleTiles){
				if(tile.getActor()!=null&&tile.getActor().getType()==ActorType.PLAYER){
					setTarget(tile.getActor());
				}
			}
			
			TileMap tileMap = Controller.getTileMap();

			if(target != null){
				Tile targetTile = PathFinder.getNextTile(this, target);
				moveActor(targetTile.getX(),targetTile.getY());
			}
			else{
				int x;
				int y;
				int count = 0;
				do{
					x = getX();
					y = getY();

					int random = new Random().nextInt(100);

					switch(random){
					case 0://LEFT
						x--;
						break;
					case 1://RIGHT
						x++;
						break;
					case 2://DOWN
						y--;
						break;
					case 3://UP
						y++;
						break;
					}
					count++;
				}while(Tile.isValidTile(Controller.getTileMap().getTile(x, y)) == false
						&&
						(x!=getX()
								||
								y!=getY())
								&&
								count<10);

				if(count>=10){
					return;
				}
				else moveActor(x,y);
			}
		}
	}

	public ArrayList<Actor> getValidEnemies(){
		ArrayList<Actor> validEnemies = new ArrayList<Actor>();
		for(int x = getX()-getRange();x<=getX()+getRange();x++){
			for(int y = getY()-getRange();y<=getY()+getRange();y++){
				Actor actor = Controller.getTileMap().getTile(x,y).getActor();
				//if(actor!=null && actor.getType()==ActorType.ENEMY&&Point2D.distance(x, y, getX(), getY())<=getRange()){
				if(actor!=null &&actor.getType()==ActorType.PLAYER){
					validEnemies.add(actor);
				}
			}
		}
		return validEnemies;
	}
}

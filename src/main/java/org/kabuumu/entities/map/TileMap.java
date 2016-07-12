package org.kabuumu.entities.map;

import java.util.ArrayList;

import java.util.Random;

import org.kabuumu.main.Controller;
import org.kabuumu.enums.TileType;
import org.kabuumu.factories.TileFactory;
import org.kabuumu.factories.RaceFactory;

public class TileMap {
	private Tile[][] map;
	private int size;
	private ArrayList<Town> towns = new ArrayList<Town>();
	private ArrayList<Cave> caves = new ArrayList<Cave>();
	private ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();

	public TileMap(int size){
		map = new Tile[size][size];
		this.size = size;
	}

	public Tile[][] getMap(){
		return map;
	}

	public void setTile(int x, int y, Tile tile) throws ArrayIndexOutOfBoundsException{
		map[x][y]= tile;
	}

	public Tile getTile(int x, int y) throws ArrayIndexOutOfBoundsException{
		return map[x][y];
	}

	public void initialise(){
		Controller.Log("Beginning TileMap initialisation.");
		createTown(size/2,size/2,5);

		//Creating the oceans.
		for (int x=0;x<size;x++){
			for (int y=0;y<size;y++){
				setTile(x,y,TileFactory.getTile(x,y,TileType.WATER));
			}
		}

		//Creating the land.
		for (int x=size/10;x<size-(size/10);x++){
			for (int y=size/10;y<size-(size/10);y++){
				if(new Random().nextInt(80)==0){
					createLandBlock(x,y);
				}
			}
		}
		
		//Creating the initial town
		createTown(size/2,size/2,5);
		
		for(int x=0;x<size;x++){
			for(int y=0;y<size;y++){
				if(getTile(x,y)==null){
					setTile(x,y,TileFactory.getTile(x,y,null));
					getTileTypeBlock(x,y, getTile(x,y).getType());
				}
			}
		}
		int count = 0;
		//Debugging code for non-valid tiles, didn't fix intended bug but did catch some.
		for(Dungeon dungeon:dungeons){
			ArrayList<Tile> removeDungeonTiles = new ArrayList<Tile>();
			for(Tile tile:dungeon.getTileArea()){
				if(getTile(tile.getX(),tile.getY())!=tile){
					removeDungeonTiles.add(tile);
				}
			}
			for(Tile tile:removeDungeonTiles){
				dungeon.getTileArea().remove(tile);
				count++;
			}
		}
		Controller.Log(count + " invalid dungeon tiles removed from " + dungeons.size() + " dungeons.");
		count= 0;
		
		for(Cave cave:caves){
			ArrayList<Tile> removeCaveTiles = new ArrayList<Tile>();
			for(Tile tile:cave.getTileArea()){
				if(getTile(tile.getX(),tile.getY())!=tile){
					removeCaveTiles.add(tile);
				}
			}
			for(Tile tile:removeCaveTiles){
				cave.getTileArea().remove(tile);
				count++;
			}
		}
		
		Controller.Log(count + " invalid cave tiles removed from " + caves.size() + " caves.");
		
		
		Controller.Log("TileMap Initialisation Complete.");
	}

	private void createLandBlock(int originX, int originY) {

		int random = new Random().nextInt(5)+5;
		for(int x = originX-random;x<=originX+random;x++){
			int offset = Math.abs(x-originX);
			offset = (int)Math.round(Math.sqrt((random*random)-(offset*offset)));
			for(int y = originY-offset;y<=originY+offset;y++){
				try{
					setTile(x,y, null);
				}
				catch(Exception e){
					continue;
				}
			}
		}
	}

	private void getTileTypeBlock(int startX, int startY, TileType tileType){
		int size = new Random().nextInt(6)+1;//minimum field size
		for(int x=startX-20;x<startX+10;x++){
			for(int y=startY-20;y<=startY+10;y++){
				try{
					if((getTile(x, y)!=null&&(x!=startX||y!=startY)
							&&
							(		getTile(x,y).getType()==TileType.TOWN 
									||
									getTile(x,y).getType()==TileType.DUNGEON 
									||
									getTile(x,y).getType()==TileType.CAVE
							)) 
							|| 
							Math.abs(this.size/2-startX)<10
							||
							Math.abs(this.size/2-startY)<10

					){
						while(tileType==TileType.TOWN ||
								tileType==TileType.DUNGEON ||
								tileType==TileType.CAVE){
							setTile(startX, startY, TileFactory.getTile(startX, startY, null));
							tileType=getTile(startX,startY).getType();
						}
					}
				}
				catch (Exception e){
					tileType=TileType.FIELD;
					break;
				}
			}
		}
		if(tileType==TileType.TOWN){
			createTown(startX, startY);
		}

		else if (tileType == TileType.CAVE){
			createCave(startX, startY, null);
		}

		else if (tileType == TileType.DUNGEON){
			createDungeon(startX, startY, null);
		}
		
		else{//For all other types of terrain.
			for(int x=startX-size;x<=startX+size;x++){//From the starting x point - the random number.
				for(int y=startY-size;y<=startY+size;y++){//From the starting y point...
					try{//To catch array out of bounds exceptions.
						if(getTile(x,y)==null){//If the tile is null.
							setTile(x,y,TileFactory.getTile(x,y,tileType));//Set it to the new terrain type.
							if(getTile(x,y).getType()==TileType.DIRT){
								if(new Random().nextInt(10)==0){
									setTile(x,y,TileFactory.getTile(x,y,TileType.FOREST));
								}
							}
						}
					}
					catch(Exception e){

					}
				}
			}
		}
	}

	private void createTown(int startX, int startY){
		createTown(startX, startY, 0);
	}

	private void createTown(int startX, int startY, int size){
		if(size==0){
			size = new Random().nextInt(2)+3;
		}


		Town town = new Town();
		for(Town oldTown:towns){
			if(oldTown.getName().equals(town.getName())){
				town = new Town();
			}
		}
		if(startX==this.size/2&&startY==this.size/2){
			towns.add(0,town);
		}
		else{
			towns.add(town);
		}

		for(int x=startX-size-2;x<=startX+size+2;x++){//From the starting x point - the random number.
			for(int y=startY-size-2;y<=startY+size+2;y++){//From the starting y point...
				try{//To catch array out of bounds exceptions.
					if(getTile(x,y)==null || (getTile(x,y).getType()!=TileType.TOWN && getTile(x,y).getType()!=TileType.WALL)){
						setTile(x,y,TileFactory.getTile(x,y,TileType.ROAD));
					}
				}
				catch(Exception e){}
			}
		}

		for(int x=startX-size-1;x<=startX+size+1;x++){//From the starting x point - the random number.
			for(int y=startY-size-1;y<=startY+size+1;y++){//From the starting y point...
				try{//To catch array out of bounds exceptions.
					if(getTile(x,y)==null || getTile(x,y).getType()!=TileType.TOWN){
						setTile(x,y,TileFactory.getTile(x,y,TileType.WALL));
					}
				}
				catch(Exception e){}
			}
		}
		for(int x=startX-size;x<=startX+size;x++){//From the starting x point - the random number.
			for(int y=startY-size;y<=startY+size;y++){//From the starting y point...
				try{//To catch array out of bounds exceptions.
					Tile tile = TileFactory.getTile(x,y,TileType.TOWN);
					setTile(x,y,tile);
					town.addTile(tile);
				}
				catch(Exception e){}
			}
		}
		int random = 0;
		random=getDirectionCentre(startX,startY);

		int x = startX;
		int y = startY;
		switch(random){
		case 0: 
			x = startX-size-1;
			break;
		case 1:
			x = startX+size+1;
			break;
		case 2:
			y = startY-size-1;
			break;
		case 3:
			y = startY+size+1;
			break;
		}
		//Setting entrances
		if(startX==this.size/2&&startY==this.size/2){
			x=startX+size+1;
			y=startY;
			setTile(x,y,TileFactory.getTile(x,y,TileType.DOOR));
			x=startX-size-1;
			y=startY;
			setTile(x,y,TileFactory.getTile(x,y,TileType.DOOR));
			x=startX;
			y=startY+size+1;
			setTile(x,y,TileFactory.getTile(x,y,TileType.DOOR));
			x=startX;
			y=startY-size-1;
			setTile(x,y,TileFactory.getTile(x,y,TileType.DOOR));
		}
		else try{//To catch array out of bounds exceptions.
			setTile(x,y,TileFactory.getTile(x,y,TileType.DOOR));
		}
		catch(Exception e){}

		createRoad(x,y,random);
	}

	private void createRoad(int x, int y, int random) {
		switch(random){
		case 0://west
			x--;
			break;
		case 1://east
			x++;
			break;
		case 2://south
			y--;
			break;
		case 3://north
			y++;
			break;
		}
		do{
			if((random==0||random==1)&&x==size/2){
				random=getDirectionCentre(x,y);
			}
			else if((random==2||random==3)&&y==size/2){
				random=getDirectionCentre(x,y);
			}
			switch(random){
			case 0://west
				x--;
				break;
			case 1://east
				x++;
				break;
			case 2://south
				y--;
				break;
			case 3://north
				y++;
				break;
			}
			try{//To catch array out of bounds exceptions.
				if(getTile(x,y)!=null && 
						(getTile(x,y).getType() == TileType.ROAD ||
								getTile(x,y).getType() == TileType.DOOR || 
								getTile(x,y).getType() == TileType.WALL ||
								getTile(x,y).getType() == TileType.ROCK ||
								getTile(x,y).getType() == TileType.CAVE ||
								getTile(x,y).getType() == TileType.DUNGEON ||
								getTile(x,y).getType() == TileType.DUNGEONWALL)
								||
								(x==size/2&&y==size/2)){
					break;
				}	
				setTile(x,y,TileFactory.getTile(x,y,TileType.ROAD));//Set tile to road
			}
			catch(Exception e){
				break;
			}
		}while(true);
	}

	private int getDirectionCentre(int x, int y) {
		int random = 4;
		//If the town is closer (or equally) from the centre along the y axis than the x axis.
		if((Math.abs(x-size/2)>=Math.abs(y-size/2)&&y!=size/2)||x==size/2){
			//If the town is higher than the centre line.
			if(y>size/2){
				random=2;
			}
			//If the town is lower than the centre line.
			else if(y<size/2){
				random=3;
			}
		}
		else{
			//If the town is left of the centre line.
			if(x>size/2){
				random=0;
			}
			//If the town is right of the centre line.
			else if(x<size/2){
				random=1;
			}
		}
		if(random==4){
			//System.out.println(x+ ", " + y);
		}
		return random;
	}

	public ArrayList<Town> getTowns() {
		return towns;
	}

	private void createCave(int startX, int startY, Cave cave){
		int size = new Random().nextInt(2)+1;

		if(cave==null){
			cave = new Cave();
			caves.add(cave);
		}

		for(int x = startX-size-1;x<=startX+size+1;x++){
			int offset = Math.abs(x-startX);
			offset = (int)Math.round(Math.sqrt((size*size)-(offset*offset)));
			for(int y = startY-offset-1;y<=startY+offset+1;y++){
				try{//To catch array out of bounds exceptions.
					if(getTile(x,y)==null || 
							(getTile(x,y).getType()!=TileType.CAVE &&
									getTile(x,y).getType()!=TileType.WALL &&
									getTile(x,y).getType()!=TileType.TOWN &&
									getTile(x,y).getType()!=TileType.ROAD &&
									getTile(x,y).getType()!=TileType.DUNGEON &&
									getTile(x,y).getType()!=TileType.DUNGEONWALL &&
									getTile(x,y).getType()!=TileType.ROCK)){
						setTile(x,y,TileFactory.getTile(x,y,TileType.ROCK));
					}
				}
				catch(Exception e){}
			}
		}
		for(int x = startX-size;x<=startX+size;x++){
			int offset = Math.abs(x-startX);
			offset = (int)Math.round(Math.sqrt((size*size)-(offset*offset)));
			for(int y = startY-offset;y<=startY+offset;y++){
				try{//To catch array out of bounds exceptions.
					if(getTile(x,y)==null ||
							getTile(x,y).getType()!=TileType.CAVE ){
						Tile tile = TileFactory.getTile(x,y,TileType.CAVE);
						setTile(x,y,tile);
						cave.addTile(tile); 
					}
				}
				catch(Exception e){}
			}
		}

		while(cave.getTileArea().size()<100){
			ArrayList<Tile> caveTiles =  cave.getTileArea();
			int caveSize = caveTiles.size();
			int randomTile = new Random().nextInt(caveSize);
			int x = caveTiles.get(randomTile).getX();
			int y = caveTiles.get(randomTile).getY();
			createCave(x,y,cave);
		}
		//Creating entrance
		if(cave.hasEntrance == false){
			ArrayList<Tile> caveTiles =  cave.getTileArea();
			int caveSize = caveTiles.size();
			int randomTile = new Random().nextInt(caveSize);
			int x = caveTiles.get(randomTile).getX();
			int y = caveTiles.get(randomTile).getY();
			createCaveEntrance(x,y, new Random().nextInt(4), cave);
			cave.hasEntrance=true;
		}
	}

	private void createCaveEntrance(int x, int y, int random, Cave cave) {
		switch(random){
		case 0://south
			x--;
			break;
		case 1://north
			x++;
			break;
		case 2://west
			y--;
			break;
		case 3://east
			y++;
			break;
		}
		do{
			switch(random){
			case 0://south
				x--;
				break;
			case 1://north
				x++;
				break;
			case 2://west
				y--;
				break;
			case 3://east
				y++;
				break;
			}
			try{//To catch array out of bounds exceptions.
				TileType tileType = getTile(x,y).getType();
				if(tileType!=TileType.CAVE && tileType!=TileType.ROCK){
					break;
				}	
				Tile tile = TileFactory.getTile(x,y,TileType.CAVE);
				setTile(x,y,tile);//Set tile to cave
				cave.addTile(tile);
			}
			catch(Exception e){
				break;
			}
		}while(true);
	}
	public ArrayList<Cave> getCaves() {
		return caves;
	}

	public ArrayList<Dungeon> getDungeons() {
		return dungeons;
	}

	public Dungeon getDungeon(Tile tile){
		for(Dungeon dungeon:dungeons){
			for(Tile dungeonTile:dungeon.getTileArea()){
				if(tile==dungeonTile){
					return dungeon;
				}
			}
		}
		System.out.println(tile.getType() + " is not in a dungeon.");
		return null;
	}

	private void createDungeon(int startX, int startY, Dungeon dungeon){
		if(dungeon==null){
			dungeon = new Dungeon(RaceFactory.getDungeonRaceType());
			for(Dungeon oldDungeon:getDungeons()){
				while(oldDungeon.getName().equals(dungeon.getName())){
					dungeon = new Dungeon(RaceFactory.getDungeonRaceType());
				}
			}
			dungeons.add(dungeon);
		}
		
		int oldRandom = 0;
		int random = 0;
		int size = new Random().nextInt(3);
		
		while(dungeon.getTileArea().size()<50){
			for(int x = startX-size-1;x<=startX+size+1;x++){
				for(int y = startY-size-1; y<=startY+size+1; y++){
					if(getTile(x,y)==null||(getTile(x,y).getType()!=TileType.DOOR
							&&
							getTile(x,y).getType()!=TileType.DUNGEON)){
						setTile(x,y,TileFactory.getTile(x, y, TileType.DUNGEONWALL));
					}
				}
			}

			for(int x = startX-size;x<=startX+size;x++){
				for(int y = startY-size; y<=startY+size; y++){
					Tile tile = TileFactory.getTile(x, y, TileType.DUNGEON);
					setTile(x,y,tile);
					dungeon.addTile(tile);
				}
			}

			do{
				random = new Random().nextInt(4);
			}
			while((random==0&&oldRandom==1)
					||
					(random==1&&oldRandom==0)
					||
					(random==2&&oldRandom==3)
					||
					(random==3&&oldRandom==2));

			oldRandom=random;
			int x=startX;
			int y=startY;

			switch(random){
			case 0://west
				x-=size;
				x--;
				break;
			case 1://east
				x+=size;
				x++;
				break;
			case 2://south
				y-=size;
				y--;
				break;
			case 3://north
				y+=size;
				y++;
				break;
			}
			Tile tile = TileFactory.getTile(x, y, TileType.DOOR);
			setTile(x,y,tile);
			dungeon.addTile(tile);

			int pathSize=new Random().nextInt(3);

			while (pathSize>=0){
				switch(random){
				case 0://west
					x--;
					setTile(x,y-1,TileFactory.getTile(x, y-1, TileType.DUNGEONWALL));
					setTile(x,y+1,TileFactory.getTile(x, y+1, TileType.DUNGEONWALL));
					break;
				case 1://east
					x++;
					setTile(x,y-1,TileFactory.getTile(x, y-1, TileType.DUNGEONWALL));
					setTile(x,y+1,TileFactory.getTile(x, y+1, TileType.DUNGEONWALL));
					break;
				case 2://south
					y--;
					setTile(x-1,y,TileFactory.getTile(x-1, y, TileType.DUNGEONWALL));
					setTile(x+1,y,TileFactory.getTile(x+1, y, TileType.DUNGEONWALL));
					break;
				case 3://north
					y++;
					setTile(x-1,y,TileFactory.getTile(x-1, y, TileType.DUNGEONWALL));
					setTile(x+1,y,TileFactory.getTile(x+1, y, TileType.DUNGEONWALL));
					break;
				}
				tile = TileFactory.getTile(x, y, TileType.DUNGEON);
				setTile(x,y,tile);
				dungeon.addTile(tile);
				pathSize--;
			}

			switch(random){
			case 0://west
				x--;
				setTile(x,y-1,TileFactory.getTile(x, y-1, TileType.DUNGEONWALL));
				setTile(x,y+1,TileFactory.getTile(x, y+1, TileType.DUNGEONWALL));
				break;
			case 1://east
				x++;
				setTile(x,y-1,TileFactory.getTile(x, y-1, TileType.DUNGEONWALL));
				setTile(x,y+1,TileFactory.getTile(x, y+1, TileType.DUNGEONWALL));
				break;
			case 2://south
				y--;
				setTile(x-1,y,TileFactory.getTile(x-1, y, TileType.DUNGEONWALL));
				setTile(x+1,y,TileFactory.getTile(x+1, y, TileType.DUNGEONWALL));
				break;
			case 3://north
				y++;
				setTile(x-1,y,TileFactory.getTile(x-1, y, TileType.DUNGEONWALL));
				setTile(x+1,y,TileFactory.getTile(x+1, y, TileType.DUNGEONWALL));
				break;
			}
			tile = TileFactory.getTile(x, y, TileType.DOOR);
			setTile(x,y,tile);
			dungeon.addTile(tile);

			switch(random){
			case 0://west
				x--;
				break;
			case 1://east
				x++;
				break;
			case 2://south
				y--;
				break;
			case 3://north
				y++;
				break;
			}

			size = new Random().nextInt(2)+1;

			switch(random){
			case 0://west
				x-=size;
				//x--;
				break;
			case 1://east
				x+=size;
				//x++;
				break;
			case 2://south
				y-=size;
				//y--;
				break;
			case 3://north
				y+=size;
				//y++;
				break;
			}
			startX = x;
			startY = y;
		}

		switch(random){
		case 0://west
			startX+=size;
			//x--;
			break;
		case 1://east
			startX-=size;
			//x++;
			break;
		case 2://south
			startY+=size;
			//y--;
			break;
		case 3://north
			startY-=size;
			//y++;
			break;
		}
		


		
		createPath(startX,startY,random);
		
		dungeon.setEntrance(getTile(startX,startY));
		dungeon.setOrigin(getTile(startX,startY));
		dungeon.addTile(dungeon.getOrigin());
		
		if(dungeon.getEntrance()==null){
			System.out.println(dungeon.getName());
			System.out.println(getTile(startX,startY));
		}
	}

	private void createPath(int x, int y, int random) {
		do{
			try{//To catch array out of bounds exceptions.
				if(getTile(x,y)!=null && 
						(getTile(x,y).getType() == TileType.ROAD ||
								getTile(x,y).getType() == TileType.DOOR || 
								getTile(x,y).getType() == TileType.WALL ||
								getTile(x,y).getType() == TileType.ROCK ||
								getTile(x,y).getType() == TileType.CAVE ||
								getTile(x,y).getType() == TileType.DUNGEON ||
								getTile(x,y).getType() == TileType.DUNGEONWALL)
								||
								(x==size/2&&y==size/2)){
					break;
				}	
				setTile(x,y,TileFactory.getTile(x,y,TileType.DIRT));//Set tile to dirt
			}
			catch(Exception e){
				break;
			}
			random=getDirectionCentre(x,y);
			switch(random){
			case 0://west
				x--;
				break;
			case 1://east
				x++;
				break;
			case 2://south
				y--;
				break;
			case 3://north
				y++;
				break;
			}
		}while(true);
	}
}


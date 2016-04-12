package entities.actors;

import java.util.ArrayList;
import java.util.Random;

import main.Controller;

import entities.map.Tile;
import enums.ActorType;
import enums.NPCType;

public class NPC extends Actor{
	private final NPCType npcType;

	public NPC(String name, int level, NPCType npcType, Race race, ActorClass actorClass) {
		super(name, ActorType.NPC, level, race, actorClass);
		this.npcType = npcType;
	}

	public NPCType getNpcType() {
		return npcType;
	}

	@Override
	public void processAI() {
		if(getTurn()){
			int x;
			int y;
			int count = 0;
			do{
				x = getX();
				y = getY();

				int random = new Random().nextInt(50);

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

	@Override
	public ArrayList<Actor> getValidEnemies() {
		// TODO Auto-generated method stub
		return null;
	}
}

package factories;

import java.util.ArrayList;
import java.util.Random;

import main.Controller;

import entities.actors.ActorClass;
import entities.actors.AttributeModifier;
import entities.actors.Race;
import entities.map.Tile;
import enums.ActorClassType;
import enums.AttributeName;
import enums.NPCType;
import enums.RaceType;
import enums.TileType;

public class RaceFactory {
	static final ArrayList<Race> EnemyRaces = new ArrayList<Race>();
	static final ArrayList<Race> NPCRaces = new ArrayList<Race>();

	static final float playerStartingStats = 3f;

	public static ArrayList<Race> getEnemyRaces(Tile tile, int level){
		TileType tileType = tile.getType();

		ArrayList<Race> enemyRaces = new ArrayList<Race>();
		for(Race race:EnemyRaces){
			for(TileType raceTileType:race.getAvailableTileTypes()){
				if(tileType==raceTileType&&
						level>=race.getMinimumLevel()&&
						(tileType!=TileType.DUNGEON||
								Controller.getTileMap().getDungeon(tile).getRaceType()==race.getRaceType()
						)){
					enemyRaces.add(race);
					break;
				}
			}
		}
		return enemyRaces;
	}

	public static ArrayList<Race> getNPCRaces(){
		return NPCRaces;
	}

	public static void initialise(){
		//Creating Fighter class.
		ActorClass fighterClass = new ActorClass("FIGHTER", 
				ActorClassType.FIGHTER,
				new AttributeModifier[]{
				new AttributeModifier(AttributeName.STRENGTH, 1.5f),
				new AttributeModifier(AttributeName.PERCEPTION, 0.75f)
		});
		
		//Creating Fighter class.
		ActorClass barbarianClass = new ActorClass("BARBARIAN", 
				ActorClassType.BARBARIAN,
				new AttributeModifier[]{
				new AttributeModifier(AttributeName.FORTITUDE, 1.5f),
				new AttributeModifier(AttributeName.AGILITY, 0.75f)
		});
		//Creating Scout class.
		ActorClass rogueClass = new ActorClass("ROGUE", 
				ActorClassType.ROGUE,
				new AttributeModifier[]{
				new AttributeModifier(AttributeName.AGILITY, 1.5f),
				new AttributeModifier(AttributeName.FORTITUDE, 0.75f)
		});
		
		//Creating Scout class.
		ActorClass scoutClass = new ActorClass("SCOUT", 
				ActorClassType.SCOUT,
				new AttributeModifier[]{
				new AttributeModifier(AttributeName.PERCEPTION, 1.5f),
				new AttributeModifier(AttributeName.STRENGTH, 0.75f)
		});
		//Creating beastClass.
		ActorClass beastClass = new ActorClass("",
				ActorClassType.BEAST,
				new AttributeModifier[0]
		);

		//Creating beastAlphaClass.
		ActorClass beastAlphaClass = new ActorClass("ALPHA",
				ActorClassType.BEAST,
				new AttributeModifier[]{
				new AttributeModifier(AttributeName.STRENGTH, 1.25f),
				new AttributeModifier(AttributeName.AGILITY, 1.25f),
				new AttributeModifier(AttributeName.PERCEPTION, 1.25f),
				new AttributeModifier(AttributeName.FORTITUDE, 1.25f),
				new AttributeModifier(AttributeName.STRENGTH, 1.25f)
		});
		
		//Creating Goblin race.
		EnemyRaces.add(new Race(
				RaceType.GOBLINOID,
				"GOBLIN",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.STRENGTH, 0.75f),
						new AttributeModifier(AttributeName.FORTITUDE, 0.75f),
						new AttributeModifier(AttributeName.AGILITY, 2f),
						new AttributeModifier(AttributeName.WILLPOWER, 0.75f)
				},
				new TileType[]{
						TileType.DUNGEON,
						TileType.CAVE,
						TileType.SWAMP,
				},
				new ActorClass[]{
						fighterClass,
						scoutClass,
						rogueClass
				},
				1,
				5
		));

		//Creating Orc race.
		EnemyRaces.add(new Race(
				RaceType.GOBLINOID,
				"ORC",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.STRENGTH, 1.5f),
						new AttributeModifier(AttributeName.FORTITUDE, 1.5f),
				},
				new TileType[]{
						TileType.DUNGEON,
						TileType.CAVE,
						TileType.SWAMP,
				},
				new ActorClass[]{
						fighterClass,
						rogueClass,
						barbarianClass
				},
				3,
				8
		));

		//Creating troll race.
		EnemyRaces.add(new Race(
				RaceType.GOBLINOID,
				"TROLL",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.FORTITUDE, 2f),
						new AttributeModifier(AttributeName.STRENGTH, 2f),
						new AttributeModifier(AttributeName.AGILITY, .5f),
						
				},
				new TileType[]{
						TileType.DUNGEON,
						TileType.CAVE,
						TileType.SWAMP,
				},
				new ActorClass[]{
						fighterClass,
						barbarianClass
				},
				5,
				10
		));

		//Creating Wolf race.
		EnemyRaces.add(new Race(
				RaceType.BEAST,
				"WOLF",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.FORTITUDE, 0.75f),
						new AttributeModifier(AttributeName.STRENGTH, 1.5f),
						new AttributeModifier(AttributeName.PERCEPTION, 2f)
				},
				new TileType[]{
						TileType.CAVE,
						TileType.FOREST,
				},
				new ActorClass[]{
						beastClass,
						beastAlphaClass
				},
				2,
				5
		));

		EnemyRaces.add(new Race(
				RaceType.BEAST,
				"RAT",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.AGILITY, 3f),
						new AttributeModifier(AttributeName.STRENGTH, 0.25f),
						new AttributeModifier(AttributeName.FORTITUDE, 0.25f),
				},
				new TileType[]{
						TileType.DIRT,
						TileType.SWAMP,
						TileType.FIELD,
						TileType.CAVE,
				},
				new ActorClass[]{
						beastClass
				},
				1,
				3
		));

		EnemyRaces.add(new Race(
				RaceType.BEAST,
				"FOX",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.PERCEPTION, 1.5f),
						new AttributeModifier(AttributeName.STRENGTH, 0.5f),
						new AttributeModifier(AttributeName.FORTITUDE, 0.6f),
				},
				new TileType[]{
						TileType.DIRT,
						TileType.FOREST,
						TileType.FIELD,
				},
				new ActorClass[]{
						beastClass
				},
				1,
				3
		));

		EnemyRaces.add(new Race(
				RaceType.BEAST,
				"SNAKE",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.FORTITUDE, 1.5f),
						new AttributeModifier(AttributeName.AGILITY, 0.5f),
						new AttributeModifier(AttributeName.PERCEPTION, 0.5f),
				},
				new TileType[]{
						TileType.FOREST,
						TileType.SWAMP,
				},
				new ActorClass[]{
						beastClass
				},
				1,
				3
		));

		EnemyRaces.add(new Race(
				RaceType.UNDEAD,
				"ZOMBIE",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.FORTITUDE, 2f),
						new AttributeModifier(AttributeName.AGILITY, 0.5f),
						new AttributeModifier(AttributeName.PERCEPTION, 0.5f),
				},
				new TileType[]{
						TileType.DUNGEON,
				},
				new ActorClass[]{
						beastClass
				},
				1,
				3
		));

		EnemyRaces.add(new Race(
				RaceType.UNDEAD,
				"SKELETON",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.FORTITUDE, 1.5f),
						new AttributeModifier(AttributeName.AGILITY, 0.75f),
						new AttributeModifier(AttributeName.PERCEPTION, 0.5f),
				},
				new TileType[]{
						TileType.DUNGEON,
				},
				new ActorClass[]{
						fighterClass,
						scoutClass,
				},
				1,
				5
		));

		EnemyRaces.add(new Race(
				RaceType.UNDEAD,
				"VAMPIRE",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.STRENGTH, 1.75f),
						new AttributeModifier(AttributeName.FORTITUDE, 1.5f),
						new AttributeModifier(AttributeName.AGILITY, 2f),
						new AttributeModifier(AttributeName.PERCEPTION, 2f),
						new AttributeModifier(AttributeName.WILLPOWER, 2f),
				},
				new TileType[]{
						TileType.DUNGEON,
				},
				new ActorClass[]{
						fighterClass,
						barbarianClass,
				},
				10,
				20
		));

		EnemyRaces.add(new Race(
				RaceType.DRACONIC,
				"DRAGON",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.STRENGTH, 3f),
						new AttributeModifier(AttributeName.FORTITUDE, 3f),
						new AttributeModifier(AttributeName.AGILITY, 3f),
						new AttributeModifier(AttributeName.PERCEPTION, 3f),
						new AttributeModifier(AttributeName.WILLPOWER, 3f),
				},
				new TileType[]{
						TileType.DUNGEON,
				},
				new ActorClass[]{
						beastClass,
				},
				10,
				20
		));

		EnemyRaces.add(new Race(
				RaceType.DRACONIC,
				"KOBOLD",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.PERCEPTION, 1.5f),
						new AttributeModifier(AttributeName.AGILITY, 2f),
						new AttributeModifier(AttributeName.FORTITUDE, .5f),
				},
				new TileType[]{
						TileType.DUNGEON,
				},
				new ActorClass[]{
						fighterClass,
						rogueClass,
						barbarianClass,
						scoutClass
				},
				1,
				20
		));

		EnemyRaces.add(new Race(
				RaceType.DRACONIC,
				"WYRMLING",
				new AttributeModifier[0],
				new TileType[]{
						TileType.DUNGEON,
				},
				new ActorClass[]{
						beastClass,
				},
				3,
				20
		));
	}

	public static Race getPlayerRace(){
		Race playerRace = new Race(
				null,
				"Player",
				new AttributeModifier[]{
						new AttributeModifier(AttributeName.STRENGTH, playerStartingStats),
						new AttributeModifier(AttributeName.FORTITUDE, playerStartingStats),
						new AttributeModifier(AttributeName.PERCEPTION, playerStartingStats),
						new AttributeModifier(AttributeName.AGILITY, playerStartingStats),
						new AttributeModifier(AttributeName.WILLPOWER, playerStartingStats)
				},
				null,
				null,
				0,
				0
		);
		return playerRace;
	}

	public static ActorClass getPlayerClass(){
		ActorClass actorClass = new ActorClass("Player",
				null,
				new AttributeModifier[0]);
		return actorClass;
	}

	public static ActorClass getNPCClass(NPCType npcType){
		//Creating merchantClass.
		ActorClass merchantClass = new ActorClass("Merchant",
				ActorClassType.NPC,
				new AttributeModifier[0]);

		//Creating questgiverClass.
		ActorClass questGiverClass = new ActorClass("Questgiver",
				ActorClassType.NPC,
				new AttributeModifier[0]);

		if(npcType==NPCType.MERCHANT){
			return merchantClass;
		}
		else return questGiverClass;
	}

	public static Race getNPCRace(){
		//Temporary code.
		Race NPCRace = new Race(
				RaceType.NPC,
				"Human",
				new AttributeModifier[0],
				null,
				null,
				0,
				0
		);
		return NPCRace;			
	}

	public static RaceType getDungeonRaceType() {
		RaceType[] raceTypes = RaceType.values();
		RaceType raceType = null;
		while(raceType==null||raceType==RaceType.NPC||raceType==RaceType.BEAST){
			raceType = raceTypes[new Random().nextInt(raceTypes.length)];
		}
		return raceType;
	}

	public static Race getRace(Tile tile, int level) {
		ArrayList<Race> enemyRaces = RaceFactory.getEnemyRaces(tile, level);

		int random = new Random().nextInt(enemyRaces.size());

		return enemyRaces.get(random);
	}

	public static Race getQuestRace() {
		//Creating Dragon Race
		Race race = new Race(RaceType.DRACONIC,
				"BAHAMUT",
				new AttributeModifier[0],
				null,
				null,
				0,
				0);		

		return race;
	}
}

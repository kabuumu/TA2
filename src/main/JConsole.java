package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import javax.swing.JPanel;

import entities.actors.Actor;
import entities.actors.NPC;
import entities.actors.Player;
import entities.map.Tile;
import entities.map.TileMap;
import entities.quest.KillQuest;
import entities.quest.Quest;
import enums.ActorType;
import enums.AttributeName;
import enums.GameState;
import enums.NPCType;
import enums.QuestType;
import enums.TileType;
import factories.TextFactory;

public class JConsole extends JPanel {
	private static final long serialVersionUID = 3571518591759968333L;

	private static Font font;
	private int fontWidth;
	private int fontHeight;

	public JConsole(){
		Map<TextAttribute, Object> map =new Hashtable<TextAttribute, Object>();
		map.put(TextAttribute.WIDTH,1.5);
		map.put(TextAttribute.TRACKING,0.2);
		map.put(TextAttribute.SIZE,12);
		font = new Font("Courier New",Font.BOLD,12).deriveFont(map);
		setFont(font);
		setBackground(Color.BLACK);
		setOpaque(true);

		FontRenderContext fontRenderContext=new FontRenderContext(font.getTransform(),false,false);
		Rectangle2D charBounds=font.getStringBounds("X", fontRenderContext);
		fontWidth=(int)charBounds.getWidth();
		fontHeight=(int)charBounds.getHeight();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		String sprite = "";
		Color spriteColour = Color.WHITE;
		Color bgColour = Color.BLACK;
		Player player = Controller.getPlayer();
		TileMap tileMap = Controller.getTileMap();
		ArrayList<Tile> visibleTiles = player.getVisibleTiles();

		int yTileAmount = (getHeight()/fontHeight/2);
		int xTileAmount = (getWidth()/fontWidth/2);

		int yStart=player.getY()-yTileAmount;
		int xStart = player.getX()-xTileAmount;

		for(int y = player.getY()-yTileAmount;y<=player.getY()+yTileAmount;y++){
			for(int x = player.getX()-xTileAmount;x<=player.getX()+xTileAmount;x++){
				Tile tile = null;
				try{
					tile = tileMap.getTile(x, y);
				}
				catch(Exception e){}

				TileType tileType=null;
				if(tile!=null){
					tileType = tile.getType();
					bgColour = Color.BLACK;
					switch(tileType){
					case FIELD:
						spriteColour = new Color(0,150,0);
						bgColour = new Color(40,60,0);
						sprite =("^");
						break;
					case FOREST:
						spriteColour = new Color(0,70,10);
						bgColour = new Color(20,20,0);
						sprite =("¥");
						break;
					case SWAMP:
						spriteColour = new Color(0,100,80);
						bgColour = new Color(0,25,25);
						sprite =("~");
						break;
					case DIRT:
						spriteColour = new Color(100,100,0);
						bgColour = new Color(25,25,0);
						sprite =(".");
						break;
					case TOWN:
						spriteColour = new Color(30,30,30);
						bgColour = new Color(35,35,35);
						sprite =("#");
						break;
					case WALL:
						spriteColour = new Color(150,150,150);
						bgColour = new Color(35,35,35);
						sprite =("O");
						break;
					case WATER:
						spriteColour = new Color(0,50,200);
						bgColour = new Color(0,0,150);
						switch(new Random().nextInt(2)){
						case 0:
							sprite =("~");
							break;
						case 1:
							sprite = "˜";
							break;
						}
						break;
					case DOOR:
						spriteColour = new Color(150,150,150);
						bgColour = new Color(35,35,35);
						sprite =("/");
						break;
					case ROAD:
						spriteColour = new Color(100,100,100);
						bgColour = new Color(35,35,35);
						sprite =("+");
						break;
					case CAVE:
						spriteColour = new Color(80,80,50);
						bgColour = new Color(10,10,0);
						sprite =(".");
						break;
					case ROCK:
						spriteColour = new Color(150,150,100);
						bgColour = new Color(51,25,0);
						sprite =("O");
						break;
					case DUNGEONWALL:
						spriteColour = new Color(150,150,100);
						bgColour = new Color(35,35,35);
						sprite =("Ω");
						break;
					case DUNGEON:
						spriteColour = new Color(50,0,10);
						bgColour = new Color(35,35,35);
						sprite =("#");
						break;
					default:
						sprite =("X");
						break;
					}
				}

				else {
					sprite =("X");
				}

				double distance = Point2D.distance((float)x, (float)y, (float)player.getX(), (float)player.getY());

				if(Controller.getGameState()==GameState.CAST_TARGET&&
						distance<=Controller.getSelectedSpell().getRange()
						&&
						player.getVisibleTiles().contains(tile)){
					bgColour = new Color(100,50,0);
				}

				//Darkening tiles that are far away
				spriteColour = spriteColour.brighter();
				spriteColour = spriteColour.brighter();
				while(distance>0){
					distance-=player.getAttribute(AttributeName.PERCEPTION)/10;
					if(distance>0){
						spriteColour = spriteColour.darker();
						bgColour = bgColour.darker();
					}
					if(player.getAttribute(AttributeName.PERCEPTION)==0){ 
						break; //Bug fix to prevent getting stuck
					}
				}
				
				distance = Point2D.distance((float)x, (float)y, (float)player.getX(), (float)player.getY());

				if(Controller.getGameState()==GameState.CAST_TARGET&&
						distance<=Controller.getSelectedSpell().getRange()
						&&
						player.getVisibleTiles().contains(tile)){
					bgColour = new Color(100,50,0);
				}

				if(visibleTiles.contains(tile)==false){
					spriteColour = spriteColour.darker();
				}
				else{					
					tile.isLocated=true;
					if(tile!=null && tile.getActor() != null){
						Actor actor = tile.getActor();
						if(actor.getType()==ActorType.PLAYER){
							spriteColour = new Color(255,255,255);
							sprite =("P");
						}
						else if(actor.getType()==ActorType.ENEMY){
							spriteColour = TextFactory.getSpriteColour(actor);
							sprite =TextFactory.getEnemySprite(actor);
						}
						else if(actor.getType()==ActorType.NPC){
							NPC npc = (NPC)actor;
							spriteColour = TextFactory.getSpriteColour(actor);
							if(npc.getNpcType()==NPCType.MERCHANT){
								sprite =("M");
							}
							if(npc.getNpcType()==NPCType.TRAINER){
								sprite =("T");
							}
							if(npc.getNpcType()==NPCType.QUEST){
								sprite =("Q");
							}
						}
						if(actor==Controller.getTarget()||actor==Controller.getSelectedObject()){
							spriteColour = new Color(0,255,0);
						}
					}
					else if(tile.getInventory().size()>0){
						spriteColour = new Color(0,100,255);
						sprite = ("$");
					}

					//Set the quest target to yellow, but not if selected.
					for(Quest quest:player.getQuests()){
						if(quest.getType()==QuestType.KILL){
							KillQuest killQuest = (KillQuest)quest;
							if(killQuest.getX()==x&&killQuest.getY()==y&&
									killQuest.getTarget()!=Controller.getTarget()&&
									killQuest.getTarget()!=Controller.getSelectedObject()){
								spriteColour = Color.YELLOW;
							}
						}
					}
				}
				try{
					if(tile.isLocated == true){
						//Flipping y coordinates because of the way it draws.
						int displayY=player.getY()-(y-player.getY());

						g.setColor(bgColour);
						g.fillRect((x*fontWidth)-(xStart*fontWidth)-2, (displayY*fontHeight)-(yStart*fontHeight)-10, fontWidth, fontHeight);

						g.setColor(spriteColour);
						g.drawString(sprite, (x*fontWidth)-(xStart*fontWidth), (displayY*fontHeight)-(yStart*fontHeight));
					}
				}
				catch(Exception e){
				}
			}
		}
	}
}
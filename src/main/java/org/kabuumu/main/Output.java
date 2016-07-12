package org.kabuumu.main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import org.kabuumu.enums.*;
import org.kabuumu.factories.TextFactory;

public class Output {
	private static JFrame frame = new JFrame("Text Adventure 2 " + Controller.getVersion());
	private static JTextArea outputArea = new JTextArea();
	private static JEditorPane inventory = new JEditorPane(){
		private static final long serialVersionUID = 610600433300304355L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			int vLeft = 6;
			int top = 79;
			int v2 = 214;
			int v3 = 302;
			int v4 = 350;
			int vRight = 398;
			int inventorySize = Controller.getPlayer().getDisplayInventory().size();

			if(Controller.getGameState()!=GameState.BUY){
				Graphics2D g2 = (Graphics2D)g;
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(vLeft, 52, vLeft, top+inventorySize*22);
				g2.drawLine(vLeft, top, 398, top);
				g2.drawLine(v2, 52, v2, top+inventorySize*22);
				g2.drawLine(v3, 52, v3, top+inventorySize*22);
				g2.drawLine(v4, 52, v4, top+inventorySize*22);
				g2.drawLine(vRight, 52, vRight, top+inventorySize*22);
			}
		}
	};
	private static JEditorPane helpArea  = new JEditorPane();
	private static JEditorPane modularPane  = new JEditorPane(){
		private static final long serialVersionUID = 832091645972412592L;
		
		public void paint(Graphics g){
			super.paint(g);
			GameState gameState = Controller.getGameState();
			Graphics2D g2 = (Graphics2D)g;

			if(gameState==GameState.BUY||gameState==GameState.MERCHANT){
				int vLeft = 6;
				int top = 59;
				int vTop = 32;
				int v2 = 214;
				int v3 = 302;
				int v4 = 350;
				int vRight = 398;
				int inventorySize = Controller.getTarget().getDisplayInventory().size();;
				
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(vLeft, vTop, vLeft, top+inventorySize*22);
				g2.drawLine(vLeft, top, 398, top);
				g2.drawLine(v2, vTop, v2, top+inventorySize*22);
				g2.drawLine(v3, vTop, v3, top+inventorySize*22);
				g2.drawLine(v4, vTop, v4, top+inventorySize*22);
				g2.drawLine(vRight, vTop, vRight, top+inventorySize*22);
			}
			else if(gameState==GameState.DROP
					||
					gameState==GameState.GET
					||
					(gameState==GameState.FIELD
							&&
							Controller.getPlayerTile().getInventory().size()>0)){
				int vLeft = 6;
				int top = 59;
				int vTop = 32;
				int v2 = 214;
				int v3 = 302;
				int v4 = 350;
				int vRight = 398;
				int inventorySize = Controller.getPlayerTile().getDisplayInventory().size();;
				
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(vLeft, vTop, vLeft, top+inventorySize*22);
				g2.drawLine(vLeft, top, 398, top);
				g2.drawLine(v2, vTop, v2, top+inventorySize*22);
				g2.drawLine(v3, vTop, v3, top+inventorySize*22);
				g2.drawLine(v4, vTop, v4, top+inventorySize*22);
				g2.drawLine(vRight, vTop, vRight, top+inventorySize*22);
			}
			else if(gameState==GameState.CAST){
				int vLeft = 6;
				int top = 59;
				int vTop = 32;
				int v2 = 134;
				int v3 = 174;
				int v4 = 222;
				int v5 = 254;
				int v6 = 286;
				int vRight = 398;
				int spellSize = Controller.getPlayer().getSpells().size();
				
				g2.setColor(Color.WHITE);
				g2.setStroke(new BasicStroke(2));
				g2.drawLine(vLeft, vTop, vLeft, top+spellSize*22);
				g2.drawLine(vLeft, top, 398, top);
				g2.drawLine(v2, vTop, v2, top+spellSize*22);
				g2.drawLine(v3, vTop, v3, top+spellSize*22);
				g2.drawLine(v4, vTop, v4, top+spellSize*22);
				g2.drawLine(v5, vTop, v5, top+spellSize*22);
				g2.drawLine(v6, vTop, v6, top+spellSize*22);
				g2.drawLine(vRight, vTop, vRight, top+spellSize*22);
			}
			else{
				//modularPane.setText(TextFactory.getCharacterInfo(Controller.getPlayer()));
			}
		}
		
	};
	private static JConsole mapPane = new JConsole();
	public static Controller controller;
	private static Font font = new Font("Courier New",Font.BOLD,14);

	private Output(){
	}

	public static JFrame getFrame(){
		try {
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		Controller.start();

		frame.add(getPane());
		frame.setFocusable(true);

		frame.setSize(1025,670);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		frame.addKeyListener(new CustomKeyListener());

		initialise();
		frame.setVisible(true);
		return frame;
	}

	public static String getName(){

		String s = null; 
		while(s==null){
			s = (String)JOptionPane.showInputDialog(
					frame,
					"",
					"Please enter name:",
					JOptionPane.PLAIN_MESSAGE
					);
			if(s!=null && s.length()==0){
				s = null;
			}
		}
		
		return s;
	}

	private static JPanel getPane(){
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		pane.add(getLeftPane(), BorderLayout.CENTER);
		pane.add(getRightPane(), BorderLayout.LINE_END);
		//pane.add(getBottomPane(), BorderLayout.PAGE_END);

		pane.setFocusable(false);

		return pane;
	}

	private static JPanel getLeftPane(){
		JPanel pane = new JPanel();

		pane.setLayout(new BorderLayout());
		//pane.add(getTitleField(), BorderLayout.PAGE_START);
		pane.add(getUpperLeftPane(), BorderLayout.CENTER);
		pane.add(getHelpArea(), BorderLayout.PAGE_END);

		pane.setFocusable(false);

		return pane;
	}

	private static JPanel getUpperLeftPane(){
		JPanel pane = new JPanel();

		pane.setLayout(new GridLayout(2,1));
		pane.add(getMap());//, BorderLayout.CENTER);
		pane.add(getOutputField());//,BorderLayout.PAGE_END);

		pane.setFocusable(false);
		return pane;
	}

	private static JEditorPane getHelpArea(){
		helpArea.setFont(font);
		helpArea.setBorder(BorderFactory.createLoweredBevelBorder());
		helpArea.setContentType("text/html");		
		helpArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		helpArea.setFocusable(false);

		return helpArea;
	}

	private static JPanel getRightPane(){
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(2,1,10,10));
		//pane.add(getPlayerStats());#
		//pane.add(getMap());
		pane.add(getModularPane());
		pane.add(getPlayerInventory());
		pane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

		pane.setFocusable(false);

		return pane;
	}

	private static JEditorPane getModularPane(){
		modularPane.setEditable(false);
		modularPane.setFont(font);
		modularPane.setBackground(Color.BLACK);
		modularPane.setForeground(Color.white);
		modularPane.setContentType("text/html");
		modularPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		modularPane.setBorder(BorderFactory.createLoweredBevelBorder());
		modularPane.setPreferredSize(new Dimension(200,200));

		modularPane.setFocusable(false);

		return modularPane;
	}

	private static void setModularPaneText(){
		GameState gameState = Controller.getGameState();
		if(gameState==GameState.BUY||gameState==GameState.MERCHANT){
			modularPane.setText(TextFactory.getInventoryText((Controller.getTarget())));
		}
		else if(gameState==GameState.DROP||gameState==GameState.GET){
			modularPane.setText(TextFactory.getInventoryText(Controller.getPlayerTile()));
		}
		else if(gameState==GameState.FIELD&&Controller.getPlayerTile().getInventory().size()>0){
			modularPane.setText(TextFactory.getInventoryText(Controller.getPlayerTile()));
		}
		else if(gameState==GameState.TALK||gameState==GameState.ATTACK){
			modularPane.setText(TextFactory.getAvailableObjects());
		}
		else if(gameState==GameState.CAST){
			modularPane.setText(TextFactory.getAvailableSpells());
		}
		else if(gameState==GameState.CAST_TARGET){
			modularPane.setText(TextFactory.getAvailableObjects());
		}
		else if(gameState==GameState.USE||gameState==GameState.SELL){
			modularPane.setText(TextFactory.getItemDescription());
		}
		else{
			modularPane.setText(TextFactory.getCharacterInfo(Controller.getPlayer()));
		}
	}

	private static JConsole getMap(){

		mapPane.setBorder(BorderFactory.createLoweredBevelBorder());
		mapPane.setBackground(Color.BLACK);
		mapPane.setFocusable(false);
		
		return mapPane;
	}

	private static JEditorPane getPlayerInventory(){
		inventory.setEditable(false);
		inventory.setFont(font);
		inventory.setBackground(Color.black);
		inventory.setForeground(Color.white);
		inventory.setContentType("text/html");
		inventory.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		inventory.setBorder(BorderFactory.createLoweredBevelBorder());

		inventory.setFocusable(false);
		inventory.setPreferredSize(new Dimension(405,200));
		
		return inventory;
	}

	private static JScrollPane getOutputField(){ 
		outputArea.setEditable(false);
		outputArea.setLineWrap(true);
		outputArea.setWrapStyleWord(true);
		outputArea.setFont(font);
		outputArea.setBackground(Color.black);
		outputArea.setForeground(Color.white);

		JScrollPane outputField = new JScrollPane(outputArea);
		outputArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		outputField.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		outputArea.setFocusable(false);
		outputField.setFocusable(false);

		return outputField;
	}

	public static void setOutputField(String output){
		outputArea.setText(outputArea.getText()+output);
	}

	public static void initialise(){
		setInventoryPaneText();
		helpArea.setText(TextFactory.getAvailableCommandText(Controller.getGameState()));
		//setTileMap();
		setModularPaneText();
		//mapPane.revalidate();
		//mapPane.repaint();
		//frame.revalidate();
		frame.repaint();
	}
	
	private static void setInventoryPaneText(){
		if(Controller.getGameState()==GameState.BUY){
			inventory.setText(TextFactory.getItemDescription());
		}
		else{
			inventory.setText(TextFactory.getInventoryText(Controller.getPlayer()));
		}
	}

	private static class CustomKeyListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_UP){
				Controller.testProcess("NORTH");
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				Controller.testProcess("SOUTH");
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				Controller.testProcess("EAST");
			}
			else if(e.getKeyCode() == KeyEvent.VK_LEFT){
				Controller.testProcess("WEST");
			}
			else if(e.getKeyCode() == KeyEvent.VK_A){
				Controller.testProcess("ATTACK");
			}
			else if(e.getKeyCode() == KeyEvent.VK_G){
				Controller.testProcess("GET");
			}
			else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				Controller.testProcess("CANCEL");
			}
			else if(e.getKeyCode() == KeyEvent.VK_D){
				Controller.testProcess("DROP");
			}
			else if(e.getKeyCode() == KeyEvent.VK_U){
				Controller.testProcess("USE");
			}
			else if(e.getKeyCode() == KeyEvent.VK_F5){
				Controller.testProcess("SAVE");
			}
			else if(e.getKeyCode() == KeyEvent.VK_ENTER){
				Controller.testProcess("ENTER");
			}
			else if(e.getKeyCode() == KeyEvent.VK_B){
				Controller.testProcess("BUY");
			}
			else if(e.getKeyCode() == KeyEvent.VK_S){
				Controller.testProcess("SELL");
			}
			else if(e.getKeyCode() == KeyEvent.VK_T){
				Controller.testProcess("TALK");
			}
			else if(e.getKeyCode() == KeyEvent.VK_C){
				Controller.testProcess("CAST");
			}
			initialise();
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	}
}

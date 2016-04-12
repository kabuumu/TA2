package interfaces;

import java.util.ArrayList;

import entities.items.Item;

public interface HasInventory {
	public ArrayList<Item> getInventory();
	
	public String getName();
	
	public void addItem(Item item);
	
	public void removeItem(Item item);
	
	public int getMin(); 

	public int getMax();
	
	public ArrayList<Item> getDisplayInventory();
	
}

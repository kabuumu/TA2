package entities.map;

import factories.TextFactory;

public class Town extends Area{
	private final String name;
	public boolean isVisited = false;
	
	Town(){
		name = TextFactory.getTownName();
	}
	
	public String getName(){
		return name;
	}
}

package entities.actors;

import enums.AttributeName;
import interfaces.HasName;

public class Attribute implements HasName{
	private final AttributeName name;
	private int value;

	public Attribute(AttributeName name, int value){
		this.name = name;
		this.value = value;
	}
	
	
	public AttributeName getAttributeName() {
		return name;
	}
	
	@Override
	public String getName() {
		return name.toString();
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}

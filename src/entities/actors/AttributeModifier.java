package entities.actors;

import interfaces.HasName;
import enums.AttributeName;

public class AttributeModifier implements HasName{
	private final AttributeName name;
	private final float modifier;

	public AttributeModifier(AttributeName name, float modifier){
		this.name = name;
		this.modifier = modifier;
	}
	
	@Override
	public String getName() {
		return name.toString();
	}

	public float getModifier() {
		return modifier;
	}
}

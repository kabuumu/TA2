package org.kabuumu.entities.actors;

import org.kabuumu.interfaces.HasName;
import org.kabuumu.enums.AttributeName;

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

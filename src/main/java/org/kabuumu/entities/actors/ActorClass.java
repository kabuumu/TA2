package org.kabuumu.entities.actors;

import org.kabuumu.enums.ActorClassType;

public class ActorClass extends ActorClassification{
	private final ActorClassType actorClassType;
	
	public ActorClass(String name, ActorClassType actorClassType, AttributeModifier[] attributeModifiers){
		super(name,attributeModifiers);
		this.actorClassType = actorClassType;
	}

	public ActorClassType getType() {
		return actorClassType;
	}
}

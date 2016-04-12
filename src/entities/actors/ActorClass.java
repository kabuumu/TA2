package entities.actors;

import enums.ActorClassType;

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

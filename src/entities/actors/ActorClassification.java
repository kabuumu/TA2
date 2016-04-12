package entities.actors;

import enums.AttributeName;

public abstract class ActorClassification {
	private final String name;
	private final AttributeModifier[] attributeModifiers;
	
	public ActorClassification(String name, AttributeModifier[] attributeModifiers){
		this.name = name;
		this.attributeModifiers = attributeModifiers;
	}
	
	public String getName() {
		return name;
	}	

	public float getAttributeModifier(AttributeName name){
		for(AttributeModifier attributeModifier:attributeModifiers){
			if(attributeModifier.getName()==name.toString()){
				return attributeModifier.getModifier();
			}
		}
		return 1;
	}
}

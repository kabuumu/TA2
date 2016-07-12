package org.kabuumu.entities;

public class StatusEffect {
	public enum Effect{
		STRENGTH, AGILITY, SLOW, WEAKEN
	}
	private final Effect effect;
	private final int amount;
	private int time;

	public StatusEffect(Effect effect, int amount, int time){
		this.effect = effect;
		this.amount=amount;
		this.time = time;
	}
	public StatusEffect(StatusEffect statusEffect){
		this.effect = statusEffect.getEffect();
		this.amount = statusEffect.getAmount();
		this.time = statusEffect.getTime();
	}

	public boolean isActive(){
		time--;
		if(time==0){
			return false;
		}
		else{
			return true;
		}
	}

	public Effect getEffect() {
		return effect;
	}

	public int getAmount() {
		return amount;
	}

	public int getTime() {
		return time;
	}
}

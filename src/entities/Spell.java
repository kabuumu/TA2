package entities;

import interfaces.HasName;
import enums.DamageType;

public class Spell implements HasName{
	public enum SpellType{
		SELF, TARGET
	}
	
	private final String name;
	private final DamageType damageType;
	private final StatusEffect statusEffect;
	private final int damage;
	private final int manaCost;
	private final SpellType spellType;
	private final int range;
	
	public Spell(String name, DamageType damageType, int damage, int manaCost, StatusEffect statusEffect, SpellType spellType, int range){
		this.name = name;
		this.damageType = damageType;
		this.damage = damage;
		this.manaCost = manaCost;
		this.statusEffect = statusEffect;
		this.spellType = spellType;
		this.range = range;
	}

	public String getName() {
		return name;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	public int getDamage() {
		return damage;
	}

	public int getManaCost() {
		return manaCost;
	}

	public StatusEffect getStatusEffect() {
		return statusEffect;
	}

	public SpellType getSpellType() {
		return spellType;
	}

	public int getRange() {
		return range;
	}
}
